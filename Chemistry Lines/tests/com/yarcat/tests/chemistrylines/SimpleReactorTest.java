package com.yarcat.tests.chemistrylines;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.yarcat.chemistrylines.algorithms.SimpleReactor;
import com.yarcat.chemistrylines.field.Element;

/** Test SimpleReactor.getCompounds */
public class SimpleReactorTest {

    private Registry mRegistry;
    private SimpleReactor mReactor;

    @Before
    public void setUp() {
        Registry r;
        r = mRegistry = new Registry();

        r.def("Start").startsCompound(true);
        r.def("StartFin").startsCompound(true).isFinal(true);
        r.def("A");
        r.def("A{started}");
        r.def("B");
        r.def("C");
        r.def("Fin").isFinal(true);
        r.def("AB{x}").isFinal(true);
        r.def("AB{y}").isFinal(true);

        // @formatter:off
        r.register("Start", "A",
            "A{started}",
            "Fin");
        r.register("A{started}", "B",
            "AB{x}",
            "AB{y}");
        r.register("Start", "C",
            "B");
        // @formatter:on

        mReactor = new SimpleReactor(r);
    }

    @Test
    public void alreadyFinal() {
        assertEq(
            getElements("StartFin"),
            mReactor.getCompounds(getElements("StartFin")));
    }

    @Test
    public void goodReactions() {
        assertEq(
            getElements("Fin"),
            mReactor.getCompounds(getElements("Start", "A")));
        assertEq(
            getElements("AB{x}", "AB{y}"),
            mReactor.getCompounds(getElements("Start", "A", "B")));
    }

    @Test
    public void noReaction() {
        assertEmpty(mReactor.getCompounds(getElements("Start", "Fin")));
        assertEmpty(mReactor.getCompounds(getElements("B", "A")));
        assertEmpty(mReactor.getCompounds(getElements("Start", "A", "C")));
        assertEmpty(mReactor.getCompounds(getElements("Start", "C", "A")));
    }

    @Test
    public void nonFinal() {
        assertEmpty(mReactor.getCompounds(getElements("Start", "C")));
    }

    @Test
    public void reactionDoesNotStarts() {
        assertEmpty(mReactor.getCompounds(getElements("A", "B")));
    }

    @Test
    public void extraElements() {
        // getCompounds() requires all the elements to play role in a reaction
        assertEmpty(mReactor.getCompounds(getElements("Start", "A", "Fin", "Start")));
        // Each reaction producing a compound should be detected separately.
        assertEmpty(mReactor.getCompounds(getElements("Start", "A", "Fin", "Start", "A", "Fin")));
    }

    public void assertEq(ArrayList<Element> expected, ArrayList<Element> actual) {
        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    public void assertEmpty(ArrayList<Element> actual) {
        assertEquals(0, actual.size());
    }

    public ArrayList<Element> getElements(String... ids) {
        ArrayList<Element> rv = new ArrayList<Element>(ids.length);
        for (String id : ids) {
            rv.add(mRegistry.get(id));
        }
        return rv;
    }
}
