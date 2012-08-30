package com.yarcat.chemistrylines;

/** User-Field interaction events name-space */
public final class FieldInteraction {

    public interface CellTouchEvent {

        /** Return MotionEvent.getAction() */
        public int getAction();

        /** Return touched cell index */
        public int getCellIndex();
    }

    public interface CellTouchListener {

        /** React on cell touch */
        public void onCellTouch(CellTouchEvent event);
    }
}
