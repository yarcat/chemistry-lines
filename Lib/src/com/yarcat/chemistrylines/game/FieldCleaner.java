package com.yarcat.chemistrylines.game;

import java.util.ArrayList;

import com.yarcat.chemistrylines.algorithms.CompoundReporter.CompoundListener;
import com.yarcat.chemistrylines.field.Field;

public class FieldCleaner {
    private CompoundListener mRemoveListener;
    private Field mField;

    FieldCleaner(Field field) {
        mField = field;
    }

    public void setRemoveListener(CompoundListener removeListener) {
        mRemoveListener = removeListener;
    }

    public boolean process(ArrayList<int[]> compounds) {
        for (int[] cells : compounds) {
            onCompoundRemove(mField, cells);
        }
        for (int[] cells : compounds) {
            mField.removeCompound(cells);
        }
        return !compounds.isEmpty();
    }

    public void onCompoundRemove(Field field, int[] cells) {
        if (mRemoveListener != null) {
            mRemoveListener.foundCompound(field, cells);
        }
    }
}
