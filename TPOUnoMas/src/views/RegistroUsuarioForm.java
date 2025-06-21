package views;

import controller.DeporteController;
import controller.UsuarioController;
import enums.NivelJuego;
import model.dto.DeporteDTO;
import model.dto.DeporteUsuarioDTO;
import model.dto.UsuarioDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class RegistroUsuarioForm extends JPanel {

    private JTextField txtNombre;
    private JTextField txtEmail;
    private JPasswordField txtContraseña;
    private JTextField txtUbicacion;
    private JList<String> listaDeportes;
    private JPanel panelNiveles;
    private Map<String, JComboBox<NivelJuego>> nivelesPorDeporte;
    private Map<String, JRadioButton> favoritosPorDeporte;
    private ButtonGroup grupoFavoritos;
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
        this.nivelesPorDeporte = new HashMap<>();
        this.favoritosPorDeporte = new HashMap<>();
        this.grupoFavoritos = new ButtonGroup();

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

        JLabel lblDep = new JLabel("Deportes Favoritos");
        lblDep.setForeground(Color.WHITE);
        lblDep.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblDep.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(lblDep);

        String[] deportes = obtenerNombresDeportes();
        listaDeportes = new JList<>(deportes);
        listaDeportes.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listaDeportes.setVisibleRowCount(5);
        JScrollPane scroll = new JScrollPane(listaDeportes);
        scroll.setMaximumSize(new Dimension(400, 80));
        scroll.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(scroll);

        panelNiveles = new JPanel();
        panelNiveles.setLayout(new BoxLayout(panelNiveles, BoxLayout.Y_AXIS));
        panelNiveles.setOpaque(false);
        panelNiveles.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(panelNiveles);

        listaDeportes.addListSelectionListener(e -> actualizarPanelNiveles());

        btnRegistrar = crearBoton(usuarioExistente == null ? "Registrar" : "Guardar Cambios", new Color(46, 204, 113));
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
    }

    private void actualizarPanelNiveles() {
        panelNiveles.removeAll();
        nivelesPorDeporte.clear();
        favoritosPorDeporte.clear();
        grupoFavoritos = new ButtonGroup();

        for (String nombre : listaDeportes.getSelectedValuesList()) {
            JPanel fila = new JPanel();
            fila.setLayout(new BoxLayout(fila, BoxLayout.Y_AXIS));
            fila.setOpaque(false);
            fila.setMaximumSize(new Dimension(400, 100));
            fila.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel lbl = new JLabel("Nivel para " + nombre);
            lbl.setForeground(Color.WHITE);
            lbl.setFont(new Font("Tahoma", Font.BOLD, 14));
            lbl.setAlignmentX(Component.CENTER_ALIGNMENT);

            JComboBox<NivelJuego> combo = new JComboBox<>(NivelJuego.values());
            combo.setMaximumSize(new Dimension(400, 30));
            combo.setAlignmentX(Component.CENTER_ALIGNMENT);

            JRadioButton favorito = new JRadioButton("Marcar como favorito");
            favorito.setForeground(Color.LIGHT_GRAY);
            favorito.setOpaque(false);
            favorito.setAlignmentX(Component.CENTER_ALIGNMENT);
            grupoFavoritos.add(favorito);

            fila.add(lbl);
            fila.add(Box.createVerticalStrut(5));
            fila.add(combo);
            fila.add(Box.createVerticalStrut(5));
            fila.add(favorito);

            panelNiveles.add(fila);
            panelNiveles.add(Box.createVerticalStrut(10));

            nivelesPorDeporte.put(nombre, combo);
            favoritosPorDeporte.put(nombre, favorito);
        }

        panelNiveles.revalidate();
        panelNiveles.repaint();
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

    private String[] obtenerNombresDeportes() {
        List<DeporteDTO> lista = DeporteController.getInstance().obtenerTodos();
        List<String> nombres = new ArrayList<>();

        for (DeporteDTO d : lista) {
            nombres.add(d.getNombre());
        }

        String[] resultado = new String[nombres.size()];
        for (int i = 0; i < nombres.size(); i++) {
            resultado[i] = nombres.get(i);
        }

        return resultado;
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

            String deporteFavorito = null;
            for (Map.Entry<String, JRadioButton> entry : favoritosPorDeporte.entrySet()) {
                if (entry.getValue().isSelected()) {
                    deporteFavorito = entry.getKey();
                    break;
                }
            }

            List<DeporteUsuarioDTO> preferencias = new ArrayList<>();
            for (Map.Entry<String, JComboBox<NivelJuego>> entry : nivelesPorDeporte.entrySet()) {
                String nombreDeporte = entry.getKey();
                NivelJuego nivel = (NivelJuego) entry.getValue().getSelectedItem();

                DeporteDTO dto = DeporteController.getInstance().obtenerTodos().stream()
                        .filter(d -> d.getNombre().equals(nombreDeporte))
                        .findFirst().orElse(null);

                if (dto != null && nivel != null) {
                    boolean esFavorito = nombreDeporte.equals(deporteFavorito);
                    preferencias.add(new DeporteUsuarioDTO(dto, nivel, esFavorito));
                }
            }

            if (!preferencias.isEmpty()) {
                uc.actualizarPreferencias(usuario.getEmail(), preferencias);
            }

            lblMensaje.setText("✓ Usuario registrado y preferencias guardadas.");

        } catch (Exception ex) {
            lblMensaje.setText("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
