package com.yarcat.chemistrylines;

/** Game rules and related stuff
 * 
 * - Checks if a move is possible -- reach-ability and reactions
 * - Make a move. E.g. updates the field and interacts to PresentationController.
 * - Adds new items to the field.
 */
public interface GameLogic {
	public class InvalidMove extends Exception {
	}

	/** Check whether a move from origin cell to fin is a valid one */
	public boolean isMoveValid(int origin, int fin);

	/** Make a move actually */
	public void makeMove(int origin, int fin) throws InvalidMove;

	/** Add three more items to the board */
	public void addRandomItems();
}
