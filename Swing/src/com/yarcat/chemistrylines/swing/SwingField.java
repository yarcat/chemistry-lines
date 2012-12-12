package com.yarcat.chemistrylines.swing;

import java.awt.Color;

import com.yarcat.chemistrylines.field.Cell;
import com.yarcat.chemistrylines.field.Element;
import com.yarcat.chemistrylines.game.GameLogic;
import com.yarcat.chemistrylines.view.SelectionInView;

class SwingField {

    @SuppressWarnings("serial")
    class FieldButton extends ElementButton {
        public final int n;

        public FieldButton(int index) {
            n = index;
        }

        @Override
        public Element getElement() {
            return at(n).getElement();
        }

        @Override
        boolean isEmpty() {
            return at(n).isEmpty();
        }

        @Override
        Color getBgColor() {
            Color bg;
            if (mSel.hasSource() && mSel.getSource() == n) {
                bg = Color.DARK_GRAY;
            } else if (mSel.hasDestination() && mSel.getDestination() == n) {
                bg = Color.GRAY;
            } else {
                bg = super.getBgColor();
            }
            return bg;
        }

    }

    private final GameLogic mGame;
    private final FieldButton[] mButtons;
    private final SelectionInView mSel;

    public SwingField(GameLogic game, FieldButton[] buttons) {
        mGame = game;
        mButtons = buttons;
        mSel = new SelectionInView();
    }

    SelectionInView selection() {
        return mSel;
    }

    void refresh() {
        for (int i = 0; i < mButtons.length; ++i) {
            mButtons[i].refresh();
        }
    }

    Cell at(int i) {
        return mGame.getField().at(i);
    }

    public FieldButton getButton(int n) {
        return mButtons[n];
    }

    public void clear(int n) {
        mButtons[n].setText(null);
    }

    FieldButton newButton(int n) {
        return new FieldButton(n);
    }
}
