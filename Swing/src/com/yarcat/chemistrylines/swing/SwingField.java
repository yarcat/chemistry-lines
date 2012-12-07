package com.yarcat.chemistrylines.swing;

import javax.swing.JLabel;

import com.yarcat.chemistrylines.field.Cell;
import com.yarcat.chemistrylines.game.GameLogic;
import com.yarcat.chemistrylines.view.SelectionInView;

class SwingField {

    @SuppressWarnings("serial")
    static class Button extends JLabel {
        private final int mId;

        public Button(int id) {
            mId = id;
        }

        public int id() {
            return mId;
        }
    }

    private final GameLogic mGame;
    private final Button[] mButtons;
    private final SelectionInView mSelection;

    public SwingField(GameLogic game, Button[] buttons) {
        mGame = game;
        mButtons = buttons;
        mSelection = new SelectionInView();
    }

    SelectionInView selection() {
        return mSelection;
    }

    void refresh() {
        for (int i = 0; i < mButtons.length; ++i) {
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
        return at(i).isEmpty() ? "" : at(i).getElement().getId();
    }

    private Cell at(int i) {
        return mGame.getField().at(i);
    }

    public Button getButton(int n) {
        return mButtons[n];
    }
}
