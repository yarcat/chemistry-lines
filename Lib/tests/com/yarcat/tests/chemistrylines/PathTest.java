package com.yarcat.tests.chemistrylines;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.yarcat.chemistrylines.algorithms.Path;
import com.yarcat.chemistrylines.field.Element;
import com.yarcat.chemistrylines.field.Field;
import com.yarcat.chemistrylines.field.RectField;

public class PathTest {

    @Test
    public void reachableItselfEmpty() throws Exception {
        Field field = getRectField(2, 2); // Nothing is filled.
        Path p = Path.prepare(field, 0);
        assertTrue(p.isReachable(0));
        assertEquals(0, p.distanceTo(0));
        assertArrayEquals(new int[] { 0 }, p.pathTo(0));
        assertEquals(4, Path.prepare(field, 0).reachableCount);
    }

    @Test
    public void reachableItselfTaken() throws Exception {
        Field field = getRectField(2, 2, 0); // Origin is filled.
        Path p = Path.prepare(field, 0);
        assertTrue(p.isReachable(0));
        assertEquals(0, p.distanceTo(0));
        assertArrayEquals(new int[] { 0 }, p.pathTo(0));
        assertEquals(4, Path.prepare(field, 0).reachableCount);
    }

    @Test
    public void reachableWithDestinationFree() throws Exception {
        Field field = getRectField(2, 2, 2);
        Path p = Path.prepare(field, 0);
        assertTrue(p.isReachable(3));
        assertEquals(2, p.distanceTo(3));
        assertArrayEquals(new int[] { 0, 1, 3 }, p.pathTo(3));
        assertEquals(4, Path.prepare(field, 0).reachableCount);
    }

    @Test
    public void reachableWithDestinationTaken() throws Exception {
        Field field = getRectField(2, 2, 2, 3);
        Path p = Path.prepare(field, 0);
        assertTrue(p.isReachable(3));
        assertEquals(2, p.distanceTo(3));
        assertArrayEquals(new int[] { 0, 1, 3 }, p.pathTo(3));
        assertEquals(4, Path.prepare(field, 0).reachableCount);
    }

    @Test
    public void reachableSourceAndWithDestinationTaken() throws Exception {
        Field field = getRectField(2, 2, 0, 2, 3);
        Path p = Path.prepare(field, 0);
        assertTrue(p.isReachable(3));
        assertEquals(2, p.distanceTo(3));
        assertArrayEquals(new int[] { 0, 1, 3 }, p.pathTo(3));
        assertEquals(4, Path.prepare(field, 0).reachableCount);
    }

    @Test
    public void unreachable() throws Exception {
        Field field = getRectField(3, 3, 2, 4, 6);
        Path p = Path.prepare(field, 0);
        assertFalse(p.isReachable(8));
        assertEquals(-1, p.distanceTo(8));
        assertNull(p.pathTo(8));
        assertEquals(6, Path.prepare(field, 0).reachableCount);
    }

    @Test
    public void longPath() throws Exception {
        Field field = getRectField(3, 3, 4, 5);
        Path p = Path.prepare(field, 2);
        assertEquals(6, p.distanceTo(8));
        assertArrayEquals(new int[] { 2, 1, 0, 3, 6, 7, 8 }, p.pathTo(8));
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
