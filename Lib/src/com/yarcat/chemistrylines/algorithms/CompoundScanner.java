package com.yarcat.chemistrylines.algorithms;

import java.util.ArrayList;
import java.util.List;

import com.yarcat.chemistrylines.algorithms.CompoundReporter.CompoundDetector;
import com.yarcat.chemistrylines.algorithms.CompoundReporter.CompoundListener;
import com.yarcat.chemistrylines.algorithms.CompoundReporter.CompoundReference;
import com.yarcat.chemistrylines.field.Element;
import com.yarcat.chemistrylines.field.Field;

/**
 * Algorithm that removes compounds from the field.
 *
 * It should be called after each field change.
 *
 */
public class CompoundScanner {
    private final ChemicalReactor mReactor;
    private final CompoundDetector mDetector;
    private final CompoundReporter mReporter;

    public CompoundScanner() {
        this(new SimpleReactor());
    }

    public CompoundScanner(ChemicalReactor reactor) {
        mReactor = reactor;
        mDetector = new ChemicalCompoundDetector(mReactor);
        mReporter = new LinearCompoundReporter();
    }

    /** Collect all field cell sequences containing chemical compounds. */
    public List<CompoundReference> scan(Field field) {
        final ArrayList<CompoundReference> rv = new ArrayList<CompoundReference>();
        mReporter.scan(field, mDetector, new CompoundListener() {
            @Override
            public void foundCompound(CompoundReference ref) {
                rv.add(ref);
            }
        });

        return rv;
    }

    private static class ChemicalCompoundDetector implements CompoundDetector {

        private ChemicalReactor mReactor;

        public ChemicalCompoundDetector(ChemicalReactor reactor) {
            mReactor = reactor;
        }

        /**
         * Cells start a compound if first one contains start-element.
         *
         * We could try to add some logic to stop earlier than at an empty cell
         * or at the field border, but it looks like overkill.
         */
        @Override
        public boolean startsCompound(Field field, int[] cells) {
            assert cells.length > 0;
            return field.at(cells[0]).getElement().startsCompound();
        }

        /**
         * Use chemical reactor to check compounds.
         *
         * @return either a compound formed by the field cells or null.
         */
        @Override
        public Element getCompound(Field field, int[] cells) {
            assert cells.length > 0;
            ArrayList<Element> items = cellsToElements(field, cells);
            ArrayList<Element> compounds = mReactor.getCompounds(items);
            // TODO(luch): Review this implementing chemical reactions mode.
            // Is compounds.size() > 1 possible?
            return compounds.isEmpty() ? null : compounds.get(0);
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
