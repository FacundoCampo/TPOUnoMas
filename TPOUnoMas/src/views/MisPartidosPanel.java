package views;

import controller.PartidoController;
import model.dto.PartidoDTO;
import model.estadosDelPartido.Cancelado;

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

        boolean resultado = PartidoController.getInstance().cambiarEstado(seleccionado.getId(), new Cancelado());
        if (resultado) {
            lblMensaje.setText("✔ Partido cancelado correctamente.");
            cargarPartidos();
        } else {
            lblMensaje.setText("No se pudo cancelar el partido.");
        }
    }

    private static class PartidoRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof PartidoDTO dto) {
                String deporte = dto.getDeporte().getNombre();
                String fecha = new SimpleDateFormat("EEE dd/MM/yyyy HH:mm").format(dto.getFechaHora());
                String estado = dto.getEstado().getNombre();
                int actuales = dto.getJugadoresInscritos().size();
                int total = dto.getDeporte().getCantidadJugadoresEstandar();
                int faltan = total - actuales;
                String faltanTexto = faltan > 0 ? String.format(" (faltan %d)", faltan) : "";

                setText(String.format("%s - %s - Estado: %s | Jugadores: %d/%d%s",
                        deporte, fecha, estado, actuales, total, faltanTexto));
                setForeground(Color.WHITE);
                setBackground(isSelected ? new Color(44, 62, 80) : new Color(34, 34, 34));
            }

            return this;
        }
    }
}
