package com.yarcat.chemistrylines.algorithms;

import com.yarcat.chemistrylines.field.Field;
import com.yarcat.chemistrylines.field.Field.SequenceVisitor;

/** Looks up compounds located linearly on the field */
public class LinearCompoundReporter extends CompoundReporter {

    @Override
    public void startScan(Field field, SequenceVisitor visitor) {
        field.linearScan(visitor);
    }
}
