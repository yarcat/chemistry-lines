package com.yarcat.tests.chemistrylines;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Before;
import org.junit.Test;

import com.yarcat.chemistrylines.algorithms.FieldCleaner;
import com.yarcat.chemistrylines.algorithms.SimpleReactor;
import com.yarcat.chemistrylines.field.Element;
import com.yarcat.chemistrylines.field.Field;
import com.yarcat.chemistrylines.field.RectField;

public class FieldCleanerTest {

    private FieldCleaner mCleaner;
    private Registry mRegistry;

    @Before
    public void setUp() {
        Registry r = new Registry();

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
        r.register("StartFin", "A",
            "A{started}");
        r.register("A{started}", "StartFin",
            "StartFin");
        r.register("A{started}", "B",
            "AB{x}",
            "AB{y}");
        r.register("Start", "C",
            "B");
        // @formatter:on

        mRegistry = r;
        mCleaner = new FieldCleaner(new SimpleReactor(r));
    }

    @Test
    public void oneCompound() {
        Field field =
            filledField(new RectField(1, 2), elementsById("Start", "A"));
        assertArrayEquals(new int[] { 0, 0 }, mapEmpties(field));
        mCleaner.cleanField(field);
        assertArrayEquals(new int[] { 1, 1 }, mapEmpties(field));
    }

    @Test
    public void oneSymmetricalCompound() {
        Field field =
            filledField(new RectField(1, 3), elementsById("StartFin", "A", "StartFin"));
        assertArrayEquals(new int[] { 0, 0, 0 }, mapEmpties(field));
        mCleaner.cleanField(field);
        assertArrayEquals(new int[] { 1, 1, 1 }, mapEmpties(field));
    }

    @Test
    public void oneCompoundWithExtraStuff() {
        Field field =
            filledField(new RectField(1, 3), elementsById("Start", "A", "C"));
        assertArrayEquals(new int[] { 0, 0, 0 }, mapEmpties(field));
        mCleaner.cleanField(field);
        assertArrayEquals(new int[] { 1, 1, 0 }, mapEmpties(field));
    }

    @Test
    public void twoOverlappingCompounds() {
        Field field =
            filledField(new RectField(1, 3), elementsById("Start", "A", "Start"));
        assertArrayEquals(new int[] { 0, 0, 0 }, mapEmpties(field));
        mCleaner.cleanField(field);
        assertArrayEquals(new int[] { 1, 1, 1 }, mapEmpties(field));
    }

    @Test
    public void separateCompoundsWithExtraStuff() {
        // @formatter:off
        Field field =
            filledField(new RectField(3, 3), elementsById(
                "Start", "A", null,
                "StartFin", "C", "B",
                "Start", "A", "B"
            ));
        assertArrayEquals(new int[] {
            0, 0, 1,
            0, 0, 0,
            0, 0, 0
        }, mapEmpties(field));
        mCleaner.cleanField(field);
        assertArrayEquals(new int[] {
            1, 1, 1,
            1, 0, 0,
            1, 1, 1
        }, mapEmpties(field));
        // @formatter:on
    }

    /** Helper to fill field with elements */
    private Field filledField(Field field, Element[] elements) {
        for (int n = 0; n < elements.length && n < field.getLength(); ++n) {
            field.at(n).setElement(elements[n]);
        }
        return field;
    }

    /**
     * Helper to fill the field
     *
     * Use null for entries you want to skip.
     *
     */
    private Element[] elementsById(String... ids) {
        Element[] rv = new Element[ids.length];
        for (int i = 0; i < ids.length; ++i) {
            rv[i] = ids[i] == null ? null : mRegistry.get(ids[i]);
        }

        return rv;
    }

    private int[] mapEmpties(Field field) {
        int[] rv = new int[field.getLength()];
        for (int n = 0; n < field.getLength(); ++n) {
            rv[n] = field.at(n).isEmpty() ? 1 : 0;
        }
        return rv;
    }
}
