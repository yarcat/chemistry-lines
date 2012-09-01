package com.yarcat.chemistrylines.touch;

import com.yarcat.chemistrylines.view.FieldView;

import android.view.MotionEvent;
import android.view.View;

public class SlideTouchLogic implements View.OnTouchListener {

    public boolean onTouch(View v, MotionEvent event) {
        FieldView fv = (FieldView) v;
        int n = fv.getIndex(event.getX(), event.getY());
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            fv.cancel();
            fv.select(n);
            break;
        case MotionEvent.ACTION_UP:
            fv.apply();
            break;
        case MotionEvent.ACTION_CANCEL:
            fv.cancel();
            break;
        case MotionEvent.ACTION_MOVE:
            fv.select(n);
            break;
        }
        return true;
    }
}
