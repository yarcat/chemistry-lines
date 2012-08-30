package com.yarcat.chemistrylines;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

public class MainView extends View implements View.OnTouchListener {

    private static final int LINE_WIDTH = 5;
    private static final int ROWS = 8;
    private static final int COLS = 9;
    private static final int BORDER = 20;
    private Paint mPaint = new Paint();
    private int mStep;
    private Rect mField = new Rect();
    private Point mSelection;
    private Paint mSelectionPaint = new Paint();

    public MainView(Context context) {
        super(context);

        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(LINE_WIDTH);

        mSelectionPaint.setColor(Color.GRAY);
        mSelectionPaint.setStyle(Paint.Style.FILL);

        setOnTouchListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (mSelection != null) {
            int left = mField.left + mSelection.x * mStep;
            int right = left + mStep;
            int top = mField.top + mSelection.y * mStep;
            int bottom = top + mStep;
            canvas.drawRect(left, top, right, bottom, mSelectionPaint);
        }

        for (int col = 0; col <= COLS; ++col) {
            int x = mField.left + mStep * col;
            canvas.drawLine(x, mField.top, x, mField.bottom, mPaint);
        }

        for (int row = 0; row <= ROWS; ++row) {
            int y = mField.top + mStep * row;
            canvas.drawLine(mField.left, y, mField.right, y, mPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mStep = Math.min((w - BORDER) / COLS, (h - BORDER) / ROWS);

        mField.left = BORDER / 2;
        mField.top = BORDER / 2;
        mField.right = mField.left + mStep * COLS;
        mField.bottom = mField.top + mStep * ROWS;
    }

    public boolean onTouch(View v, MotionEvent event) {
        if (true /* event.getAction() == MotionEvent.ACTION_DOWN */) {

            int x = (int) event.getX();
            int y = (int) event.getY();
            if (mField.contains(x, y)) {
                touchCell((x - mField.left) / mStep, (y - mField.top) / mStep);
                return true;
            }
        }
        mSelection = null;
        return false;
    }

    protected void touchCell(int col, int row) {
        mSelection = new Point(col, row);
        invalidate();
    }
}
