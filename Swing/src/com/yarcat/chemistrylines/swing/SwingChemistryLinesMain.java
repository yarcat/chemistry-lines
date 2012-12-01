package com.yarcat.chemistrylines.swing;

import javax.swing.SwingUtilities;

public class SwingChemistryLinesMain {
    static final int COLS = 10;
    static final int ROWS = 10;

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                GameFactory factory =
                    GameFactory.byName(args.length == 0 ? "compound" : args[0]);
                if (args.length > 1) {
                    factory.setCleaner(args[1]);
                }
                newGame(factory, COLS, ROWS);
            }
        });
    }

    public static SwingChemistryLines newGame(GameFactory factory,
            int cols, int rows) {
        SwingUIFactory f = new SwingUIFactory(factory, cols, rows);
        return f.newInstance();
    }
}
