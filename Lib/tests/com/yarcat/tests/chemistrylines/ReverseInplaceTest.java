package com.yarcat.tests.chemistrylines;

import static org.junit.Assert.assertArrayEquals;

import static com.yarcat.chemistrylines.algorithms.CompoundReporter.CompoundReference.reverseInplace;

import org.junit.Test;

public class ReverseInplaceTest {

    @Test
    public void emptyDoesNotRiseException() {
        int[] a = new int[] {};
        reverseInplace(a);
    }

    @Test
    public void test() {
        int[] a = new int[] { 0, 1, 2 };
        reverseInplace(a);
        assertArrayEquals(new int[] { 2, 1, 0 }, a);
    }
}
