package com.yarcat.tests.chemistrylines;

import com.yarcat.chemistrylines.field.Element;
import com.yarcat.chemistrylines.field.ElementRegistry;

<<<<<<< HEAD
/** Element registry used for testing */
=======
>>>>>>> [#35412149] Created a shared library
class Registry extends ElementRegistry {
    public Element def(String id) {
        Element e = new Element(id, id);
        register(e);
        return e;
    }
}
