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
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.ITextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.ITextureAtlas.ITextureAtlasStateListener;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.anddev.andengine.opengl.texture.region.ITextureRegion;

import android.widget.Toast;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
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
	private BitmapTextureAtlas mBitmapTextureAtlas;
	private ITextureRegion mPNGTextureRegion;
	private ITextureRegion mJPGTextureRegion;
	private ITextureRegion mGIFTextureRegion;
	private ITextureRegion mBMPTextureRegion;

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
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(128, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA, new ITextureAtlasStateListener.TextureAtlasStateAdapter<IBitmapTextureAtlasSource>() {
			@Override
			public void onTextureAtlasSourceLoadExeption(final ITextureAtlas<IBitmapTextureAtlasSource> pTextureAtlas, final IBitmapTextureAtlasSource pBitmapTextureAtlasSource, final Throwable pThrowable) {
				ImageFormatsExample.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(ImageFormatsExample.this, "Failed loading TextureSource: " + pBitmapTextureAtlasSource.toString(), Toast.LENGTH_LONG).show();
					}
				});
			}
		});

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		this.mPNGTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "imageformat_png.png", 0, 0);
		this.mJPGTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "imageformat_jpg.jpg", 49, 0);
		this.mGIFTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "imageformat_gif.gif", 0, 49);
		this.mBMPTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "imageformat_bmp.bmp", 49, 49);

		this.mEngine.getTextureManager().loadTexture(this.mBitmapTextureAtlas);
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
