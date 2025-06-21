package views;

import controller.PartidoController;
import enums.TipoEmparejamiento;
import model.dto.PartidoDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.stream.Collectors;

public class ListaPartidosPanel extends JPanel {

    private String usuarioid;

    private JComboBox<String> comboEstado;
    private DefaultListModel<PartidoDTO> listModel;
    private JList<PartidoDTO> listaPartidos;
    private JLabel lblMensaje;

    public ListaPartidosPanel(String usuarioid) {
        this.usuarioid = usuarioid;
        setLayout(new BorderLayout());
        setBackground(new Color(25, 25, 25));

        // -------- TOP PANEL --------
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.setBackground(new Color(25, 25, 25));

        JLabel lblEstado = new JLabel("Filtrar por estado:");
        lblEstado.setForeground(Color.WHITE);
        lblEstado.setFont(new Font("Tahoma", Font.BOLD, 16));
        topPanel.add(lblEstado);

        comboEstado = new JComboBox<>();
        comboEstado.addItem("Todos");
        cargarEstados();
        comboEstado.setPreferredSize(new Dimension(180, 35));
        comboEstado.setFont(new Font("Tahoma", Font.PLAIN, 14));
        comboEstado.setBackground(Color.WHITE);
        comboEstado.setForeground(Color.BLACK);
        comboEstado.addActionListener(this::filtrarPartidos);
        topPanel.add(comboEstado);

        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.setPreferredSize(new Dimension(100, 35));
        btnActualizar.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setBackground(new Color(44, 62, 80));
        btnActualizar.setFocusPainted(false);
        btnActualizar.setBorderPainted(false);
        btnActualizar.addActionListener(e -> cargarPartidos());
        topPanel.add(btnActualizar);

        add(topPanel, BorderLayout.NORTH);

        // -------- CENTER PANEL (Lista) --------
        listModel = new DefaultListModel<>();
        listaPartidos = new JList<>(listModel);
        listaPartidos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaPartidos.setCellRenderer(new PartidoRenderer());
        listaPartidos.setBackground(new Color(25, 25, 25));
        listaPartidos.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(listaPartidos);
        scrollPane.setPreferredSize(new Dimension(600, 350));
        scrollPane.setBackground(new Color(25, 25, 25));
        scrollPane.getViewport().setBackground(new Color(25, 25, 25));
        add(scrollPane, BorderLayout.CENTER);

        // -------- BOTTOM PANEL --------
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBackground(new Color(25, 25, 25));

        JButton btnUnirse = new JButton("Sumarse al partido");
        btnUnirse.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnUnirse.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnUnirse.setForeground(Color.WHITE);
        btnUnirse.setBackground(new Color(44, 62, 80));
        btnUnirse.setFocusPainted(false);
        btnUnirse.setBorderPainted(false);
        btnUnirse.setPreferredSize(new Dimension(200, 40));
        btnUnirse.addActionListener(this::sumarseAPartido);
        bottomPanel.add(btnUnirse);

        bottomPanel.add(Box.createVerticalStrut(8));

        lblMensaje = new JLabel("", SwingConstants.CENTER);
        lblMensaje.setForeground(Color.LIGHT_GRAY);
        lblMensaje.setAlignmentX(Component.CENTER_ALIGNMENT);
        bottomPanel.add(lblMensaje);

        add(bottomPanel, BorderLayout.SOUTH);

        // Cargar lista inicial
        cargarPartidos();
    }

    private void cargarEstados() {
        List<PartidoDTO> todos = PartidoController.getInstance().obtenerTodos();
        todos.stream()
                .map(p -> p.getEstado().getNombre())
                .distinct()
                .forEach(comboEstado::addItem);
    }

    private void cargarPartidos() {
        listModel.clear();
        List<PartidoDTO> partidos = PartidoController.getInstance().obtenerTodos();
        for (PartidoDTO dto : partidos) {
            listModel.addElement(dto);
        }
    }

    private void filtrarPartidos(ActionEvent e) {
        String estadoSeleccionado = (String) comboEstado.getSelectedItem();
        listModel.clear();
        List<PartidoDTO> partidos = PartidoController.getInstance().obtenerTodos();

        List<PartidoDTO> filtrados = "Todos".equals(estadoSeleccionado) ?
                partidos :
                partidos.stream()
                        .filter(p -> p.getEstado().getNombre().equals(estadoSeleccionado))
                        .collect(Collectors.toList());

        for (PartidoDTO dto : filtrados) {
            listModel.addElement(dto);
        }
    }

    private void sumarseAPartido(ActionEvent e) {
        PartidoDTO seleccionado = listaPartidos.getSelectedValue();
        if (seleccionado == null) {
            lblMensaje.setText("Debe seleccionar un partido.");
            return;
        }

        if (!seleccionado.getEstado().puedeAgregarJugadores()) {
            lblMensaje.setText("Este partido no acepta más jugadores.");
            return;
        }

        boolean resultado = PartidoController.getInstance().sumarseAlPartido(seleccionado.getId(), this.usuarioid);
        lblMensaje.setText(resultado ? "\u2713 Te uniste al partido correctamente" : "No fue posible unirse al partido");
    }

    private static class PartidoRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof PartidoDTO dto) {
                String deporte = dto.getDeporte().getNombre();
                String fecha = new java.text.SimpleDateFormat("EEE dd/MM/yyyy HH:mm").format(dto.getFechaHora());
                String estado = dto.getEstado().getNombre();
                String organizador = dto.getOrganizador();
                String estrategia = dto.getTipoEmparejamiento() != null ? dto.getTipoEmparejamiento().name() : "(Sin estrategia)";

                int jugadoresActuales = dto.getJugadoresInscritos().size();
                int jugadoresTotales = dto.getDeporte().getCantidadJugadoresEstandar();
                int faltan = jugadoresTotales - jugadoresActuales;
                String faltanTexto = (faltan > 0) ? String.format(" (faltan %d)", faltan) : "";

                String texto = String.format(
                        "%s - %s - Estado: %s | Organizador: %s | Jugadores: %d/%d%s | Estrategia: %s",
                        deporte, fecha, estado, organizador, jugadoresActuales, jugadoresTotales, faltanTexto, estrategia
                );

                setText(texto);
                setForeground(Color.WHITE);
                setBackground(isSelected ? new Color(44, 62, 80) : new Color(34, 34, 34));
            }

            return this;
        }
    }

}
