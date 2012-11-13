package com.yarcat.chemistrylines.game;

import static com.yarcat.chemistrylines.algorithms.RandomCell.random;
import static com.yarcat.chemistrylines.field.KnownElements.knownElements;

import com.yarcat.chemistrylines.algorithms.CompoundRemover;
import com.yarcat.chemistrylines.field.Element;
import com.yarcat.chemistrylines.field.ElementRegistry;
import com.yarcat.chemistrylines.field.Field;

public class ChemistryLinesGame extends LinesGame implements GameLogic {

    private ElementRegistry mRegistry;

    public ChemistryLinesGame(Field field) {
        super(field, new CompoundRemover());
        mRegistry = knownElements;
    }

    @Override
    protected Element getRandomElement() {
        Element e;
        do {
            // TODO(yarcat): Find better way of skipping final elements.
            e = mRegistry.getByIndex(random.nextInt(mRegistry.size()));
        } while (e.isFinal());
        return e;
    }
}
