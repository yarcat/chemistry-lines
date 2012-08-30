package com.yarcat.chemistrylines.touchlogic;

/** Game field visual interface. */
public interface FieldView {
    /**
     * Returns index in the field.
     * 
     * @param x
     *            Screen coordinate.
     * @param y
     *            Screen coordinate.
     * @return Zero based index in the field or -1.
     */
    public int getIndex(float x, float y);

    /**
     * Called when user selects a cell.
     * 
     * @param n
     *            Index in the field.
     * @param isSource
     *            True if this is a first cell, false if last.
     */
    public void select(int n, boolean isSource);

    /** Called to clean any selection. */
    public void clean();
}
