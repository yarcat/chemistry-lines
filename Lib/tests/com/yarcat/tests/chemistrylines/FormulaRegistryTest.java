package com.yarcat.tests.chemistrylines;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.yarcat.chemistrylines.algorithms.ChemicalReactor;
import com.yarcat.chemistrylines.algorithms.CompoundRemover;
import com.yarcat.chemistrylines.algorithms.CompoundReporter.CompoundListener;
import com.yarcat.chemistrylines.algorithms.SimpleReactor;
import com.yarcat.chemistrylines.field.Element;
import com.yarcat.chemistrylines.field.ElementRegistry;
import com.yarcat.chemistrylines.field.Field;
import com.yarcat.chemistrylines.field.KnownFormulas;
import com.yarcat.chemistrylines.field.RectField;

public class FormulaRegistryTest {

    ElementRegistry mRegistry;
    CompoundRemover mRemover;
    ChemicalReactor mReactor;

    @Before
    public void setUp() {
        mRegistry = KnownFormulas.contents;
        mReactor = new SimpleReactor(mRegistry);
        mRemover = new CompoundRemover(mReactor);
    }

    @Test
    public void testO() {
        Element O = mRegistry.get("O");
        assertTrue(O.startsCompound());
        assertTrue(O.isFinal());
        assertFalse(formulaIsRemoved("O"));
    }

    @Test
    public void testHCl() {
        assertTrue(reactionProduces("HCl", "H", "Cl"));
        assertTrue(formulaIsRemoved("H", "Cl"));
    }

    @Test
    public void testH2O() {
        assertTrue(reactionProduces("H2O", "H", "2", "O"));
        // Tests also compound overlap - H2 & H2O.
        assertTrue(formulaIsRemoved("H", "2", "O"));
    }

    private boolean formulaIsRemoved(String... fieldMap) {
        Field field = new RectField(fieldMap.length, 1);
        for (int i = 0; i < fieldMap.length; ++i) {
            field.at(i).setElement(mRegistry.get(fieldMap[i]));
        }
        mRemover.setRemoveListener(new CompoundListener() {
            @Override
            public void foundCompound(Field field, int[] cells) {
                for (int i : cells) {
                    assertFalse(field.at(i).isEmpty());
                }
            }
        });
        mRemover.removeAllCompounds(field);
        for (int i = 0; i < fieldMap.length; ++i) {
            if (!field.at(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private boolean reactionProduces(String what, String... terms) {
        ArrayList<Element> elements = new ArrayList<Element>(terms.length);
        for (String it : terms) {
            elements.add(mRegistry.get(it));
        }

        ArrayList<Element> productions = mReactor.getCompounds(elements);
        Element target = mRegistry.get(what);
        return productions.contains(target);
    }
}
