package com.yarcat.chemistrylines.swing;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTextArea;

import com.yarcat.chemistrylines.game.GameLogic;
import com.yarcat.chemistrylines.game.GameLogic.GameListener;
import com.yarcat.chemistrylines.game.GameLogic.InvalidMove;
import com.yarcat.chemistrylines.swing.SwingField.FieldButton;
import com.yarcat.chemistrylines.view.SelectionInView;

class SwingChemistryLines implements MouseListener, GameListener {

    private final SwingField mFieldUI;
    private final GameLogic mGame;
    private final JTextArea mScoreUI;
    private SwingPreview mPreviewUI;
    private DefferedCleanerUI mCleanerUI;

    public SwingChemistryLines(GameLogic game, SwingField fieldUI,
            SwingPreview previewUI, JTextArea scoreUI) {
        mFieldUI = fieldUI;
        mGame = game;
        mPreviewUI = previewUI;
        mCleanerUI = null;
        mScoreUI = scoreUI;
    }

    void refresh() {
        refreshField();
        mPreviewUI.refresh();
        if (mCleanerUI != null) {
            mCleanerUI.refresh();
        }
        refreshScore();
    }

    private void refreshScore() {
        mScoreUI.setText("");
        mScoreUI.append(mGame.getScorer().get());
    }

    private void refreshField() {
        mFieldUI.refresh();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (selection().hasSource()) {
            FieldButton b = (FieldButton) e.getSource();
            if (b.n == selection().getSource()) {
                selection().clearTarget();
            } else {
                selection().select(b.n);
            }
            refreshField();
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        final FieldButton b = (FieldButton) e.getSource();
        if (selection().hasSource()) {
            selection().select(b.n);
            tryMakeMove();
        } else {
            selection().select(b.n);
        }
        // TODO: refresh field via selection listener
        refreshField();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (selection().hasSource()) {
            // mouseReleased() gets the same button that was pressed.
            tryMakeMove();
        }
    }

    private void tryMakeMove() {
        if (selection().hasTarget()) {
            int s, t;
            if (mGame.getField().at(selection().getSource()).isEmpty()) {
                s = selection().getTarget();
                t = selection().getSource();
            } else {
                s = selection().getSource();
                t = selection().getTarget();
            }
            try {
                mGame.makeMove(s, t);
                refresh();
            } catch (InvalidMove e1) {
            }
            selection().clear();
            refreshField();
        }
    }

    public void setCleanerUI(DefferedCleanerUI cleanerUI) {
        mCleanerUI = cleanerUI;
    }

    private SelectionInView selection() {
        return mFieldUI.selection();
    }

    @Override
    public void onFinished() {
    }

    @Override
    public void onFieldChange(GameLogic game) {
        refresh();
    }

    @Override
    public void onScoreChange(GameLogic game) {
        refreshScore();
    }
}
