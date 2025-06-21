package views;

import controller.PartidoController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PartidoPanel {

    private JTabbedPane tabbedPane;
    private JButton btnAgregar;
    private JButton btnEliminar;
    private JButton btnVerListados;
    private JPanel panelPrincipal;
    private JPanel crearPartidoTab;
    private JPanel misPartidosTab;
    private JPanel listadoTab;

    /**
     * @wbp.parser.entryPoint
     */
    public JTabbedPane setTab(String usuarioid) {
        tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
        tabbedPane.setBackground(new Color(20, 20, 20));
        tabbedPane.setForeground(Color.black);
        tabbedPane.setFont(new Font("Tahoma", Font.BOLD, 14));

        tabbedPane.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
            @Override
            protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex,
                                              int x, int y, int w, int h, boolean isSelected) {
                g.setColor(isSelected ? new Color(30, 30, 30) : new Color(20, 20, 20));
                g.fillRect(x, y, w, h);
            }

            @Override
            protected void paintTabArea(Graphics g, int tabPlacement, int selectedIndex) {
                g.setColor(new Color(20, 20, 20));
                g.fillRect(0, 0, tabbedPane.getWidth(), calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight));
                super.paintTabArea(g, tabPlacement, selectedIndex);
            }

            @Override
            protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {}

            @Override
            protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex,
                                          int x, int y, int w, int h, boolean isSelected) {}

            @Override
            protected void paintText(Graphics g, int tabPlacement, Font font,
                                     FontMetrics metrics, int tabIndex,
                                     String title, Rectangle textRect, boolean isSelected) {
                g.setFont(font);
                g.setColor(Color.WHITE);
                g.drawString(title, textRect.x, textRect.y + metrics.getAscent());
            }
        });

        panelPrincipal = new JPanel();
        panelPrincipal.setBackground(new Color(25, 25, 25));
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(60, 200, 60, 200));

        btnAgregar = crearBoton("Agregar partido", new Color(46, 204, 113));
        btnEliminar = crearBoton("Cancelar partido", new Color(44, 62, 80));
        btnVerListados = crearBoton("Ver listados de partidos", new Color(44, 62, 80));

        panelPrincipal.add(Box.createVerticalGlue());
        panelPrincipal.add(btnAgregar);
        panelPrincipal.add(Box.createVerticalStrut(25));
        panelPrincipal.add(btnEliminar);
        panelPrincipal.add(Box.createVerticalStrut(25));
        panelPrincipal.add(btnVerListados);
        panelPrincipal.add(Box.createVerticalGlue());

        crearPartidoTab = new PartidoForm(tabbedPane, panelPrincipal, usuarioid);
        listadoTab = new ListaPartidosPanel(usuarioid);
        misPartidosTab = new MisPartidosPanel(usuarioid);

        tabbedPane.add("Menu", panelPrincipal);
        tabbedPane.add("Crear", crearPartidoTab);
        tabbedPane.add("Mis partidos", misPartidosTab);
        tabbedPane.add("Listado", listadoTab);
        tabbedPane.add("Historial", crearPanelPlaceholder());
        setAnchoFijoTabs(tabbedPane, 120);

        asociarEventos();

        return tabbedPane;
    }

    private JButton crearBoton(String texto, Color fondo) {
        JButton boton = new JButton(texto);
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setMaximumSize(new Dimension(400, 60));
        boton.setFont(new Font("Tahoma", Font.BOLD, 18));
        boton.setForeground(Color.WHITE);
        boton.setBackground(fondo);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return boton;
    }

    private JPanel crearPanelPlaceholder() {
        JPanel placeholder = new JPanel();
        placeholder.setBackground(new Color(30, 30, 30));
        JLabel label = new JLabel("Historial en construcción...");
        label.setForeground(Color.LIGHT_GRAY);
        label.setFont(new Font("Tahoma", Font.ITALIC, 16));
        placeholder.add(label);
        return placeholder;
    }

    private void setAnchoFijoTabs(JTabbedPane tabbedPane, int anchoDeseado) {
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            tabbedPane.setTabComponentAt(i, crearEtiquetaTab(tabbedPane.getTitleAt(i), anchoDeseado));
        }
    }

    private JLabel crearEtiquetaTab(String titulo, int ancho) {
        JLabel label = new JLabel(titulo, SwingConstants.CENTER);
        label.setPreferredSize(new Dimension(ancho, 40)); // Alto tab
        label.setMaximumSize(new Dimension(ancho, 40));
        label.setFont(new Font("Tahoma", Font.BOLD, 14));
        label.setForeground(Color.WHITE);
        return label;
    }


    private void asociarEventos() {
        PartidoController controller = PartidoController.getInstance();

        btnAgregar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tabbedPane.setSelectedComponent(crearPartidoTab);
            }
        });

        btnEliminar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tabbedPane.setSelectedComponent(misPartidosTab);
            }
        });

        btnVerListados.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tabbedPane.setSelectedComponent(listadoTab);
            }
        });
    }
}
