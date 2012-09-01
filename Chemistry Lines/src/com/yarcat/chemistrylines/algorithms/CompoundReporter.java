package com.yarcat.chemistrylines.algorithms;

import java.util.ArrayList;

import com.yarcat.chemistrylines.field.Cell;
import com.yarcat.chemistrylines.field.Field;
import com.yarcat.chemistrylines.field.Field.SequenceVisitor;

/** Looks up compounds on a field */
public abstract class CompoundReporter {

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
         * Check whether a sequence of cells is a valid start of a compound.
         *
         * @param cells
         *            sequence of indexes of cells forming a compound.
         */
        public boolean startsCompound(Field field, final int[] cells);

        /**
         * Check if a sequence of cells forms a compound.
         *
         * @param cells
         *            sequence of indexes of cells forming a compound.
         */
        public boolean isCompound(Field field, final int[] cells);
    }

    /** Start scanning the field using an initialized sequence visitor */
    public abstract void startScan(Field field, SequenceVisitor visitor);

    /** Scan the field and report compounds to the listener */
    public void scan(Field field, CompoundDetector detector,
            CompoundListener listener) {
        // TODO(luch) move field to the SequenceVisitor calls
        startScan(field, new ReportingVisitor(field, detector, listener));
    }

    /** Actual compound reporting is performed here */
    private final class ReportingVisitor implements SequenceVisitor {

        final Field mField;
        final CompoundDetector mDetector;
        final CompoundListener mListener;
        final ArrayList<Integer> mPath = new ArrayList<Integer>(8);

        public ReportingVisitor(Field field, CompoundDetector detector,
                CompoundListener listener) {
            mField = field;
            mDetector = detector;
            mListener = listener;
        }

        public void reset() {
            mPath.clear();
        }

        public void visit(int n, Cell cell) {
            mPath.add(n);
            if (mDetector.isCompound(mField, clonePath())) {
                mListener.foundCompound(mField, clonePath());
            }
        }

        public boolean stopScan() {
            return !mDetector.startsCompound(mField, clonePath());
        }

        int[] clonePath() {
            int size = mPath.size();
            int[] rv = new int[size];
            for (int i = 0; i < size; ++i) {
                rv[i] = mPath.get(i);
            }
            return rv;
        }
    }
}
