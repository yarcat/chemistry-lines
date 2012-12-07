package com.yarcat.chemistrylines.swing;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;

import com.yarcat.chemistrylines.field.Element;
import com.yarcat.chemistrylines.game.GameLogic;
import com.yarcat.chemistrylines.game.GameLogic.InvalidMove;
import com.yarcat.chemistrylines.swing.SwingField.Button;
import com.yarcat.chemistrylines.view.SelectionInView;

class SwingChemistryLines implements MouseListener {

    private final SwingField mFieldUI;
    private final GameLogic mGame;
    private final JLabel[] mPreview;
    private DefferedCleanerUI mCleanerUI;

    public SwingChemistryLines(GameLogic game, SwingField fieldUI,
            JLabel[] preview) {
        mFieldUI = fieldUI;
        mGame = game;
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

    private void refreshField() {
        mFieldUI.refresh();
    }

    private void refreshPreview() {
        Element[] nextElements = mGame.previewNextElements();
        for (int i = 0; i < mPreview.length; ++i) {
            mPreview[i].setText(nextElements[i].getId());
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (selection().hasSource()) {
            Button b = (Button) e.getSource();
            selection().select(b.id());
            refreshField();
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Button b = (Button) e.getSource();
        if (selection().hasSource() && selection().hasDestination()
            && selection().getSource() == selection().getDestination()
            && selection().getSource() == b.id()) {
            selection().clear();
            refreshField();
        } else {
            tryMakeMove(b.id());
        }
    }

    private void tryMakeMove(int id) {
        // We need this because for the drag case mouseReleased is called for
        // the source button, and we don't wanna overwrite the value.
        if (!selection().hasDestination()) {
            selection().select(id);
        }
        if (selection().hasDestination()
            && selection().getSource() != selection().getDestination()) {
            try {
                mGame.makeMove(
                    selection().getSource(), selection().getDestination());
                refresh();
            } catch (InvalidMove e1) {
            }
            selection().clear();
        }
        refreshField();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (selection().hasSource()) {
            Button b = (Button) e.getSource();
            tryMakeMove(b.id());
        }
    }

    public void setCleanerUI(DefferedCleanerUI cleanerUI) {
        mCleanerUI = cleanerUI;
    }

    private SelectionInView selection() {
        return mFieldUI.selection();
    }
}
