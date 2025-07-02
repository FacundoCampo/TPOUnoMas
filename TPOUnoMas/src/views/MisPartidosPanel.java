package views;

import controller.PartidoController;
import enums.TipoEmparejamiento;
import model.dto.PartidoDTO;
import model.dto.UsuarioDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

public class MisPartidosPanel extends JPanel {

    private final String usuarioid;
    private final DefaultListModel<PartidoDTO> listModel;
    private final JList<PartidoDTO> listaPartidos;
    private final JLabel lblMensaje;

    public MisPartidosPanel(String usuarioid) {
        this.usuarioid = usuarioid;

        setLayout(new BorderLayout());
        setBackground(new Color(25, 25, 25));

        // -------- TOP PANEL --------
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.setBackground(new Color(25, 25, 25));

        JLabel lblTitulo = new JLabel("Mis Partidos como Organizador");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);
        topPanel.add(lblTitulo);

        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.setPreferredSize(new Dimension(120, 35));
        btnActualizar.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setBackground(new Color(44, 62, 80));
        btnActualizar.setFocusPainted(false);
        btnActualizar.setBorderPainted(false);
        btnActualizar.addActionListener(e -> cargarPartidos());
        topPanel.add(btnActualizar);

        add(topPanel, BorderLayout.NORTH);

        // -------- CENTER PANEL --------
        listModel = new DefaultListModel<>();
        listaPartidos = new JList<>(listModel);
        listaPartidos.setCellRenderer(new PartidoRenderer());
        listaPartidos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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

        JButton btnCancelar = new JButton("Cancelar partido");
        btnCancelar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCancelar.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setBackground(new Color(192, 57, 43));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setBorderPainted(false);
        btnCancelar.setPreferredSize(new Dimension(200, 40));
        btnCancelar.addActionListener(this::cancelarPartido);
        bottomPanel.add(btnCancelar);

        JButton btnEmparejar = new JButton("Emparejar");
        btnEmparejar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnEmparejar.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnEmparejar.setForeground(Color.WHITE);
        btnEmparejar.setBackground(new Color(39, 174, 96));
        btnEmparejar.setFocusPainted(false);
        btnEmparejar.setBorderPainted(false);
        btnEmparejar.setPreferredSize(new Dimension(200, 40));
        btnEmparejar.addActionListener(this::emparejarPartido);
        bottomPanel.add(Box.createVerticalStrut(8));
        bottomPanel.add(btnEmparejar);

        JButton btnCambiarEstrategia = new JButton("Cambiar estrategia");
        btnCambiarEstrategia.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCambiarEstrategia.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnCambiarEstrategia.setForeground(Color.WHITE);
        btnCambiarEstrategia.setBackground(new Color(41, 128, 185));
        btnCambiarEstrategia.setFocusPainted(false);
        btnCambiarEstrategia.setBorderPainted(false);
        btnCambiarEstrategia.setPreferredSize(new Dimension(200, 40));
        btnCambiarEstrategia.addActionListener(this::cambiarEstrategia);
        bottomPanel.add(Box.createVerticalStrut(8));
        bottomPanel.add(btnCambiarEstrategia);

        bottomPanel.add(Box.createVerticalStrut(8));

        lblMensaje = new JLabel("", SwingConstants.CENTER);
        lblMensaje.setForeground(Color.LIGHT_GRAY);
        lblMensaje.setAlignmentX(Component.CENTER_ALIGNMENT);
        bottomPanel.add(lblMensaje);

        add(bottomPanel, BorderLayout.SOUTH);

        cargarPartidos();
    }

    private void cargarPartidos() {
        listModel.clear();
        List<PartidoDTO> partidos = PartidoController.getInstance().obtenerPartidosDelUsuario(this.usuarioid);
        for (PartidoDTO dto : partidos) {
            listModel.addElement(dto);
        }
    }

    private void cancelarPartido(ActionEvent e) {
        PartidoDTO seleccionado = listaPartidos.getSelectedValue();
        if (seleccionado == null) {
            lblMensaje.setText("Debe seleccionar un partido para cancelar.");
            return;
        }

        boolean resultado = PartidoController.getInstance().cancelarPartido(seleccionado.getId(), null);
        lblMensaje.setText(resultado ? "✔ Partido cancelado correctamente." : "No se pudo cancelar el partido.");
    }

    private void emparejarPartido(ActionEvent e) {
        PartidoDTO seleccionado = listaPartidos.getSelectedValue();
        if (seleccionado == null) {
            lblMensaje.setText("Debe seleccionar un partido para emparejar.");
            return;
        }

        List<UsuarioDTO> emparejados = PartidoController.getInstance().emparejar(seleccionado.getId());

        if (emparejados == null || emparejados.isEmpty()) {
            lblMensaje.setText("No se encontraron jugadores emparejados.");
            return;
        }

        String mensaje = emparejados.stream()
                .map(UsuarioDTO::getEmail)
                .collect(Collectors.joining("\n"));

        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Resultado del Emparejamiento", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(new Color(25, 25, 25));

        JLabel titulo = new JLabel("Jugadores emparejados:");
        titulo.setFont(new Font("Tahoma", Font.BOLD, 16));
        titulo.setForeground(Color.WHITE);
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        JTextArea textArea = new JTextArea(mensaje);
        textArea.setEditable(false);
        textArea.setBackground(new Color(44, 62, 80));
        textArea.setForeground(Color.WHITE);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton cerrar = new JButton("Cerrar");
        cerrar.setFont(new Font("Tahoma", Font.BOLD, 14));
        cerrar.setForeground(Color.WHITE);
        cerrar.setBackground(new Color(41, 128, 185));
        cerrar.setFocusPainted(false);
        cerrar.setBorderPainted(false);
        cerrar.addActionListener(ev -> dialog.dispose());

        JPanel panelBoton = new JPanel();
        panelBoton.setBackground(new Color(25, 25, 25));
        panelBoton.add(cerrar);

        dialog.add(titulo, BorderLayout.NORTH);
        dialog.add(new JScrollPane(textArea), BorderLayout.CENTER);
        dialog.add(panelBoton, BorderLayout.SOUTH);

        dialog.setVisible(true);

        lblMensaje.setText("✔ Emparejamiento realizado con éxito.");
    }

    private void cambiarEstrategia(ActionEvent e) {
        PartidoDTO seleccionado = listaPartidos.getSelectedValue();
        if (seleccionado == null) {
            lblMensaje.setText("Debe seleccionar un partido para cambiar la estrategia.");
            return;
        }

        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Cambiar estrategia", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setSize(350, 180);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(new Color(25, 25, 25));

        JLabel label = new JLabel("Seleccioná una nueva estrategia:");
        label.setFont(new Font("Tahoma", Font.BOLD, 15));
        label.setForeground(Color.WHITE);
        label.setBorder(BorderFactory.createEmptyBorder(15, 15, 0, 15));

        JComboBox<TipoEmparejamiento> combo = new JComboBox<>(TipoEmparejamiento.values());
        combo.setSelectedItem(seleccionado.getTipoEmparejamiento());
        combo.setFont(new Font("Tahoma", Font.PLAIN, 14));
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setBackground(isSelected ? new Color(60, 80, 100) : new Color(44, 62, 80));
                label.setForeground(Color.white);
                label.setFont(new Font("Tahoma", Font.PLAIN, 14));
                return label;
            }
        });
        combo.setBackground(new Color(44, 62, 80));
        combo.setForeground(Color.black);
        combo.setFocusable(false);
        combo.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(new Color(25, 25, 25));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(combo, BorderLayout.CENTER);

        JButton btnAceptar = new JButton("Cambiar");
        btnAceptar.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnAceptar.setForeground(Color.WHITE);
        btnAceptar.setBackground(new Color(39, 174, 96));
        btnAceptar.setFocusPainted(false);
        btnAceptar.setBorderPainted(false);
        btnAceptar.addActionListener(ev -> {
            TipoEmparejamiento seleccion = (TipoEmparejamiento) combo.getSelectedItem();
            boolean resultado = PartidoController.getInstance().cambiarTipoEmparejamiento(seleccionado.getId(), seleccion);
            lblMensaje.setText(resultado ? "✔ Estrategia cambiada a: " + seleccion : "No se pudo cambiar la estrategia.");
            dialog.dispose();
        });

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setBackground(new Color(192, 57, 43));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setBorderPainted(false);
        btnCancelar.addActionListener(ev -> dialog.dispose());

        JPanel botonera = new JPanel();
        botonera.setBackground(new Color(25, 25, 25));
        botonera.add(btnAceptar);
        botonera.add(Box.createHorizontalStrut(10));
        botonera.add(btnCancelar);

        dialog.add(label, BorderLayout.NORTH);
        dialog.add(centerPanel, BorderLayout.CENTER);
        dialog.add(botonera, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private static class PartidoRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof PartidoDTO dto) {
                String deporte = dto.getDeporte().getNombre();
                String fecha = new SimpleDateFormat("EEE dd/MM/yyyy HH:mm").format(dto.getFechaHora());
                String estado = dto.getEstado().toString();
                String estrategia = dto.getTipoEmparejamiento().name();
                int actuales = dto.getJugadoresInscritos().size();
                int total = dto.getDeporte().getCantidadJugadoresEstandar();
                int faltan = total - actuales;
                String faltanTexto = faltan > 0 ? String.format(" (faltan %d)", faltan) : "";

                setText(String.format(
                        "%s - %s\nEstado: %s | Estrategia: %s | Jugadores: %d/%d%s",
                        deporte, fecha, estado, estrategia, actuales, total, faltanTexto
                ));

                setForeground(Color.WHITE);
                setBackground(isSelected ? new Color(44, 62, 80) : new Color(34, 34, 34));
            }

            return this;
        }
    }
}
