package com.yarcat.chemistrylines.game;

import com.yarcat.chemistrylines.algorithms.CompoundReporter.CompoundReference;
import com.yarcat.chemistrylines.field.Field;

public interface GameLogger {

    public void elementsAdded(Field field, int[] cells);

    public void compoundRemoved(Field field, CompoundReference ref);

    public static class DummyGameLogger implements GameLogger {

        @Override
        public void elementsAdded(Field field, int[] cells) {
        }

        @Override
        public void compoundRemoved(Field field, CompoundReference ref) {
        }
    }
}
