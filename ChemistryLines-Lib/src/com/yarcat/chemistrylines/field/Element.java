package com.yarcat.chemistrylines.field;

/** Represents chemical ion or compound. */
public class Element {
    private final String mId;
    private final String mName;
    private boolean mStartsCompound;
    private boolean mIsFinal;

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

    public final boolean startsCompound() {
        return mStartsCompound;
    }

    public final boolean isFinal() {
        return mIsFinal;
    }

    public Element startsCompound(boolean flag) {
        mStartsCompound = flag;
        return this;
    }

    public Element isFinal(boolean flag) {
        mIsFinal = flag;
        return this;
    }

    @Override
    public String toString() {
        return getId();
    }
}
