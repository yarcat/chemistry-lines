package com.yarcat.chemistrylines.gamelogic;

import com.yarcat.chemistrylines.Field;

public class ChemistryLinesGame implements GameLogic {
    
    public ChemistryLinesGame(Field field) {
    }

    public boolean isMoveValid(int origin, int fin) {
        return false;
    }

    public void makeMove(int origin, int fin) throws InvalidMove {
        throw new InvalidMove();
    }

    public void addRandomItems() {
    }

}
