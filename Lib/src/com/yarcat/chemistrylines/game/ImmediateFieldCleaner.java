package com.yarcat.chemistrylines.game;

import java.util.ArrayList;

import com.yarcat.chemistrylines.field.Field;

public class ImmediateFieldCleaner extends FieldCleaner.Base implements FieldCleaner {
    private Field mField;

    public ImmediateFieldCleaner (Field field) {
        mField = field;
    }

    @Override
    public boolean process(ArrayList<int[]> compounds) {
        for (int[] cells : compounds) {
            onCompoundRemove(mField, cells);
        }
        for (int[] cells : compounds) {
            mField.removeCompound(cells);
        }
        return !compounds.isEmpty();
    }
}
