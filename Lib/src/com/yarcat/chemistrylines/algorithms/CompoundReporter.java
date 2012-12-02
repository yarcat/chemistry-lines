package com.yarcat.chemistrylines.algorithms;

import java.util.ArrayList;
import java.util.Arrays;

import com.yarcat.chemistrylines.field.Element;
import com.yarcat.chemistrylines.field.Field;
import com.yarcat.chemistrylines.field.Field.SequenceVisitor;

/** Looks up compounds on a field */
public abstract class CompoundReporter {

    public static class CompoundReference {
        private int[] mCells;
        private Element mCompound;

        public CompoundReference(int[] cells, Element compound) {
            super();
            mCells = cells;
            // Let mCells always be ascending sequence.
            // This has side-effects for calling code.
            if (mCells[mCells.length - 1] < mCells[0]) {
                reverseInplace(mCells);
            }
            mCompound = compound;
        }

        public int[] getCells() {
            return mCells;
        }

        public int getLength() {
            return mCells.length;
        }

        public Element getCompound() {
            return mCompound;
        }

        public static void reverseInplace(int[] a) {
            int l = a.length / 2;
            for (int i = 0; i < l; ++i) {
                int x = a[a.length - 1 - i];
                a[a.length - 1 - i] = a[i];
                a[i] = x;
            }
        }

        @Override
        // To ease debug
        public String toString() {
            return String.format(
                "(%s %s)", Arrays.toString(mCells), mCompound.getId());
        }
    }

    /** Process compounds found on the field */
    public interface CompoundListener {

        public void foundCompound(CompoundReference ref);
    }

    /** Detects a compound or its start during field scan */
    public interface CompoundDetector {

        /**
         * Check whether a sequence of cells is a valid start of a compound.
         *
         * @param cells sequence of indexes of cells forming a compound.
         */
        public boolean startsCompound(Field field, final int[] cells);

        /**
         * Check if a sequence of cells forms a compound.
         *
         * @param cells sequence of indexes of cells forming a compound.
         * @return either an Element representing the compound or null.
         */
        public Element getCompound(Field field, final int[] cells);
    }

    /** Start scanning the field using an initialized sequence visitor */
    public abstract void startScan(Field field, SequenceVisitor visitor);

    /** Scan the field and report compounds to the listener */
    public void scan(Field field, CompoundDetector detector,
            CompoundListener listener) {
        startScan(field, new ReportingVisitor(detector, listener));
    }

    /** Actual compound reporting is performed here */
    private final class ReportingVisitor implements SequenceVisitor {

        final CompoundDetector mDetector;
        final CompoundListener mListener;
        final ArrayList<Integer> mPath = new ArrayList<Integer>(8);

        public ReportingVisitor(CompoundDetector detector,
                CompoundListener listener) {
            mDetector = detector;
            mListener = listener;
        }

        @Override
        public void reset() {
            mPath.clear();
        }

        @Override
        public void visit(int n, Field field) {
            mPath.add(n);
            Element compound = mDetector.getCompound(field, clonePath());
            if (compound != null) {
                CompoundReference ref =
                    new CompoundReference(clonePath(), compound);
                mListener.foundCompound(ref);
            }
        }

        @Override
        public boolean stopScan(Field field) {
            return !mDetector.startsCompound(field, clonePath());
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
