package com.yarcat.chemistrylines.swing.release;

import com.yarcat.chemistrylines.swing.GameFactory;
import com.yarcat.chemistrylines.swing.GameFactory.FormulaRandomMode;
import com.yarcat.chemistrylines.swing.SwingChemistryLinesMain;

public class ManualRandom extends SwingChemistryLinesMain {

    public static void main(String[] args) {
        GameFactory f = new FormulaRandomMode();
        f.setCleaner("Deffered");
        run(f);
    }
}
