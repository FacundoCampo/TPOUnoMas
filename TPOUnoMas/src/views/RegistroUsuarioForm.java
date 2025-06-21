package views;

import controller.DeporteController;
import controller.UsuarioController;
import enums.NivelJuego;
import model.dto.DeporteDTO;
import model.dto.DeporteUsuarioDTO;
import model.dto.UsuarioDTO;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class RegistroUsuarioForm extends JPanel {

    private JTextField txtNombre;
    private JTextField txtEmail;
    private JPasswordField txtContraseña;
    private JTextField txtUbicacion;
    private JTable tabla;
    private PreferenciasTableModel tablaModel;
    private JButton btnRegistrar;
    private JButton btnVolver;
    private JLabel lblMensaje;
    private Runnable onVolver;
    private UsuarioDTO usuarioExistente;

    public RegistroUsuarioForm(Runnable onVolver) {
        this(onVolver, null);
    }

    public RegistroUsuarioForm(Runnable onVolver, UsuarioDTO usuarioExistente) {
        this.onVolver = onVolver;
        this.usuarioExistente = usuarioExistente;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(25, 25, 25));
        setBorder(BorderFactory.createEmptyBorder(40, 100, 40, 100));

        JLabel lblTitulo = new JLabel(usuarioExistente == null ? "Registro de Usuario" : "Modificar Usuario");
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(new Color(46, 204, 113));
        add(lblTitulo);
        add(Box.createVerticalStrut(20));

        txtNombre = crearCampoTexto("Nombre");
        txtEmail = crearCampoTexto("Email");
        txtContraseña = crearCampoPassword("Contraseña");
        txtUbicacion = crearCampoTexto("Ubicación");

        JLabel lblDep = new JLabel("Preferencias deportivas");
        lblDep.setForeground(Color.WHITE);
        lblDep.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblDep.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(lblDep);

        tablaModel = new PreferenciasTableModel();
        tabla = new JTable(tablaModel);
        tabla.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(new JComboBox<>(NivelJuego.values())));
        tabla.setRowHeight(30);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setPreferredSize(new Dimension(600, 300));
        scroll.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(scroll);

        add(Box.createVerticalStrut(10));
        btnRegistrar = crearBoton("Registrar", new Color(46, 204, 113));
        btnRegistrar.addActionListener(this::registrarUsuario);
        add(btnRegistrar);
        add(Box.createVerticalStrut(10));

        btnVolver = crearBoton("Volver", new Color(44, 62, 80));
        btnVolver.addActionListener(e -> {
            if (onVolver != null) onVolver.run();
        });
        add(btnVolver);

        lblMensaje = new JLabel("", SwingConstants.CENTER);
        lblMensaje.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblMensaje.setForeground(Color.LIGHT_GRAY);
        add(Box.createVerticalStrut(10));
        add(lblMensaje);

        cargarDeportes();
    }

    private void cargarDeportes() {
        List<DeporteDTO> deportes = DeporteController.getInstance().obtenerTodos();
        tablaModel.setData(deportes);
    }

    private JButton crearBoton(String texto, Color fondo) {
        JButton boton = new JButton(texto);
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setMaximumSize(new Dimension(400, 50));
        boton.setFont(new Font("Tahoma", Font.BOLD, 18));
        boton.setForeground(Color.WHITE);
        boton.setBackground(fondo);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return boton;
    }

    private JTextField crearCampoTexto(String label) {
        JLabel lbl = new JLabel(label);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Tahoma", Font.BOLD, 14));
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(lbl);

        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(400, 35));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(field);
        add(Box.createVerticalStrut(10));
        return field;
    }

    private JPasswordField crearCampoPassword(String label) {
        JLabel lbl = new JLabel(label);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Tahoma", Font.BOLD, 14));
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(lbl);

        JPasswordField field = new JPasswordField();
        field.setMaximumSize(new Dimension(400, 35));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(field);
        add(Box.createVerticalStrut(10));
        return field;
    }

    private void registrarUsuario(ActionEvent e) {
        try {
            UsuarioController uc = UsuarioController.getInstance();
            String nombre = txtNombre.getText().trim();
            String email = txtEmail.getText().trim();
            String contraseña = new String(txtContraseña.getPassword()).trim();
            String ubicacion = txtUbicacion.getText().trim();

            if (nombre.isEmpty() || email.isEmpty() || contraseña.isEmpty()) {
                lblMensaje.setText("Nombre, email y contraseña son obligatorios.");
                return;
            }

            UsuarioDTO usuario = new UsuarioDTO(nombre, email, contraseña, ubicacion);
            boolean ok = uc.registrar(usuario);

            if (!ok) {
                lblMensaje.setText("Error: ya existe un usuario con ese email.");
                return;
            }

            List<DeporteUsuarioDTO> preferencias = tablaModel.getPreferencias();
            if (!preferencias.isEmpty()) {
                uc.actualizarPreferencias(usuario.getEmail(), preferencias);
            }

            lblMensaje.setText("✓ Usuario registrado y preferencias guardadas.");

        } catch (Exception ex) {
            lblMensaje.setText("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static class PreferenciasTableModel extends AbstractTableModel {
        private final String[] columnas = {"Seleccionar", "Deporte", "Nivel", "Favorito"};
        private final List<DeporteDTO> deportes = new ArrayList<>();
        private final List<Boolean> seleccionados = new ArrayList<>();
        private final List<NivelJuego> niveles = new ArrayList<>();
        private int favoritoIndex = -1;

        public void setData(List<DeporteDTO> disponibles) {
            deportes.clear();
            seleccionados.clear();
            niveles.clear();
            for (DeporteDTO d : disponibles) {
                deportes.add(d);
                seleccionados.add(false);
                niveles.add(NivelJuego.INTERMEDIO);
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
