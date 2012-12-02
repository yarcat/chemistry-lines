package com.yarcat.chemistrylines.game;

import java.util.List;

import com.yarcat.chemistrylines.algorithms.CompoundReporter.CompoundReference;
import com.yarcat.chemistrylines.algorithms.ReduceSubSets;
import com.yarcat.chemistrylines.algorithms.ReduceSubSets.AscIntArraySubSetCheck;
import com.yarcat.chemistrylines.algorithms.ReduceSubSets.SubSetCheck;
import com.yarcat.chemistrylines.field.Field;

public class DefferedFormulaFieldCleaner extends DefferedFieldCleaner {

    public DefferedFormulaFieldCleaner(Field field) {
        super(field);
    }

    @Override
    public boolean process(List<CompoundReference> compounds) {
        return super.process(removeExtra(compounds));
    }

    /** Remove formulas that are "substrings" of other formulas */
    public List<CompoundReference> removeExtra(List<CompoundReference> compounds) {
        return ReduceSubSets.nm2(compounds, mOverlaps);
    }

    private static CompoundSubSetCheck mOverlaps = new CompoundSubSetCheck();

    private static class CompoundSubSetCheck implements
            SubSetCheck<CompoundReference> {
        private final AscIntArraySubSetCheck check =
            new AscIntArraySubSetCheck();

        @Override
        public boolean isSub(CompoundReference sub, CompoundReference sup) {
            return check.isSub(sub.getCells(), sup.getCells());
        }
    }
}
