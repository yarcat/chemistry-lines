package com.yarcat.chemistrylines.algorithms;

import java.util.LinkedList;
import java.util.Queue;

import com.yarcat.chemistrylines.field.Field;
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
    private final int[] mParents;

    /** Create a Path algorithm for a field counting from the origin cell */
    public Path(Field field, int origin) {
        super();
        mField = field;
        mOrigin = origin;
        mStep = new int[field.getLength()];
        mParents = new int[field.getLength()];
    }

    /** Execute the algorithm */
    public void evaluate() {
        final Queue<Integer> queue = new LinkedList<Integer>();

        mStep[mOrigin] = 1;
        mParents[mOrigin] = -1;
        queue.add(mOrigin);
        do {
            final int cur = queue.poll();
            final int nextStep = mStep[cur] + 1;

            mField.visitSiblings(cur, new CellVisitor() {
                public void visit(int n, Field field) {
                    if (mStep[n] == 0) {
                        if (field.at(n).isEmpty()) {
                            queue.add(n);
                        }
                        mStep[n] = nextStep;
                        mParents[n] = cur;
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

    /** Return shortest path from origin to cell or null when unreachable */
    public int[] pathTo(int fin) {
        assert mStep[mOrigin] == 1; // Ensure evaluate() was ran.
        if (mStep[fin] == 0) {
            return null;
        }

        int step = mStep[fin];
        int parent = fin;
        final int[] rv = new int[step];
        do {
            rv[--step] = parent;
            parent = mParents[parent];
        } while (parent != -1);
        return rv;
    }
}
