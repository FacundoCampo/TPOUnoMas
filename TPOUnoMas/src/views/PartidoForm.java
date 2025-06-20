package views;

import controller.DeporteController;
import controller.PartidoController;
import enums.TipoEmparejamiento;
import model.dto.DeporteDTO;
import model.dto.PartidoDTO;
import strategy.EmparejamientoStrategyFactory;
import strategy.IEstrategiaEmparejamiento;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PartidoForm extends JPanel {

    private JComboBox<String> comboDeporte;
    private JTextField txtDuracion;
    private JTextField txtUbicacion;
    private JTextField txtFecha;
    private JComboBox<TipoEmparejamiento> comboEstrategia;
    private JButton btnCrear;
    private JButton btnVolver;
    private JLabel lblMensaje;

    public PartidoForm(JTabbedPane tabbedPane, JPanel panelAnterior) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(25, 25, 25));
        setBorder(BorderFactory.createEmptyBorder(40, 100, 40, 100));

        JLabel lblTitulo = new JLabel("Crear Partido");
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(new Color(46, 204, 113));
        add(lblTitulo);
        add(Box.createVerticalStrut(20));

        comboDeporte = crearComboBox("Deporte", obtenerNombresDeportesDesdeController());
        txtDuracion = crearCampoTexto("Duración (minutos)");
        txtUbicacion = crearCampoTexto("Ubicación");
        txtFecha = crearCampoTexto("Fecha y hora (dd/MM/yyyy HH:mm)");
        comboEstrategia = crearComboBox("Estrategia de emparejamiento", TipoEmparejamiento.values());

        btnCrear = crearBoton("Crear partido", new Color(46, 204, 113));
        btnCrear.addActionListener(this::crearPartido);
        add(btnCrear);
        add(Box.createVerticalStrut(10));

        btnVolver = crearBoton("Volver", new Color(44, 62, 80));
        btnVolver.addActionListener(e -> tabbedPane.setSelectedComponent(panelAnterior));
        add(btnVolver);

        lblMensaje = new JLabel("", SwingConstants.CENTER);
        lblMensaje.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblMensaje.setForeground(Color.LIGHT_GRAY);
        add(Box.createVerticalStrut(10));
        add(lblMensaje);
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

    private String[] obtenerNombresDeportesDesdeController() {
        List<DeporteDTO> lista = DeporteController.getInstance().obtenerTodos();
        return lista.stream().map(DeporteDTO::getNombre).toArray(String[]::new);
    }

    private void crearPartido(ActionEvent e) {
        try {
            String nombreDeporte = (String) comboDeporte.getSelectedItem();
            String duracionStr = txtDuracion.getText().trim();
            String ubicacion = txtUbicacion.getText().trim();
            String fechaStr = txtFecha.getText().trim();
            TipoEmparejamiento tipo = (TipoEmparejamiento) comboEstrategia.getSelectedItem();

            if (nombreDeporte == null || duracionStr.isEmpty() || ubicacion.isEmpty() || fechaStr.isEmpty()) {
                lblMensaje.setText("Todos los campos son obligatorios.");
                return;
            }

            int duracion = Integer.parseInt(duracionStr);
            Date fecha = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(fechaStr);

            DeporteDTO deporte = DeporteController.getInstance().buscarPorNombre(nombreDeporte);
            if (deporte == null) {
                lblMensaje.setText("Deporte no válido.");
                return;
            }

            PartidoDTO dto = new PartidoDTO(deporte, duracion, ubicacion, fecha);
            IEstrategiaEmparejamiento estrategia = EmparejamientoStrategyFactory.crear(tipo);
            PartidoController.getInstance().crearPartido(dto, estrategia);

            lblMensaje.setText("\u2713 Partido creado correctamente.");
        } catch (Exception ex) {
            lblMensaje.setText("Error: " + ex.getMessage());
        }
    }
}
