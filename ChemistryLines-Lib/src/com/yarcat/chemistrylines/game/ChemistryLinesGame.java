package com.yarcat.chemistrylines.game;

import static com.yarcat.chemistrylines.algorithms.RandomCell.getRandomEmptyCell;
import static com.yarcat.chemistrylines.algorithms.RandomCell.random;
import static com.yarcat.chemistrylines.field.KnownElements.knownElements;

import com.yarcat.chemistrylines.algorithms.CompoundRemover;
import com.yarcat.chemistrylines.algorithms.Path;
import com.yarcat.chemistrylines.field.Element;
import com.yarcat.chemistrylines.field.Field;

public class ChemistryLinesGame implements GameLogic {

    private Field mField;
    private CompoundRemover mCleaner;

    public ChemistryLinesGame(Field field) {
        mField = field;
        mCleaner = new CompoundRemover();
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
            mCleaner.removeAllCompounds(mField);
            addItems();
        }
    }

    @Override
    public void addItems() {
        for (int i = 0; i < 3; ++i) {
            int n = getRandomEmptyCell(mField);
            if (n < 0) {
                break;
            }
            mField.at(n).setElement(getRandomElement());
        }
        mCleaner.removeAllCompounds(mField);
    }

    private Element getRandomElement() {
        Element e;
        do {
            // TODO(yarcat): Find better way of skipping final elements.
            e = knownElements.getByIndex(random.nextInt(knownElements.size()));
        } while (e.isFinal());
        return e;
    }
}
