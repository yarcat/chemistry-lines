package com.yarcat.chemistrylines.field;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Collection;
import java.util.HashMap;

/** Knows all possible (pseudo)ions, theirs possible productions, etc. */
public class ElementRegistry {
    private final HashMap<String, Element> mElements = new HashMap<String, Element>();
    private final HashMap<SimpleImmutableEntry<String, String>, Element[]> mProductions = new HashMap<SimpleImmutableEntry<String, String>, Element[]>();

    /** Adds element to the list of known elements. */
    public final void addElement(String id, String name) {
        assert !mElements.containsKey(id);
        mElements.put(id, new Element(id, name));
    }

    /** Returns all known elements. */
    public final Collection<Element> getKnownElements() {
        return mElements.values();
    }

    /**
     * Adds element to the list of known productions.
     * 
     * @param ids
     *            Array of ids. First two elements are keys, and the rest is
     *            production.
     */
    public final void addProduction(String[] ids) {
        assert ids.length > 2;
        assert mElements.containsKey(ids[0]);
        assert mElements.containsKey(ids[1]);
        SimpleImmutableEntry<String, String> key = new SimpleImmutableEntry<String, String>(
                ids[0], ids[1]);
        assert !mElements.containsKey(key);
        Element[] production = new Element[ids.length - 2];
        for (int i = 2; i < ids.length; ++i) {
            assert mElements.containsKey(ids[i]);
            production[i - 2] = get(ids[i]);
        }
        mProductions.put(key, production);
    }

    /** Returns element by a given id. */
    public Element get(String id) {
        return mElements.get(id);
    }

    /** Returns list of possible productions. */
    public Element[] getProductions(Element e1, Element e2) {
        SimpleImmutableEntry<String, String> key = new SimpleImmutableEntry<String, String>(
                e1.getId(), e2.getId());
        return mProductions.get(key);
    }
}
