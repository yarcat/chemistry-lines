package com.yarcat.chemistrylines;

/** Represents a cell in a game field. */
public final class Cell {

    private boolean mEmpty = true;

    public boolean isEmpty() {
        return mEmpty;
    }

    public void setEmpty(boolean empty) {
        mEmpty = empty;
    }
}