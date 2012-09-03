package com.yarcat.chemistrylines.swing;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.yarcat.chemistrylines.field.Field;
import com.yarcat.chemistrylines.field.RectField;
import com.yarcat.chemistrylines.game.ChemistryLinesGame;
import com.yarcat.chemistrylines.game.GameLogic.InvalidMove;

public class ChemistryLines implements Runnable, ActionListener {

    private static final int COLS = 8;
    private static final int ROWS = 8;

    private int mSelectedIdx = -1;
    private final Button[] mButtons = new Button[COLS * ROWS];

    private final Field mField = new RectField(COLS, ROWS);
    private final ChemistryLinesGame mGame = new ChemistryLinesGame(mField);

    private class Button extends JButton {
        private int mId;

        public Button(int id) {
            mId = id;
        }
    }

    @Override
    public void run() {
        JFrame f = new JFrame("");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GridLayout l = new GridLayout(COLS, ROWS);
        f.setLayout(l);
        for (int i = 0; i < COLS * ROWS; ++i) {
            Button b = new Button(i);
            mButtons[i] = b;
            b.addActionListener(this);
            f.getContentPane().add(b);
        }
        f.pack();
        f.setVisible(true);

        mGame.addItems();
        refreshField();
    }

    public static void main(String[] args) {
        ChemistryLines cl = new ChemistryLines();
        SwingUtilities.invokeLater(cl);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Button b = (Button) e.getSource();
        if (mSelectedIdx == -1) {
            if (!mField.at(b.mId).isEmpty()) {
                mSelectedIdx = b.mId;
            }
        } else if (mSelectedIdx == b.mId) {
            mSelectedIdx = -1;
        } else {
            try {
                mGame.makeMove(mSelectedIdx, b.mId);
            } catch (InvalidMove e1) {
            }
            mSelectedIdx = -1;
        }
        refreshField();
    }

    private void refreshField() {
        for (int i = 0; i < mButtons.length; ++i) {
            mButtons[i].setText(getTitle(i));
        }
    }

    private String getTitle(int i) {
        return mField.at(i).isEmpty() ? "" : mField.at(i).getElement().getId();
    }

}
