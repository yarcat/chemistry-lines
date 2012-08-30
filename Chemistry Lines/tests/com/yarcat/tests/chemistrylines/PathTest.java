package com.yarcat.tests.chemistrylines;

import static com.yarcat.chemistrylines.Path.isReachable;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.yarcat.chemistrylines.RectField;

public class PathTest {
    private RectField mField;

    @Before
    public void setUp() throws Exception {
        mField = new RectField(3, 3);
    }

    @Test
    public void sourceIsReachable() throws Exception {
        assertTrue(isReachable(mField, 0, 0));
    }

    @Test
    public void emptyIsReachable() throws Exception {
        assertTrue(isReachable(mField, 0, 8));
    }

    @Test
    public void notReachable() throws Exception {
        mField.at(2).setEmpty(false);
        mField.at(4).setEmpty(false);
        mField.at(6).setEmpty(false);
        assertFalse(isReachable(mField, 0, 8));
    }
}
