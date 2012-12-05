package com.yarcat.chemistrylines.field;

/** Rectangular game field. */
public class RectField extends Field.BaseField {

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

    @Override
    public int getLength() {
        return mCells.length;
    }

    protected final int cellCol(int n) {
        return 0 <= n && n < getLength() ? n % mCols : -1;
    }

    protected final int cellRow(int n) {
        return 0 <= n && n < getLength() ? n / mCols : -1;
    }

    protected final int cellNo(int col, int row) {
        if (0 <= col && col < mCols && 0 <= row && row < mRows) {
            return col + row * mCols;
        } else {
            return -1;
        }
    }

    protected final boolean cellExists(int col, int row) {
        return 0 <= col && col < mCols && 0 <= row && row < mRows;
    }

    @Override
    public Cell at(int n) {
        return 0 <= n && n < getLength() ? mCells[n] : null;
    }

    private static final int[][] siblingShifts = new int[][] { { -1, 0 },
            { 0, -1 }, { +1, 0 }, { 0, +1 } };

    @Override
    public void visitSiblings(int n, CellVisitor visitor) {
        int col = cellCol(n);
        int row = cellRow(n);

        for (int i = 0; i < siblingShifts.length; ++i) {
            int m = cellNo(col + siblingShifts[i][0], row + siblingShifts[i][1]);
            if (at(m) != null) {
                visitor.visit(m, this);
            }
        }
    }

    @Override
    public void linearScan(SequenceVisitor visitor) {
        for (int origin = 0; origin < getLength(); ++origin) {
            if (at(origin).isEmpty()) {
                continue;
            }
            for (int i = 0; i < siblingShifts.length; ++i) {
                int[] shift = siblingShifts[i];
                int col = cellCol(origin);
                int row = cellRow(origin);

                if (!cellExists(col + shift[0], row + shift[1])
                        || at(cellNo(col + shift[0], row + shift[1])).isEmpty()) {
                    continue;
                }

                visitor.reset();
                visitor.visit(origin, this);

                if (visitor.stopScan(this)) {
                    continue;
                }

                boolean stop;
                do {
                    col += shift[0];
                    row += shift[1];

                    final int m = cellNo(col, row);
                    final Cell cell = at(m);

                    stop = cell == null || cell.isEmpty()
                            || visitor.stopScan(this);
                    if (!stop) {
                        visitor.visit(m, this);
                    }
                } while (!stop);
            }
        }
    }
}
