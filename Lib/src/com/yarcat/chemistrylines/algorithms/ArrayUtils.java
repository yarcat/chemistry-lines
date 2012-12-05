package com.yarcat.chemistrylines.algorithms;

public class ArrayUtils {

    public static void reverseInplace(int[] a) {
        for (int i = 0, j = a.length - 1; i < j; ++i, --j) {
            int t = a[i];
            a[i] = a[j];
            a[j] = t;
        }
    }
}
