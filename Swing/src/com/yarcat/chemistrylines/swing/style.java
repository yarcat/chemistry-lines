package com.yarcat.chemistrylines.swing;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class style {
    public final static Color DEFAULT_BG = Color.BLACK;
    public final static Color DEFAULT_FG = Color.WHITE;
    private final static Color HIGHLIGHT_BG = new Color(0, 0, 71);
    private final static Color HIGHLIGHT_FG = Color.YELLOW;

    // @formatter:off
    private final static Border BORDER =
        BorderFactory.createLineBorder(Color.DARK_GRAY);
    private final static Border INVISIBLE_BORDER =
            BorderFactory.createLineBorder(DEFAULT_BG);
    // @formatter:on

    public static void defaultColor(Component c) {
        c.setBackground(DEFAULT_BG);
        c.setForeground(DEFAULT_FG);
    }

    public static void highlight(Component c) {
        c.setBackground(HIGHLIGHT_BG);
        c.setForeground(HIGHLIGHT_FG);
    }

    public static void invisible(Component c) {
        c.setBackground(DEFAULT_BG);
        c.setForeground(DEFAULT_BG);
    }

    public static void button(JLabel b) {
        defaultColor(b);
        b.setBorder(BORDER);
        b.setHorizontalAlignment(SwingConstants.CENTER);
        b.setOpaque(true);
    }

    public static void invisibleButton(JLabel b) {
        invisible(b);
        b.setBorder(INVISIBLE_BORDER);
    }
}
