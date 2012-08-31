package com.yarcat.chemistrylines.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import com.yarcat.chemistrylines.touchlogic.FieldView;

public class MainView extends View implements FieldView {

    private static final int LINE_WIDTH = 5;
    private static final int BORDER = 20;

    private int mCols;
    private int mRows;
    private int mStep;
    private Paint mPaint = new Paint();
    private Rect mField = new Rect();
    private Paint mFirstSelectionPaint = new Paint();
    private Paint mSelectionPaint = new Paint();

    private final SelectionInView mSelection = new SelectionInView();

    public MainView(Context context) {
        super(context);

        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(LINE_WIDTH);

        mFirstSelectionPaint.setColor(Color.LTGRAY);
        mFirstSelectionPaint.setStyle(Paint.Style.FILL);
        mSelectionPaint.setColor(Color.GRAY);
        mSelectionPaint.setStyle(Paint.Style.FILL);

        mCols = mRows = 4; // Just something for the visual tool.
    }

    public MainView(Context context, int cols, int rows) {
        this(context);
        mCols = cols;
        mRows = rows;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mSelection.hasSource()) {
            drawSourceSelection(mSelection.getSource(), canvas);
            if (mSelection.hasDestination()) {
                drawDestinationSelection(mSelection.getDestination(), canvas);
            }
        }

        for (int col = 0; col <= mCols; ++col) {
            int x = mField.left + mStep * col;
            canvas.drawLine(x, mField.top, x, mField.bottom, mPaint);
        }

        for (int row = 0; row <= mRows; ++row) {
            int y = mField.top + mStep * row;
            canvas.drawLine(mField.left, y, mField.right, y, mPaint);
        }
    }

    private void drawSelection(int n, Canvas canvas, Paint paint) {
        int col = n % mCols;
        int row = n / mRows;
        int left = mField.left + col * mStep;
        int right = left + mStep;
        int top = mField.top + row * mStep;
        int bottom = top + mStep;
        canvas.drawRect(left, top, right, bottom, paint);
    }

    private void drawDestinationSelection(int n, Canvas canvas) {
        drawSelection(n, canvas, mFirstSelectionPaint);
    }

    private void drawSourceSelection(int n, Canvas canvas) {
        drawSelection(n, canvas, mSelectionPaint);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mStep = Math.min((w - BORDER) / mCols, (h - BORDER) / mRows);

        mField.left = BORDER / 2;
        mField.top = BORDER / 2;
        mField.right = mField.left + mStep * mCols;
        mField.bottom = mField.top + mStep * mRows;
    }

    public int getIndex(float x, float y) {
        int row = (int) (y - mField.top) / mStep;
        int col = (int) (x - mField.left) / mStep;
        return row * mCols + col;
    }

    public void select(int n) {
        mSelection.select(n);
        invalidate();
    }

    public void clean() {
        mSelection.clear();
        invalidate();
    }
}
