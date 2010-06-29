package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.Layer;
import org.anddev.andengine.entity.Scene;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;

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
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera, false));
	}

	@Override
	public void onLoadResources() {
		this.mTexture = new Texture(128, 128, TextureOptions.BILINEAR);
		this.mPngTextureRegion = TextureRegionFactory.createFromAsset(this.mTexture, this, "gfx/imageformat_png.png", 0, 0);
		this.mJpgTextureRegion = TextureRegionFactory.createFromAsset(this.mTexture, this, "gfx/imageformat_jpg.jpg", 49, 0);
		/* GIF is not supported yet. Use PNG instead, it's the better format anyway! */
		this.mGifTextureRegion = TextureRegionFactory.createFromAsset(this.mTexture, this, "gfx/imageformat_gif.png", 0, 49);
		this.mBmpTextureRegion = TextureRegionFactory.createFromAsset(this.mTexture, this, "gfx/imageformat_bmp.bmp", 49, 49);

		this.getEngine().getTextureManager().loadTexture(this.mTexture);
	}

	@Override
	public Scene onLoadScene() {
		this.getEngine().registerPreFrameHandler(new FPSLogger());

		final Scene scene = new Scene(1);
		scene.setBackgroundColor(0.09804f, 0.6274f, 0.8784f);
		
		/* Create the icons and add them to the scene. */
		final Layer topLayer = scene.getTopLayer();
		
		topLayer.addEntity(new Sprite(160 - 24, 106 - 24, this.mPngTextureRegion));
		topLayer.addEntity(new Sprite(160 - 24, 213 - 24, this.mJpgTextureRegion));
		topLayer.addEntity(new Sprite(320 - 24, 106 - 24, this.mGifTextureRegion));
		topLayer.addEntity(new Sprite(320 - 24, 213 - 24, this.mBmpTextureRegion));

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
