package com.yarcat.chemistrylines.game;

import java.util.ArrayList;

import com.yarcat.chemistrylines.algorithms.CompoundReporter.CompoundListener;
import com.yarcat.chemistrylines.field.Field;

public interface FieldCleaner {

    public abstract void setRemoveListener(CompoundListener removeListener);

    public abstract boolean process(ArrayList<int[]> compounds);

    public abstract class Base implements FieldCleaner {

        private CompoundListener mRemoveListener;

        @Override
        public void setRemoveListener(CompoundListener removeListener) {
            mRemoveListener = removeListener;
        }

        protected void onCompoundRemove(Field field, int[] cells) {
            if (mRemoveListener != null) {
                mRemoveListener.foundCompound(field, cells);
            }
        }

    }

}
