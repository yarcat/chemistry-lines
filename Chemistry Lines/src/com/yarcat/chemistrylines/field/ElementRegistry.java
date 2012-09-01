package com.yarcat.chemistrylines.field;

import java.util.ArrayList;
import java.util.HashMap;

/** Knows about elements and possible productions. */
public class ElementRegistry {

    private final ArrayList<String> mIdentifiers = new ArrayList<String>();
    private final HashMap<String, Element> mElements = new HashMap<String, Element>();
    private final HashMap<Object, Element[]> mProductions = new HashMap<Object, Element[]>();

    /** Registers element. */
    public void register(Element e) {
        mIdentifiers.add(e.getId());
        mElements.put(e.getId(), e);
    }

    /** Registers production. */
    public void register(String id1, String id2, Element[] p) {
        mProductions.put(getProductionKey(id1, id2), p);
    }

    /** Returns true if registry contains element. */
    public boolean contains(String id) {
        return mElements.containsKey(id);
    }

    /** Returns true if registry contains production. */
    public boolean contains(String id1, String id2) {
        return mProductions.containsKey(getProductionKey(id1, id2));
    }

    /** Returns element. */
    public Element get(String id) {
        return mElements.get(id);
    }

    /** Returns production for given elements. */
    public Element[] get(String id1, String id2) {
        // TODO(paranoya) return a copy or read-only something.
        return mProductions.get(getProductionKey(id1, id2));
    }

    /** Returns key for the production map. */
    private Object getProductionKey(String id1, String id2) {
        return id1 + "|" + id2;
    }

    /** Return number of registered elements */
    public int size() {
        return mElements.size();
    }

    /** Return an element by its number in the registry */
    public Element getByIndex(int index) {
        return 0 <= index && index < size() ? get(mIdentifiers.get(index)) : null;
    }
}
