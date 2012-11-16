package com.yarcat.chemistrylines.game;

import com.yarcat.chemistrylines.field.Element;

/**
 * Game rules.
 *
 * It looks like all Lines-like puzzles share lots of common functionality. The
 * main idea of this interface is encapsulation of rules of a particular game.
 * It knows about possible moves, verifies the logic, sends back notifications
 * to the presenter, etc.
 */
public interface GameLogic {

    public static class InvalidMove extends Exception {
        private static final long serialVersionUID = 1761471468300304095L;
    }

    /** Interface for callback invocation on a game actions. */
    public interface Listener {
        /**
         * Called when game is finished. TODO(yarcat): provide information about
         * type of the end.
         */
        public void onFinished();
    }

    /**
     * Check whether a move from origin cell to fin is a valid one.
     *
     * @param origin
     *            Index of the origin cell.
     * @param fin
     *            Index of the final cell.
     * @return True if this move is valid, false otherwise.
     */
    public boolean isMoveValid(int origin, int fin);

    /**
     * Make an actual move.
     *
     * @param origin
     *            Index of the origin cell.
     * @param fin
     *            Index of the final cell.
     * @throws InvalidMove
     *             If there is no valid move(s) from origin to fin.
     */
    public void makeMove(int origin, int fin) throws InvalidMove;

    /** Populates field with new elements. */
    public void addItems();

    /** Returns next elements to preview in UI */
    public Element[] previewNextElements();
}
