package com.yarcat.tests.chemistrylines;

import static com.yarcat.tests.chemistrylines.utils.markEmpty;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.yarcat.chemistrylines.field.Field;
import com.yarcat.chemistrylines.field.Field.SequenceVisitor;
import com.yarcat.chemistrylines.field.RectField;

public class LinearScanTest {

    private class ScanResults implements SequenceVisitor {
        public int mResets = 0;
        public int[] mVisited;

        public ScanResults(Field field) {
            mVisited = new int[field.getLength()];
        }

        public void reset() {
            ++mResets;
        }

        public void visit(int n, Field field) {
            ++mVisited[n];
        }

        public boolean stopScan(Field field) {
            return false;
        }
    }

    private ScanResults scan(Field field) {
        ScanResults r = new ScanResults(field);
        field.linearScan(r);
        return r;
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

    private class StopScanResults extends ScanResults {
        public StopScanResults(Field field) {
            super(field);
        }

        @Override
        public boolean stopScan(Field field) {
            return true;
        }
    }

    @Test
    public void scanStop() {
        Field field = markEmpty(new RectField(2, 2), new int[] { 0 });

        ScanResults r = new StopScanResults(field);
        field.linearScan(r);

        assertEquals(4, r.mResets);
        assertArrayEquals(new int[] { 0, 1, 1, 2 }, r.mVisited);
    }
}
