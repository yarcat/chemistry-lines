package com.yarcat.chemistrylines;

import android.app.Activity;
import android.os.Bundle;

import com.yarcat.chemistrylines.field.RectField;
import com.yarcat.chemistrylines.game.ChemistryLinesGame;
import com.yarcat.chemistrylines.game.GameLogic;
import com.yarcat.chemistrylines.touch.SlideTouchLogic;
import com.yarcat.chemistrylines.view.MainView;

public class MainActivity extends Activity {

    private SlideTouchLogic mTouchListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RectField field = new RectField(6, 6);
        GameLogic logic = new ChemistryLinesGame(field);
        logic.addItems();
        MainView mainView = new MainView(this, field, 6, 6, logic);
        mTouchListener = new SlideTouchLogic();
        mainView.setOnTouchListener(mTouchListener);
        setContentView(mainView);
    }
}
