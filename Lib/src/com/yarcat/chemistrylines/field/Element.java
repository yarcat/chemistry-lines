package com.yarcat.chemistrylines.field;

/** Represents chemical ion or compound. */
public class Element {

    // @formatter:off
    /** Wikipedia's Periodic Table categories */
    public enum Category {
        Undefined,
        AlkaliMetal,
        AlkalineMetal,
        Lanthanoid,
        Actinoid,
        TransitionMetal,
        PostTransitionMetal,
        Metalloid,
        OtherNonMetal,
        Halogen,
        NobleGas,
    }

    public enum StateOfMatter {
        Undefined,
        Solid,
        Liquid,
        Gas,
    }
    // @formatter:on

    private final String mId;
    private final String mName;
    private Category mCategory;
    private StateOfMatter mMatter;
    private boolean mStartsCompound;
    private boolean mIsFinal;
    private int mAtomCount;

    public Element(String id, String name) {
        mId = id;
        mName = name;
        mCategory = Category.Undefined;
        mMatter = StateOfMatter.Undefined;
        mAtomCount = 0;
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

    public Category category() {
        return mCategory;
    }

    public Element category(Category category) {
        mCategory = category;
        return this;
    }

    public StateOfMatter stateOfMatter() {
        return mMatter;
    }

    public Element stateOfMatter(StateOfMatter matter) {
        mMatter = matter;
        return this;
    }

    public int atomCount() {
        return mAtomCount;
    }

    public Element atomCount(int c) {
        mAtomCount = c;
        return this;
    }

    @Override
    public String toString() {
        return getId();
    }
}
