package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.sprite.batch.SpriteBatch;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

/**
 * @author Nicolas Gramlich
 * @since 14:27:22 - 14.06.2011
 */
public class SpriteBatchExample extends BaseExample {
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
	private TextureRegion mFaceTextureRegion;

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
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(32, 32, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "gfx/face_box.png", 0, 0);

		this.mEngine.getTextureManager().loadTexture(this.mBitmapTextureAtlas);
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
		scene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));

		/* Calculate the coordinates for the face, so its centered on the camera. */
		final int centerX = (CAMERA_WIDTH - this.mFaceTextureRegion.getWidth()) / 2;
		final int centerY = (CAMERA_HEIGHT - this.mFaceTextureRegion.getHeight()) / 2;
		
		final Sprite faceSprite1 = new Sprite(-50, 0, this.mFaceTextureRegion); 
		final Sprite faceSprite2 = new Sprite(50, 0, this.mFaceTextureRegion);
		
		faceSprite1.setScale(2);
		faceSprite2.setRotation(45);

		/* Create the face and add it to the scene. */
		final SpriteBatch dynamicSpriteBatch = new SpriteBatch(this.mBitmapTextureAtlas, 2) {
			@Override
			public void onDrawSpriteBatch() {
				this.draw(faceSprite1);
				this.draw(faceSprite2);
			}
		};
		
		final SpriteBatch staticSpriteBatch = new SpriteBatch(this.mBitmapTextureAtlas, 2);
		staticSpriteBatch.draw(this.mFaceTextureRegion, -50, 0, this.mFaceTextureRegion.getWidth(), this.mFaceTextureRegion.getHeight(), 2, 2);
		staticSpriteBatch.draw(this.mFaceTextureRegion, 50, 0, this.mFaceTextureRegion.getWidth(), this.mFaceTextureRegion.getHeight(), 45);
		staticSpriteBatch.submit();

		dynamicSpriteBatch.setPosition(centerX, centerY - 50);
		staticSpriteBatch.setPosition(centerX, centerY + 50);
		
		scene.attachChild(dynamicSpriteBatch);
		scene.attachChild(staticSpriteBatch);

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
