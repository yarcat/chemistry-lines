package com.yarcat.chemistrylines.game;

import com.yarcat.chemistrylines.algorithms.Path;
import com.yarcat.chemistrylines.field.Cell;
import com.yarcat.chemistrylines.field.Element;
import com.yarcat.chemistrylines.field.Field;
import com.yarcat.chemistrylines.field.KnownElements;

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
            getRandomCell().setElement(getRandomElement());
        }
    }

    // Fixed logic for the beginning.
    private int mElementIdx = 0;

    private Cell getRandomCell() {
        switch (mElementIdx) {
        case 0:
            return mField.at(0);
        case 1:
            return mField.at(35);
        case 2:
            return mField.at(21);
        }
        throw new RuntimeException();
    }

    private Element getRandomElement() {
        Element e;
        switch (mElementIdx) {
        case 0:
            e = KnownElements.get("H{+}");
            break;
        case 1:
            e = KnownElements.get("H{+}");
            break;
        case 2:
            e = KnownElements.get("Cl{-}");
            break;
        default:
            throw new RuntimeException();
        }
        ++mElementIdx;
        return e;
    }
}
