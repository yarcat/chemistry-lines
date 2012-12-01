package com.yarcat.chemistrylines.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Panel;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import com.yarcat.chemistrylines.field.Field;
import com.yarcat.chemistrylines.field.RectField;
import com.yarcat.chemistrylines.game.DefferedFieldCleaner;
import com.yarcat.chemistrylines.game.GameLogic;
import com.yarcat.chemistrylines.swing.GameFactory.Cleaner;
import com.yarcat.chemistrylines.swing.SwingChemistryLines.Button;

/** Game UI Factory (! Thread UNSAFE !) */
class SwingUIFactory {
    protected int mCols;
    protected int mRows;
    protected GameFactory mGameFactory;
    protected GameLogic mGame;
    protected Field mField;
    protected Button[] mButtons;
    protected Button[] mPreview;
    protected SwingChemistryLines mGameUI;

    public SwingUIFactory(GameFactory factory, int cols, int rows) {
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
        style.defaultColor(f);
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
        style.defaultColor(buttonPanel);
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
        style.defaultColor(previewPanel);
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
        style.button(b);
        return b;
    }

    protected Container createRightPane() {
        Panel p = new Panel();
        style.defaultColor(p);
        p.add(createLogPane());
        if (mGameFactory.getCleaner() == Cleaner.Deffered) {
            p.add(createClenerPane());
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

    private Component createClenerPane() {
        Panel p = new Panel(new GridLayout(50, 1));
        style.defaultColor(p);
        // @formatter:off
        DefferedFieldCleaner fieldCleaner =
            (DefferedFieldCleaner) mGame.getFieldCleaner();
        // @formatter:on
        mGameUI.setCleanerUI(new DefferedCleanerUI(fieldCleaner, p, mGameUI));
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
