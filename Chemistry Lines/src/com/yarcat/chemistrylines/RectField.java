package com.yarcat.chemistrylines;

/** Rectangle game field. */
public class RectField implements Field {

    private final Cell[] mCells;
    private final int mCols;
    private final int mRows;

    /**
     * Create a field.
     * 
     * @param cols
     *            Amount of columns.
     * @param rows
     *            Amount of rows.
     */
    public RectField(int cols, int rows) {
        mCols = cols;
        mRows = rows;
        mCells = new Cell[rows * cols];
        for (int i = 0; i < getLength(); ++i) {
            mCells[i] = new Cell();
        }
    }

    public int getLength() {
        return mCells.length;
    }

    protected final int cellCol(int n) {
        return (0 <= n && n < getLength()) ? (n % mCols) : -1;
    }

    protected final int cellRow(int n) {
        return (0 <= n && n < getLength()) ? (n / mCols) : -1;
    }

    protected final int cellNo(int col, int row) {
        if (0 <= col && col < mCols && 0 <= row && row < mRows) {
            return col + row * mCols;
        } else {
            return -1;
        }
    }

    public Cell at(int n) {
        return (0 <= n && n < getLength()) ? mCells[n] : null;
    }

    private static final int[][] siblingShifts = new int[][] { { -1, 0 },
            { 0, -1 }, { +1, 0 }, { 0, +1 } };

    public void visitSiblings(int n, CellVisitor visitor) {
        int col = cellCol(n);
        int row = cellRow(n);

        for (int i = 0; i < siblingShifts.length; ++i) {
            int m = cellNo(col + siblingShifts[i][0], row + siblingShifts[i][1]);
            Cell cell = at(m);
            if (cell != null) {
                visitor.visit(m, cell);
            }
        }
    }

}
