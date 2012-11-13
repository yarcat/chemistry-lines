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
                    args.length > 0 && args[0].equals("formula")
                        ? new GameFactory.FormulaMode()
                        : new GameFactory.CompoundMode();
                SwingChemistryLines.newInstance(factory, COLS, ROWS);
            }
        });
    }
}
