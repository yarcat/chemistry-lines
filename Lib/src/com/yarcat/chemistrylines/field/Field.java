package com.yarcat.chemistrylines.field;


/** Game field (model) interface. */
public interface Field {

    /** Returns a cell at a given index. */
    public Cell at(int n);

    /** Returns amount of cells. */
    public int getLength();

    /** Allows to visit _all_ siblings of a given cell. */
    public interface CellVisitor {
        /** Called by a field for a cell. */
        public void visit(int n, Field field);
    }

    /** Calls CellVisitor.visit() for the surrounding cells. */
    public void visitSiblings(int n, CellVisitor visitor);

    /** Return set of sibling cells */
    public int[] getSiblings(int n);

    /** Allows to scan sequence of cells */
    public interface SequenceVisitor {
        /** Called before processing a sequence */
        public void reset();

        /** Called by a field for a cell. */
        public void visit(int n, Field field);

        /** Check whether the sequence scan should be stopped. */
        public boolean stopScan(Field field);
    }

    /**
     * Scan all linear sequences of non-empty cells.
     *
     * Sequences must be of 2 cells at least.
     */
    public void linearScan(SequenceVisitor visitor);

    public void removeCompound(int[] cells);

    /* Implementation of functionality common for all fields */
    public static abstract class BaseField implements Field {

        @Override
        public void removeCompound(int[] cells) {
            for (int n : cells) {
                at(n).setElement(null);
            }
        }
    }
}
