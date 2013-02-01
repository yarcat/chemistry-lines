package com.yarcat.chemistrylines.game;

import java.util.List;

import com.yarcat.chemistrylines.algorithms.CompoundReporter.CompoundReference;
import com.yarcat.chemistrylines.field.Field;

public interface FieldCleaner {

    public boolean process(List<CompoundReference> compounds);

    public interface RemoveListener {
        public void afterCompoundRemoved(CompoundReference ref);

        public void beforeCompoundRemoved(CompoundReference ref);
    }

    public void setRemoveListener(RemoveListener removeListener);

    public interface ProcessListener {
        public void afterCleanerProcess();
    }

    public void setProcessListener(ProcessListener l);

    public abstract class Base implements FieldCleaner {

        private RemoveListener mRemoveListener;
        private ProcessListener mProcessListener;
        protected final Field mField;

        protected Base(Field field) {
            mField = field;
        }

        @Override
        public void setRemoveListener(RemoveListener removeListener) {
            mRemoveListener = removeListener;
        }

        protected void afterCompoundRemoved(CompoundReference ref) {
            if (mRemoveListener != null) {
                mRemoveListener.afterCompoundRemoved(ref);
            }
        }

        protected void beforeCompoundRemoved(CompoundReference ref) {
            if (mRemoveListener != null) {
                mRemoveListener.beforeCompoundRemoved(ref);
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
