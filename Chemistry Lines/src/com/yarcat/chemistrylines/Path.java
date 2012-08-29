package com.yarcat.chemistrylines;

import java.util.LinkedList;
import java.util.Queue;

import com.yarcat.chemistrylines.Field.CellVisitor;

public class Path {

    public static boolean isReachable(Field field, int a, int b) {
        final Path path = new Path(field, a, b);
        path.evaluate();
        return path.isReachable();
    }

    private final Field mField;
    private final int mFrom, mTo;
    private final int[] mStep;

    public Path(Field field, int from, int to) {
        super();
        mField = field;
        mFrom = from;
        mTo = to;
        mStep = new int[field.getLength()];
    }

    public void evaluate() {
        mStep[mFrom] = 1;
        if (mTo == mFrom) {
            return;
        }

        final Queue<Integer> queue = new LinkedList<Integer>();

        queue.add(mFrom);
        do {
            final int cur = queue.poll();
            final int nextStep = mStep[cur] + 1;

            mField.visitSiblings(cur, new CellVisitor() {
                public void visit(int n, Cell cell) {
                    if (mStep[n] == 0 && cell.isEmpty()) {
                        queue.add(n);
                        mStep[n] = nextStep;
                    }
                }
            });

        } while (mStep[mTo] == 0 && !queue.isEmpty());

    }

    public boolean isReachable() {
        return mStep[mFrom] == 1 && mStep[mTo] > 0;
    }
}