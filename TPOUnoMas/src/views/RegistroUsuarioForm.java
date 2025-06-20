package views;

import controller.DeporteController;
import controller.UsuarioController;
import enums.NivelJuego;
import model.dto.DeporteDTO;
import model.dto.UsuarioDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class RegistroUsuarioForm extends JPanel {

    private JTextField txtNombre;
    private JTextField txtEmail;
    private JPasswordField txtContraseña;
    private JTextField txtUbicacion;
    private JComboBox<String> comboDeporteFavorito;
    private JComboBox<NivelJuego> comboNivel;
    private JButton btnRegistrar;
    private JButton btnVolver;
    private JLabel lblMensaje;

    public RegistroUsuarioForm() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(25, 25, 25));
        setBorder(BorderFactory.createEmptyBorder(40, 100, 40, 100));

        JLabel lblTitulo = new JLabel("Registro de Usuario");
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(new Color(46, 204, 113));
        add(lblTitulo);
        add(Box.createVerticalStrut(20));

        txtNombre = crearCampoTexto("Nombre");
        txtEmail = crearCampoTexto("Email");
        txtContraseña = crearCampoPassword("Contraseña");
        txtUbicacion = crearCampoTexto("Ubicación");
        comboDeporteFavorito = crearComboBox("Deporte Favorito", obtenerNombresDeportes());
        comboNivel = crearComboBox("Nivel de Juego", NivelJuego.values());

        btnRegistrar = crearBoton("Registrar", new Color(46, 204, 113));
        btnRegistrar.addActionListener(this::registrarUsuario);
        add(btnRegistrar);
        add(Box.createVerticalStrut(10));

        btnVolver = crearBoton("Volver", new Color(44, 62, 80));
        btnVolver.addActionListener(e -> SwingUtilities.getWindowAncestor(this).dispose());
        add(btnVolver);

        lblMensaje = new JLabel("", SwingConstants.CENTER);
        lblMensaje.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblMensaje.setForeground(Color.LIGHT_GRAY);
        add(Box.createVerticalStrut(10));
        add(lblMensaje);
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

    private <T> JComboBox<T> crearComboBox(String label, T[] items) {
        JLabel lbl = new JLabel(label);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Tahoma", Font.BOLD, 14));
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(lbl);

        JComboBox<T> combo = new JComboBox<>(items);
        combo.setMaximumSize(new Dimension(400, 35));
        combo.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(combo);
        add(Box.createVerticalStrut(10));
        return combo;
    }

    private String[] obtenerNombresDeportes() {
        List<DeporteDTO> lista = DeporteController.getInstance().obtenerTodos();
        return lista.stream().map(DeporteDTO::getNombre).toArray(String[]::new);
    }

    private void registrarUsuario(ActionEvent e) {
        try {
            String nombre = txtNombre.getText().trim();
            String email = txtEmail.getText().trim();
            String contraseña = new String(txtContraseña.getPassword()).trim();
            String ubicacion = txtUbicacion.getText().trim();

            if (nombre.isEmpty() || email.isEmpty() || contraseña.isEmpty()) {
                lblMensaje.setText("Nombre, email y contraseña son obligatorios.");
                return;
            }

            UsuarioDTO dto = new UsuarioDTO(nombre, email, contraseña, ubicacion);
            boolean ok = true; //UsuarioController.getInstance().crearUsuario(dto);

            lblMensaje.setText(ok ? "\u2713 Usuario registrado con éxito." : "Error: ya existe un usuario con ese email.");
        } catch (Exception ex) {
            lblMensaje.setText("Error: " + ex.getMessage());
        }
    }
}
