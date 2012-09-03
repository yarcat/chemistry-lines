package com.yarcat.chemistrylines.algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import com.yarcat.chemistrylines.field.Element;
import com.yarcat.chemistrylines.field.ElementRegistry;
import com.yarcat.chemistrylines.field.KnownElements;

/**
 * A reactor that is capable to produce compounds only.
 *
 * Order of reactions is strictly defined by the sequence of elements.
 */
public class SimpleReactor implements ChemicalReactor {

    /** Custom known elements are required by tests */
    private ElementRegistry knownElements;

    public SimpleReactor() {
        super();
        knownElements = KnownElements.knownElements;
    }

    public SimpleReactor(ElementRegistry registry) {
        super();
        knownElements = registry;
    }

    /** Return compounds that are result of reactions involving *all* given items */
    public ArrayList<Element> getCompounds(ArrayList<Element> items) {
        ArrayList<Element> rv = getProductions(items);
        removeNonCompounds(rv);
        return rv;
    }

    /** Return all productions of reactions involving *all* given items */
    private ArrayList<Element> getProductions(ArrayList<Element> items) {

        ArrayList<Element> present = new ArrayList<Element>();
        Iterator<Element> iter = items.iterator();
        if (!iter.hasNext()) {
            return present;
        }

        Element first = iter.next();
        if (!first.startsCompound()) {
            return present;
        }

        present.add(first);
        while (!present.isEmpty() && iter.hasNext()) {
            Element b = iter.next();
            ArrayList<Element> following = new ArrayList<Element>();

            for (Element a : present) {
                // TODO(luch) make ElementRegistry.get(a,b) return List or
                // Iterable.
                if (knownElements.contains(a, b)) {
                    following.addAll(Arrays.asList(knownElements.get(a, b)));
                }
            }

            present = following;
        }

        removeNonCompounds(present);
        return present;
    }

    private static final void removeNonCompounds(ArrayList<Element> items) {
        Iterator<Element> iter = items.iterator();
        while (iter.hasNext()) {
            if (!iter.next().isFinal()) {
                iter.remove();
            }
        }
    }
}
