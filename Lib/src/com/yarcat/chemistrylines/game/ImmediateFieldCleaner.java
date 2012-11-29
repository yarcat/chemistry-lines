package com.yarcat.chemistrylines.game;

import java.util.ArrayList;

import com.yarcat.chemistrylines.algorithms.CompoundReporter.CompoundReference;
import com.yarcat.chemistrylines.field.Field;

// @formatter:off
public class ImmediateFieldCleaner extends FieldCleaner.Base
        implements FieldCleaner {
// @formatter:on
    private Field mField;

    public ImmediateFieldCleaner (Field field) {
        mField = field;
    }

    @Override
    public boolean process(ArrayList<CompoundReference> compounds) {
        for (CompoundReference ref : compounds) {
            onCompoundRemove(ref);
        }
        for (CompoundReference ref : compounds) {
            mField.removeCompound(ref.getCells());
        }
        return !compounds.isEmpty();
    }
}
