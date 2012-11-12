package com.yarcat.chemistrylines.swing;

import com.yarcat.chemistrylines.field.Field;
import com.yarcat.chemistrylines.game.ChemistryLinesGame;
import com.yarcat.chemistrylines.game.FormulaLinesGame;
import com.yarcat.chemistrylines.game.LinesGame;

interface GameFactory {
    /** Returns new instance of the game initialized with the given field. */
    LinesGame newInstance(Field field);

    /** Returns a string, describing current mode. */
    String getModeName();

    static class CompoundMode implements GameFactory {
        @Override
        public LinesGame newInstance(Field field) {
            return new ChemistryLinesGame(field);
        }

        @Override
        public String getModeName() {
            return "Compound Mode";
        }
    }

    static class FormulaMode implements GameFactory {
        @Override
        public LinesGame newInstance(Field field) {
            return new FormulaLinesGame(field);
        }

        @Override
        public String getModeName() {
            return "Formula Mode";
        }
    }

}
