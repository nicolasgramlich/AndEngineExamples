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
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;

/**
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

	private Texture mTexture;
	private Texture mTextureBilinear;
	private Texture mTextureRepeating;

	private TextureRegion mFaceTextureRegion;
	private TextureRegion mFaceTextureRegionBilinear;
	private TextureRegion mFaceTextureRegionRepeating;

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
		TextureRegionFactory.setAssetBasePath("gfx/");
		this.mTexture = new Texture(32, 32, TextureOptions.DEFAULT);
		this.mFaceTextureRegion = TextureRegionFactory.createFromAsset(this.mTexture, this, "face_box.png", 0, 0);

		this.mTextureBilinear = new Texture(32, 32, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mFaceTextureRegionBilinear = TextureRegionFactory.createFromAsset(this.mTextureBilinear, this, "face_box.png", 0, 0);

		this.mTextureRepeating = new Texture(32, 32, TextureOptions.REPEATING_PREMULTIPLYALPHA);
		this.mFaceTextureRegionRepeating = TextureRegionFactory.createFromAsset(this.mTextureRepeating, this, "face_box.png", 0, 0);
		/* The following statement causes the Texture to be printed horizontally 10x on any Sprite that uses it.
		 * So we will later increase the width of such a sprite by the same factor to avoid distortion. */
		this.mFaceTextureRegionRepeating.setWidth(10 * this.mFaceTextureRegionRepeating.getWidth());

		this.mEngine.getTextureManager().loadTextures(this.mTexture, this.mTextureBilinear, this.mTextureRepeating);
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene(1);
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

		final IEntity lastChild = scene.getLastChild();
		lastChild.attachChild(face);
		lastChild.attachChild(faceBilinear);
		lastChild.attachChild(faceRepeating);

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
