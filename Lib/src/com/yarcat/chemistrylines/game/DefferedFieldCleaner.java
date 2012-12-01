package com.yarcat.chemistrylines.game;

import java.util.ArrayList;
import java.util.List;

import com.yarcat.chemistrylines.algorithms.CompoundReporter;
import com.yarcat.chemistrylines.algorithms.CompoundReporter.CompoundReference;
import com.yarcat.chemistrylines.field.Field;

public class DefferedFieldCleaner extends FieldCleaner.Base implements
        FieldCleaner {
    private final ArrayList<CompoundReference> mContents;
    private int[] mMarks;

    public DefferedFieldCleaner(Field field) {
        mContents = new ArrayList<CompoundReference>();
        mField = field;
        mMarks = new int[field.getLength()];
        reset();
    }

    @Override
    public boolean process(List<CompoundReference> compounds) {
        reset();
        mContents.addAll(compounds);
        for (CompoundReference ref : compounds) {
            for (int n : ref.getCells()) {
                ++mMarks[n];
            }
        }
        return false;
    }

    public List<CompoundReference> listCompounds() {
        return new ArrayList<CompoundReporter.CompoundReference>(mContents);
    }

    public void remove(CompoundReference ref) {
        if (mContents.contains(ref)) {
            onCompoundRemove(ref);
            removeCompound(ref.getCells());
            mContents.remove(ref);
        }
    }

    /** Remove a compound keeping overlaps in mind. */
    private void removeCompound(int[] cells) {
        int checkSum = 0;
        for (int n : cells) {
            checkSum += mMarks[n] > 0 ? 1 : 0;
        }
        if (checkSum == cells.length) {
            for (int n : cells) {
                --mMarks[n];
                if (mMarks[n] == 0) {
                    mField.at(n).setElement(null);
                }
            }
        }
    }

    private void reset() {
        mContents.clear();
        for (int n = 0; n < mMarks.length; ++n) {
            mMarks[n] = 0;
        }
    }

    public boolean isCellEmpty(int n) {
        return mMarks[n] == 0;
    }

}
