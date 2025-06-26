package views;

import controller.PartidoController;
import model.dto.PartidoDTO;
import model.dto.UsuarioDTO;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

public class HistorialPanel extends JPanel {

    private final String usuarioid;
    private final DefaultListModel<PartidoDTO> listModel;
    private final JList<PartidoDTO> listaPartidos;

    public HistorialPanel(String usuarioid) {
        this.usuarioid = usuarioid;

        setLayout(new BorderLayout());
        setBackground(new Color(25, 25, 25));

        // -------- TOP PANEL --------
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.setBackground(new Color(25, 25, 25));

        JLabel lblTitulo = new JLabel("Historial");
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

        cargarPartidos();
    }

    private void cargarPartidos() {
        listModel.clear();
        List<PartidoDTO> partidos = PartidoController.getInstance().obtenerHistorial(this.usuarioid);

        for (PartidoDTO dto : partidos) {
            listModel.addElement(dto);
        }
    }

    private static class PartidoRenderer extends DefaultListCellRenderer {
        private static final Color COLOR_PAR = new Color(30, 30, 30);
        private static final Color COLOR_IMPAR = new Color(38, 38, 38);
        private static final Color COLOR_SELECCIONADO = new Color(60, 80, 100);
        private static final Border BORDE_INFERIOR = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.DARK_GRAY);
        private static final Insets PADDING = new Insets(8, 12, 8, 12);

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof PartidoDTO dto) {
                String deporte = dto.getDeporte().getNombre();
                int totalJugadores = dto.getDeporte().getCantidadJugadoresEstandar();
                String fecha = new SimpleDateFormat("EEE dd/MM/yyyy HH:mm").format(dto.getFechaHora());
                String estado = dto.getEstado().getNombre();
                int duracion = dto.getDuracion();
                String ubicacion = dto.getUbicacion();
                String organizador = dto.getOrganizador();
                String estrategia = dto.getTipoEmparejamiento().name();

                int actuales = dto.getJugadoresInscritos().size();
                int faltan = totalJugadores - actuales;
                String faltanTexto = faltan > 0 ? String.format(" (faltan %d)", faltan) : "";

                String jugadores = dto.getJugadoresInscritos().stream()
                        .map(UsuarioDTO::getEmail)
                        .collect(Collectors.joining(", "));

                String texto = String.format("""
                <b>Deporte:</b> %s (%d jugadores)<br>
                <b>Fecha:</b> %s<br>
                <b>Duración:</b> %d min<br>
                <b>Ubicación:</b> %s<br>
                <b>Estado:</b> %s<br>
                <b>Organizador:</b> %s<br>
                <b>Emparejamiento:</b> %s<br>
                <b>Jugadores:</b> %d/%d%s<br>
                <b>Inscriptos:</b> %s
                """,
                        deporte, totalJugadores,
                        fecha,
                        duracion,
                        ubicacion,
                        estado,
                        organizador,
                        estrategia,
                        actuales, totalJugadores, faltanTexto,
                        jugadores.isBlank() ? "Ninguno" : jugadores
                );

                label.setText("<html>" + texto + "</html>");
                label.setOpaque(true);
                label.setForeground(Color.WHITE);
                label.setBorder(BorderFactory.createCompoundBorder(
                        BORDE_INFERIOR,
                        BorderFactory.createEmptyBorder(PADDING.top, PADDING.left, PADDING.bottom, PADDING.right)
                ));
                label.setBackground(isSelected ? COLOR_SELECCIONADO : (index % 2 == 0 ? COLOR_PAR : COLOR_IMPAR));
            }

            return label;
        }
    }

}
