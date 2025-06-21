package views;

import controller.UsuarioController;
import enums.NivelJuego;
import model.dto.DeporteDTO;
import model.dto.DeporteUsuarioDTO;
import model.dto.UsuarioDTO;
import services.DeporteService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;

public class PreferenciasDeportivasPanel extends JPanel {

    private final String usuarioId;
    private final UsuarioDTO usuario;
    private final JList<String> listaDeportes;
    private final JPanel panelNiveles;
    private final Map<String, JComboBox<NivelJuego>> nivelesPorDeporte;
    private final JLabel lblMensaje;
    private final DeporteService deporteService = new DeporteService();

    public PreferenciasDeportivasPanel(String usuarioId) {
        this.usuarioId = usuarioId;
        this.usuario = UsuarioController.getInstance().buscarPorId(usuarioId);
        this.nivelesPorDeporte = new HashMap<>();

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
        listaDeportes.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scroll = new JScrollPane(listaDeportes);
        scroll.setMaximumSize(new Dimension(400, 80));
        add(scroll);

        panelNiveles = new JPanel();
        panelNiveles.setLayout(new BoxLayout(panelNiveles, BoxLayout.Y_AXIS));
        panelNiveles.setOpaque(false);
        add(panelNiveles);

        listaDeportes.addListSelectionListener(e -> actualizarPanelNiveles());

        JButton btnGuardar = new JButton("Guardar preferencias");
        btnGuardar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnGuardar.setBackground(new Color(46, 204, 113));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.addActionListener(this::guardarPreferencias);
        add(Box.createVerticalStrut(10));
        add(btnGuardar);

        lblMensaje = new JLabel("", SwingConstants.CENTER);
        lblMensaje.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblMensaje.setForeground(Color.LIGHT_GRAY);
        add(Box.createVerticalStrut(10));
        add(lblMensaje);
    }

    private void actualizarPanelNiveles() {
        panelNiveles.removeAll();
        nivelesPorDeporte.clear();

        for (String nombre : listaDeportes.getSelectedValuesList()) {
            JLabel lbl = new JLabel("Nivel para " + nombre);
            lbl.setForeground(Color.WHITE);
            lbl.setFont(new Font("Tahoma", Font.PLAIN, 14));
            panelNiveles.add(lbl);

            JComboBox<NivelJuego> combo = new JComboBox<>(NivelJuego.values());
            combo.setMaximumSize(new Dimension(400, 30));
            panelNiveles.add(combo);
            panelNiveles.add(Box.createVerticalStrut(5));
            nivelesPorDeporte.put(nombre, combo);
        }

        panelNiveles.revalidate();
        panelNiveles.repaint();
    }

    private String[] obtenerNombresDeportes() {
        List<DeporteDTO> lista = deporteService.obtenerTodos();
        List<String> nombres = new ArrayList<>();
        for (DeporteDTO d : lista) {
            nombres.add(d.getNombre());
        }
        return nombres.toArray(new String[0]);
    }

    private void guardarPreferencias(ActionEvent e) {
        List<DeporteUsuarioDTO> preferencias = new ArrayList<>();

        for (String nombre : listaDeportes.getSelectedValuesList()) {
            JComboBox<NivelJuego> combo = nivelesPorDeporte.get(nombre);
            NivelJuego nivel = (NivelJuego) combo.getSelectedItem();

            DeporteDTO deporte = null;
            for (DeporteDTO dto : deporteService.obtenerTodos()) {
                if (dto.getNombre().equals(nombre)) {
                    deporte = dto;
                    break;
                }
            }

            if (deporte != null && nivel != null) {
                preferencias.add(new DeporteUsuarioDTO(deporte, nivel));
            }
        }

        try {
            UsuarioController.getInstance().actualizarPreferencias(usuarioId, preferencias);
            lblMensaje.setText("✓ Preferencias guardadas correctamente.");
        } catch (Exception ex) {
            lblMensaje.setText("Error al guardar preferencias: " + ex.getMessage());
        }
    }

    public JTabbedPane getTabbedPane() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Preferencias", this);
        return tabs;
    }
}
