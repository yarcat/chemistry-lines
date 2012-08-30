package com.yarcat.tests.chemistrylines;

import static com.yarcat.chemistrylines.Path.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.yarcat.chemistrylines.Field;
import com.yarcat.chemistrylines.RectField;

public class PathTest {

    @Test
    public void emptyField() throws Exception {
        Field field = getRectField(2, 2);

        assertTrue(isReachable(field, 0, 0));
        assertEquals(0, distance(field, 0, 0));

        assertTrue(isReachable(field, 0, 3));
        assertEquals(2, distance(field, 0, 3));
    }

    @Test
    public void filledOrigin() throws Exception {
        Field field = getRectField(2, 2, new int[] { 0 });

        assertFalse(isReachable(field, 0, 0));
        assertEquals(-1, distance(field, 0, 0));

        assertFalse(isReachable(field, 0, 3));
        assertEquals(-1, distance(field, 0, 3));
    }

    @Test
    public void filledFin() throws Exception {
        Field field = getRectField(2, 2, new int[] { 3 });

        assertFalse(isReachable(field, 0, 3));
        assertEquals(-1, distance(field, 0, 3));
    }

    @Test
    public void unreachable() throws Exception {
        Field field = getRectField(3, 3, new int[] { 2, 4, 6 });
        assertFalse(isReachable(field, 0, 8));
        assertEquals(-1, distance(field, 0, 8));
    }

    @Test
    public void longPath() throws Exception {
        Field field = getRectField(3, 3, new int[] { 4, 5 });
        assertEquals(6, distance(field, 2, 8));
    }

    private final static RectField getRectField(int cols, int rows) {
        return new RectField(cols, rows);
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
