package org.andengine.examples;

import java.io.IOException;

import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.animator.SlideMenuSceneAnimator;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.bitmap.AssetBitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;



/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 11:33:33 - 01.04.2010
 */
public class SubMenuExample extends MenuExample {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int MENU_QUIT_OK = MenuExample.MENU_QUIT + 1;
	private static final int MENU_QUIT_BACK = MENU_QUIT_OK + 1;

	// ===========================================================
	// Fields
	// ===========================================================

	private MenuScene mSubMenuScene;

	private ITexture mSubMenuOkTexture;
	private ITextureRegion mSubMenuOkTextureRegion;
	private ITexture mSubMenuBackTexture;
	private ITextureRegion mSubMenuBackTextureRegion;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onCreateResources() throws IOException {
		super.onCreateResources();

		this.mSubMenuOkTexture = new AssetBitmapTexture(this.getTextureManager(), this.getAssets(), "gfx/menu_ok.png", TextureOptions.BILINEAR);
		this.mSubMenuOkTextureRegion = TextureRegionFactory.extractFromTexture(this.mSubMenuOkTexture);
		this.mSubMenuOkTexture.load();

		this.mSubMenuBackTexture = new AssetBitmapTexture(this.getTextureManager(), this.getAssets(), "gfx/menu_back.png", TextureOptions.BILINEAR);
		this.mSubMenuBackTextureRegion = TextureRegionFactory.extractFromTexture(this.mSubMenuBackTexture);
		this.mSubMenuBackTexture.load();
	}

	@Override
	protected void createMenuScene() {
		super.createMenuScene();

		this.mSubMenuScene = new MenuScene(this.mCamera, new SlideMenuSceneAnimator());
		this.mSubMenuScene.addMenuItem(new SpriteMenuItem(MENU_QUIT_OK, this.mSubMenuOkTextureRegion, this.getVertexBufferObjectManager()));
		this.mSubMenuScene.addMenuItem(new SpriteMenuItem(MENU_QUIT_BACK, this.mSubMenuBackTextureRegion, this.getVertexBufferObjectManager()));
		this.mSubMenuScene.setMenuSceneAnimator(new SlideMenuSceneAnimator());
		this.mSubMenuScene.buildAnimations();

		this.mSubMenuScene.setBackgroundEnabled(false);

		this.mSubMenuScene.setOnMenuItemClickListener(this);
	}

	@Override
	public boolean onMenuItemClicked(final MenuScene pMenuScene, final IMenuItem pMenuItem, final float pMenuItemLocalX, final float pMenuItemLocalY) {
		switch(pMenuItem.getID()) {
			case MENU_RESET:
				this.mMainScene.reset();

				this.mMenuScene.back();
				return true;
			case MENU_QUIT:
				pMenuScene.setChildSceneModal(this.mSubMenuScene);
				return true;
			case MENU_QUIT_BACK:
				this.mMenuScene.resetAnimations();
				this.mSubMenuScene.back();
				return true;
			case MENU_QUIT_OK:
				this.finish();
				return true;
			default:
				return false;
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
