package com.yarcat.chemistrylines.algorithms;

import java.util.ArrayList;
import java.util.List;

public class ReduceSubSets {

    public interface SubSetCheck<E> {
        public boolean isSub(E sub, E sup);
    }

    public static class AscIntArraySubSetCheck implements SubSetCheck<int[]> {
        @Override
        public boolean isSub(int[] sub, int[] sup) {
            if (sub.length > sup.length) {
                return false;
            }
            int i = 0, j = 0;
            while (i < sub.length && j < sup.length) {
                if (sub[i] == sup[j]) {
                    ++i;
                    ++j;
                } else if (sub[i] < sup[j]) {
                    return false;
                } else {
                    ++j;
                }
            }
            return i == sub.length;
        }
    }

    public static final <E> List<E> nm2(List<E> sets,
            SubSetCheck<E> check) {
        ArrayList<E> r = new ArrayList<E>();
        for (E s : sets) {
            if (!isSubSetOfAny(s, sets, check)) {
                r.add(s);
            }
        }
        return r;
    }

    private static final <E> boolean isSubSetOfAny(E sub, List<E> sets,
            SubSetCheck<E> check) {
        for (E sup: sets) {
            if (sub != sup && check.isSub(sub, sup)) {
                return true;
            }
        }
        return false;
    }
}
