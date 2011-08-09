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
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.ITextureRegion;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 11:54:51 - 03.04.2010
 */
public class TextureOptionsExample extends BaseExample {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mCamera;

	private BitmapTextureAtlas mBitmapTextureAtlas;
	private BitmapTextureAtlas mBitmapTextureAtlasBilinear;
	private BitmapTextureAtlas mBitmapTextureAtlasRepeating;

	private ITextureRegion mFaceTextureRegion;
	private ITextureRegion mFaceTextureRegionBilinear;
	private ITextureRegion mFaceTextureRegionRepeating;

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
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(32, 32, TextureOptions.DEFAULT);
		this.mFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "face_box.png", 0, 0);

		this.mBitmapTextureAtlasBilinear = new BitmapTextureAtlas(32, 32, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mFaceTextureRegionBilinear = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlasBilinear, this, "face_box.png", 0, 0);

		this.mBitmapTextureAtlasRepeating = new BitmapTextureAtlas(32, 32, TextureOptions.REPEATING_NEAREST_PREMULTIPLYALPHA);
		this.mFaceTextureRegionRepeating = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlasRepeating, this, "face_box.png", 0, 0);
		/* The following statement causes the BitmapTextureAtlas to be printed horizontally 10x on any Sprite that uses it.
		 * So we will later increase the width of such a sprite by the same factor to avoid distortion. */
		this.mFaceTextureRegionRepeating.setWidth(10 * this.mFaceTextureRegionRepeating.getWidth());

		this.mEngine.getTextureManager().loadTextures(this.mBitmapTextureAtlas, this.mBitmapTextureAtlasBilinear, this.mBitmapTextureAtlasRepeating);
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
		scene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));

		final int centerX = (CAMERA_WIDTH - this.mFaceTextureRegion.getWidth()) / 2;
		final int centerY = (CAMERA_HEIGHT - this.mFaceTextureRegion.getHeight()) / 2;

		final Sprite face = new Sprite(centerX - 160, centerY - 40, this.mFaceTextureRegion);
		face.setScale(4);

		final Sprite faceBilinear = new Sprite(centerX + 160, centerY - 40, this.mFaceTextureRegionBilinear);
		faceBilinear.setScale(4);

		/* Make sure sprite has the same size as mTextureRegionRepeating.
		 * Giving the sprite twice the height shows you'd also have to change the height of the TextureRegion! */
		final Sprite faceRepeating = new Sprite(centerX - 160, centerY + 100, this.mFaceTextureRegionRepeating.getWidth(), this.mFaceTextureRegionRepeating.getHeight() * 2, this.mFaceTextureRegionRepeating);

		scene.attachChild(face);
		scene.attachChild(faceBilinear);
		scene.attachChild(faceRepeating);

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
