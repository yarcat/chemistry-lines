package com.yarcat.chemistrylines.swing;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;

import com.yarcat.chemistrylines.field.Element;
import com.yarcat.chemistrylines.field.Field;
import com.yarcat.chemistrylines.game.GameLogic;
import com.yarcat.chemistrylines.game.GameLogic.InvalidMove;
import com.yarcat.chemistrylines.view.SelectionInView;

public class SwingChemistryLines implements MouseListener {

    private final SelectionInView mSelection = new SelectionInView();

    private final Field mField;
    private final GameLogic mGame;
    private final Button[] mButtons;
    private final Button[] mPreview;
    private DefferedCleanerUI mCleanerUI;

    @SuppressWarnings("serial")
    static class Button extends JLabel {
        private int mId;

        public Button(int id) {
            mId = id;
        }
    }

    public SwingChemistryLines(Field field, GameLogic game, Button[] buttons,
            Button[] preview) {
        mField = field;
        mGame = game;
        mButtons = buttons;
        mPreview = preview;
        mCleanerUI = null;
    }

    void refresh() {
        refreshField();
        refreshPreview();
        if (mCleanerUI != null) {
            mCleanerUI.refresh();
        }
    }

    private void refreshPreview() {
        Element[] nextElements = mGame.previewNextElements();
        for (int i = 0; i < mPreview.length; ++i) {
            mPreview[i].setText(nextElements[i].getId());
        }
    }

    private void refreshField() {
        for (int i = 0; i < mButtons.length; ++i) {
            // TODO(luch): define color as style.selected
            if (mSelection.hasSource() && mSelection.getSource() == i) {
                style.selected(mButtons[i]);
            } else if (mSelection.hasDestination()
                && mSelection.getDestination() == i) {
                style.underCursor(mButtons[i]);
            } else {
                style.defaultColor(mButtons[i]);
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
                refresh();
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

    public void setCleanerUI(DefferedCleanerUI cleanerUI) {
        mCleanerUI = cleanerUI;
    }

    public JLabel[] getField() {
        return mButtons;
    }
}
