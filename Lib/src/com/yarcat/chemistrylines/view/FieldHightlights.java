package com.yarcat.chemistrylines.view;

import java.util.ArrayList;
import java.util.EnumSet;

import com.yarcat.chemistrylines.algorithms.Path;
import com.yarcat.chemistrylines.field.Field;
import com.yarcat.chemistrylines.view.SelectionInView;

public class FieldHightlights implements SelectionInView.Listener {
    // @formatter:off
    public enum Mark {
        SOURCE,
        TARGET,
        REACHABLE,
    }
    // @formatter:on

    public interface Listener {
        public void onHighlightChange();
    }

    final ArrayList<EnumSet<Mark>> mMarks;
    final Field mField;
    private Listener mListener;
    private int mLastSource;

    private FieldHightlights(Field f, ArrayList<EnumSet<Mark>> m) {
        mField = f;
        mMarks = m;
        mLastSource = -1;
    }

    public static FieldHightlights create(Field f) {
        int s = f.getLength();
        final ArrayList<EnumSet<Mark>> m = new ArrayList<EnumSet<Mark>>(s);
        for (int i = 0; i < s; ++i) {
            m.add(EnumSet.noneOf(Mark.class));
        }
        return new FieldHightlights(f, m);
    }

    private void setMark(int n, Mark m) {
        mMarks.get(n).add(m);
    }

    private void clearMark(int n, Mark m) {
        mMarks.get(n).remove(m);
    }

    public boolean hasMark(int n, Mark m) {
        return mMarks.get(n).contains(m);
    }

    @Override
    public void onNewSource(int n) {
        mLastSource = n;
        setMark(n, Mark.SOURCE);
        markCellsReachableFrom(mLastSource);
        onChange();
    }

    @Override
    public void onNewTarget(int n) {
        setMark(n, Mark.TARGET);
        onChange();
    }

    @Override
    public void onSourceCleared(int n) {
        mLastSource = -1;
        clearMark(n, Mark.SOURCE);
        clearReachableFromSource();
        onChange();
    }

    @Override
    public void onTargetCleared(int n) {
        clearMark(n, Mark.TARGET);
        onChange();
    }

    private void markCellsReachableFrom(int n) {
        Path p = Path.prepare(mField, n);
        boolean markEmpties = p.reachableCount < mField.getLength() * 2 / 3;
        for (int i = 0; i < mField.getLength(); ++i) {
            if (p.isReachable(i) && (!mField.at(i).isEmpty() || markEmpties)) {
                setMark(i, Mark.REACHABLE);
            }
        }

    }

    private void clearReachableFromSource() {
        for (int i = 0; i < mField.getLength(); ++i) {
            clearMark(i, Mark.REACHABLE);
        }
    }

    private void onChange() {
        if (mListener != null) {
            mListener.onHighlightChange();
        }
    }

    public void setListener(Listener l) {
        mListener = l;
    }

    public void onFieldChange() {
        if (mLastSource >= 0) {
            clearReachableFromSource();
            markCellsReachableFrom(mLastSource);
        }
    }
}
