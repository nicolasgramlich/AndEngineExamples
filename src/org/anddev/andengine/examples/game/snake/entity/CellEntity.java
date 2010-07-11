package org.anddev.andengine.examples.game.snake.entity;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.examples.game.snake.util.constants.SnakeConstants;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

/**
 * @author Nicolas Gramlich
 * @since 17:13:44 - 09.07.2010
 */
public abstract class CellEntity extends Sprite implements SnakeConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected int mCellX;
	protected int mCellY;

	// ===========================================================
	// Constructors
	// ===========================================================

	public CellEntity(final int pCellX, final int pCellY, final int pWidth, final int pHeight, final TextureRegion pTextureRegion) {
		super(pCellX * CELL_WIDTH, pCellY * CELL_HEIGHT, pWidth, pHeight, pTextureRegion);
		this.mCellX = pCellX;
		this.mCellY = pCellY;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public int getCellX() {
		return this.mCellX;
	}

	public int getCellY() {
		return this.mCellY;
	}

	public void setCell(final CellEntity pSnakePart) {
		this.setCell(pSnakePart.mCellX, pSnakePart.mCellY);
	}

	public void setCell(final int pCellX, final int pCellY) {
		this.mCellX = pCellX;
		this.mCellY = pCellY;
		this.setPosition(this.mCellX * CELL_WIDTH, this.mCellY * CELL_HEIGHT);
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	
	public boolean isInSameCell(final CellEntity pCellEntity) {
		return this.mCellX == pCellEntity.mCellX && this.mCellY == pCellEntity.mCellY;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
