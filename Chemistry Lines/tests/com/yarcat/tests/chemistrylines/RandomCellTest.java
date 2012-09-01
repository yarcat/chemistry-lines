package com.yarcat.tests.chemistrylines;

import static com.yarcat.chemistrylines.algorithms.RandomCell.countEmptyCells;
import static com.yarcat.chemistrylines.algorithms.RandomCell.getRandomEmptyCell;
import static com.yarcat.tests.chemistrylines.utils.markEmpty;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.yarcat.chemistrylines.field.Field;
import com.yarcat.chemistrylines.field.RectField;

public class RandomCellTest {

    @Test
    public void oneCellEmpty() {
        Field field;
        for (int i : new int[] { 1, 2, 4 }) {
            field = markEmpty(new RectField(i, i), new int[] { i * i / 4 });

            assertEquals(1, countEmptyCells(field));
            assertEquals(i * i / 4, getRandomEmptyCell(field));
        }
    }

    @Test
    public void noEmptyCells() {
        Field field = markEmpty(new RectField(1, 1), null);

        assertEquals(0, countEmptyCells(field));
        assertEquals(-1, getRandomEmptyCell(field));
    }
}
