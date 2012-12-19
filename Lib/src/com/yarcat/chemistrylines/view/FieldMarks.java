package com.yarcat.chemistrylines.view;

import java.util.ArrayList;
import java.util.EnumSet;

import com.yarcat.chemistrylines.algorithms.Path;
import com.yarcat.chemistrylines.field.Field;
import com.yarcat.chemistrylines.view.SelectionInView.SelectionListener;

public class FieldMarks implements SelectionListener {
    // @formatter:off
    public enum Mark {
        SelectedAsSource,
        SelectedAsDestination,
        ReachableFromSource,
    }
    // @formatter:on

    final ArrayList<EnumSet<Mark>> mMarks;
    final Field mField;

    private FieldMarks(Field f, ArrayList<EnumSet<Mark>> m) {
        mField = f;
        mMarks = m;
    }

    public static FieldMarks create(Field f) {
        int s = f.getLength();
        final ArrayList<EnumSet<Mark>> m = new ArrayList<EnumSet<Mark>>(s);
        for (int i = 0; i < s; ++i) {
            m.add(EnumSet.noneOf(Mark.class));
        }
        return new FieldMarks(f, m);
    }

    public void setMark(int n, Mark m) {
        mMarks.get(n).add(m);
    }

    public void clearMark(int n, Mark m) {
        mMarks.get(n).remove(m);
    }

    public boolean hasMark(int n, Mark m) {
        return mMarks.get(n).contains(m);
    }

    @Override
    public void onNewSource(int n) {
        setMark(n, Mark.SelectedAsSource);
        markCellsReachableFrom(n);
    }

    @Override
    public void onNewTarget(int n) {
        setMark(n, Mark.SelectedAsDestination);
    }

    @Override
    public void onSourceCleared(int n) {
        clearMark(n, Mark.SelectedAsSource);
        clearCellsReachableFrom(n);
    }

    @Override
    public void onTargetCleared(int n) {
        clearMark(n, Mark.SelectedAsDestination);
    }

    private void markCellsReachableFrom(int n) {
        Path p = Path.prepare(mField, n);
        for (int i = 0; i < mField.getLength(); ++i) {
            if (p.isReachable(i)) {
                setMark(i, Mark.ReachableFromSource);
            }
        }
    }

    private void clearCellsReachableFrom(int n) {
        for (int i = 0; i < mField.getLength(); ++i) {
            clearMark(i, Mark.ReachableFromSource);
        }
    }
}
