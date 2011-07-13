package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.texture.BaseTexture.ITextureStateListener;
import org.anddev.andengine.opengl.texture.ITexture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.bitmap.BitmapTexture;
import org.anddev.andengine.opengl.texture.bitmap.BitmapTextureRegionFactory;
import org.anddev.andengine.opengl.texture.bitmap.source.IBitmapTextureSource;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

import android.widget.Toast;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 11:54:51 - 03.04.2010
 */
public class ImageFormatsExample extends BaseExample {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 480;
	private static final int CAMERA_HEIGHT = 320;

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mCamera;
	private BitmapTexture mBitmapTexture;
	private TextureRegion mPNGTextureRegion;
	private TextureRegion mJPGTextureRegion;
	private TextureRegion mGIFTextureRegion;
	private TextureRegion mBMPTextureRegion;

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
		Toast.makeText(this, "GIF is not supported yet. Use PNG instead, it's the better format anyway!", Toast.LENGTH_LONG).show();
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera));
	}

	@Override
	public void onLoadResources() {
		BitmapTextureRegionFactory.setAssetBasePath("gfx/");

		this.mBitmapTexture = new BitmapTexture(128, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA, new ITextureStateListener.TextureStateAdapter<IBitmapTextureSource>() {
			@Override
			public void onTextureSourceLoadExeption(final ITexture<IBitmapTextureSource> pTexture, final IBitmapTextureSource pBitmapTextureSource, final Throwable pThrowable) {
				ImageFormatsExample.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(ImageFormatsExample.this, "Failed loading BitmapTextureSource: " + pBitmapTextureSource.toString(), Toast.LENGTH_LONG).show();
					}
				});
			}
		});

		this.mPNGTextureRegion = BitmapTextureRegionFactory.createFromAsset(this.mBitmapTexture, this, "imageformat_png.png", 0, 0);
		this.mJPGTextureRegion = BitmapTextureRegionFactory.createFromAsset(this.mBitmapTexture, this, "imageformat_jpg.jpg", 49, 0);
		this.mGIFTextureRegion = BitmapTextureRegionFactory.createFromAsset(this.mBitmapTexture, this, "imageformat_gif.gif", 0, 49);
		this.mBMPTextureRegion = BitmapTextureRegionFactory.createFromAsset(this.mBitmapTexture, this, "imageformat_bmp.bmp", 49, 49);

		this.mEngine.getTextureManager().loadTexture(this.mBitmapTexture);
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
		scene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));

		/* Create the icons and add them to the scene. */
		scene.attachChild(new Sprite(160 - 24, 106 - 24, this.mPNGTextureRegion));
		scene.attachChild(new Sprite(160 - 24, 213 - 24, this.mJPGTextureRegion));
		scene.attachChild(new Sprite(320 - 24, 106 - 24, this.mGIFTextureRegion));
		scene.attachChild(new Sprite(320 - 24, 213 - 24, this.mBMPTextureRegion));

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
