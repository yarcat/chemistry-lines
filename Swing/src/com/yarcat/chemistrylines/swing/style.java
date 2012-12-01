package com.yarcat.chemistrylines.swing;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class style {
    private final static Color HIGHLIGHT_BG = new Color(0, 0, 71);
    private final static Color HIGHLIGHT_FG = Color.YELLOW;

    // @formatter:off
    private final static Border BORDER =
        BorderFactory.createLineBorder(Color.DARK_GRAY);
    private final static Border INVISIBLE_BORDER =
            BorderFactory.createLineBorder(Color.BLACK);
    // @formatter:on

    public static void defaultColor(Component c) {
        c.setBackground(Color.BLACK);
        c.setForeground(Color.WHITE);
    }

    public static void selected(Component c) {
        defaultColor(c);
        c.setBackground(Color.DARK_GRAY);
    }

    public static void underCursor(Component c) {
        defaultColor(c);
        c.setBackground(Color.GRAY);
    }

    public static void highlight(Component c) {
        c.setBackground(HIGHLIGHT_BG);
        c.setForeground(HIGHLIGHT_FG);
    }

    public static void invisible(Component c) {
        c.setBackground(Color.BLACK);
        c.setForeground(Color.BLACK);
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
