package org.anddev.andengine.examples.game.snake.adt;

/**
 * @author Nicolas Gramlich
 * @since 02:29:05 - 08.07.2010
 */
public enum Direction {
	// ===========================================================
	// Elements
	// ===========================================================

	UP(), DOWN, LEFT, RIGHT;

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public static int addToX(final Direction pDirection, final int pX) {
		switch(pDirection) {
			case DOWN:
			case UP:
				return pX;
			case LEFT:
				return pX - 1;
			case RIGHT:
				return pX + 1;
			default:
				throw new IllegalArgumentException();
		}
	}

	public static int addToY(final Direction pDirection, final int pY) {
		switch(pDirection) {
			case LEFT:
			case RIGHT:
				return pY;
			case UP:
				return pY - 1;
			case DOWN:
				return pY + 1;
			default:
				throw new IllegalArgumentException();
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
