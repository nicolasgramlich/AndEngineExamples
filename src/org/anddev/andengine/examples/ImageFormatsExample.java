package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.Texture.ITextureStateListener;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.source.ITextureSource;

import android.widget.Toast;

/**
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
	private Texture mTexture;
	private TextureRegion mPngTextureRegion;
	private TextureRegion mJpgTextureRegion;
	private TextureRegion mGifTextureRegion;
	private TextureRegion mBmpTextureRegion;

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
		this.mTexture = new Texture(128, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA, new ITextureStateListener.TextureStateAdapter() {
			@Override
			public void onTextureSourceLoadExeption(final Texture pTexture, final ITextureSource pTextureSource, final Throwable pThrowable) {
				ImageFormatsExample.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(ImageFormatsExample.this, "Failed loading TextureSource: " + pTextureSource.toString(), Toast.LENGTH_LONG).show();
					}
				});
			}
		});

		this.mPngTextureRegion = TextureRegionFactory.createFromAsset(this.mTexture, this, "gfx/imageformat_png.png", 0, 0);
		this.mJpgTextureRegion = TextureRegionFactory.createFromAsset(this.mTexture, this, "gfx/imageformat_jpg.jpg", 49, 0);
		this.mGifTextureRegion = TextureRegionFactory.createFromAsset(this.mTexture, this, "gfx/imageformat_gif.gif", 0, 49);
		this.mBmpTextureRegion = TextureRegionFactory.createFromAsset(this.mTexture, this, "gfx/imageformat_bmp.bmp", 49, 49);

		this.mEngine.getTextureManager().loadTexture(this.mTexture);
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene(1);
		scene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));

		/* Create the icons and add them to the scene. */
		final IEntity lastChild = scene.getLastChild();

		lastChild.attachChild(new Sprite(160 - 24, 106 - 24, this.mPngTextureRegion));
		lastChild.attachChild(new Sprite(160 - 24, 213 - 24, this.mJpgTextureRegion));
		lastChild.attachChild(new Sprite(320 - 24, 106 - 24, this.mGifTextureRegion));
		lastChild.attachChild(new Sprite(320 - 24, 213 - 24, this.mBmpTextureRegion));

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
