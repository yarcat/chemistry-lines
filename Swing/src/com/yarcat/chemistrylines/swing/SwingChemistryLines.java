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
            selection().select(b.n);
            refreshField();
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        FieldButton b = (FieldButton) e.getSource();
        if (selection().hasSource() && selection().hasTarget()
            && selection().getSource() == selection().getTarget()
            && selection().getSource() == b.n) {
            selection().clear();
        } else {
            tryMakeMove(b.n);
        }
        refreshField();
    }

    private void tryMakeMove(int id) {
        // We need this because for the drag case mouseReleased is called for
        // the source button, and we don't wanna overwrite the value.
        if (!selection().hasTarget()) {
            selection().select(id);
        }
        if (selection().hasTarget()
            && selection().getSource() != selection().getTarget()) {
            try {
                mGame.makeMove(selection().getSource(), selection()
                    .getTarget());
                refresh();
            } catch (InvalidMove e1) {
            }
            selection().clear();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (selection().hasSource()) {
            FieldButton b = (FieldButton) e.getSource();
            tryMakeMove(b.n);
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
