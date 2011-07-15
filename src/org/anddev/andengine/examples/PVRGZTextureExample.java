package org.anddev.andengine.examples;

import java.io.IOException;
import java.util.zip.GZIPInputStream;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.SmoothCamera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.examples.adt.ZoomState;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.ITexture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.compressed.pvr.PVRGZTexture;
import org.anddev.andengine.opengl.texture.compressed.pvr.PVRTexture.PVRTextureFormat;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.util.Debug;

import android.widget.Toast;

/**
 * @author Nicolas Gramlich
 * @since 11:54:51 - 03.04.2010
 */
public class PVRGZTextureExample extends BaseExample {
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
	private TextureRegion mHouseTextureRegion;

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
				switch (PVRGZTextureExample.this.mZoomState) {
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
			this.mTexture = new PVRGZTexture(PVRTextureFormat.RGBA_8888, TextureOptions.BILINEAR) {
				@Override
				protected GZIPInputStream getGZIPInputStream() throws IOException {
					return new GZIPInputStream(PVRGZTextureExample.this.getResources().openRawResource(R.raw.house_pvrgz_argb_8888));
				}
			};

			this.mHouseTextureRegion = TextureRegionFactory.extractFromTexture(this.mTexture, 0, 0, 512, 512, true);

			this.mEngine.getTextureManager().loadTextures(this.mTexture);
		} catch (final Throwable e) {
			Debug.e(e);
		}
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
		scene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));

		final int centerX = (CAMERA_WIDTH - this.mHouseTextureRegion.getWidth()) / 2;
		final int centerY = (CAMERA_HEIGHT - this.mHouseTextureRegion.getHeight()) / 2;

		scene.attachChild(new Sprite(centerX, centerY, this.mHouseTextureRegion));

		scene.setOnSceneTouchListener(new IOnSceneTouchListener() {
			@Override
			public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
				if (pSceneTouchEvent.isActionDown() || pSceneTouchEvent.isActionMove()) {
					if(pSceneTouchEvent.getY() < CAMERA_HEIGHT / 2) {
						PVRGZTextureExample.this.mZoomState = ZoomState.IN;
					} else {
						PVRGZTextureExample.this.mZoomState = ZoomState.OUT;
					}
				} else {
					PVRGZTextureExample.this.mZoomState = ZoomState.NONE;
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
