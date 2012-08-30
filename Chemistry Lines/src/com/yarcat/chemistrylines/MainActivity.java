package com.yarcat.chemistrylines;

import com.yarcat.chemistrylines.gamelogic.ChemistryLinesGame;
import com.yarcat.chemistrylines.touchlogic.SlideTouchLogic;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.app.Activity;

public class MainActivity extends Activity {

    private SlideTouchLogic mTouchListener;
    private ChemistryLinesGame mGameLogic;
    private GestureDetector mGestureDetector;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RectField field = new RectField(8, 8);
        MainView mainView = new MainView(this, 8, 8);
        mTouchListener = new SlideTouchLogic(mainView);
        mGestureDetector = new GestureDetector(this, mTouchListener);
        mGameLogic = new ChemistryLinesGame(field);
        setContentView(mainView);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }
}
