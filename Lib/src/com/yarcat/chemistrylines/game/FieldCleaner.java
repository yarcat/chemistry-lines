package com.yarcat.chemistrylines.game;

import java.util.List;

import com.yarcat.chemistrylines.algorithms.CompoundReporter.CompoundListener;
import com.yarcat.chemistrylines.algorithms.CompoundReporter.CompoundReference;
import com.yarcat.chemistrylines.field.Field;

public interface FieldCleaner {

    public void setRemoveListener(CompoundListener removeListener);

    public boolean process(List<CompoundReference> compounds);

    public interface ProcessListener {
        public void afterCleanerProcess();
    }

    public void setProcessListener(ProcessListener l);

    public abstract class Base implements FieldCleaner {

        private CompoundListener mRemoveListener;
        private ProcessListener mProcessListener;
        protected final Field mField;

        protected Base(Field field) {
            mField = field;
        }

        @Override
        public void setRemoveListener(CompoundListener removeListener) {
            mRemoveListener = removeListener;
        }

        protected void onCompoundRemove(CompoundReference ref) {
            if (mRemoveListener != null) {
                mRemoveListener.foundCompound(ref);
            }
        }

        @Override
        public void setProcessListener(ProcessListener l) {
            mProcessListener = l;
        }

        protected void afterProcess() {
            if (mProcessListener != null) {
                mProcessListener.afterCleanerProcess();
            }
        }
    }
}
