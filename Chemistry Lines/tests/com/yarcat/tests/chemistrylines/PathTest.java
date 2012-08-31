package com.yarcat.tests.chemistrylines;

import static com.yarcat.chemistrylines.algorithms.Path.distance;
import static com.yarcat.chemistrylines.algorithms.Path.isReachable;
import static com.yarcat.chemistrylines.algorithms.Path.path;
import static org.junit.Assert.*;

import org.junit.Test;

import com.yarcat.chemistrylines.field.Fieldield;
import com.yarcat.chemistrylines.field.Field;

public class PathTest {

    @Test
    public void reachable() throws Exception {
        Field field = getRectField(2, 2, new int[] { 2 });

        assertTrue(isReachable(field, 0, 0));
        assertEquals(0, distance(field, 0, 0));
        assertArrayEquals(new int[] { 0 }, path(field, 0, 0));

        assertTrue(isReachable(field, 0, 3));
        assertEquals(2, distance(field, 0, 3));
        assertArrayEquals(new int[] { 0, 1, 3 }, path(field, 0, 3));
    }

    @Test
    public void filledOrigin() throws Exception {
        Field field = getRectField(2, 2, new int[] { 0 });

        assertFalse(isReachable(field, 0, 0));
        assertEquals(-1, distance(field, 0, 0));
        assertNull(path(field, 0, 0));

        assertFalse(isReachable(field, 0, 3));
        assertEquals(-1, distance(field, 0, 3));
        assertNull(path(field, 0, 3));
    }

    @Test
    public void filledFin() throws Exception {
        Field field = getRectField(2, 2, new int[] { 3 });

        assertFalse(isReachable(field, 0, 3));
        assertEquals(-1, distance(field, 0, 3));
        assertNull(path(field, 0, 3));
    }

    @Test
    public void unreachable() throws Exception {
        Field field = getRectField(3, 3, new int[] { 2, 4, 6 });
        assertFalse(isReachable(field, 0, 8));
        assertEquals(-1, distance(field, 0, 8));
        assertNull(path(field, 0, 8));
    }

    @Test
    public void longPath() throws Exception {
        Field field = getRectField(3, 3, new int[] { 4, 5 });
        assertEquals(6, distance(field, 2, 8));
        assertArrayEquals(new int[] { 2, 1, 0, 3, 6, 7, 8 }, path(field, 2, 8));
    }

    private final static RectField getRectField(int cols, int rows,
            int[] filledCells) {
        RectField field = new RectField(cols, rows);
        for (int n : filledCells) {
            field.at(n).setEmpty(false);
        }
        return field;
    }

}
