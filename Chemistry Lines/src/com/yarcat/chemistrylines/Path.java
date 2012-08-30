package com.yarcat.chemistrylines;

import java.util.LinkedList;
import java.util.Queue;

import com.yarcat.chemistrylines.Field.CellVisitor;

public class Path {

    public static int distance(Field field, int origin, int fin) {
        final Path path = new Path(field, origin);
        path.evaluate();
        return path.distanceTo(fin);
    }

    public static boolean isReachable(Field field, int origin, int fin) {
        return distance(field, origin, fin) >= 0;
    }

    private final Field mField;
    private final int mFrom;
    private final int[] mStep;

    public Path(Field field, int from) {
        super();
        mField = field;
        mFrom = from;
        mStep = new int[field.getLength()];
    }

    public void evaluate() {
        if (!mField.at(mFrom).isEmpty()) {
            return;
        }

        final Queue<Integer> queue = new LinkedList<Integer>();

        mStep[mFrom] = 1;
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
        } while (!queue.isEmpty());
    }

    public int distanceTo(int n) {
        return mStep[mFrom] == 1 ? mStep[n] - 1 : -1;
    }

    public boolean isReachable(int n) {
        return distanceTo(n) >= 0;
    }
}