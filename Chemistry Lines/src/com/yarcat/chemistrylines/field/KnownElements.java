package com.yarcat.chemistrylines.field;

/** Static definition of all known elements and their productions. */
public final class KnownElements {

    public final static ElementRegistry knownElements = new ElementRegistry();
    static {
        E("H{+}")
          .startsCompound(true);
        E("H2{0}", "Hydrogen molecule")
          .isFinal(true);
        E("H2{2+}")
          .startsCompound(true);
        E("Cl{-}");
        E("Cl{2+}")
          .startsCompound(true);
        E("Cl2{0}", "Chlorine")
          .isFinal(true);
        E("O{2-}");
        E("O2{0}", "Oxygen")
          .isFinal(true);
        E("HCl{0}", "Hydrochloric acid")
          .isFinal(true);
        E("ClO{0}", "Chlorine monoxide")
          .isFinal(true);
        E("H2O{0}", "Hydrogen hydromonoxide")
          .isFinal(true);
        E("NH4[Cr(SCN)4(NH3)2]{0}", "Ammonium tetrathiocyanatodiamminechromate(III)")
          .isFinal(true);

        P("H{+}", "Cl{-}", "HCl{0}");
        P("H{+}", "H{+}", "H2{0}", "H2{2+}");
        P("H2{2+}", "O{2-}", "H2O{0}");
        P("H{+}", "O{2-}", "HO{-}");
        P("HO{-}", "H{+}", "H2O{0}");
        P("Cl{-}", "Cl{-}", "Cl2{0}");
    }

    /** Registers element with the given id and name. */
    private final static Element E(String id, String name) {
        Element e = new Element(id, name);
        knownElements.register(e);
        return e;
    }

    /** Registers element, sets name to its id. */
    private final static Element E(String id) {
        return E(id, id);
    }

    /** Registers production. */
    private final static void P(String id1, String id2, String... ids) {
        knownElements.register(id1, id2, ids);
    }
}
