package com.yarcat.chemistrylines.game;

import static com.yarcat.chemistrylines.algorithms.RandomCell.getRandomEmptyCell;

import com.yarcat.chemistrylines.algorithms.CompoundRemover;
import com.yarcat.chemistrylines.algorithms.Path;
import com.yarcat.chemistrylines.field.Element;
import com.yarcat.chemistrylines.field.Field;

public abstract class LinesGame implements GameLogic {

    private int mNewPortionSize;
    private Field mField;
    private CompoundRemover mRemover;
    private ElementGenerator mElementGenerator;

    public LinesGame(Field f, CompoundRemover r, ElementGenerator g) {
        mField = f;
        mRemover = r;
        mElementGenerator = g;
        mNewPortionSize = 3;
    }

    @Override
    public boolean isMoveValid(int origin, int fin) {
        return Path.isReachable(mField, origin, fin);
    }

    @Override
    public void makeMove(int origin, int fin) throws InvalidMove {
        if (!isMoveValid(origin, fin)) {
            throw new InvalidMove();
        }
        if (origin != fin && !mField.at(origin).isEmpty()
            && mField.at(fin).isEmpty()) {
            mField.at(fin).setElement(mField.at(origin).getElement());
            mField.at(origin).setElement(null);
            mRemover.removeAllCompounds(mField);
            addItems();
        }
    }

    @Override
    public void addItems() {
        for (int i = 0; i < mNewPortionSize; ++i) {
            int n = getRandomEmptyCell(mField);
            if (n < 0) {
                break;
            }
            mField.at(n).setElement(mElementGenerator.getNext());
        }
        mRemover.removeAllCompounds(mField);
    }

    @Override
    public Element[] previewNextElements() {
        return mElementGenerator.preview(mNewPortionSize);
    }
}
