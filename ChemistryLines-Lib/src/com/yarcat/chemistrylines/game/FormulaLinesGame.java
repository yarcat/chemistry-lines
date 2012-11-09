package com.yarcat.chemistrylines.game;

import static com.yarcat.chemistrylines.algorithms.RandomCell.random;

import com.yarcat.chemistrylines.algorithms.CompoundRemover;
import com.yarcat.chemistrylines.field.Element;
import com.yarcat.chemistrylines.field.ElementRegistry;
import com.yarcat.chemistrylines.field.Field;
import com.yarcat.chemistrylines.field.KnownFormulas;
import com.yarcat.chemistrylines.field.WeightedArrayOfStrings;


public class FormulaLinesGame extends LinesGame {

    private ElementRegistry mRegistry;
    private WeightedArrayOfStrings mTerms;

    public FormulaLinesGame(Field field) {
        super(field, new CompoundRemover());
        mRegistry = KnownFormulas.contents;
        mTerms = KnownFormulas.terms;
    }

    @Override
    protected Element getRandomElement() {
        return mRegistry.get(mTerms.get(random.nextInt(mTerms.size())));
    }

}
