package com.yarcat.chemistrylines;

/** Move animation stuff */
public interface MoveAnimation {
	
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
