package com.yarcat.chemistrylines;

/** Visual Field state 
 *
 * Keeps cell highlighting information.
 * 
 */
public interface FieldPresentationState {
	
	/** Return cell highlighting */
	public int getHighlight(int cell_index);

	/** Highlight a cell */
	public int highlightCell(int cell_index);
	
	/** Reset all highlight marks */
	public void clearHighlights();
}
