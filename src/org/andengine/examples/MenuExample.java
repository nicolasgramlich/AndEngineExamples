package org.andengine.examples;

import java.io.IOException;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.animator.AlphaMenuSceneAnimator;
import org.andengine.entity.scene.menu.animator.SlideMenuSceneAnimator;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.bitmap.AssetBitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.adt.align.VerticalAlign;
import org.andengine.util.adt.spatial.Direction;
import org.andengine.util.modifier.ease.EaseBounceOut;

import android.view.KeyEvent;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 01:30:15 - 02.04.2010
 */
public class MenuExample extends SimpleBaseGameActivity implements IOnMenuItemClickListener {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	protected static final int MENU_RESET = 0;
	protected static final int MENU_QUIT = MENU_RESET + 1;

	// ===========================================================
	// Fields
	// ===========================================================

	protected Camera mCamera;

	protected Scene mMainScene;
	protected MenuScene mMenuScene;

	private ITexture mFaceTexture;
	protected ITextureRegion mFaceTextureRegion;

	private ITexture mMenuResetTexture;
	protected ITextureRegion mMenuResetTextureRegion;
	private ITexture mMenuQuitTexture;
	protected ITextureRegion mMenuQuitTextureRegion;

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
	public EngineOptions onCreateEngineOptions() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera);
	}

	@Override
	public void onCreateResources() throws IOException {
		this.mFaceTexture = new AssetBitmapTexture(this.getTextureManager(), this.getAssets(), "gfx/face_box_menu.png", TextureOptions.BILINEAR);
		this.mFaceTextureRegion = TextureRegionFactory.extractFromTexture(this.mFaceTexture);
		this.mFaceTexture.load();

		this.mMenuResetTexture = new AssetBitmapTexture(this.getTextureManager(), this.getAssets(), "gfx/menu_reset.png", TextureOptions.BILINEAR);
		this.mMenuResetTextureRegion = TextureRegionFactory.extractFromTexture(this.mMenuResetTexture);
		this.mMenuResetTexture.load();

		this.mMenuQuitTexture = new AssetBitmapTexture(this.getTextureManager(), this.getAssets(), "gfx/menu_quit.png", TextureOptions.BILINEAR);
		this.mMenuQuitTextureRegion = TextureRegionFactory.extractFromTexture(this.mMenuQuitTexture);
		this.mMenuQuitTexture.load();
	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		this.createMenuScene();

		/* Just a simple scene with an sprite flying around. */
		this.mMainScene = new Scene();
		this.mMainScene.getBackground().setColor(0.09804f, 0.6274f, 0.8784f);

		final Sprite sprite = new Sprite(0, 0, this.mFaceTextureRegion, this.getVertexBufferObjectManager());
		sprite.registerEntityModifier(new MoveModifier(30, 0, 0, CAMERA_WIDTH, CAMERA_HEIGHT - sprite.getHeight()));
		this.mMainScene.attachChild(sprite);

		return this.mMainScene;
	}

	@Override
	public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent) {
		if(pKeyCode == KeyEvent.KEYCODE_MENU && pEvent.getAction() == KeyEvent.ACTION_DOWN) {
			if(this.mMainScene.hasChildScene()) {
				/* Remove the menu and reset it. */
				this.mMenuScene.back();
			} else {
				/* Attach the menu. */
				this.mMainScene.setChildScene(this.mMenuScene, false, true, true);
			}
			return true;
		} else {
			return super.onKeyDown(pKeyCode, pEvent);
		}
	}

	@Override
	public boolean onMenuItemClicked(final MenuScene pMenuScene, final IMenuItem pMenuItem, final float pMenuItemLocalX, final float pMenuItemLocalY) {
		switch(pMenuItem.getID()) {
			case MENU_RESET:
				/* Restart the animation. */
				this.mMainScene.reset();

				/* Remove the menu and reset it. */
				this.mMainScene.clearChildScene();
				this.mMenuScene.resetAnimations();
				return true;
			case MENU_QUIT:
				/* End Activity. */
				this.finish();
				return true;
			default:
				return false;
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	protected void createMenuScene() {
		this.mMenuScene = new MenuScene(this.mCamera, new AlphaMenuSceneAnimator());

		final SpriteMenuItem resetMenuItem = new SpriteMenuItem(MENU_RESET, this.mMenuResetTextureRegion, this.getVertexBufferObjectManager());
		this.mMenuScene.addMenuItem(resetMenuItem);

		final SpriteMenuItem quitMenuItem = new SpriteMenuItem(MENU_QUIT, this.mMenuQuitTextureRegion, this.getVertexBufferObjectManager());
		this.mMenuScene.addMenuItem(quitMenuItem);

		final SlideMenuSceneAnimator menuSceneAnimator = new SlideMenuSceneAnimator(HorizontalAlign.CENTER, VerticalAlign.CENTER, Direction.UP_LEFT, EaseBounceOut.getInstance());
		menuSceneAnimator.setMenuItemSpacing(10);
		this.mMenuScene.setMenuSceneAnimator(menuSceneAnimator);

		this.mMenuScene.buildAnimations();

		this.mMenuScene.setBackgroundEnabled(false);

		this.mMenuScene.setOnMenuItemClickListener(this);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}