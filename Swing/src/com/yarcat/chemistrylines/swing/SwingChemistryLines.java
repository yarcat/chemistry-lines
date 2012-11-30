package com.yarcat.chemistrylines.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import com.yarcat.chemistrylines.field.Element;
import com.yarcat.chemistrylines.field.Field;
import com.yarcat.chemistrylines.field.RectField;
import com.yarcat.chemistrylines.game.GameLogic;
import com.yarcat.chemistrylines.game.GameLogic.InvalidMove;
import com.yarcat.chemistrylines.view.SelectionInView;

public class SwingChemistryLines implements MouseListener {

    private final SelectionInView mSelection = new SelectionInView();

    private final Field mField;
    private final GameLogic mGame;
    private final Button[] mButtons;
    private final Button[] mPreview;

    @SuppressWarnings("serial")
    private static class Button extends JLabel {
        private int mId;

        public Button(int id) {
            mId = id;
        }
    }

    /** Game UI Factory (! Thread UNSAFE !) */
    private static class UIFactory {
        protected int mCols;
        protected int mRows;
        protected GameFactory mGameFactory;
        protected GameLogic mGame;
        protected Field mField;
        protected Button[] mButtons;
        protected Button[] mPreview;
        protected SwingChemistryLines mGameUI;

        public UIFactory(GameFactory factory, int cols, int rows) {
            mGameFactory = factory;
            mCols = cols;
            mRows = rows;
        }

        public SwingChemistryLines newInstance() {
            mField = new RectField(mCols, mRows);
            mButtons = new Button[mField.getLength()];
            // TODO(luch): global constant for 3.
            mPreview = new Button[3];

            mGame = mGameFactory.newInstance(mField);
            SwingChemistryLines r =
                mGameUI =
                    new SwingChemistryLines(mField, mGame, mButtons, mPreview);

            JFrame f =
                new JFrame("Chemistry Lines - " + mGameFactory.getModeName());
            Container rootPane = f.getContentPane();
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setBackground(Color.BLACK);
            f.setForeground(Color.WHITE);
            // f.setLayout(new BoxLayout(rootPane, BoxLayout.X_AXIS));

            fillRootPane(rootPane);

            f.pack();
            f.setVisible(true);

            mGame.addItems();
            mGameUI.refresh();

            mGame = null;
            mField = null;
            mButtons = null;
            mPreview = null;
            mGameUI = null;

            return r;
        }

        protected void fillRootPane(Container rootPane) {
            rootPane.setLayout(new BoxLayout(rootPane, BoxLayout.X_AXIS));
            rootPane.add(createMainPane());
            rootPane.add(createRightPane());
        }

        protected Container createMainPane() {
            Panel p = new Panel();
            p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
            p.add(createButtonsPane(mButtons));
            p.add(createPreviewPane(mPreview));
            return p;
        }

        protected Container createButtonsPane(Button[] buttons) {
            Panel buttonPanel = new Panel(new GridLayout(mCols, mRows));
            for (int i = 0; i < mCols * mRows; ++i) {
                Button b = createButton(i);
                b.addMouseListener(mGameUI);
                buttons[i] = b;
                buttonPanel.add(b);
            }
            return buttonPanel;
        }

        protected Container createPreviewPane(Button[] preview) {
            Panel previewPanel = new Panel();
            previewPanel.setBackground(Color.BLACK);
            for (int i = 0; i < preview.length; ++i) {
                Button b = createButton(i);
                preview[i] = b;
                previewPanel.add(b);
            }
            return previewPanel;
        }

        protected Button createButton(int n) {
            Button b = new Button(n);
            b.setPreferredSize(new Dimension(60, 60));
            b.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            b.setForeground(Color.WHITE);
            b.setHorizontalAlignment(SwingConstants.CENTER);
            b.setOpaque(true);
            return b;
        }

        protected Container createRightPane() {
            Panel p = new Panel();
            p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
            p.add(createLog());
            return p;
        }

        protected Component createLog() {
            JTextArea logArea = new JTextArea(10, 10);
            mGame.setGameLogger(new SwingGameLogger(logArea));
            logArea.setEditable(false);
            logArea.setBackground(Color.BLACK);
            logArea.setForeground(Color.WHITE);
            return scrollPane(logArea);
        }

        protected Container scrollPane(Component c) {
            JScrollPane s = new JScrollPane(c);
            s.setBorder(BorderFactory.createEmptyBorder());
            // @formatter:off
            s.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
            s.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            // @formatter:on
            return s;
        }
    }

    public static SwingChemistryLines newInstance(GameFactory factory,
            int cols, int rows) {
        UIFactory f = new UIFactory(factory, cols, rows);
        return f.newInstance();
    }

    public SwingChemistryLines(Field field, GameLogic game, Button[] buttons,
            Button[] preview) {
        mField = field;
        mGame = game;
        mButtons = buttons;
        mPreview = preview;
    }

    private void refresh() {
        refreshField();
        refreshPreview();
    }

    private void refreshPreview() {
        Element[] nextElements = mGame.previewNextElements();
        for (int i = 0; i < mPreview.length; ++i) {
            mPreview[i].setText(nextElements[i].getId());
        }
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
            refresh();
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
            refresh();
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
        refresh();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (mSelection.hasSource()) {
            Button b = (Button) e.getSource();
            tryMakeMove(b.mId);
        }
    }

}
