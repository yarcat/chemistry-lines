package com.yarcat.chemistrylines.algorithms;

import com.yarcat.chemistrylines.field.Field;

/** Looks up compounds on a field */
public interface CompoundReporter {

    /** Process compounds found on the field */
    public interface CompoundListener {

        /**
         * Callback on a compound found.
         *
         * @param field
         *            is a field.
         * @param cells
         *            sequence of indexes of cells forming a compound.
         */
        public void foundCompound(Field field, int[] cells);
    }

    /** Detects a compound or its start during field scan */
    public interface CompoundDetector {

        /**
         * Check whether a sequence of field cells starts a formula.
         *
         * @param field
         *            is a field.
         * @param cells
         *            sequence of indexes of cells forming a compound.
         */
        public boolean isStart(Field field, final int[] cells);

        /**
         * Check if a sequence of field cells form a ready compound.
         *
         * @param field
         *            is a field.
         * @param cells
         *            sequence of indexes of cells forming a compound.
         */
        public boolean isCompound(Field field, final int[] cells);
    }

    /** Scan the field and report compounds to the listener */
    public void scan(Field field, CompoundDetector detector,
            CompoundListener listener);
}
