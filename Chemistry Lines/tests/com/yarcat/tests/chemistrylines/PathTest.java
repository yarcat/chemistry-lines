package com.yarcat.tests.chemistrylines;

import static com.yarcat.chemistrylines.Path.isReachable;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.yarcat.chemistrylines.RectField;

public class PathTest {

    @Test
    public void emptyField() throws Exception {
        RectField field = getRectField(2, 2);
        assertTrue(isReachable(field, 0, 0));
        assertTrue(isReachable(field, 0, 3));
    }

    @Test
    public void oneCellFilled() throws Exception {
        RectField field = getRectField(2, 2);
        field.at(0).setEmpty(false);
        assertFalse(isReachable(field, 0, 0));
        assertTrue(isReachable(field, 1, 2));
    }

    @Test
    public void unreachable() throws Exception {
        RectField field = getRectField(3, 3);
        field.at(2).setEmpty(false);
        field.at(4).setEmpty(false);
        field.at(6).setEmpty(false);
        assertFalse(isReachable(field, 0, 8));
    }

    private final static RectField getRectField(int cols, int rows) {
        RectField field = new RectField(3, 3);
        return field;
    }
}
