package com.yarcat.chemistrylines.swing;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTextArea;

import com.yarcat.chemistrylines.game.GameLogic;
import com.yarcat.chemistrylines.game.GameLogic.GameListener;
import com.yarcat.chemistrylines.game.GameLogic.InvalidMove;
import com.yarcat.chemistrylines.game.Scorer;
import com.yarcat.chemistrylines.game.Scorer.ScoreListener;
import com.yarcat.chemistrylines.swing.SwingField.FieldButton;
import com.yarcat.chemistrylines.view.SelectionInView;

class SwingChemistryLines implements GameListener, MouseListener, ScoreListener {

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
        if (selection().hasSource() && selection().hasDestination()
            && selection().getSource() == selection().getDestination()
            && selection().getSource() == b.n) {
            selection().clear();
            refreshField();
        } else {
            tryMakeMove(b.n);
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
                mGame.makeMove(selection().getSource(), selection()
                    .getDestination());
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
    public void onScoreChange(Scorer scorer) {
        mScoreUI.setText("");
        mScoreUI.append(scorer.get());
    }
}
