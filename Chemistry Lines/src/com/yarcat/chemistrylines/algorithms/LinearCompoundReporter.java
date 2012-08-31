package com.yarcat.chemistrylines.algorithms;

import java.util.ArrayList;

import com.yarcat.chemistrylines.field.Cell;
import com.yarcat.chemistrylines.field.Field;
import com.yarcat.chemistrylines.field.Field.SequenceVisitor;

public class LinearCompoundReporter implements CompoundReporter {

    public void scan(Field field, CompoundDetector detector,
            CompoundListener listener) {
        field.linearScan(new SeqVisitor(field, detector, listener));
    }

    private final class SeqVisitor implements SequenceVisitor {

        final Field mField;
        final CompoundDetector mDetector;
        final CompoundListener mListener;
        final ArrayList<Integer> mPath = new ArrayList<Integer>(8);

        public SeqVisitor(Field field, CompoundDetector detector,
                CompoundListener listener) {
            mField = field;
            mDetector = detector;
            mListener = listener;
        }

        public void reset() {
            mPath.clear();
            System.out.println();
        }

        public void visit(int n, Cell cell) {
            mPath.add(n);
            System.out.print(n);
            System.out.print(" ");
            if (mDetector.isCompound(mField, clonePath())) {
                mListener.foundCompound(mField, clonePath());
            }
        }

        public boolean stopScan() {
            System.out.print(mDetector.isStart(mField, clonePath()) ? ". "
                    : "]");
            return !mDetector.isStart(mField, clonePath());
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
