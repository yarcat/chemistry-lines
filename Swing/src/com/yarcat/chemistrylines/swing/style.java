package com.yarcat.chemistrylines.swing;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class style {
    // public final static Color DEFAULT_BG = new Color(0x99, 0x88, 0x77);
    // public final static Color DEFAULT_FG = new Color(0, 0x33, 0);
    public static final Color DEFAULT_BG = new Color(0x80, 0x70, 0x50);
    public static final Color DEFAULT_FG = Color.WHITE;
    public static final Color REACHABLE_BG = new Color(0x70, 0x60, 0x45);;
    private static final Color HIGHLIGHT_BG = new Color(0, 0, 71);
    private static final Color HIGHLIGHT_FG = Color.YELLOW;

    // @formatter:off
    final static Border DEFAULT_BORDER =
        BorderFactory.createLineBorder(new Color(0x55, 0x44, 0x33));
    final static Border REACHABLE_BORDER =
        BorderFactory.createLineBorder(new Color(0x20, 0x00, 0x30), 2);
    // @formatter:on

    static Component defaultColor(Component c) {
        c.setBackground(DEFAULT_BG);
        c.setForeground(DEFAULT_FG);
        return c;
    }

    static void highlight(Component c) {
        c.setBackground(HIGHLIGHT_BG);
        c.setForeground(HIGHLIGHT_FG);
    }

    static void button(JLabel b) {
        frame(b);
        b.setHorizontalAlignment(SwingConstants.CENTER);
    }

    static void frame(JComponent c) {
        defaultColor(c);
        c.setBorder(DEFAULT_BORDER);
        c.setOpaque(true);
    }
}
