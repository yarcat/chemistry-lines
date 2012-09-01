package com.yarcat.chemistrylines.game;

import static com.yarcat.chemistrylines.algorithms.RandomCell.getRandomEmptyCell;
import static com.yarcat.chemistrylines.algorithms.RandomCell.random;
import static com.yarcat.chemistrylines.field.KnownElements.knownElements;

import com.yarcat.chemistrylines.algorithms.Path;
import com.yarcat.chemistrylines.field.Element;
import com.yarcat.chemistrylines.field.Field;

public class ChemistryLinesGame implements GameLogic {

    private Field mField;

    public ChemistryLinesGame(Field field) {
        mField = field;
    }

    public boolean isMoveValid(int origin, int fin) {
        return Path.isReachable(mField, origin, fin);
    }

    public void makeMove(int origin, int fin) throws InvalidMove {
        if (!isMoveValid(origin, fin)) {
            throw new InvalidMove();
        }
        if (origin != fin) {
            mField.at(fin).setElement(mField.at(origin).getElement());
            mField.at(origin).setElement(null);
        }
    }

    public void addItems() {
        for (int i = 0; i < 3; ++i) {
            int n = getRandomEmptyCell(mField);
            if (n < 0) {
                break;
            }
            mField.at(n).setElement(getRandomElement());
        }
    }

    private Element getRandomElement() {
        return knownElements.getByIndex(random.nextInt(knownElements.size()));
    }
}
