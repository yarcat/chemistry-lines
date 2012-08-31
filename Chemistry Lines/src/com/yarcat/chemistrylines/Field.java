package com.yarcat.chemistrylines;

/** Game field (model) interface. */
public interface Field {

    /** Returns a cell at a given index. */
    public Cell at(int n);

    /** Returns amount of cells. */
    public int getLength();

    /** Allows to visit _all_ siblings of a given cell. */
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

    /** Calls CellVisitor.visit() for the surrounding cells. */
    public void visitSiblings(int n, CellVisitor visitor);

    /** Allows to find a sibling of a given cell */
    public interface CellMatcher {
        /**
         * Called by a field for a cell.
         *
         * @param n
         *            Index of the cell.
         * @param cell
         *            Cell value.
         * @return Whether a cell matches requirements.
         */
        public boolean match(int n, Cell cell);
    }

    /** Index of a matching cell within surrounding cells or -1 if none */
    public int matchSibling(int n, CellMatcher matcher);
}
