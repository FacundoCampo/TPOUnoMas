package views;

import javax.swing.*;

import java.awt.*;
import java.util.Objects;

public class InternalPanel extends JTabbedPane {

    public InternalPanel(String id) {
        super(JTabbedPane.LEFT);
        setBorder(null);
        setBounds(100, 100, 770, 620);

        setBackground(new Color(20, 20, 20));
        setForeground(Color.WHITE);
        setFont(new Font("Arial", Font.BOLD, 18)); // Fuente consistente

        UIManager.put("TabbedPane.contentOpaque", false);

        PartidoPanel pnlPartido = new PartidoPanel();
        PreferenciasDeportivasPanel pnlPref = new PreferenciasDeportivasPanel(id);
        DeporteForm pnlDeporte = new DeporteForm();

        this.addTab("Partido", pnlPartido.setTab(id));
        this.addTab("Preferencias", pnlPref);
        this.addTab("Deportes", pnlDeporte);

        setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
            @Override
            protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex,
                                              int x, int y, int w, int h, boolean isSelected) {
                g.setColor(isSelected ? new Color(30, 30, 30) : new Color(20, 20, 20));
                g.fillRect(x, y, w, h);
            }

            @Override
            protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex,
                                          int x, int y, int w, int h, boolean isSelected) {
                // Sin bordes
            }

            @Override
            protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
                // Sin bordes
            }

            @Override
            protected void paintText(Graphics g, int tabPlacement, Font font,
                                     FontMetrics metrics, int tabIndex,
                                     String title, Rectangle textRect, boolean isSelected) {
                g.setFont(new Font("Arial", Font.BOLD, 18)); // Fuente unificada
                g.setColor(Color.WHITE);
                g.drawString(title, textRect.x, textRect.y + metrics.getAscent());
            }
        });

        setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        setPreferredSize(new Dimension(770, 620));

        int tabWidth = 130;
        int tabHeight = 50;
        setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
            @Override
            protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
                return tabHeight;
            }

            @Override
            protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
                return tabWidth;
            }

            @Override
            protected void paintText(Graphics g, int tabPlacement, Font font,
                                     FontMetrics metrics, int tabIndex,
                                     String title, Rectangle textRect, boolean isSelected) {
                g.setFont(new Font("Arial", Font.BOLD, 18));
                g.setColor(Color.WHITE);
                g.drawString(title, textRect.x, textRect.y + metrics.getAscent());
            }

            @Override
            protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex,
                                              int x, int y, int w, int h, boolean isSelected) {
                g.setColor(isSelected ? new Color(40, 40, 40) : new Color(25, 25, 25));
                g.fillRect(x, y, w, h);
            }
        });
    }
}
