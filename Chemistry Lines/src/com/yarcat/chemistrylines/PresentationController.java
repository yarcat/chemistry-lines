package com.yarcat.chemistrylines;

/** Presentation controller having a state */
public interface PresentationController {
	
	/** Return cell highlighting */
	public int getHighlight(int cell_index);

	/** Highlight a cell */
	public int highlightCell(int cell_index);
	
	/** Reset all highlight marks */
	public void clearHighlights();
	
	/** Visual move callback*/
	public interface MoveCallback {
		
		/** Finished a move from origin cell to fin  
		 * 
		 * @param origin cell index
		 * @param fin cell index
		 */
		public void done(int origin, int fin);
	}

    /** Display a move from origin to fin
     * 
     * @param origin cell index
     * @param fin cell index
     * @param callback after the move is done
     */
	public void makeMove(int origin, int fin, MoveCallback callback);
}
