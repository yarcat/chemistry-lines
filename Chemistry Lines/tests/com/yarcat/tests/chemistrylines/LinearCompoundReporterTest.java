package com.yarcat.tests.chemistrylines;

import static com.yarcat.tests.chemistrylines.utils.markEmpty;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

import com.yarcat.chemistrylines.algorithms.CompoundReporter.CompoundDetector;
import com.yarcat.chemistrylines.algorithms.CompoundReporter.CompoundListener;
import com.yarcat.chemistrylines.algorithms.LinearCompoundReporter;
import com.yarcat.chemistrylines.field.Field;
import com.yarcat.chemistrylines.field.RectField;

public class LinearCompoundReporterTest {

    final int MAX_LEN = 3;

    class FakeDetector implements CompoundDetector {

        @Override
        public boolean startsCompound(Field field, int[] cells) {
            return cells.length <= MAX_LEN;
        }

        @Override
        public boolean isCompound(Field field, int[] cells) {
            // only ascending sequences are test compounds
            return 1 < cells.length && cells.length <= MAX_LEN
                    && cells[0] < cells[1];
        }
    }

    class ReportResults implements CompoundListener {

        final int[] mCount = new int[MAX_LEN + 1];

        @Override
        public void foundCompound(Field field, int[] cells) {
            ++mCount[cells.length];
        }
    }

    final FakeDetector mDetector = new FakeDetector();
    final LinearCompoundReporter mReporter = new LinearCompoundReporter();

    public ReportResults scan(Field field) {
        ReportResults rv = new ReportResults();
        mReporter.scan(field, mDetector, rv);
        return rv;
    }

    public Field getFilledRect(int cols, int rows) {
        return markEmpty(new RectField(cols, rows), null);
    }

    @Test
    public void scan1x1() {
        ReportResults r;
        r = scan(getFilledRect(1, 1));
        assertArrayEquals(new int[] { 0, 0, 0, 0 }, r.mCount);
        r = scan(markEmpty(new RectField(1, 1), new int[] { 0 }));
        assertArrayEquals(new int[] { 0, 0, 0, 0 }, r.mCount);
    }

    @Test
    public void scanOneFilledLine() {
        ReportResults r;
        for (int cols = 2; cols <= 4; ++cols) {
            r = scan(getFilledRect(cols, 1));
            assertArrayEquals(
                    new int[] { 0, 0, (cols - 1), (cols - 2) },
                    r.mCount);
        }
    }

    @Test
    public void scanLetterL() {
        ReportResults r = scan(markEmpty(new RectField(3, 3), new int[] { 1, 2,
                4, 5 }));
        assertArrayEquals(new int[] { 0, 0, 4, 2 }, r.mCount);
    }
}
