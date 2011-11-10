package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.modifier.LoopEntityModifier;
import org.anddev.andengine.entity.modifier.RotationModifier;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.Background;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.bitmap.BitmapTexture.BitmapTextureFormat;
import org.anddev.andengine.opengl.texture.region.ITextureRegion;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 11:54:51 - 03.04.2010
 */
public class SpriteExample extends BaseExample {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mCamera;

	private ITextureRegion[] mFaceTextureRegion = new ITextureRegion[3];

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
		this.mCamera = new Camera(0, 0, SpriteExample.CAMERA_WIDTH, SpriteExample.CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(SpriteExample.CAMERA_WIDTH, SpriteExample.CAMERA_HEIGHT), this.mCamera));
	}

	@Override
	public void onLoadResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		BitmapTextureAtlas bitmapTextureAtlas;
		
		bitmapTextureAtlas = new BitmapTextureAtlas(256, 256, BitmapTextureFormat.RGBA_8888);
		this.mFaceTextureRegion[0] = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bitmapTextureAtlas, this, "player.png", 0, 0);
		bitmapTextureAtlas.load();
		
		bitmapTextureAtlas = new BitmapTextureAtlas(256, 256, BitmapTextureFormat.RGBA_4444);
		this.mFaceTextureRegion[1] = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bitmapTextureAtlas, this, "player.png", 0, 0);
		bitmapTextureAtlas.load();
		
		bitmapTextureAtlas = new BitmapTextureAtlas(256, 256, BitmapTextureFormat.RGB_565);
		this.mFaceTextureRegion[2] = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bitmapTextureAtlas, this, "player.png", 0, 0);
		bitmapTextureAtlas.load();
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
		scene.setBackground(new Background(1, 1, 1));

		/* Calculate the coordinates for the face, so its centered on the camera. */
		/* Create the face and add it to the scene. */
		scene.attachChild(new Sprite(100, 100, this.mFaceTextureRegion[0]));
		scene.attachChild(new Sprite(300, 100, this.mFaceTextureRegion[1]));
		scene.attachChild(new Sprite(500, 100, this.mFaceTextureRegion[2]));

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
