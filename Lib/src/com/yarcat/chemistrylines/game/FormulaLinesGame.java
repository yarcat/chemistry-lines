package com.yarcat.chemistrylines.game;

import static com.yarcat.chemistrylines.algorithms.RandomCell.random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import com.yarcat.chemistrylines.algorithms.CompoundRemover;
import com.yarcat.chemistrylines.algorithms.SimpleReactor;
import com.yarcat.chemistrylines.field.Element;
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

    /** Terminal generator based on final formulas. */
    public static class FormulaTerminalGenerator extends ElementGenerator {

        private final Random mRandom;

        /** Final formula terminals */
        private Element[][] mFormulaTerms;

        /**
         * Number of formulas used to fill the queue. It influences difficulty.
         *
         * Probably, good number should be in [1 .. 2] * mNewPortionSize.
         */
        private final int mMixFormulas;

        FormulaTerminalGenerator(Element[][] formulaTerms, int mixLevel) {
            mRandom = random;
            mMixFormulas = mixLevel;
            mFormulaTerms = formulaTerms;
        }

        @Override
        protected void fill() {
            ArrayList<Element> terms = new ArrayList<Element>();

            for (int i = 0; i < mMixFormulas; ++i) {
                int r = mRandom.nextInt(mFormulaTerms.length);
                for (Element e : mFormulaTerms[r]) {
                    terms.add(e);
                }
            }

            Collections.shuffle(terms, mRandom);
            add(terms);
        }
    }

    protected FormulaLinesGame(Field field, ElementGenerator terminalGenerator) {
        // @formatter:off
        super(field,
            new CompoundRemover(new SimpleReactor(KnownFormulas.contents)),
            terminalGenerator);
        // @formatter:on
    }

    public static FormulaLinesGame randomTerminalGame(Field field) {
        // @formatter:off
        return new FormulaLinesGame(field,
            new RandomTerminalGenerator(KnownFormulas.contents, KnownFormulas.terms));
        // @formatter:on
    }

    public static FormulaLinesGame formulaShuffleGame(Field field) {
        // @formatter:off
        // TODO(luch): use LinesGame.mNewPortionSize in place of 3
        return new FormulaLinesGame(field,
            new FormulaTerminalGenerator(KnownFormulas.formulaTerms, 3));
        // @formatter:on
    }

    public static class DebugGenerator extends ElementGenerator {

        ArrayList<Element> mElements;

        DebugGenerator(ElementRegistry registry, String... items) {
            mElements = new ArrayList<Element>(items.length);
            for (String id : items) {
                 mElements.add(registry.get(id));
            }
        }

        @Override
        protected void fill() {
            this.add(mElements);
        }
    };

    public static FormulaLinesGame formulaDebugGame(Field field) {
        // @formatter:off
        return new FormulaLinesGame(field,
            new DebugGenerator(KnownFormulas.contents, "H", "2", "O"));
        // @formatter:on
    }
}
