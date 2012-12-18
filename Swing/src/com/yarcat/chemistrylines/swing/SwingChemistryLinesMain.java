package com.yarcat.chemistrylines.swing;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class SwingChemistryLinesMain {
    protected static final int COLS = 10;
    protected static final int ROWS = 10;

    public static void main(String[] args) {
        run(getFactory(args));
    }

    static GameFactory getFactory(String[] args) {
        GameFactory f =
            GameFactory.byName(args.length == 0 ? "compound" : args[0]);
        if (args.length > 1) {
            f.setCleaner(args[1]);
        }
        return f;
    }

    protected static void run(final GameFactory f) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    newGame(f, COLS, ROWS);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(
                        null, e.getMessage(),
                        f.getModeName() + " Error",
                        JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }
        });
    }

    protected static SwingChemistryLines newGame(GameFactory factory, int cols,
            int rows) throws Exception {
        SwingUIFactory f = new SwingUIFactory(factory, cols, rows);
        return f.newInstance();
    }
}
