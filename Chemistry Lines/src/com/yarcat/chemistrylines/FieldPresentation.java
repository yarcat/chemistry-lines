package com.yarcat.chemistrylines;

import android.graphics.Point;
import android.graphics.Canvas;


/** Represents Visual Game Field
 *
 * It is directly connected to the game Field and PresentationController. 
 * PresentationController is a source of interactive information of cells.
 * 
 */
public interface FieldPresentation {
	
	/** Redraw all the field */
	public void redraw(Canvas canvas);

	/** Redraw one cell */
	public int drawCell(Canvas canvas, int cell_index);
	
	/** Returns cell index if p is _inside_ any of them else -1 */
	public int getCellIndex(Point p);

}
