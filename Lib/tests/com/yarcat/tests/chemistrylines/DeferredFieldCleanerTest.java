package com.yarcat.tests.chemistrylines;

import com.yarcat.chemistrylines.field.Field;
import com.yarcat.chemistrylines.game.DeferredFieldCleaner;

public class DeferredFieldCleanerTest extends DeferredFieldCleanerBase {

    @Override
    protected DeferredFieldCleaner createFieldCleaner(Field field) {
        return new DeferredFieldCleaner(field);
    }
}
