package com.yarcat.chemistrylines.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import com.yarcat.chemistrylines.field.Cell;
import com.yarcat.chemistrylines.field.Element;
import com.yarcat.chemistrylines.field.Field;
import com.yarcat.chemistrylines.game.GameLogic;
import com.yarcat.chemistrylines.game.GameLogic.InvalidMove;

public class RectFieldView extends View implements FieldView {

    private static final int LINE_WIDTH = 5;
    private static final int BORDER = 20;

    private int mCols;
    private int mRows;
    private int mStep;
    private Paint mPaint = new Paint();
    private Rect mFieldRect = new Rect();
    private Paint mFirstSelectionPaint = new Paint();
    private Paint mSelectionPaint = new Paint();

    private final SelectionInView mSelection = new SelectionInView();
    private Field mField;
    private GameLogic mLogic;

    public RectFieldView(Context context) {
        super(context);

        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(LINE_WIDTH);

        mFirstSelectionPaint.setColor(Color.LTGRAY);
        mFirstSelectionPaint.setStyle(Paint.Style.FILL);
        mSelectionPaint.setColor(Color.GRAY);
        mSelectionPaint.setStyle(Paint.Style.FILL);

        mCols = mRows = 4; // Just something for the visual tool.
    }

    public RectFieldView(Context context, Field field, int cols, int rows,
            GameLogic logic) {
        this(context);
        mField = field;
        mCols = cols;
        mRows = rows;
        mLogic = logic;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawSelection(canvas);
        drawGrid(canvas);
        drawElements(canvas);
    }

    private void drawSelection(Canvas canvas) {
        if (mSelection.hasSource()) {
            drawSourceSelection(mSelection.getSource(), canvas);
            if (mSelection.hasDestination()) {
                drawDestinationSelection(mSelection.getDestination(), canvas);
            }
        }
    }

    private void drawGrid(Canvas canvas) {
        for (int col = 0; col <= mCols; ++col) {
            int x = mFieldRect.left + mStep * col;
            canvas.drawLine(x, mFieldRect.top, x, mFieldRect.bottom, mPaint);
        }

        for (int row = 0; row <= mRows; ++row) {
            int y = mFieldRect.top + mStep * row;
            canvas.drawLine(mFieldRect.left, y, mFieldRect.right, y, mPaint);
        }
    }

    private void drawElements(Canvas canvas) {
        for (int n = 0; n < mField.getLength(); ++n) {
            Cell cell = mField.at(n);
            if (!cell.isEmpty()) {
                Element e = cell.getElement();
                int col = n % mCols;
                int row = n / mRows;
                int left = mFieldRect.left + col * mStep;
                int top = mFieldRect.top + row * mStep;
                Paint p = new Paint();
                p.setColor(Color.MAGENTA);
                p.setStyle(Paint.Style.FILL);
                p.setTextAlign(Paint.Align.CENTER);
                p.setTextSize(50);
                Rect bounds = new Rect();
                String text = e.getId();
                p.getTextBounds(text, 0, text.length(), bounds);
                int d = (mStep + bounds.top + bounds.bottom) / 2;
                canvas.drawText(text, left + mStep / 2, top + mStep - d, p);
            }
        }
    }

    private void drawSelection(int n, Canvas canvas, Paint paint) {
        int col = n % mCols;
        int row = n / mRows;
        int left = mFieldRect.left + col * mStep;
        int right = left + mStep;
        int top = mFieldRect.top + row * mStep;
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

        mFieldRect.left = BORDER / 2;
        mFieldRect.top = BORDER / 2;
        mFieldRect.right = mFieldRect.left + mStep * mCols;
        mFieldRect.bottom = mFieldRect.top + mStep * mRows;
    }

    public int getIndex(float x, float y) {
        if (mFieldRect.contains((int) x, (int) y)) {
            int row = (int) (y - mFieldRect.top) / mStep;
            int col = (int) (x - mFieldRect.left) / mStep;
            return row * mCols + col;
        }
        return -1;
    }

    public void select(int n) {
        mSelection.select(n);
        invalidate();
    }

    public void cancel() {
        mSelection.clear();
        invalidate();
    }

    public void apply() {
        if (mSelection.hasDestination()) {
            try {
                mLogic.makeMove(mSelection.getSource(),
                        mSelection.getDestination());
            } catch (InvalidMove e) {
                // TODO(yarcat): Notify the player.
            }
        }
        mSelection.clear();
        invalidate();
    }
}
