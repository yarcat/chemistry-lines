package com.yarcat.chemistrylines.game;

import static com.yarcat.chemistrylines.algorithms.RandomCell.random;
import static com.yarcat.chemistrylines.field.KnownElements.knownElements;

import java.util.Random;

import com.yarcat.chemistrylines.algorithms.CompoundRemover;
import com.yarcat.chemistrylines.field.Element;
import com.yarcat.chemistrylines.field.ElementRegistry;
import com.yarcat.chemistrylines.field.Field;

public class ChemistryLinesGame extends LinesGame implements GameLogic {

    /** Random terminal generator */
    public static class RandomFinalElementGenerator extends ElementGenerator {

        private ElementRegistry mRegistry;
        private final Random mRandom;

        public RandomFinalElementGenerator(ElementRegistry registry) {
            mRandom = random;
            mRegistry = registry;
        }

        @Override
        protected void fill() {
            Element e;
            do {
                // TODO(yarcat): Find better way of skipping final elements.
                e = mRegistry.getByIndex(mRandom.nextInt(mRegistry.size()));
            } while (e.isFinal());
            add(e);
        }
    }

    public ChemistryLinesGame(Field field) {
        // @formatter:off
        super(field, new CompoundRemover(),
            new RandomFinalElementGenerator(knownElements));
        // @formatter:on
    }

}
