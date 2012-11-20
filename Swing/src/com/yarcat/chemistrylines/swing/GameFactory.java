package com.yarcat.chemistrylines.swing;

import com.yarcat.chemistrylines.field.Field;
import com.yarcat.chemistrylines.game.ChemistryLinesGame;
import com.yarcat.chemistrylines.game.FormulaLinesGame;
import com.yarcat.chemistrylines.game.LinesGame;

public abstract class GameFactory {
    /** Returns new instance of the game initialized with the given field. */
    abstract LinesGame newInstance(Field field);

    /** Returns a string, describing current mode. */
    abstract String getModeName();

    public static GameFactory byName(String name) {
        if (name.equals("formula-random")) {
            return new FormulaRandomMode();
        } else if (name.equals("formula-shuffle")) {
            return new FormulaShuffleMode();
        } else {
            return new CompoundMode();
        }
    }

    static class CompoundMode extends GameFactory {
        @Override
        public LinesGame newInstance(Field field) {
            return new ChemistryLinesGame(field);
        }

        @Override
        public String getModeName() {
            return "Compound Mode";
        }
    }

    static class FormulaRandomMode extends GameFactory {
        @Override
        public LinesGame newInstance(Field field) {
            return FormulaLinesGame.randomTerminalGame(field);
        }

        @Override
        public String getModeName() {
            return "Formula Random Mode";
        }
    }

    static class FormulaShuffleMode extends GameFactory {
        @Override
        public LinesGame newInstance(Field field) {
            return FormulaLinesGame.formulaShuffleGame(field);
        }

        @Override
        public String getModeName() {
            return "Formula Shuffle Mode";
        }
    }

}
