package com.yarcat.tests.chemistrylines;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import com.yarcat.chemistrylines.algorithms.CompoundReporter.CompoundReference;
import com.yarcat.chemistrylines.field.Element;
import com.yarcat.chemistrylines.field.Field;
import com.yarcat.chemistrylines.field.RectField;
import com.yarcat.chemistrylines.game.DefferedFieldCleaner;

public abstract class DefferedFieldCleanerBase {

    Element F = new Element("F", "Fake Element");

    @Test
    public void noOverlap() {
        Field field = markFilled(new RectField(3, 1), 0, 2);
        CompoundReference[] refs = pack(new int[] { 0 }, new int[] { 2 });
        DefferedFieldCleaner cleaner = prepareClean(field, refs);

        assertArrayEquals(new int[] { 1, 0, 1 }, mapFilled(field));
        cleaner.remove(refs[0]);
        assertArrayEquals(new int[] { 0, 0, 1 }, mapFilled(field));
        cleaner.remove(refs[1]);
        assertTrue(isEmpty(field));
    }

    @Test
    public void lineOverlap() {
        Field field = markFilled(new RectField(3, 1), 0, 1, 2);
        CompoundReference[] refs = pack(new int[] { 0, 1 }, new int[] { 1, 2 });
        DefferedFieldCleaner cleaner = prepareClean(field, refs);

        assertArrayEquals(new int[] { 1, 1, 1 }, mapFilled(field));
        cleaner.remove(refs[0]);
        assertArrayEquals(new int[] { 0, 1, 1 }, mapFilled(field));
        cleaner.remove(refs[1]);
        assertTrue(isEmpty(field));
    }

    @Test
    public void removeTwice() {
        Field field = markFilled(new RectField(3, 1), 0, 1, 2);
        CompoundReference[] refs = pack(new int[] { 0, 1 }, new int[] { 1, 2 });
        DefferedFieldCleaner cleaner = prepareClean(field, refs);

        assertArrayEquals(new int[] { 1, 1, 1 }, mapFilled(field));
        cleaner.remove(refs[0]);
        assertArrayEquals(new int[] { 0, 1, 1 }, mapFilled(field));
        cleaner.remove(refs[0]);
        assertArrayEquals(new int[] { 0, 1, 1 }, mapFilled(field));
    }

    @Test
    public void lineDoubleOverlap() {
        Field field = markFilled(new RectField(3, 1), 0, 1, 2);
        CompoundReference[] refs =
            pack(new int[] { 0, 1 }, new int[] { 1, 2 }, new int[] { 0, 1, 2 });
        DefferedFieldCleaner cleaner = prepareClean(field, refs);

        assertArrayEquals(new int[] { 1, 1, 1 }, mapFilled(field));
        cleaner.remove(refs[0]);
        assertArrayEquals(new int[] { 1, 1, 1 }, mapFilled(field));
        cleaner.remove(refs[1]);
        assertArrayEquals(new int[] { 1, 1, 1 }, mapFilled(field));
        cleaner.remove(refs[2]);
        assertTrue(isEmpty(field));
    }

    @Test
    public void crossOverlap() {
        Field field = markFilled(new RectField(3, 3), 1, 3, 4, 5, 7);
        CompoundReference[] refs =
            pack(
                new int[] { 1, 4 }, new int[] { 3, 4 }, new int[] { 5, 4 },
                new int[] { 7, 4 });
        DefferedFieldCleaner cleaner = prepareClean(field, refs);

        // @formatter:off
        assertArrayEquals(new int[] {
            0, 1, 0,
            1, 1, 1,
            0, 1, 0
        }, mapFilled(field));

        cleaner.remove(refs[0]);
        assertArrayEquals(new int[] {
            0, 0, 0,
            1, 1, 1,
            0, 1, 0
        }, mapFilled(field));

        cleaner.remove(refs[1]);
        assertArrayEquals(new int[] {
            0, 0, 0,
            0, 1, 1,
            0, 1, 0
        }, mapFilled(field));

        cleaner.remove(refs[2]);
        assertArrayEquals(new int[] {
            0, 0, 0,
            0, 1, 0,
            0, 1, 0
        }, mapFilled(field));

        cleaner.remove(refs[3]);
        assertTrue(isEmpty(field));
        // @formatter:on
    }

    public Field markFilled(Field field, int... toBeFilled) {
        for (int n : toBeFilled) {
            field.at(n).setElement(F);
        }
        return field;
    }

    private int[] mapFilled(Field field) {
        int[] rv = new int[field.getLength()];
        for (int n = 0; n < field.getLength(); ++n) {
            rv[n] = field.at(n).isEmpty() ? 0 : 1;
        }
        return rv;
    }

    private boolean isEmpty(Field field) {
        for (int n = 0; n < field.getLength(); ++n) {
            if (!field.at(n).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private DefferedFieldCleaner prepareClean(Field field,
            CompoundReference[] compounds) {
        DefferedFieldCleaner cleaner = createFieldCleaner(field);
        cleaner.process(Arrays.asList(compounds));
        return cleaner;
    }

    protected abstract DefferedFieldCleaner createFieldCleaner(Field field);

    private CompoundReference[] pack(int[]... compounds) {
        CompoundReference[] refs = new CompoundReference[compounds.length];
        for (int i = 0; i < compounds.length; ++i) {
            refs[i] = new CompoundReference(compounds[i], F);
        }
        return refs;
    }

}
