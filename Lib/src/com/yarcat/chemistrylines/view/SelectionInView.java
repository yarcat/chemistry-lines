package com.yarcat.chemistrylines.view;

public class SelectionInView {
    public interface SelectionListener {

        public void onNewSource(int n);

        public void onNewTarget(int n);

        public void onSourceCleared(int i);

        public void onTargetCleared(int i);
    }

    private SelectionListener mListener;
    private int[] mSelection = new int[] { -1, -1 };

    public void select(int n) {
        if (hasSource()) {
            if (getSource() != n) {
                selectTarget(n);
            }
        } else {
            selectSource(n);
        }
    }

    private void selectSource(int n) {
        if (mListener != null && hasSource()) {
            mListener.onSourceCleared(mSelection[0]);
        }
        mSelection[0] = n;
        if (mListener != null && hasSource()) {
            mListener.onNewSource(n);
        }
    }

    private void selectTarget(int n) {
        if (mListener != null && hasDestination()) {
            mListener.onTargetCleared(mSelection[1]);
        }
        mSelection[1] = n;
        if (mListener != null && hasDestination()) {
            mListener.onNewTarget(n);
        }
    }

    public void clear() {
        if (mListener != null) {
            if (hasSource()) {
                mListener.onSourceCleared(mSelection[0]);
            }
            if (hasDestination()) {
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

    public boolean hasDestination() {
        return mSelection[1] != -1;
    }

    public int getDestination() {
        return mSelection[1];
    }

    public void setListener(SelectionListener l) {
        mListener = l;
    }
}
