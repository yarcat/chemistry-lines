package com.yarcat.chemistrylines.game;

import java.util.List;

import com.yarcat.chemistrylines.algorithms.CompoundReporter.CompoundReference;
import com.yarcat.chemistrylines.field.Field;

public class ImmediateFieldCleaner extends FieldCleaner.Base {

    public ImmediateFieldCleaner (Field field) {
        super(field);
    }

    @Override
    public boolean process(List<CompoundReference> compounds) {
        for (CompoundReference ref : compounds) {
            beforeCompoundRemoved(ref);
        }
        for (CompoundReference ref : compounds) {
            mField.removeCompound(ref.getCells());
        }
        for (CompoundReference ref : compounds) {
            afterCompoundRemoved(ref);
        }
        afterProcess();
        return !compounds.isEmpty();
    }
}
