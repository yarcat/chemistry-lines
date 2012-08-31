package com.yarcat.tests.chemistrylines;

import static org.junit.Assert.*;

import org.junit.Test;

import com.yarcat.chemistrylines.field.Element;
import com.yarcat.chemistrylines.field.ElementRegistry;

public class ElementRegistryTest {

    private ElementRegistry createElementRegistry() {
        ElementRegistry registry = new ElementRegistry();
        registry.addElement("H{1+}", "One atom");
        registry.addElement("H2{0}", "One molecula");
        return registry;
    }

    @Test
    public void testPositiveLookup() {
        ElementRegistry registry = createElementRegistry();

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
        assertNull(registry.get("?"));
    }

    @Test
    public void testProduction() {
        ElementRegistry registry = createElementRegistry();
        registry.addElement("H3{0}", "One super molecula");
        registry.addElement("H4{-1}", "One freaking ion");
        registry.addProduction(new String[] { "H{1+}", "H{1+}", "H2{0}" });
        registry.addProduction(new String[] { "H{1+}", "H2{0}", "H3{0}" });
        registry.addProduction(new String[] { "H2{0}", "H{1+}", "H3{0}",
                "H4{-1}" });

        Element h = registry.get("H{1+}");
        Element h2 = registry.get("H2{0}");
        Element h3 = registry.get("H3{0}");
        Element h4 = registry.get("H4{-1}");

        assertArrayEquals(new Element[] { h2 }, registry.getProductions(h, h));
        assertArrayEquals(new Element[] { h3 }, registry.getProductions(h, h2));
        assertArrayEquals(new Element[] { h3, h4 },
                registry.getProductions(h2, h));
        assertNull(registry.getProductions(h2, h3));
    }
}
