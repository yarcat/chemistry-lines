package com.yarcat.chemistrylines.algorithms;

import java.util.LinkedList;
import java.util.Queue;

import com.yarcat.chemistrylines.field.Cell;
import com.yarcat.chemistrylines.field.Field;
import com.yarcat.chemistrylines.field.Field.CellMatcher;
import com.yarcat.chemistrylines.field.Field.CellVisitor;

/** Shorted path algorithm */
public class Path {

    /** Return distance between 2 cells on the field or -1 when unreachable */
    public static int distance(Field field, int origin, int fin) {
        final Path path = new Path(field, origin);
        path.evaluate();
        return path.distanceTo(fin);
    }

    /** Check whether there is a path between 2 cells on the field */
    public static boolean isReachable(Field field, int origin, int fin) {
        return distance(field, origin, fin) >= 0;
    }

    /** Return a path from the origin to the fin or null when unreachable */
    public static int[] path(Field field, int origin, int fin) {
        final Path path = new Path(field, origin);
        path.evaluate();
        return path.pathTo(fin);
    }

    private final Field mField;
    private final int mOrigin;
    private final int[] mStep;

    /** Create a Path algorithm for a field counting from the origin cell */
    public Path(Field field, int origin) {
        super();
        mField = field;
        mOrigin = origin;
        mStep = new int[field.getLength()];
    }

    /** Execute the algorithm */
    public void evaluate() {
        if (!mField.at(mOrigin).isEmpty()) {
            return;
        }

        final Queue<Integer> queue = new LinkedList<Integer>();

        mStep[mOrigin] = 1;
        queue.add(mOrigin);
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

    /** Return distance from the origin to a cell */
    public int distanceTo(int n) {
        return mStep[mOrigin] == 1 ? mStep[n] - 1 : -1;
    }

    /** Check whether a cell is reachable from the origin */
    public boolean isReachable(int n) {
        return distanceTo(n) >= 0;
    }

    /** Return a shorted path from the origin to a cell or null when unreachable */
    public int[] pathTo(int fin) {
        if (mStep[mOrigin] != 1 || mStep[fin] == 0) {
            return null;
        }

        int step = mStep[fin];
        final int[] rv = new int[step];

        rv[step - 1] = fin;
        for (; step > 1; --step) {
            final int prevStep = step - 1;
            rv[prevStep - 1] = mField.matchSibling(rv[step - 1],
                    new CellMatcher() {
                        public boolean match(int n, Cell cell) {
                            return mStep[n] == prevStep;
                        }
                    });
        }

        return rv;
    }
}
