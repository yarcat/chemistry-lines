package com.yarcat.chemistrylines.swing;

import java.awt.Color;

import com.yarcat.chemistrylines.algorithms.Path;
import com.yarcat.chemistrylines.field.Cell;
import com.yarcat.chemistrylines.field.Cell.Mark;
import com.yarcat.chemistrylines.field.Element;
import com.yarcat.chemistrylines.field.Field;
import com.yarcat.chemistrylines.game.GameLogic;
import com.yarcat.chemistrylines.view.SelectionInView;
import com.yarcat.chemistrylines.view.SelectionInView.SelectionListener;

class SwingField {

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

        private boolean marked(Mark m) {
            return cell().hasMark(m);
        }

        @Override
        Color getBgColor() {
            Color bg;
            if (marked(Mark.SelectedAsSource)) {
                bg = Color.DARK_GRAY;
            } else if (marked(Mark.SelectedAsDestination)) {
                bg = Color.GRAY;
            } else if (isEmpty() && marked(Mark.ReachableFromSource)) {
                bg = style.REACHABLE_BG;
            } else {
                bg = super.getBgColor();
            }
            return bg;
        }
    }

    class FieldSelection implements SelectionListener {

        @Override
        public void onNewSource(int n) {
            at(n).setMark(Mark.SelectedAsSource);
            markCellsReachableFrom(n);
        }

        @Override
        public void onNewTarget(int n) {
            at(n).setMark(Mark.SelectedAsDestination);
        }

        @Override
        public void onSourceCleared(int n) {
            at(n).clearMark(Mark.SelectedAsSource);
            clearCellsReachableFrom(n);
        }

        @Override
        public void onTargetCleared(int n) {
            at(n).clearMark(Mark.SelectedAsDestination);
        }

        private void markCellsReachableFrom(int n) {
            Path p = new Path(getField(), n);
            p.evaluate();
            for (int i = 0; i < getLength(); ++i) {
                if (p.isReachable(i)) {
                    at(i).setMark(Mark.ReachableFromSource);
                }
            }
        }

        private void clearCellsReachableFrom(int n) {
            for (int i = 0; i < getLength(); ++i) {
                at(i).clearMark(Mark.ReachableFromSource);
            }
        }

        private int getLength() {
            return getField().getLength();
        }
    }

    private final GameLogic mGame;
    private final FieldButton[] mButtons;
    private final SelectionInView mSel;

    public SwingField(GameLogic game, FieldButton[] buttons) {
        mGame = game;
        mButtons = buttons;
        mSel = new SelectionInView();
        mSel.setListener(new FieldSelection());
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
        return getField().at(i);
    }

    private Field getField() {
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
}
