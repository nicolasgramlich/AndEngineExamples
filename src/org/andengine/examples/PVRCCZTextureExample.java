package org.andengine.examples;

import java.io.IOException;
import java.io.InputStream;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.examples.adt.ZoomState;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.compressed.pvr.PVRCCZTexture;
import org.andengine.opengl.texture.compressed.pvr.PVRTexture.PVRTextureFormat;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.util.debug.Debug;

import android.widget.Toast;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 15:31:11 - 27.07.2011
 */
public class PVRCCZTextureExample extends BaseExample {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	// ===========================================================
	// Fields
	// ===========================================================

	private SmoothCamera mSmoothCamera;

	private ITexture mTexture;
	private ITextureRegion mHouseTextureRegion;

	private ZoomState mZoomState = ZoomState.NONE;

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
		Toast.makeText(this, "Click the top half of the screen to zoom in or the bottom half to zoom out!", Toast.LENGTH_LONG);
		this.mSmoothCamera = new SmoothCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT, 0, 0, 0.1f) {
			@Override
			public void onUpdate(final float pSecondsElapsed) {
				switch (PVRCCZTextureExample.this.mZoomState) {
					case IN:
						this.setZoomFactor(this.getZoomFactor() + 0.1f * pSecondsElapsed);
						break;
					case OUT:
						this.setZoomFactor(this.getZoomFactor() - 0.1f * pSecondsElapsed);
						break;
				}
				super.onUpdate(pSecondsElapsed);
			}
		};
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mSmoothCamera));
	}

	@Override
	public void onLoadResources() {
		try {
			this.mTexture = new PVRCCZTexture(PVRTextureFormat.RGBA_8888, TextureOptions.BILINEAR) {
				@Override
				protected InputStream onGetInputStream() throws IOException {
					return PVRCCZTextureExample.this.getResources().openRawResource(R.raw.house_pvrccz_argb_8888);
				}
			}.load();

			this.mHouseTextureRegion = TextureRegionFactory.extractFromTexture(this.mTexture, 0, 0, 512, 512, true);
		} catch (final Throwable e) {
			Debug.e(e);
		}
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
		scene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));

		final int centerX = (CAMERA_WIDTH - this.mHouseTextureRegion.getWidth()) / 2;
		final int centerY = (CAMERA_HEIGHT - this.mHouseTextureRegion.getHeight()) / 2;

		scene.attachChild(new Sprite(centerX, centerY, this.mHouseTextureRegion));

		scene.setOnSceneTouchListener(new IOnSceneTouchListener() {
			@Override
			public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
				if (pSceneTouchEvent.isActionDown() || pSceneTouchEvent.isActionMove()) {
					if(pSceneTouchEvent.getY() < CAMERA_HEIGHT / 2) {
						PVRCCZTextureExample.this.mZoomState = ZoomState.IN;
					} else {
						PVRCCZTextureExample.this.mZoomState = ZoomState.OUT;
					}
				} else {
					PVRCCZTextureExample.this.mZoomState = ZoomState.NONE;
				}
				return true;
			}
		});

		return scene;
	}

	@Override
	public void onLoadComplete() {

	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
