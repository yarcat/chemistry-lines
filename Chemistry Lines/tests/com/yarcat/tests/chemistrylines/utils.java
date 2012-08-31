package com.yarcat.tests.chemistrylines;

import com.yarcat.chemistrylines.field.Field;

public final class utils {

    public static final Field markEmpty(Field field, int[] cells) {
        for (int i = 0; i < field.getLength(); ++i) {
            field.at(i).setEmpty(false);
        }
        if (cells != null) {
            for (int i : cells) {
                field.at(i).setEmpty(true);
            }
        }
        return field;
    }
}
