package com.yarcat.chemistrylines.field;

/** Represents a cell in a game field. */
public final class Cell {

    private Element mElement = null;

    public boolean isEmpty() {
        return getElement() == null;
    }

    public void setElement(Element element) {
        mElement = element;
    }

    public Element getElement() {
        return mElement;
    }
}
