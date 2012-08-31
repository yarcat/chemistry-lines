package com.yarcat.chemistrylines;

import android.app.Activity;
import android.os.Bundle;

import com.yarcat.chemistrylines.touchlogic.SlideTouchLogic;

public class MainActivity extends Activity {

    private SlideTouchLogic mTouchListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO(yarcat): Create RecvField here.
        MainView mainView = new MainView(this, 8, 8);
        mTouchListener = new SlideTouchLogic();
        mainView.setOnTouchListener(mTouchListener);
        setContentView(mainView);
    }
}
