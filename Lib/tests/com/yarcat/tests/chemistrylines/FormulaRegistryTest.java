package com.yarcat.tests.chemistrylines;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.yarcat.chemistrylines.algorithms.ChemicalReactor;
import com.yarcat.chemistrylines.algorithms.CompoundScanner;
import com.yarcat.chemistrylines.algorithms.SimpleReactor;
import com.yarcat.chemistrylines.field.Element;
import com.yarcat.chemistrylines.field.ElementRegistry;
import com.yarcat.chemistrylines.field.Field;
import com.yarcat.chemistrylines.field.KnownFormulas;
import com.yarcat.chemistrylines.field.RectField;

public class FormulaRegistryTest {

    ElementRegistry mRegistry;
    CompoundScanner mScanner;
    ChemicalReactor mReactor;

    @Before
    public void setUp() {
        mRegistry = KnownFormulas.contents;
        mReactor = new SimpleReactor(mRegistry);
        mScanner = new CompoundScanner(mReactor);
    }

    @Test
    public void testO() {
        Element O = mRegistry.get("O");
        assertTrue(O.startsCompound());
        assertFalse(O.isFinal());
        assertFalse(elements("O").allRemoved());
        assertTrue(elements("O", "H").noneRemoved());
    }

    @Test
    public void testHCl() {
        assertTrue(elements("H", "Cl").produceCompound("HCl"));
        assertTrue(elements("H", "Cl").allRemoved());
    }

    @Test
    public void testH2O() {
        assertTrue(elements("H", "2", "O").produceCompound("H2O"));
        // TODO(yarcat): Remove "O" (Check bug #45).
        assertTrue(elements("H", "2", "O").removeCompounds("H2O", "H2", "O2"));
        // Tests also compound overlap - H2 & H2O.
        assertTrue(elements("H", "2", "O").allRemoved());
    }

    private ExpectedRegistry elements(String... elements) {
        Field field = new RectField(elements.length, 1);
        for (int i = 0; i < elements.length; ++i) {
            field.at(i).setElement(mRegistry.get(elements[i]));
        }
        return new ExpectedRegistry(field);
    }

    /**
     * Helper allowing to set expectations for the set of terminals in a nice
     * way.
     */
    private class ExpectedRegistry {
        Field mField;

        ExpectedRegistry(Field field) {
            mField = field;
        }

        boolean produceCompound(String compound) {
            ArrayList<Element> elements = new ArrayList<Element>();
            for (int i = 0; i < mField.getLength(); ++i) {
                elements.add(mField.at(i).getElement());
            }
            ArrayList<Element> productions = mReactor.getCompounds(elements);
            Element target = mRegistry.get(compound);
            if (!productions.contains(target)) {
                return false;
            }
            productions.remove(target);
            return productions.isEmpty();
        }

        boolean removeCompounds(String... compounds) {
            final ArrayList<Element> removed = new ArrayList<Element>();
            removeCompounds();
            for (String c : compounds) {
                Element target = mRegistry.get(c);
                if (!removed.contains(target)) {
                    return false;
                }
                removed.remove(target);
            }
            return removed.isEmpty();
        }

        private void removeCompounds() {
            for (int[] cells : mScanner.scan(mField)) {
                mField.removeCompound(cells);
            }
        }

        boolean allRemoved() {
            removeCompounds();
            for (int i = 0; i < mField.getLength(); ++i) {
                if (!mField.at(i).isEmpty()) {
                    return false;
                }
            }
            return true;
        }

        boolean noneRemoved() {
            removeCompounds();
            for (int i = 0; i < mField.getLength(); ++i) {
                if (mField.at(i).isEmpty()) {
                    return false;
                }
            }
            return true;
        }
    }
}
