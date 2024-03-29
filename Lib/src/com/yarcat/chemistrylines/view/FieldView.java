package com.yarcat.chemistrylines.view;

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
     */
    public void select(int n);

    /** Called to cancel any selections. */
    public void cancel();

    /** Apply current selection. */
    public void apply();
}
