package com.yarcat.tests.chemistrylines;

import static org.junit.Assert.*;

import org.junit.Test;

import com.yarcat.chemistrylines.field.Element;
import com.yarcat.chemistrylines.field.ElementRegistry;

public class ElementRegistryTest {
    @Test
    public void testPositiveLookup() {
        ElementRegistry registry = new ElementRegistry();
        registry.addElement("H{1+}", "One atom");
        registry.addElement("H2{0}", "One molecula");

        Element element = registry.get("H{1+}");
        assertEquals("H{1+}", element.getId());
        assertEquals("One atom", element.getName());

        element = registry.get("H2{0}");
        assertEquals("H2{0}", element.getId());
        assertEquals("One molecula", element.getName());
    }

    @Test
    public void testNegativeLookup() {
        ElementRegistry registry = new ElementRegistry();
        assertEquals(null, registry.get("?"));
    }

}
