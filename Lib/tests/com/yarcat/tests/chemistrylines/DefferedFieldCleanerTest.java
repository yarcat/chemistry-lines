package com.yarcat.tests.chemistrylines;

import com.yarcat.chemistrylines.field.Field;
import com.yarcat.chemistrylines.game.DefferedFieldCleaner;

public class DefferedFieldCleanerTest extends DefferedFieldCleanerBase {

    @Override
    protected DefferedFieldCleaner createFieldCleaner(Field field) {
        return new DefferedFieldCleaner(field);
    }
}
