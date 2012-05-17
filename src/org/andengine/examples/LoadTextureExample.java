package org.andengine.examples;

import java.io.IOException;

import org.andengine.engine.Engine.EngineLock;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.bitmap.AssetBitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.debug.Debug;

import android.widget.Toast;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 12:14:29 - 30.06.2010
 */
public class LoadTextureExample extends SimpleBaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	// ===========================================================
	// Fields
	// ===========================================================

	private Scene mScene;

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
		Toast.makeText(this, "Touch the screen to load a completely new Texture and create a sprite from it!\nTouch it again to unload the Texture and remove the sprite.", Toast.LENGTH_LONG).show();

		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
	}

	@Override
	public void onCreateResources() {
		/* Nothing done here. */
	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		this.mScene = new Scene();
		this.mScene.getBackground().setColor(0.09804f, 0.6274f, 0.8784f);

		this.mScene.setOnSceneTouchListener(new IOnSceneTouchListener() {
			@Override
			public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
				if(pSceneTouchEvent.isActionDown()) {
					try {
						LoadTextureExample.this.loadNewTexture(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
					} catch (final IOException e) {
						Debug.e(e);
					}
				}

				return true;
			}
		});
		this.mScene.setOnSceneTouchListenerBindingOnActionDownEnabled(true);

		return this.mScene;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void loadNewTexture(final float pX, final float pY) throws IOException {
		final ITexture texture = new AssetBitmapTexture(this.getTextureManager(), this.getAssets(), "gfx/face_box.png", TextureOptions.BILINEAR);
		final ITextureRegion faceTextureRegion = TextureRegionFactory.extractFromTexture(texture);
		texture.load();

		final Sprite sprite = new Sprite(pX, pY, faceTextureRegion, this.getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionUp()) {
					final EngineLock engineLock = LoadTextureExample.this.mEngine.getEngineLock();
					engineLock.lock();

					this.detachSelf();
					texture.unload();
					LoadTextureExample.this.mScene.unregisterTouchArea(this);

					engineLock.unlock();
				}
				return true;
			}
		};
		this.mScene.attachChild(sprite);
		this.mScene.registerTouchArea(sprite);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
