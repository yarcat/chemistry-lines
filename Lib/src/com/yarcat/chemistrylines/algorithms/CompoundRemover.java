package com.yarcat.chemistrylines.algorithms;

import java.util.ArrayList;

import com.yarcat.chemistrylines.algorithms.CompoundReporter.CompoundDetector;
import com.yarcat.chemistrylines.algorithms.CompoundReporter.CompoundListener;
import com.yarcat.chemistrylines.field.Element;
import com.yarcat.chemistrylines.field.Field;

/**
 * Algorithm that removes compounds from the field.
 *
 * It should be called after each field change.
 *
 */
public class CompoundRemover {
    private final ChemicalReactor mReactor;
    private final CompoundDetector mDetector;
    private final CompoundReporter mReporter;

    public CompoundRemover() {
        this(new SimpleReactor());
    }

    public CompoundRemover(ChemicalReactor reactor) {
        mReactor = reactor;
        mDetector = new ChemicalCompoundDetector(mReactor);
        mReporter = new LinearCompoundReporter();
    }

    /** Find all chemical reactions on the field and remove compounds. */
    public void removeAllCompounds(Field field) {
        for (int[] cells : scan(field)) {
            for (int n : cells) {
                field.at(n).setElement(null);
            }
        }
    }

    /** Collect all field cell sequences containing chemical compounds. */
    private ArrayList<int[]> scan(Field field) {
        final ArrayList<int[]> rv = new ArrayList<int[]>();
        mReporter.scan(field, mDetector, new CompoundListener() {
            @Override
            public void foundCompound(Field field, int[] cells) {
                rv.add(cells);
            }
        });

        return rv;
    }

    private static class ChemicalCompoundDetector implements CompoundDetector {

        private ChemicalReactor mReactor;

        public ChemicalCompoundDetector(ChemicalReactor reactor) {
            mReactor = reactor;
        }

        /** Cells start a compound if first one contains start-element.
         *
         * We could try to add some logit to stop earlier than at an empty cell or
         * at the field border, but it looks like overkill.
         */
        @Override
        public boolean startsCompound(Field field, int[] cells) {
            assert cells.length > 0;
            return field.at(cells[0]).getElement().startsCompound();
        }

        /** Use chemical reactor to check compounds. */
        @Override
        public boolean isCompound(Field field, int[] cells) {
            assert cells.length > 0;
            ArrayList<Element> items = cellsToElements(field, cells);
            return !mReactor.getCompounds(items).isEmpty();
        }
    }

    /** Convert a sequence of field cells into a sequence of chemical elements. */
    private static ArrayList<Element> cellsToElements(Field field, int[] cells) {
        ArrayList<Element> items = new ArrayList<Element>(cells.length);
        for (int n : cells) {
            items.add(field.at(n).getElement());
        }
        return items;
    }
}
