package com.yarcat.tests.chemistrylines;

import static com.yarcat.chemistrylines.game.FormulaShuffleFactory.filterByTerminals;
import static com.yarcat.chemistrylines.game.FormulaShuffleFactory.filterByFormulas;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.yarcat.chemistrylines.field.Element;

public class FormulaShuffleFactoryTest {

    private Map<Element, Element[]> terms;

    public FormulaShuffleFactoryTest() {
        Element a = E("A");
        Element b = E("B");
        Element c = E("C");

        terms = new HashMap<Element, Element[]>();
        terms.put(E("AB"), new Element[] { a, b });
        terms.put(E("ABC"), new Element[] { a, b, c });
        terms.put(E("AC"), new Element[] { a, c });
        terms.put(E("BC"), new Element[] { b, c });
    }

    @Test
    public void testFilterByFormulas() {
        assertEquals(4, filterByFormulas(terms, null).size());
        assertEquals(4, filterByFormulas(terms, exc()).size());
        assertEquals(3, filterByFormulas(terms, exc("AB")).size());
        assertEquals(3, filterByFormulas(terms, exc("ABC", "ABC")).size());
        // @formatter:off
        assertEquals(0,
            filterByFormulas(terms, exc("BC", "ABC", "AC", "AB")).size());
        // @formatter:on
    }

    @Test
    public void testFilterByElements() {
        assertEquals(4, filterByTerminals(terms, null).size());
        assertEquals(4, filterByTerminals(terms, exc()).size());
        assertEquals(1, filterByTerminals(terms, exc("A")).size());
        assertEquals(1, filterByTerminals(terms, exc("B")).size());
        assertEquals(1, filterByTerminals(terms, exc("C")).size());
        assertEquals(0, filterByTerminals(terms, exc("A", "B")).size());
        assertEquals(0, filterByTerminals(terms, exc("A", "C")).size());
        assertEquals(0, filterByTerminals(terms, exc("B", "C")).size());
    }

    public Set<String> exc(String... idList) {
        Set<String> r = new HashSet<String>();
        for (String id : idList) {
            r.add(id);
        }
        return r;
    }

    Element E(String id) {
        return new Element(id, id);
    }
}
