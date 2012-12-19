package com.yarcat.chemistrylines.view;

import android.annotation.SuppressLint;

public class SelectionInView {
    public interface SelectionListener {

        public void onNewSource(int n);

        public void onNewTarget(int n);

        public void onSourceCleared(int i);

        public void onTargetCleared(int i);
    }

    private SelectionListener mListener;
    private final int[] mSelection = new int[] { -1, -1 };

    public void select(int n) {
        if (hasSource()) {
            if (getSource() == n) {
                clear();
            } else {
                selectTarget(n);
            }
        } else {
            selectSource(n);
        }
    }

    private void selectSource(int n) {
        clearSource();
        mSelection[0] = n;
        if (mListener != null && hasSource()) {
            mListener.onNewSource(n);
        }
    }

    private void selectTarget(int n) {
        clearTarget();
        mSelection[1] = n;
        if (mListener != null && hasTarget()) {
            mListener.onNewTarget(n);
        }
    }

    private void clearSource() {
        if (hasSource()) {
            if (mListener != null) {
                mListener.onSourceCleared(mSelection[0]);
            }
            mSelection[0] = -1;
        }
    }

    private void clearTarget() {
        if (hasTarget()) {
            if (mListener != null) {
                mListener.onTargetCleared(mSelection[1]);
            }
            mSelection[1] = -1;
        }
    }

    public void clear() {
        if (mListener != null) {
            if (hasSource()) {
                mListener.onSourceCleared(mSelection[0]);
            }
            if (hasTarget()) {
                mListener.onTargetCleared(mSelection[1]);
            }
        }
        mSelection[0] = mSelection[1] = -1;
    }

    public boolean hasSource() {
        return mSelection[0] != -1;
    }

    public int getSource() {
        return mSelection[0];
    }

    public boolean hasTarget() {
        return mSelection[1] != -1;
    }

    public int getTarget() {
        return mSelection[1];
    }

    public void setListener(SelectionListener l) {
        mListener = l;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String toString() {
        return String
            .format("Selection(%d,  %d)", mSelection[0], mSelection[1]);
    }
}
