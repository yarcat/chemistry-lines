package com.yarcat.chemistrylines.swing;

import com.yarcat.chemistrylines.field.Field;
import com.yarcat.chemistrylines.game.ChemistryLinesGame;
import com.yarcat.chemistrylines.game.DeferredFieldCleaner;
import com.yarcat.chemistrylines.game.DeferredFormulaFieldCleaner;
import com.yarcat.chemistrylines.game.FieldCleaner;
import com.yarcat.chemistrylines.game.FormulaLinesGame;
import com.yarcat.chemistrylines.game.FormulaShuffleFactory;
import com.yarcat.chemistrylines.game.GameLogic;
import com.yarcat.chemistrylines.game.ImmediateFieldCleaner;

public abstract class GameFactory {

    public enum Cleaner {
        Immediate, Deffered
    }

    private Cleaner mCleaner = Cleaner.Immediate;

    /** Returns new instance of the game initialized with the given field.
     * @throws Exception TODO*/
    protected abstract GameLogic createInstance(Field field) throws Exception;

    /** Returns a string, describing current mode. */
    abstract String getModeName();

    public static GameFactory byName(String name) {
        if (name.equals("formula-random")) {
            return new FormulaRandomMode();
        } else if (name.equals("formula-shuffle")) {
            return new FormulaShuffleMode();
        } else if (name.equals("formula-debug")) {
            return new FormulaDebugMode();
        } else {
            return new CompoundMode();
        }
    }

    public static class CompoundMode extends GameFactory {
        @Override
        protected GameLogic createInstance(Field field) throws Exception {
            return new ChemistryLinesGame(field);
        }

        @Override
        public String getModeName() {
            return "Compound Mode";
        }
    }

    static abstract class FormulaGameFactory extends GameFactory {
        @Override
        protected FieldCleaner createDefferedCleaner(Field field) {
            return new DeferredFormulaFieldCleaner(field);
        }
    }

    public static class FormulaRandomMode extends FormulaGameFactory {
        @Override
        protected GameLogic createInstance(Field field) throws Exception {
            return FormulaLinesGame.randomTerminalGame(field);
        }

        @Override
        protected String getModeName() {
            return "Formula Random Mode";
        }
    }

    public static class FormulaShuffleMode extends FormulaGameFactory {
        @Override
        protected GameLogic createInstance(Field field) throws Exception {
            return FormulaShuffleFactory.formulaShuffleGame(field);
        }

        @Override
        public String getModeName() {
            return "Formula Shuffle Mode";
        }
    }

    public static class FormulaDebugMode extends FormulaGameFactory {
        @Override
        protected GameLogic createInstance(Field field) throws Exception {
            return FormulaLinesGame.formulaDebugGame(field);
        }

        @Override
        public String getModeName() {
            return "Formula Debug Mode";
        }
    }

    protected GameLogic newInstance(Field field) throws Exception {
        GameLogic game = createInstance(field);
        game.setFieldCleaner(createCleaner(field));
        return game;
    }

    protected FieldCleaner createCleaner(Field field) {
        switch (mCleaner) {
        case Deffered:
            return createDefferedCleaner(field);
        default:
            return new ImmediateFieldCleaner(field);
        }
    }

    protected FieldCleaner createDefferedCleaner(Field field) {
        return new DeferredFieldCleaner(field);
    }

    public Cleaner getCleaner() {
        return mCleaner;
    }

    public void setCleaner(String name) {
        mCleaner = Cleaner.valueOf(name);
    }
}
