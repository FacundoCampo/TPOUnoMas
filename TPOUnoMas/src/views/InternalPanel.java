package views;

import javax.swing.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Objects;

public class InternalPanel extends JTabbedPane {

    public InternalPanel(String id) {
        super(JTabbedPane.LEFT); 
        setBorder(null);
        setBounds(100, 100, 770, 620);

        setBackground(new Color(20, 20, 20));
        setForeground(Color.WHITE);

        UIManager.put("TabbedPane.contentOpaque", false);


        PartidoPanel pnlPartido = new PartidoPanel();
        PreferenciasDeportivasPanel pnlPref = new PreferenciasDeportivasPanel(id);

        this.addTab("Partido", pnlPartido.setTab(id));
        this.addTab("Prefrenrecias", pnlPref);

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
                // No borders
            }

            @Override
            protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
                // No borders
            }

            @Override
            protected void paintText(Graphics g, int tabPlacement, Font font,
                                     FontMetrics metrics, int tabIndex,
                                     String title, Rectangle textRect, boolean isSelected) {
                g.setFont(font);
                g.setColor(Color.WHITE);
                g.drawString(title, textRect.x, textRect.y + metrics.getAscent());
            }
        });
    }
}