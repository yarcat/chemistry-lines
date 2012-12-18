package com.yarcat.chemistrylines.view;

import android.annotation.SuppressLint;

public class SelectionInView {
    public interface Listener {

        public void onNewSource(int n);

        public void onNewTarget(int n);

        public void onSourceCleared(int n);

        public void onTargetCleared(int n);
    }

    private Listener mListener;
    private int mSource = -1;
    private int mTarget = -1;

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
        mSource = n;
        if (mListener != null && hasSource()) {
            mListener.onNewSource(n);
        }
    }

    private void selectTarget(int n) {
        clearTarget();
        mTarget = n;
        if (mListener != null && hasTarget()) {
            mListener.onNewTarget(n);
        }
    }

    public void clearSource() {
        if (hasSource()) {
            if (mListener != null) {
                mListener.onSourceCleared(mSource);
            }
            mSource = -1;
        }
    }

    public void clearTarget() {
        if (hasTarget()) {
            if (mListener != null) {
                mListener.onTargetCleared(mTarget);
            }
            mTarget = -1;
        }
    }

    public void clear() {
        clearSource();
        clearTarget();
    }

    public boolean hasSource() {
        return mSource != -1;
    }

    public int getSource() {
        return mSource;
    }

    public boolean hasTarget() {
        return mTarget != -1;
    }

    public int getTarget() {
        return mTarget;
    }

    public void setListener(Listener l) {
        mListener = l;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String toString() {
        return String
            .format("Selection(%d,  %d)", mSource, mTarget);
    }
}
