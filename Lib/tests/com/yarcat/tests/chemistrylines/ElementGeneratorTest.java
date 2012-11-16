package com.yarcat.tests.chemistrylines;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;

import org.junit.Test;

import com.yarcat.chemistrylines.field.Element;
import com.yarcat.chemistrylines.game.ElementGenerator;

public class ElementGeneratorTest {

    private static final Element e1 = new Element("A", "A");
    private static final Element e2 = new Element("B", "B");
    private static final Element e3 = new Element("C", "C");

    public class G extends ElementGenerator {

        @Override
        protected void fill() {
            ArrayList<Element> l = new ArrayList<Element>(2);
            l.add(e1);
            l.add(e2);
            this.add(l);
            this.add(e3);
        }
    }

    @Test
    public void testPreview() {
        G g = new G();
        assertNull(g.preview(0));
        assertArrayEquals(new Element[] { e1 }, g.preview(1));
        assertArrayEquals(new Element[] { e1, e2 }, g.preview(2));
        assertArrayEquals(new Element[] { e1, e2, e3 }, g.preview(3));
        assertArrayEquals(new Element[] { e1, e2, e3, e1 }, g.preview(4));
    }

    @Test
    public void testGetNext() {
        G g = new G();
        assertEquals(e1, g.getNext());
        assertEquals(e2, g.getNext());
        assertEquals(e3, g.getNext());
    }

    @Test
    public void testMix() {
        G g = new G();
        assertArrayEquals(new Element[] { e1, e2, e3 }, g.preview(3));
        assertEquals(e1, g.getNext());
        assertArrayEquals(new Element[] { e2, e3, e1 }, g.preview(3));
        assertEquals(e2, g.getNext());
        assertArrayEquals(new Element[] { e3, e1, e2 }, g.preview(3));
        assertEquals(e3, g.getNext());
        assertArrayEquals(new Element[] { e1, e2, e3 }, g.preview(3));
    }
}
