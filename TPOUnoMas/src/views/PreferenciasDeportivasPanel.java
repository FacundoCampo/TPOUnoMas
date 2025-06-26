package views;

import controller.DeporteController;
import controller.UsuarioController;
import enums.NivelJuego;
import model.dto.DeporteDTO;
import model.dto.DeporteUsuarioDTO;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class PreferenciasDeportivasPanel extends JPanel {

    private final String usuarioId;
    private final JTable tabla;
    private final PreferenciasTableModel tablaModel;
    private final JLabel lblMensaje;

    public PreferenciasDeportivasPanel(String usuarioId) {
        this.usuarioId = usuarioId;
        this.tablaModel = new PreferenciasTableModel();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(25, 25, 25));
        setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // Panel título + botón Actualizar
        JPanel panelTitulo = new JPanel();
        panelTitulo.setLayout(new BoxLayout(panelTitulo, BoxLayout.X_AXIS));
        panelTitulo.setOpaque(false);
        panelTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitulo = new JLabel("Tus preferencias deportivas");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(46, 204, 113));

        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.setPreferredSize(new Dimension(120, 35));
        btnActualizar.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setBackground(new Color(44, 62, 80));
        btnActualizar.setFocusPainted(false);
        btnActualizar.setBorderPainted(false);
        btnActualizar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnActualizar.addActionListener(e -> cargarPreferenciasPrevias());

        panelTitulo.add(lblTitulo);
        panelTitulo.add(Box.createHorizontalStrut(20));
        panelTitulo.add(btnActualizar);

        add(panelTitulo);
        add(Box.createVerticalStrut(20));

        tabla = new JTable(tablaModel) {
            @Override
            public TableCellRenderer getCellRenderer(int row, int column) {
                if (column == 0 || column == 3) {
                    return getDefaultRenderer(Boolean.class);
                }
                return super.getCellRenderer(row, column);
            }

            @Override
            public TableCellEditor getCellEditor(int row, int column) {
                if (column == 0 || column == 3) {
                    return getDefaultEditor(Boolean.class);
                }
                return super.getCellEditor(row, column);
            }
        };
        tabla.setBackground(new Color(30, 30, 30));
        tabla.setForeground(Color.WHITE);
        tabla.setGridColor(Color.DARK_GRAY);
        tabla.setSelectionBackground(new Color(60, 60, 60));
        tabla.setRowHeight(30);
        tabla.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(new JComboBox<>(NivelJuego.values())));

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setBackground(new Color(30, 30, 30));
        renderer.setForeground(Color.WHITE);
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setPreferredSize(new Dimension(700, 300));
        scroll.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(scroll);
        add(Box.createVerticalStrut(20));

        JButton btnGuardar = new JButton("Guardar preferencias");
        btnGuardar.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btnGuardar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnGuardar.setFont(new Font("Tahoma", Font.BOLD, 18));
        btnGuardar.setBackground(new Color(46, 204, 113));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnGuardar.addActionListener(this::guardarPreferencias);
        add(btnGuardar);

        lblMensaje = new JLabel("");
        lblMensaje.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblMensaje.setForeground(Color.LIGHT_GRAY);
        add(Box.createVerticalStrut(10));
        add(lblMensaje);

        cargarPreferenciasPrevias();
    }

    private void cargarPreferenciasPrevias() {
        List<DeporteDTO> deportes = DeporteController.getInstance().obtenerTodos();
        List<DeporteUsuarioDTO> preferencias = UsuarioController.getInstance().obtenerPreferencias(usuarioId);
        tablaModel.setData(deportes, preferencias);
    }

    private void guardarPreferencias(ActionEvent e) {
        try {
            List<DeporteUsuarioDTO> preferencias = tablaModel.getPreferencias();
            UsuarioController.getInstance().actualizarPreferencias(usuarioId, preferencias);
            lblMensaje.setText("✓ Preferencias guardadas correctamente.");
        } catch (Exception ex) {
            lblMensaje.setText("Error al guardar preferencias: " + ex.getMessage());
        }
    }

    private static class PreferenciasTableModel extends AbstractTableModel {
        private final String[] columnas = {"Seleccionar", "Deporte", "Nivel", "Favorito"};
        private final List<DeporteDTO> deportes = new ArrayList<>();
        private final List<Boolean> seleccionados = new ArrayList<>();
        private final List<NivelJuego> niveles = new ArrayList<>();
        private int favoritoIndex = -1;

        public void setData(List<DeporteDTO> disponibles, List<DeporteUsuarioDTO> previas) {
            deportes.clear();
            seleccionados.clear();
            niveles.clear();
            favoritoIndex = -1;
            for (DeporteDTO d : disponibles) {
                deportes.add(d);
                boolean seleccionado = false;
                NivelJuego nivel = NivelJuego.INTERMEDIO;
                if (previas != null) {
                    for (DeporteUsuarioDTO pref : previas) {
                        if (pref.getDeporte().getNombre().equals(d.getNombre())) {
                            seleccionado = true;
                            nivel = pref.getNivel();
                            if (pref.esFavorito()) favoritoIndex = deportes.size() - 1;
                            break;
                        }
                    }
                }
                seleccionados.add(seleccionado);
                niveles.add(nivel);
            }
            fireTableDataChanged();
        }

        public List<DeporteUsuarioDTO> getPreferencias() {
            List<DeporteUsuarioDTO> lista = new ArrayList<>();
            for (int i = 0; i < deportes.size(); i++) {
                if (!seleccionados.get(i)) continue;
                boolean esFavorito = (i == favoritoIndex);
                lista.add(new DeporteUsuarioDTO(deportes.get(i), niveles.get(i), esFavorito));
            }
            return lista;
        }

        @Override
        public int getRowCount() {
            return deportes.size();
        }

        @Override
        public int getColumnCount() {
            return columnas.length;
        }

        @Override
        public String getColumnName(int column) {
            return columnas[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return switch (columnIndex) {
                case 0 -> seleccionados.get(rowIndex);
                case 1 -> deportes.get(rowIndex).getNombre();
                case 2 -> niveles.get(rowIndex);
                case 3 -> rowIndex == favoritoIndex;
                default -> null;
            };
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex != 1;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 0 -> {
                    if (aValue instanceof Boolean b) seleccionados.set(rowIndex, b);
                }
                case 2 -> {
                    if (aValue instanceof NivelJuego nivel) niveles.set(rowIndex, nivel);
                }
                case 3 -> {
                    if ((Boolean) aValue) favoritoIndex = rowIndex;
                    else if (rowIndex == favoritoIndex) favoritoIndex = -1;
                    fireTableRowsUpdated(0, getRowCount() - 1);
                }
            }
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return switch (columnIndex) {
                case 0, 3 -> Boolean.class;
                case 2 -> NivelJuego.class;
                default -> String.class;
            };
        }
    }
}
