package com.yarcat.chemistrylines.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import com.yarcat.chemistrylines.field.Field;
import com.yarcat.chemistrylines.field.RectField;
import com.yarcat.chemistrylines.game.FormulaLinesGame;
import com.yarcat.chemistrylines.game.GameLogic.InvalidMove;
import com.yarcat.chemistrylines.game.LinesGame;
import com.yarcat.chemistrylines.view.SelectionInView;

public class ChemistryLines implements Runnable, MouseListener {

    private static final int COLS = 8;
    private static final int ROWS = 8;

    private final Button[] mButtons = new Button[COLS * ROWS];

    private final Field mField = new RectField(COLS, ROWS);
    // TODO(luch): fix game creation somehow.
    //private final LinesGame mGame = new ChemistryLinesGame(mField);
    private final LinesGame mGame = new FormulaLinesGame(mField);

    private final SelectionInView mSelection = new SelectionInView();

    private class Button extends JLabel {
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
            b.setPreferredSize(new Dimension(60, 60));
            b.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            b.setForeground(Color.WHITE);
            b.setHorizontalAlignment(JLabel.CENTER);
            b.setOpaque(true);
            mButtons[i] = b;
            b.addMouseListener(this);
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

    private void refreshField() {
        for (int i = 0; i < mButtons.length; ++i) {
            if (mSelection.hasSource() && mSelection.getSource() == i) {
                mButtons[i].setBackground(Color.DARK_GRAY);
            } else if (mSelection.hasDestination()
                && mSelection.getDestination() == i) {
                mButtons[i].setBackground(Color.GRAY);
            } else {
                mButtons[i].setBackground(Color.BLACK);
            }
            mButtons[i].setText(getTitle(i));
        }
    }

    private String getTitle(int i) {
        return mField.at(i).isEmpty() ? "" : mField.at(i).getElement().getId();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (mSelection.hasSource()) {
            Button b = (Button) e.getSource();
            mSelection.select(b.mId);
            refreshField();
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Button b = (Button) e.getSource();
        if (mSelection.hasSource() && mSelection.hasDestination()
            && mSelection.getSource() == mSelection.getDestination()
            && mSelection.getSource() == b.mId) {
            mSelection.clear();
            refreshField();
        } else {
            tryMakeMove(b.mId);
        }
    }

    private void tryMakeMove(int id) {
        // We need this because for the drag case mouseReleased is called for
        // the source button, and we don't wanna overwrite the value.
        if (!mSelection.hasDestination()) {
            mSelection.select(id);
        }
        if (mSelection.hasDestination()
            && mSelection.getSource() != mSelection.getDestination()) {
            try {
                mGame.makeMove(
                    mSelection.getSource(), mSelection.getDestination());
            } catch (InvalidMove e1) {
            }
            mSelection.clear();
        }
        refreshField();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (mSelection.hasSource()) {
            Button b = (Button) e.getSource();
            tryMakeMove(b.mId);
        }
    }

}
