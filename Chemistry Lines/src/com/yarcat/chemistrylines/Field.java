package com.yarcat.chemistrylines;

/** Game field (model) interface. */
public interface Field {

    /** Returns a cell at a given index. */
    public Cell at(int n);

    /** Returns amount of cells. */
    public int getLength();

    /** Allows to visit siblings of a given cell. */
    public interface CellVisitor {
        /**
         * Called by a field for a cell.
         * 
         * @param n
         *            Index of the cell.
         * @param cell
         *            Cell value.
         */
        public void visit(int n, Cell cell);
    }

    /** Calls CellVisitor.visit for the surrounding cells. */
    public void visitSiblings(int n, CellVisitor visitor);
}
