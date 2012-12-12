package com.yarcat.chemistrylines.game;

import static com.yarcat.chemistrylines.algorithms.RandomCell.getRandomEmptyCell;
import static com.yarcat.chemistrylines.algorithms.RandomCell.countEmptyCells;
import static com.yarcat.chemistrylines.constants.PORTION_SIZE;

import com.yarcat.chemistrylines.algorithms.CompoundReporter.CompoundListener;
import com.yarcat.chemistrylines.algorithms.CompoundReporter.CompoundReference;
import com.yarcat.chemistrylines.algorithms.CompoundScanner;
import com.yarcat.chemistrylines.algorithms.Path;
import com.yarcat.chemistrylines.field.Element;
import com.yarcat.chemistrylines.field.Field;

public abstract class LinesGame implements GameLogic {

    private static final int RETRY_ADD_CLEANUP = 108;

    private final ElementGenerator mElementGenerator;
    private final Field mField;
    private final int mNewPortionSize;
    private final CompoundScanner mScanner;
    private final Scorer mScorer;
    private GameListener mChangeListener;
    private FieldCleaner mFieldCleaner;
    private GameLogger mGameLog;

    public LinesGame(Field f, CompoundScanner s, ElementGenerator g) {
        mScorer = new Scorer.ScoreContainer();
        mElementGenerator = g;
        mField = f;
        mNewPortionSize = PORTION_SIZE;
        mScanner = s;
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
            if (!cleanupField() || isFieldEmpty()) {
                retryAddCleanup();
            }
        }
    }

    private boolean cleanupField() {
        boolean r = mFieldCleaner.process(mScanner.scan(mField));
        if (r) {
            onFieldChange();
        }
        return r;
    }

    @Override
    public void addItems() {
        retryAddCleanup();
    }

    private void retryAddCleanup() {
        int i = RETRY_ADD_CLEANUP;
        do {
            addMoreItems();
            cleanupField();
            --i;
        } while (i > 0 && isFieldEmpty());
    }

    private boolean isFieldEmpty() {
        return countEmptyCells(mField) == mField.getLength();
    }

    private void addMoreItems() {
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
    }

    private void onFieldChange() {
        mChangeListener.onFieldChange(this);
    }

    private void onElementsAdded(int[] addedCells) {
        mGameLog.elementsAdded(mField, addedCells);
        onFieldChange();
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
            updateScore(ref);
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

    @Override
    public Scorer getScorer() {
        return mScorer;
    }

    private void updateScore(CompoundReference ref) {
        mScorer.update(ref);
        mChangeListener.onScoreChange(this);
    }
}
