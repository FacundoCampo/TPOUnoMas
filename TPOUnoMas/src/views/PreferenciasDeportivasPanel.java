package views;

import controller.DeporteController;
import controller.UsuarioController;
import enums.NivelJuego;
import model.dto.DeporteDTO;
import model.dto.DeporteUsuarioDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;

public class PreferenciasDeportivasPanel extends JPanel {

    private final String usuarioId;
    private final JList<String> listaDeportes;
    private final JPanel panelNiveles;
    private final Map<String, JComboBox<NivelJuego>> nivelesPorDeporte;
    private final JLabel lblMensaje;
    private final ButtonGroup grupoFavoritos;
    private final Map<String, JRadioButton> favoritosPorDeporte;
    private List<DeporteDTO> deportes;
    private java.util.List<DeporteUsuarioDTO> preferenciasPrevias;

    public PreferenciasDeportivasPanel(String usuarioId) {
        this.usuarioId = usuarioId;
        this.nivelesPorDeporte = new HashMap<>();
        this.favoritosPorDeporte = new HashMap<>();
        this.grupoFavoritos = new ButtonGroup();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(25, 25, 25));
        setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JLabel lblTitulo = new JLabel("Tus preferencias deportivas");
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(46, 204, 113));
        add(lblTitulo);
        add(Box.createVerticalStrut(20));

        JLabel lblDep = new JLabel("Deportes favoritos:");
        lblDep.setForeground(Color.WHITE);
        lblDep.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblDep.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(lblDep);

        String[] deportes = obtenerNombresDeportes();
        listaDeportes = new JList<>(deportes);
        listaDeportes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(listaDeportes);
        scroll.setMaximumSize(new Dimension(400, 80));
        add(scroll);

        panelNiveles = new JPanel();
        panelNiveles.setLayout(new BoxLayout(panelNiveles, BoxLayout.Y_AXIS));
        panelNiveles.setOpaque(false);
        panelNiveles.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel panelNivelesConMargen = new JPanel();
        panelNivelesConMargen.setOpaque(false);
        panelNivelesConMargen.setLayout(new BoxLayout(panelNivelesConMargen, BoxLayout.Y_AXIS));
        panelNivelesConMargen.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        panelNivelesConMargen.add(panelNiveles);
        panelNivelesConMargen.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(panelNivelesConMargen);

        listaDeportes.addListSelectionListener(e -> actualizarPanelNiveles());

        JButton btnGuardar = crearBotonVerde("Guardar preferencias");
        btnGuardar.addActionListener(this::guardarPreferencias);
        add(btnGuardar);

        lblMensaje = new JLabel("", SwingConstants.CENTER);
        lblMensaje.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblMensaje.setForeground(Color.LIGHT_GRAY);
        add(Box.createVerticalStrut(10));
        add(lblMensaje);

        cargarPreferenciasPrevias();
    }

    private JButton crearBotonVerde(String texto) {
        JButton boton = new JButton(texto);
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setMaximumSize(new Dimension(400, 45));
        boton.setFont(new Font("Tahoma", Font.BOLD, 18));
        boton.setForeground(Color.WHITE);
        boton.setBackground(new Color(46, 204, 113));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return boton;
    }

    private void actualizarPanelNiveles() {
        panelNiveles.removeAll();
        nivelesPorDeporte.clear();
        favoritosPorDeporte.clear();
        grupoFavoritos.clearSelection();

        String nombre = listaDeportes.getSelectedValue();
        if (nombre != null) {
            JLabel lbl = new JLabel("Nivel para " + nombre);
            lbl.setForeground(Color.WHITE);
            lbl.setFont(new Font("Tahoma", Font.PLAIN, 14));
            lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelNiveles.add(lbl);

            JComboBox<NivelJuego> combo = new JComboBox<>(NivelJuego.values());
            combo.setMaximumSize(new Dimension(400, 30));
            combo.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelNiveles.add(combo);
            nivelesPorDeporte.put(nombre, combo);

            JRadioButton favorito = new JRadioButton("Marcar como favorito");
            favorito.setForeground(Color.WHITE);
            favorito.setOpaque(false);
            favorito.setAlignmentX(Component.CENTER_ALIGNMENT);
            grupoFavoritos.add(favorito);
            favoritosPorDeporte.put(nombre, favorito);
            panelNiveles.add(Box.createVerticalStrut(5));
            panelNiveles.add(favorito);

            for (DeporteUsuarioDTO pref : preferenciasPrevias) {
                if (pref.getDeporte().getNombre().equals(nombre)) {
                    combo.setSelectedItem(pref.getNivel());
                    if (pref.esFavorito()) favorito.setSelected(true);
                }
            }
        }

        panelNiveles.revalidate();
        panelNiveles.repaint();
    }

    private String[] obtenerNombresDeportes() {
        DeporteController dc = DeporteController.getInstance();
        deportes = dc.obtenerTodos();
        java.util.List<String> nombres = new ArrayList<>();
        for (DeporteDTO d : deportes) {
            nombres.add(d.getNombre());
        }
        return nombres.toArray(new String[0]);
    }

    private void guardarPreferencias(ActionEvent e) {
        java.util.List<DeporteUsuarioDTO> preferencias = new ArrayList<>();
        String favoritoSeleccionado = null;

        for (String nombre : nivelesPorDeporte.keySet()) {
            JComboBox<NivelJuego> combo = nivelesPorDeporte.get(nombre);
            NivelJuego nivel = (NivelJuego) combo.getSelectedItem();

            DeporteDTO deporte = null;
            for (DeporteDTO dto : deportes) {
                if (dto.getNombre().equals(nombre)) {
                    deporte = dto;
                    break;
                }
            }

            if (deporte != null && nivel != null) {
                boolean esFavorito = favoritosPorDeporte.containsKey(nombre) && favoritosPorDeporte.get(nombre).isSelected();
                if (esFavorito) favoritoSeleccionado = nombre;
                preferencias.add(new DeporteUsuarioDTO(deporte, nivel, esFavorito));
            }
        }

        try {
            UsuarioController.getInstance().actualizarPreferencias(usuarioId, preferencias);
            lblMensaje.setText("✓ Preferencias guardadas correctamente.");
        } catch (Exception ex) {
            lblMensaje.setText("Error al guardar preferencias: " + ex.getMessage());
        }
    }

    private void cargarPreferenciasPrevias() {
        UsuarioController uc = UsuarioController.getInstance();
        preferenciasPrevias = uc.obtenerPrefrecias(usuarioId);
        if (preferenciasPrevias == null || preferenciasPrevias.isEmpty()) return;

        listaDeportes.setSelectedValue(preferenciasPrevias.get(0).getDeporte().getNombre(), true);
    }
}
