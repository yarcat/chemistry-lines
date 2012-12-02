package com.yarcat.tests.chemistrylines;

import com.yarcat.chemistrylines.field.Field;
import com.yarcat.chemistrylines.game.DefferedFieldCleaner;
import com.yarcat.chemistrylines.game.DefferedFormulaFieldCleaner;

public class DefferedFormulaFieldCleanerTest extends DefferedFieldCleanerBase {

    @Override
    protected DefferedFieldCleaner createFieldCleaner(Field field) {
        return new DefferedFormulaFieldCleaner(field);
    }
}
