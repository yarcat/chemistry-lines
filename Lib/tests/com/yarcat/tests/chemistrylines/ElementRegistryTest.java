package com.yarcat.tests.chemistrylines;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.yarcat.chemistrylines.field.Element;
import com.yarcat.chemistrylines.field.ElementRegistry;
import com.yarcat.chemistrylines.field.ElementRegistry.DuplicateException;

public class ElementRegistryTest {

    private ElementRegistry mRegistry;
    private Element e1;
    private Element e2;
    private Element e3;

    @Before
    public void setUp() {
        mRegistry = new ElementRegistry();
        e1 = new Element("O{-2}", "Compound1")
            .startsCompound(true)
            .isFinal(false);
        e2 = new Element("O2{0}", "Compound2")
            .startsCompound(true)
            .isFinal(true);
        e3 = new Element("O3{0}", "Compound3")
            .startsCompound(false)
            .isFinal(true);
    }

    @Test
    public void element() {
        assertFalse(mRegistry.contains(e1.getId()));
        assertNull(mRegistry.get(e1.getId()));

        mRegistry.register(e1);

        assertTrue(mRegistry.contains(e1.getId()));
        assertEquals(e1, mRegistry.get(e1.getId()));
    }

    @Test
    public void production() {
        assertFalse(mRegistry.contains(e1.getId(), e2.getId()));
        assertNull(mRegistry.get(e1.getId(), e1.getId()));

        mRegistry.register(e1);
        mRegistry.register(e2);
        mRegistry.register(e3);
        Element[] production = new Element[] { e3 };
        mRegistry.register(e1.getId(), e2.getId(), production);

        assertTrue(mRegistry.contains(e1.getId(), e2.getId()));
        assertArrayEquals(production, mRegistry.get(e1.getId(), e2.getId()));
    }

    @Test
    public void enumeration() {
        assertEquals(0, mRegistry.size());
        assertNull(mRegistry.getByIndex(0));

        mRegistry.register(e1);

        assertEquals(1, mRegistry.size());
        assertEquals(e1, mRegistry.getByIndex(0));
        assertNull(mRegistry.getByIndex(-1));
        assertNull(mRegistry.getByIndex(mRegistry.size()));
    }

    @Test(expected=DuplicateException.class)
    public void duplicateElement() {
        mRegistry.register(e1);
        mRegistry.register(e1);
    }
}
