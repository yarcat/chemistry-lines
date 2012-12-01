package com.yarcat.chemistrylines.game;

import static com.yarcat.chemistrylines.algorithms.RandomCell.getRandomEmptyCell;

import com.yarcat.chemistrylines.algorithms.CompoundReporter.CompoundListener;
import com.yarcat.chemistrylines.algorithms.CompoundReporter.CompoundReference;
import com.yarcat.chemistrylines.algorithms.CompoundScanner;
import com.yarcat.chemistrylines.algorithms.Path;
import com.yarcat.chemistrylines.field.Element;
import com.yarcat.chemistrylines.field.Field;

public abstract class LinesGame implements GameLogic {

    private int mNewPortionSize;
    private Field mField;
    private CompoundScanner mScanner;
    private FieldCleaner mFieldCleaner;
    private ElementGenerator mElementGenerator;
    private GameLogger mGameLog;

    public LinesGame(Field f, CompoundScanner s, ElementGenerator g) {
        mField = f;
        mScanner = s;
        mElementGenerator = g;
        mNewPortionSize = 3;
        setGameLogger(null);
        setFieldCleaner(new ImmediateFieldCleaner(mField));
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
            if (!cleanupField()) {
                addItems();
            }
        }
    }

    private boolean cleanupField() {
        return mFieldCleaner.process(mScanner.scan(mField));
    }

    @Override
    public void addItems() {
        int[] addedCells = new int[mNewPortionSize];
        for (int i = 0; i < mNewPortionSize; ++i) {
            int n = getRandomEmptyCell(mField);
            if (n < 0) {
                break;
            }
            mField.at(n).setElement(mElementGenerator.getNext());
            addedCells[i] = n;
        }
        onElementsAdded(addedCells);
        cleanupField();
    }

    private void onElementsAdded(int[] addedCells) {
        mGameLog.elementsAdded(mField, addedCells);
    }

    @Override
    public Element[] previewNextElements() {
        return mElementGenerator.preview(mNewPortionSize);
    }

    @Override
    public void setGameLogger(GameLogger gameLog) {
        if (gameLog == null) {
            mGameLog = new GameLogger.DummyGameLogger();
        } else {
            mGameLog = gameLog;
        }
    }

    @Override
    public FieldCleaner getFieldCleaner() {
        return mFieldCleaner;
    }

    private final CompoundListener mRemoveListener = new CompoundListener() {
        @Override
        public void foundCompound(CompoundReference ref) {
            mGameLog.compoundRemoved(mField, ref);
        }
    };
    @Override
    public void setFieldCleaner(FieldCleaner cleaner) {
        mFieldCleaner = cleaner;
        mFieldCleaner.setRemoveListener(mRemoveListener);
    }

}
