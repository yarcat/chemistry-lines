package com.yarcat.chemistrylines.game;

import static com.yarcat.chemistrylines.algorithms.RandomCell.getRandomEmptyCell;
import static com.yarcat.chemistrylines.constants.PORTION_SIZE;

import com.yarcat.chemistrylines.algorithms.CompoundReporter.CompoundListener;
import com.yarcat.chemistrylines.algorithms.CompoundReporter.CompoundReference;
import com.yarcat.chemistrylines.algorithms.CompoundScanner;
import com.yarcat.chemistrylines.algorithms.Path;
import com.yarcat.chemistrylines.field.Element;
import com.yarcat.chemistrylines.field.Field;

public abstract class LinesGame implements GameLogic {

    private final int mNewPortionSize;
    private final Field mField;
    private final CompoundScanner mScanner;
    private final ElementGenerator mElementGenerator;
    private FieldCleaner mFieldCleaner;
    private GameLogger mGameLog;
    private GameListener mChangeListener;

    public LinesGame(Field f, CompoundScanner s, ElementGenerator g) {
        mField = f;
        mScanner = s;
        mElementGenerator = g;
        mNewPortionSize = PORTION_SIZE;
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
        boolean r = mFieldCleaner.process(mScanner.scan(mField));
        onFieldChange();
        return r;
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

    private void onFieldChange() {
        mChangeListener.onFieldChange(this);
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

    @Override
    public Field getField() {
        return mField;
    }

    @Override
    public void setChangeListener(GameListener listener) {
        mChangeListener = listener;
    }
}
