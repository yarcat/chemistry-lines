package com.yarcat.tests.chemistrylines;

import static com.yarcat.chemistrylines.algorithms.Path.distance;
import static com.yarcat.chemistrylines.algorithms.Path.isReachable;
import static com.yarcat.chemistrylines.algorithms.Path.path;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.yarcat.chemistrylines.field.Element;
import com.yarcat.chemistrylines.field.Field;
import com.yarcat.chemistrylines.field.RectField;

public class PathTest {

    @Test
    public void reachableItselfEmpty() throws Exception {
        Field field = getRectField(2, 2); // Nothing is filled.
        assertTrue(isReachable(field, 0, 0));
        assertEquals(0, distance(field, 0, 0));
        assertArrayEquals(new int[] { 0 }, path(field, 0, 0));
    }

    @Test
    public void reachableItselfTaken() throws Exception {
        Field field = getRectField(2, 2, 0); // Origin is filled.
        assertTrue(isReachable(field, 0, 0));
        assertEquals(0, distance(field, 0, 0));
        assertArrayEquals(new int[] { 0 }, path(field, 0, 0));
    }

    @Test
    public void reachableWithDestinationFree() throws Exception {
        Field field = getRectField(2, 2, 2);
        assertTrue(isReachable(field, 0, 3));
        assertEquals(2, distance(field, 0, 3));
        assertArrayEquals(new int[] { 0, 1, 3 }, path(field, 0, 3));
    }

    @Test
    public void reachableWithDestinationTaken() throws Exception {
        Field field = getRectField(2, 2, 2, 3);
        assertTrue(isReachable(field, 0, 3));
        assertEquals(2, distance(field, 0, 3));
        assertArrayEquals(new int[] { 0, 1, 3 }, path(field, 0, 3));
    }

    @Test
    public void reachableSourceAndWithDestinationTaken() throws Exception {
        Field field = getRectField(2, 2, 0, 2, 3);
        assertTrue(isReachable(field, 0, 3));
        assertEquals(2, distance(field, 0, 3));
        assertArrayEquals(new int[] { 0, 1, 3 }, path(field, 0, 3));
    }

    @Test
    public void unreachable() throws Exception {
        Field field = getRectField(3, 3, 2, 4, 6);
        assertFalse(isReachable(field, 0, 8));
        assertEquals(-1, distance(field, 0, 8));
        assertNull(path(field, 0, 8));
    }

    @Test
    public void longPath() throws Exception {
        Field field = getRectField(3, 3, 4, 5);
        assertEquals(6, distance(field, 2, 8));
        assertArrayEquals(new int[] { 2, 1, 0, 3, 6, 7, 8 }, path(field, 2, 8));
    }

    private final static RectField getRectField(int cols, int rows,
            int... filledCells) {
        RectField field = new RectField(cols, rows);
        for (int n : filledCells) {
            field.at(n).setElement(new Element("id", "name"));
        }
        return field;
    }

}
