package com.yarcat.chemistrylines.touchlogic;

import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

public class SlideTouchLogic extends SimpleOnGestureListener {

    private FieldView mFieldView;

    public SlideTouchLogic(FieldView fieldHelper) {
        mFieldView = fieldHelper;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
            float distanceY) {
        int index = mFieldView.getIndex(e2.getX(), e2.getY());
        mFieldView.select(index, false);
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        int index = mFieldView.getIndex(e.getX(), e.getY());
        mFieldView.select(index, true);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
            float velocityY) {
        mFieldView.clean();
        return true;
    }
}
