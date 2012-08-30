package com.yarcat.chemistrylines;

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

    private int mSelected = 0;
    private int[] mSelectionIndices = new int[] { -1, -1 };

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
        for (int i = 0; i < mSelected; ++i) {
            int col = mSelectionIndices[i] % mCols;
            int row = mSelectionIndices[i] / mRows;
            int left = mField.left + col * mStep;
            int right = left + mStep;
            int top = mField.top + row * mStep;
            int bottom = top + mStep;
            canvas.drawRect(left, top, right, bottom,
                    i == 0 ? mFirstSelectionPaint : mSelectionPaint);
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

    public void select(int n, boolean isSource) {
        if (isSource) {
            mSelected = 0;
        }
        mSelectionIndices[mSelected] = n;
        mSelected = ++mSelected % 2;
        invalidate();
    }

    public void clean() {
        mSelected = 0;
    }
}
