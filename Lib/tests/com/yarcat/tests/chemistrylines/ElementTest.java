package com.yarcat.tests.chemistrylines;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.yarcat.chemistrylines.field.Element;

public class ElementTest {

    private Element e;
    private String name;
    private String id;

    @Before
    public void setUp() {
        id = "02{0}";
        name = "Oxygen";
        e = new Element(id, name);
    }

    @Test
    public void checkIdAndName() {
        assertEquals(id, e.getId());
        assertEquals(name, e.getName());
    }

    @Test
    public void checkFinal() {
        assertFalse(e.isFinal()); // Default.
        e.isFinal(true);
        assertTrue(e.isFinal());

    }

    @Test
    public void checkStartsCompound() {
        assertFalse(e.startsCompound()); // Default.
        e.startsCompound(true);
        assertTrue(e.startsCompound());
    }
}
