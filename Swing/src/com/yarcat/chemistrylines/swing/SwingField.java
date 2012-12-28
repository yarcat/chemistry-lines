package com.yarcat.chemistrylines.swing;

import java.awt.Color;

import javax.swing.border.Border;

import com.yarcat.chemistrylines.field.Cell;
import com.yarcat.chemistrylines.field.Element;
import com.yarcat.chemistrylines.field.Field;
import com.yarcat.chemistrylines.game.GameLogic;
import com.yarcat.chemistrylines.view.FieldHightlights;
import com.yarcat.chemistrylines.view.FieldHightlights.Mark;
import com.yarcat.chemistrylines.view.SelectionInView;

class SwingField implements FieldHightlights.Listener {
    @SuppressWarnings("serial")
    class FieldButton extends ElementButton {
        public final int n;

        public FieldButton(int index) {
            n = index;
        }

        private Cell cell() {
            return at(n);
        }

        @Override
        public Element getElement() {
            return cell().getElement();
        }

        @Override
        boolean isEmpty() {
            return cell().isEmpty();
        }

        private boolean markedAs(Mark m) {
            return mFieldMarks.hasMark(n, m);
        }

        @Override
        void updateStyle() {
            super.updateStyle();
            setBorder(getEdge());
        }

        @Override
        Color getBgColor() {
            Color bg;
            if (markedAs(Mark.SOURCE)) {
                bg = Color.DARK_GRAY;
            } else if (markedAs(Mark.TARGET)) {
                bg = Color.GRAY;
            } else if (isEmpty() && markedAs(Mark.REACHABLE)) {
                bg = style.REACHABLE_BG;
            } else {
                bg = super.getBgColor();
            }
            return bg;
        }

        private Border getEdge() {
            return !isEmpty() && markedAs(Mark.REACHABLE)
                ? style.REACHABLE_BORDER : style.DEFAULT_BORDER;
        }
    }

    private final GameLogic mGame;
    private final FieldButton[] mButtons;
    private final SelectionInView mSel;
    private final FieldHightlights mFieldMarks;

    public SwingField(GameLogic game, FieldButton[] buttons) {
        mGame = game;
        mButtons = buttons;
        mSel = new SelectionInView();
        mFieldMarks = FieldHightlights.create(mGame.getField());
        mFieldMarks.setListener(this);
        mSel.setListener(mFieldMarks);
    }

    SelectionInView selection() {
        return mSel;
    }

    void refresh() {
        for (int i = 0; i < mButtons.length; ++i) {
            mButtons[i].refresh();
        }
    }

    Cell at(int n) {
        return getField().at(n);
    }

    Field getField() {
        return mGame.getField();
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

    @Override
    public void onHighlightChange() {
        refresh();
    }
}
