package com.yarcat.tests.chemistrylines;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.yarcat.chemistrylines.field.Celld;
import com.yarcat.chemistrylines.field.RectField;
import com.yarcat.chemistrylines.field.Field.Cell;
import com.yarcat.ccom.yarcat.chemistrylines.field.RectFieldisitor;

public class LinearScanTest {

    private class ScanResults implements SequenceVisitor {
        public int mResets = 0;
        public int[] mVisited;

        public ScanResults(Field field) {
            mVisited = new int[field.getLength()];
        }

        @Override
        public void reset() {
            ++mResets;
        }

        @Override
        public void visit(int n, Cell cell) {
            ++mVisited[n];
        }
    }

    private ScanResults scan(Field field) {
        ScanResults r = new ScanResults(field);
        field.linearScan(r);
        return r;
    }

    private Field markEmpty(Field field, int[] cells) {
        for (int i = 0; i < field.getLength(); ++i) {
            field.at(i).setEmpty(false);
        }
        if (cells != null) {
            for (int i : cells) {
                field.at(i).setEmpty(true);
            }
        }
        return field;
    }

    @Test
    public void scan1x1() {
        ScanResults r = scan(markEmpty(new RectField(1, 1), null));
        assertEquals(0, r.mResets);
        assertArrayEquals(new int[] { 0 }, r.mVisited);
    }

    @Test
    public void scanFilled() {
        ScanResults r = scan(markEmpty(new RectField(2, 2), null));
        assertEquals(8, r.mResets);
        assertArrayEquals(new int[] { 4, 4, 4, 4 }, r.mVisited);
    }

    @Test
    public void scanOneEmpty() {
        ScanResults r = scan(markEmpty(new RectField(2, 2), new int[] { 0 }));
        assertEquals(4, r.mResets);
        assertArrayEquals(new int[] { 0, 2, 2, 4 }, r.mVisited);
    }

    @Test
    public void scanSplittedField() {
        ScanResults r = scan(markEmpty(new RectField(3, 3),
                new int[] { 0, 4, 8 }));
        assertEquals(8, r.mResets);
        assertArrayEquals(new int[] { 0, 2, 4, 2, 0, 2, 4, 2, 0 }, r.mVisited);
    }
}