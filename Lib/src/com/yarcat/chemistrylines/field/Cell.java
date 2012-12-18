package com.yarcat.chemistrylines.field;

import java.util.EnumSet;

/** Represents a cell in a game field. */
public final class Cell {

    // @formatter:off
    public enum Mark {
        SelectedAsSource,
        SelectedAsDestination,
        ReachableFromSource,
    }
    // @formatter:on

    private Element mElement = null;
    private EnumSet<Mark> x = EnumSet.noneOf(Mark.class);

    public boolean isEmpty() {
        return getElement() == null;
    }

    public void setElement(Element element) {
        mElement = element;
    }

    public Element getElement() {
        return mElement;
    }

    public void setMark(Mark m) {
        x.add(m);
    }

    public void clearMark(Mark m) {
        if (x.contains(m)) {
            x.remove(m);
        }
    }

    public boolean hasMark(Mark m) {
        return x.contains(m);
    }
}
