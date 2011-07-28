package org.anddev.andengine.examples;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.SmoothCamera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.Entity;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.examples.adt.ZoomState;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.ITexture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.compressed.pvr.PVRTexture;
import org.anddev.andengine.opengl.texture.compressed.pvr.PVRTexture.PVRTextureFormat;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.util.Debug;

import android.widget.Toast;

/**
 * @author Nicolas Gramlich
 * @since 11:54:51 - 13.07.2011
 */
public class PVRTextureExample extends BaseExample {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	// ===========================================================
	// Fields
	// ===========================================================

	private SmoothCamera mSmoothCamera;

	private ITexture mTextureRGB565;
	private ITexture mTextureRGBA5551;
	private ITexture mTextureARGB4444;
	private ITexture mTextureRGBA888MipMaps;

	private TextureRegion mHouseNearestTextureRegion;
	private TextureRegion mHouseLinearTextureRegion;
	private TextureRegion mHouseMipMapsNearestTextureRegion;
	private TextureRegion mHouseMipMapsLinearTextureRegion;

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
		Toast.makeText(this, "The lower houses use MipMaps!", Toast.LENGTH_LONG);
		Toast.makeText(this, "Click the top half of the screen to zoom in or the bottom half to zoom out!", Toast.LENGTH_LONG);
		this.mSmoothCamera = new SmoothCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT, 0, 0, 0.1f) {
			@Override
			public void onUpdate(final float pSecondsElapsed) {
				switch (PVRTextureExample.this.mZoomState) {
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
			this.mTextureRGB565 = new PVRTexture(PVRTextureFormat.RGB_565, new TextureOptions(GL10.GL_LINEAR, GL10.GL_LINEAR, GL10.GL_CLAMP_TO_EDGE, GL10.GL_CLAMP_TO_EDGE, GL10.GL_MODULATE, false)) {
				@Override
				protected InputStream onGetInputStream() throws IOException {
					return PVRTextureExample.this.getResources().openRawResource(R.raw.house_pvr_rgb_565);
				}
			};

			this.mTextureRGBA5551 = new PVRTexture(PVRTextureFormat.RGBA_5551, new TextureOptions(GL10.GL_LINEAR, GL10.GL_LINEAR, GL10.GL_CLAMP_TO_EDGE, GL10.GL_CLAMP_TO_EDGE, GL10.GL_MODULATE, false)) {
				@Override
				protected InputStream onGetInputStream() throws IOException {
					return PVRTextureExample.this.getResources().openRawResource(R.raw.house_pvr_argb_5551);
				}
			};

			this.mTextureARGB4444 = new PVRTexture(PVRTextureFormat.RGBA_4444, new TextureOptions(GL10.GL_LINEAR, GL10.GL_LINEAR, GL10.GL_CLAMP_TO_EDGE, GL10.GL_CLAMP_TO_EDGE, GL10.GL_MODULATE, false)) {
				@Override
				protected InputStream onGetInputStream() throws IOException {
					return PVRTextureExample.this.getResources().openRawResource(R.raw.house_pvr_argb_4444);
				}
			};

			this.mTextureRGBA888MipMaps = new PVRTexture(PVRTextureFormat.RGBA_8888, new TextureOptions(GL10.GL_LINEAR_MIPMAP_LINEAR, GL10.GL_LINEAR, GL10.GL_CLAMP_TO_EDGE, GL10.GL_CLAMP_TO_EDGE, GL10.GL_MODULATE, false)) {
				@Override
				protected InputStream onGetInputStream() throws IOException {
					return PVRTextureExample.this.getResources().openRawResource(R.raw.house_pvr_argb_8888_mipmaps);
				}
			};

			this.mHouseNearestTextureRegion = TextureRegionFactory.extractFromTexture(this.mTextureRGB565, 0, 0, 512, 512, true);
			this.mHouseLinearTextureRegion = TextureRegionFactory.extractFromTexture(this.mTextureRGBA5551, 0, 0, 512, 512, true);
			this.mHouseMipMapsNearestTextureRegion = TextureRegionFactory.extractFromTexture(this.mTextureARGB4444, 0, 0, 512, 512, true);
			this.mHouseMipMapsLinearTextureRegion = TextureRegionFactory.extractFromTexture(this.mTextureRGBA888MipMaps, 0, 0, 512, 512, true);

			this.mEngine.getTextureManager().loadTextures(this.mTextureRGB565, this.mTextureRGBA5551, this.mTextureARGB4444, this.mTextureRGBA888MipMaps);
		} catch (final Throwable e) {
			Debug.e(e);
		}
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
		scene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));

		final int centerX = CAMERA_WIDTH / 2;
		final int centerY = CAMERA_HEIGHT / 2;

		final Entity container = new Entity(centerX, centerY);
		container.setScale(0.5f);

		container.attachChild(new Sprite(-512, -384, this.mHouseNearestTextureRegion));
		container.attachChild(new Sprite(0, -384, this.mHouseLinearTextureRegion));
		container.attachChild(new Sprite(-512, -128, this.mHouseMipMapsNearestTextureRegion));
		container.attachChild(new Sprite(0, -128, this.mHouseMipMapsLinearTextureRegion));

		scene.attachChild(container);

		scene.setOnSceneTouchListener(new IOnSceneTouchListener() {
			@Override
			public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
				if (pSceneTouchEvent.isActionDown() || pSceneTouchEvent.isActionMove()) {
					if(pSceneTouchEvent.getY() < CAMERA_HEIGHT / 2) {
						PVRTextureExample.this.mZoomState = ZoomState.IN;
					} else {
						PVRTextureExample.this.mZoomState = ZoomState.OUT;
					}
				} else {
					PVRTextureExample.this.mZoomState = ZoomState.NONE;
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
