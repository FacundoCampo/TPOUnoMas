package views;

import controller.DeporteController;
import model.dto.DeporteDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class DeporteForm extends JPanel {

    private JTextField txtNombre;
    private JTextField txtCantidadJugadores;
    private JButton btnCrear;
    private JButton btnVolver;
    private JLabel lblMensaje;
    private JList<String> listaDeportes;
    private DefaultListModel<String> listModel;

    public DeporteForm() {
        setLayout(new BorderLayout());
        setBackground(new Color(25, 25, 25));
        setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // Panel superior con el formulario
        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
        panelFormulario.setBackground(new Color(25, 25, 25));

        JLabel lblTitulo = new JLabel("Gestión de Deportes");
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(new Color(46, 204, 113));
        panelFormulario.add(lblTitulo);
        panelFormulario.add(Box.createVerticalStrut(20));

        // Campos del formulario
        txtNombre = crearCampoTexto(panelFormulario, "Nombre del deporte");
        txtCantidadJugadores = crearCampoTexto(panelFormulario, "Cantidad de jugadores por equipo");

        // Botones
        btnCrear = crearBoton("Crear Deporte", new Color(46, 204, 113));
        btnCrear.addActionListener(this::crearDeporte);
        panelFormulario.add(btnCrear);
        panelFormulario.add(Box.createVerticalStrut(10));

        lblMensaje = new JLabel("", SwingConstants.CENTER);
        lblMensaje.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblMensaje.setForeground(Color.LIGHT_GRAY);
        panelFormulario.add(Box.createVerticalStrut(10));
        panelFormulario.add(lblMensaje);

        add(panelFormulario, BorderLayout.NORTH);

        // Panel inferior con la lista de deportes existentes
        JPanel panelLista = new JPanel(new BorderLayout());
        panelLista.setBackground(new Color(25, 25, 25));
        panelLista.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JLabel lblDeportesExistentes = new JLabel("Deportes existentes:");
        lblDeportesExistentes.setForeground(Color.WHITE);
        lblDeportesExistentes.setFont(new Font("Tahoma", Font.BOLD, 16));
        panelLista.add(lblDeportesExistentes, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        listaDeportes = new JList<>(listModel);
        listaDeportes.setBackground(new Color(30, 30, 30));
        listaDeportes.setForeground(Color.WHITE);
        listaDeportes.setFont(new Font("Tahoma", Font.PLAIN, 14));
        
        JScrollPane scrollPane = new JScrollPane(listaDeportes);
        scrollPane.setPreferredSize(new Dimension(500, 200));
        scrollPane.setBackground(new Color(25, 25, 25));
        panelLista.add(scrollPane, BorderLayout.CENTER);

        add(panelLista, BorderLayout.CENTER);

        // Cargar deportes existentes
        cargarDeportesExistentes();
    }

    private JTextField crearCampoTexto(JPanel panel, String label) {
        JLabel lbl = new JLabel(label);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Tahoma", Font.BOLD, 14));
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lbl);

        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(400, 35));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(field);
        panel.add(Box.createVerticalStrut(10));
        return field;
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

    private void crearDeporte(ActionEvent e) {
        try {
            String nombre = txtNombre.getText().trim();
            String cantidadStr = txtCantidadJugadores.getText().trim();

            if (nombre.isEmpty() || cantidadStr.isEmpty()) {
                lblMensaje.setText("Todos los campos son obligatorios.");
                lblMensaje.setForeground(Color.RED);
                return;
            }

            int cantidadJugadores = Integer.parseInt(cantidadStr);
            
            if (cantidadJugadores <= 0) {
                lblMensaje.setText("La cantidad de jugadores debe ser mayor a 0.");
                lblMensaje.setForeground(Color.RED);
                return;
            }

            DeporteDTO dto = new DeporteDTO(null, nombre, cantidadJugadores);
            DeporteController.getInstance().crearDeporte(dto);

            lblMensaje.setText("✓ Deporte '" + nombre + "' creado correctamente. Recuerda actualizar las listas en Preferencias y Crear Partido.");
            lblMensaje.setForeground(new Color(46, 204, 113));

            // Limpiar campos
            txtNombre.setText("");
            txtCantidadJugadores.setText("");

            // Actualizar lista
            cargarDeportesExistentes();

        } catch (NumberFormatException ex) {
            lblMensaje.setText("La cantidad de jugadores debe ser un número válido.");
            lblMensaje.setForeground(Color.RED);
        } catch (Exception ex) {
            lblMensaje.setText("Error: " + ex.getMessage());
            lblMensaje.setForeground(Color.RED);
        }
    }

    private void cargarDeportesExistentes() {
        listModel.clear();
        DeporteController.getInstance().obtenerTodos().forEach(deporte -> {
            listModel.addElement(deporte.getNombre() + " (" + deporte.getCantidadJugadoresEstandar() + " jugadores)");
        });
    }
}