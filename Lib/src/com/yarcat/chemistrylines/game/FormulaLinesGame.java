package com.yarcat.chemistrylines.game;

import static com.yarcat.chemistrylines.algorithms.RandomCell.random;

import java.util.Random;

import com.yarcat.chemistrylines.algorithms.CompoundRemover;
import com.yarcat.chemistrylines.algorithms.SimpleReactor;
import com.yarcat.chemistrylines.field.ElementRegistry;
import com.yarcat.chemistrylines.field.Field;
import com.yarcat.chemistrylines.field.KnownFormulas;
import com.yarcat.chemistrylines.field.WeightedArrayOfStrings;

public class FormulaLinesGame extends LinesGame {

    public static class RandomTerminalGenerator extends ElementGenerator {

        private ElementRegistry mRegistry;
        private WeightedArrayOfStrings mTerms;
        private final Random mRandom;

        public RandomTerminalGenerator(ElementRegistry registry,
                WeightedArrayOfStrings terms) {
            mRandom = random;
            mRegistry = registry;
            mTerms = terms;
        }

        @Override
        protected void fill() {
            add(mRegistry.get(mTerms.get(mRandom.nextInt(mTerms.size()))));
        }
    }

    public FormulaLinesGame(Field field) {
        // @formatter:off
        super(field,
            new CompoundRemover(new SimpleReactor(KnownFormulas.contents)),
            new RandomTerminalGenerator(KnownFormulas.contents, KnownFormulas.terms)
        );
        // @formatter:on
    }
}
