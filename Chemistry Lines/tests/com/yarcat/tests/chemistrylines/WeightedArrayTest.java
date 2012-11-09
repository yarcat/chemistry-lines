package com.yarcat.tests.chemistrylines;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.yarcat.chemistrylines.field.WeightedArrayOfStrings;
import com.yarcat.chemistrylines.field.WeightedArrayOfStrings.OverflowException;

public class WeightedArrayTest {

    @Test
    public void test() {
        WeightedArrayOfStrings wa = new WeightedArrayOfStrings(3);
        assertEquals(0, wa.size());
        wa.add("", 0);
        assertEquals(0, wa.size());
        wa.add("a", 1);
        wa.add("b", 10);
        wa.add("c", 100);
        assertEquals(111, wa.size());
        assertEquals(null, wa.get(-1));
        assertEquals("a", wa.get(0));
        assertEquals("b", wa.get(1));
        assertEquals("b", wa.get(5));
        assertEquals("b", wa.get(10));
        assertEquals("c", wa.get(11));
        assertEquals("c", wa.get(110));
        assertEquals(null, wa.get(111));
    }

    @Test(expected = OverflowException.class)
    public void testIndexOutOfBoundsException() {
        WeightedArrayOfStrings wa = new WeightedArrayOfStrings(1);
        wa.add(null, 1);
        assertEquals(1, wa.size());
        wa.add(null, 1);
    }
}
