package com.yarcat.chemistrylines;

import com.yarcat.chemistrylines.gamelogic.ChemistryLinesGame;
import com.yarcat.chemistrylines.touchlogic.SlideTouchLogic;

import android.os.Bundle;
import android.app.Activity;

public class MainActivity extends Activity {

    private SlideTouchLogic touchListener;
    private ChemistryLinesGame mGameLogic;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RectField field = new RectField(8, 8);
        MainView mainView = new MainView(this, 8, 8);
        touchListener = new SlideTouchLogic(this, mainView);
        mGameLogic = new ChemistryLinesGame(field);
        setContentView(mainView);
    }
}
