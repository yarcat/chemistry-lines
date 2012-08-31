package com.yarcat.chemistrylines.field;

/** Represents chemical ion or compound. */
public class Element {
    private final String mId;
    private final String mName;

    public Element(String id, String name) {
        mId = id;
        mName = name;
    }

    public final String getId() {
        return mId;
    }

    public final String getName() {
        return mName;
    }
}
