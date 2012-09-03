package com.yarcat.chemistrylines.algorithms;

import java.util.ArrayList;

import com.yarcat.chemistrylines.field.Element;

public interface ChemicalReactor {

    /** Return compounds that are result of reactions involving *all* given items */
    public ArrayList<Element> getCompounds(ArrayList<Element> items);
}
