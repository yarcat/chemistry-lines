package com.yarcat.tests.chemistrylines;

import com.yarcat.chemistrylines.field.Field;
import com.yarcat.chemistrylines.game.DeferredFieldCleaner;
import com.yarcat.chemistrylines.game.DeferredFormulaFieldCleaner;

public class DeferredFormulaFieldCleanerTest extends DeferredFieldCleanerBase {

    @Override
    protected DeferredFieldCleaner createFieldCleaner(Field field) {
        return new DeferredFormulaFieldCleaner(field);
    }
}
