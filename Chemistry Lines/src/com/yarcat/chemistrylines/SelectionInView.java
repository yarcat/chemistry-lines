package com.yarcat.chemistrylines;

public class SelectionInView {
    private int[] mSelection = new int[] { -1, -1 };

    public void select(int n) {
        if (mSelection[0] == -1) {
            mSelection[0] = n;
        } else {
            mSelection[1] = n;
        }
    }

    public void clear() {
        mSelection[0] = mSelection[1] = -1;
    }

    public boolean hasSource() {
        return mSelection[0] != -1;
    }

    public int getSource() {
        return mSelection[0];
    }

    public boolean hasDestination() {
        return mSelection[1] != -1;
    }

    public int getDestination() {
        return mSelection[1];
    }
}
