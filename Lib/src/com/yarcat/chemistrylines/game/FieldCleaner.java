package com.yarcat.chemistrylines.game;

import java.util.ArrayList;

import com.yarcat.chemistrylines.algorithms.CompoundReporter.CompoundListener;
import com.yarcat.chemistrylines.algorithms.CompoundReporter.CompoundReference;
import com.yarcat.chemistrylines.field.Field;

public interface FieldCleaner {

    public abstract void setRemoveListener(CompoundListener removeListener);

    public abstract boolean process(ArrayList<CompoundReference> compounds);

    public abstract class Base implements FieldCleaner {

        private CompoundListener mRemoveListener;
        protected Field mField;

        @Override
        public void setRemoveListener(CompoundListener removeListener) {
            mRemoveListener = removeListener;
        }

        protected void onCompoundRemove(CompoundReference ref) {
            if (mRemoveListener != null) {
                mRemoveListener.foundCompound(ref);
            }
        }
    }
}
