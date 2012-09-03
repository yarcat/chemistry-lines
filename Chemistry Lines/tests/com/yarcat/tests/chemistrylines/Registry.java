package com.yarcat.tests.chemistrylines;

import com.yarcat.chemistrylines.field.Element;
import com.yarcat.chemistrylines.field.ElementRegistry;

class TestRegistry extends ElementRegistry {
    public Element def(String id) {
        Element e = new Element(id, id);
        register(e);
        return e;
    }
}
