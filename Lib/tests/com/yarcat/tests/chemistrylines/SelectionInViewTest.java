package com.yarcat.tests.chemistrylines;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.yarcat.chemistrylines.view.SelectionInView;

public class SelectionInViewTest {

    SelectionInView s;

    @Before
    public void setUp() {
        s = new SelectionInView();
    }

    @Test
    public void empty() {
        assertFalse(s.hasSource());
        assertFalse(s.hasTarget());
    }

    @Test
    public void oneSelect() {
        s.select(1);
        assertTrue(s.hasSource());
        assertFalse(s.hasTarget());
    }

    @Test
    public void differentCells() {
        s.select(1);
        s.select(2);
        assertTrue(s.hasSource());
        assertTrue(s.hasTarget());
        assertEquals(1, s.getSource());
        assertEquals(2, s.getTarget());
        s.select(3);
        assertTrue(s.hasSource());
        assertTrue(s.hasTarget());
        assertEquals(1, s.getSource());
        assertEquals(3, s.getTarget());
    }

    @Test
    public void reselectSource() {
        s.select(1);
        assertTrue(s.hasSource());
        assertFalse(s.hasTarget());
        s.select(1);
        assertFalse(s.hasSource());
        assertFalse(s.hasTarget());
        s.select(1);
        s.select(2);
        assertTrue(s.hasSource());
        assertTrue(s.hasTarget());
        s.select(1);
        assertFalse(s.hasSource());
        assertFalse(s.hasTarget());
    }

    @Test
    public void clear() {
        s.select(1);
        s.clear();
        assertFalse(s.hasSource());
        assertFalse(s.hasTarget());
        s.select(1);
        s.select(2);
        s.clear();
        assertFalse(s.hasSource());
        assertFalse(s.hasTarget());
    }
}
