package com.yarcat.chemistrylines.game;

import java.util.ArrayDeque;
import java.util.Collection;

import com.yarcat.chemistrylines.field.Element;

/** A generator of new elements to be placed to the field */
public abstract class ElementGenerator {

    /** Queue of generated elements */
    private ArrayDeque<Element> mNextElements;

    public ElementGenerator() {
        mNextElements = new ArrayDeque<Element>();
    }

    /** Peek n elements from the generated set */
    public Element[] preview(int n) {
        if (n <= 0) {
            return null;
        }

        Element[] r = new Element[n];

        for (int i = 0; i < n; ++i) {
            if (mNextElements.isEmpty()) {
                fill();
            }
            r[i] = mNextElements.removeFirst();
        }
        for (int i = n - 1; i >= 0; --i) {
            mNextElements.addFirst(r[i]);
        }

        return r;
    }

    /** Returns one element and removed it from the generated set */
    public Element getNext() {
        if (mNextElements.isEmpty()) {
            fill();
        }
        return mNextElements.remove();
    }

    /** Add one generated element to the queue */
    protected void add(Element e) {
        mNextElements.addLast(e);
    }

    protected void add(Collection<Element> c) {
        mNextElements.addAll(c);
    }

    /** Add at least one element to the next elements queue */
    protected abstract void fill();
}
