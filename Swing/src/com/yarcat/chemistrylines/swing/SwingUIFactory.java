package com.yarcat.chemistrylines.swing;

import static com.yarcat.chemistrylines.constants.PORTION_SIZE;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Panel;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import com.yarcat.chemistrylines.field.Field;
import com.yarcat.chemistrylines.field.RectField;
import com.yarcat.chemistrylines.game.DeferredFieldCleaner;
import com.yarcat.chemistrylines.game.GameLogic;
import com.yarcat.chemistrylines.swing.GameFactory.Cleaner;
import com.yarcat.chemistrylines.swing.SwingField.FieldButton;
import com.yarcat.chemistrylines.swing.SwingPreview.PreviewButton;

/** Game UI Factory */
class SwingUIFactory {
    final int mCols;
    final int mRows;
    final GameFactory mGameFactory;

    public SwingUIFactory(GameFactory factory, int cols, int rows) {
        mGameFactory = factory;
        mCols = cols;
        mRows = rows;
    }

    public SwingChemistryLines newInstance() {
        return new Builder().newInstance();
    }

    class Builder {
        final Dimension BUTTON_SIZE = new Dimension(60, 60);

        GameLogic mGame;
        SwingField mFieldUI;
        FieldButton[] mButtons;
        SwingPreview mPreviewUI;
        PreviewButton[] mPreview;
        SwingChemistryLines mGameUI;
        JTextArea mScoreUI;

        public SwingChemistryLines newInstance() {
            Field field = new RectField(mCols, mRows);
            mButtons = new FieldButton[field.getLength()];
            mPreview = new PreviewButton[PORTION_SIZE];

            mGame = mGameFactory.newInstance(field);
            mFieldUI = new SwingField(mGame, mButtons);
            mPreviewUI = new SwingPreview(mGame, mPreview);
            mScoreUI = new JTextArea();
            mGameUI =
                new SwingChemistryLines(mGame, mFieldUI, mPreviewUI, mScoreUI);
            mGame.setChangeListener(mGameUI);

            JFrame f =
                new JFrame("Chemistry Lines - " + mGameFactory.getModeName());
            style.defaultColor(f);
            Container rootPane = f.getContentPane();
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setLayout(new BoxLayout(rootPane, BoxLayout.X_AXIS));

            fillRootPane(rootPane);

            f.pack();
            f.setVisible(true);

            mGame.addItems();
            mGameUI.refresh();

            return mGameUI;
        }

        protected void fillRootPane(Container rootPane) {
            rootPane.setLayout(new BoxLayout(rootPane, BoxLayout.X_AXIS));
            rootPane.add(createMainPane());
            rootPane.add(createRightPane());
        }

        protected Container createMainPane() {
            Panel p = new Panel();
            p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
            p.add(createInfoPane());
            p.add(createButtonsPane(mButtons));
            return p;
        }

        protected Container createButtonsPane(FieldButton[] buttons) {
            Panel buttonPanel = new Panel(new GridLayout(mCols, mRows));
            style.defaultColor(buttonPanel);
            for (int i = 0; i < mCols * mRows; ++i) {
                FieldButton b = mFieldUI.newButton(i);
                b.init();
                b.setPreferredSize(BUTTON_SIZE);
                b.addMouseListener(mGameUI);
                buttons[i] = b;
                buttonPanel.add(b);
            }
            return buttonPanel;
        }

        private Container createInfoPane() {
            Panel p = new Panel(new GridLayout(1, 2));
            p.add(createScorePane());
            p.add(createPreviewPane(mPreview));
            return p;
        }

        protected Container createPreviewPane(PreviewButton[] preview) {
            Panel previewPanel = new Panel();
            previewPanel.add(style.defaultColor(new JLabel("Next: ")));
            previewPanel.addMouseListener(mPreviewUI);
            for (int i = 0; i < preview.length; ++i) {
                PreviewButton b = new PreviewButton();
                b.init();
                b.setPreferredSize(BUTTON_SIZE);
                preview[i] = b;
                previewPanel.add(b);
            }
            return previewPanel;
        }

        private Component createScorePane() {
            Panel p = new Panel();
            p.add(style.defaultColor(new JLabel("Score:")));
            p.add(mScoreUI);
            style.frame(mScoreUI);
            mScoreUI.setEditable(false);
            // @formatter:off
            mScoreUI.setPreferredSize(
                new Dimension(BUTTON_SIZE.width * 3, BUTTON_SIZE.height));
            // @formatter:on
            return p;
        }

        protected Container createRightPane() {
            Panel p = new Panel();
            style.defaultColor(p);
            p.add(createLogPane());
            if (mGameFactory.getCleaner() == Cleaner.Deffered) {
                p.add(createCleanerPane());
            }
            p.setLayout(new GridLayout(p.getComponentCount(), 1));
            return p;
        }

        protected Component createLogPane() {
            JTextArea logArea = new JTextArea(10, 10);
            style.defaultColor(logArea);
            mGame.setGameLogger(new SwingGameLogger(logArea));
            logArea.setEditable(false);
            return scrollPane(logArea);
        }

        private Component createCleanerPane() {
            Panel p = new Panel(new GridLayout(50, 1));
            style.defaultColor(p);
            // @formatter:off
            DeferredFieldCleaner fieldCleaner =
                (DeferredFieldCleaner) mGame.getFieldCleaner();
            mGameUI
                .setCleanerUI(new DefferedCleanerUI(fieldCleaner, p, mFieldUI));
            // @formatter:on
            return scrollPane(p);
        }

        protected Container scrollPane(Component c) {
            JScrollPane s = new JScrollPane(c);
            style.defaultColor(s);
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
}
