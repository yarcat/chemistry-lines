package com.yarcat.chemistrylines.swing.release;

import com.yarcat.chemistrylines.swing.GameFactory;
import com.yarcat.chemistrylines.swing.GameFactory.FormulaShuffleMode;
import com.yarcat.chemistrylines.swing.SwingChemistryLinesMain;

public class ManualShuffle extends SwingChemistryLinesMain {

    public static void main(String[] args) {
        GameFactory f = new FormulaShuffleMode();
        f.setCleaner("Deffered");
        run(f);
    }
}
