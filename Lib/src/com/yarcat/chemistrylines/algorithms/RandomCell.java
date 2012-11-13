package com.yarcat.chemistrylines.algorithms;

import java.util.Random;

import com.yarcat.chemistrylines.field.Field;

public class RandomCell {

    // TODO(yarcat): Drop random seed at some point.
    public static Random random = new Random(0);

    public static int getRandomEmptyCell(Field field) {
        int range = countEmptyCells(field);
        int rv;

        if (range == 0) {
            rv = -1;
        } else {
            int x = random.nextInt(range);
            rv = 0;
            while (!field.at(rv).isEmpty() || x > 0) {
                if (field.at(rv).isEmpty()) {
                    --x;
                }
                ++rv;
            }
        }

        return rv;
    }

    public static int countEmptyCells(Field field) {
        int count = 0;
        for (int i = 0; i < field.getLength(); ++i) {
            count += field.at(i).isEmpty() ? 1 : 0;
        }
        return count;
    }
}
