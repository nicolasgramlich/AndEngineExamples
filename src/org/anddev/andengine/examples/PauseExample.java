package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.modifier.MoveModifier;
import org.anddev.andengine.entity.scene.CameraScene;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;

import android.view.KeyEvent;

/**
 * @author Nicolas Gramlich
 * @since 11:33:33 - 01.04.2010
 */
public class PauseExample extends BaseExample {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	// ===========================================================
	// Fields
	// ===========================================================
	private Camera mCamera;

	private Texture mTexture;
	private Scene mMainScene;
	private TextureRegion mFaceTextureRegion;
	private TextureRegion mPausedTextureRegion;
	private CameraScene mPauseScene;

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
	public Engine onLoadEngine() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera));
	}

	@Override
	public void onLoadResources() {
		this.mTexture = new Texture(256, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mPausedTextureRegion = TextureRegionFactory.createFromAsset(this.mTexture, this, "gfx/paused.png", 0, 0);
		this.mFaceTextureRegion = TextureRegionFactory.createFromAsset(this.mTexture, this, "gfx/face_box_menu.png", 0, 50);

		this.mEngine.getTextureManager().loadTexture(this.mTexture);
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		this.mPauseScene = new CameraScene(1, this.mCamera);
		/* Make the 'PAUSED'-label centered on the camera. */
		final int x = CAMERA_WIDTH / 2 - this.mPausedTextureRegion.getWidth() / 2;
		final int y = CAMERA_HEIGHT / 2 - this.mPausedTextureRegion.getHeight() / 2;
		final Sprite pausedSprite = new Sprite(x, y, this.mPausedTextureRegion);
		this.mPauseScene.getLastChild().attachChild(pausedSprite);
		/* Makes the paused Game look through. */
		this.mPauseScene.setBackgroundEnabled(false);

		/* Just a simple */
		this.mMainScene = new Scene(1);
		this.mMainScene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));

		final Sprite face = new Sprite(0, 0, this.mFaceTextureRegion);
		face.registerEntityModifier(new MoveModifier(30, 0, CAMERA_WIDTH - face.getWidth(), 0, CAMERA_HEIGHT - face.getHeight()));
		this.mMainScene.getLastChild().attachChild(face);

		return this.mMainScene;
	}

	@Override
	public void onLoadComplete() {

	}

	@Override
	public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent) {
		if(pKeyCode == KeyEvent.KEYCODE_MENU && pEvent.getAction() == KeyEvent.ACTION_DOWN) {
			if(this.mEngine.isRunning()) {
				this.mMainScene.setChildScene(this.mPauseScene, false, true, true);
				this.mEngine.stop();
			} else {
				this.mMainScene.clearChildScene();
				this.mEngine.start();
			}
			return true;
		} else {
			return super.onKeyDown(pKeyCode, pEvent);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
