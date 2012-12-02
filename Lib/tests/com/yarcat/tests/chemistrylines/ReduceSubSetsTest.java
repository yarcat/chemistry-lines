package com.yarcat.tests.chemistrylines;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.yarcat.chemistrylines.algorithms.ReduceSubSets;
import com.yarcat.chemistrylines.algorithms.ReduceSubSets.AscIntArraySubSetCheck;
import com.yarcat.chemistrylines.algorithms.ReduceSubSets.SubSetCheck;

public class ReduceSubSetsTest {

    @Test
    public void ascIntArraySubSetCheck() {
        AscIntArraySubSetCheck check = new AscIntArraySubSetCheck();

        assertTrue(check.isSub(new int[] {}, new int[] {}));
        assertTrue(check.isSub(new int[] {}, new int[] { 0 }));
        assertTrue(check.isSub(new int[] { 0 }, new int[] { 0 }));
        assertTrue(check.isSub(new int[] { 0 }, new int[] { 0, 1 }));
        assertTrue(check.isSub(new int[] { 0, 1 }, new int[] { 0, 1 }));
        assertTrue(check.isSub(new int[] { 0, 1 }, new int[] { 0, 1, 2, 3 }));

        assertFalse(check.isSub(new int[] { 0 }, new int[] {}));
        assertFalse(check.isSub(new int[] { 0 }, new int[] { 1 }));
        assertFalse(check.isSub(new int[] { 0, 1 }, new int[] { 1, 2 }));
        assertFalse(check.isSub(new int[] { 1 }, new int[] { 0, 2 }));
        assertFalse(check.isSub(new int[] { 2 }, new int[] { 0, 1 }));
        assertFalse(check.isSub(new int[] { 1, 2 }, new int[] { 0, 1 }));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void reduceNM2() {
        assertEquals(list(), nm2(list()));
        assertEquals(list(), nm2(list(A(0), A(0))));
        assertEquals(list(A()), nm2(list(A())));
        assertEquals(list(A(0)), nm2(list(A(0))));
        assertEquals(list(A(0, 1)), nm2(list(A(0), A(0, 1))));
        assertEquals(list(A(0, 1), A(1, 2)), nm2(list(A(0, 1), A(1, 2))));
        assertEquals(
            list(A(0, 1), A(1, 2)),
            nm2(list(A(0), A(1), A(2), A(0, 1), A(1, 2))));
    }

    private static List<Integer> A(int... a) {
        List<Integer> r = new ArrayList<Integer>(a.length);
        for (Integer x : a) {
            r.add(x);
        }
        return r;
    }

    private static List<List<Integer>> list(List<Integer>... a) {
        ArrayList<List<Integer>> r = new ArrayList<List<Integer>>(a.length);
        for (List<Integer> x : a) {
            r.add(x);
        }
        return r;
    }

    private static class AscIntListSubSetCheck implements
            SubSetCheck<List<Integer>> {
        private final AscIntArraySubSetCheck check =
            new AscIntArraySubSetCheck();

        @Override
        public boolean isSub(List<Integer> sub, List<Integer> sup) {
            return check.isSub(conv(sub), conv(sup));
        }

        int[] conv(List<Integer> l) {
            int[] r = new int[l.size()];
            int i = 0;
            for (Integer x : l) {
                r[i++] = x;
            }
            return r;
        }
    }

    private static final AscIntListSubSetCheck check =
        new AscIntListSubSetCheck();

    private static List<List<Integer>> nm2(List<List<Integer>> sets) {
        return ReduceSubSets.nm2(sets, check);
    }
}
