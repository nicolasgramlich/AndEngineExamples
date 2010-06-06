package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.FPSCounter;
import org.anddev.andengine.entity.Scene;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

/**
 * @author Nicolas Gramlich
 * @since 11:54:51 - 03.04.2010
 */
public class AnimatedSpritesExample extends BaseExampleGameActivity {
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
	private TiledTextureRegion mFaceTextureRegion;
	private TiledTextureRegion mHelicopterTextureRegion;
	private TiledTextureRegion mBananaTextureRegion;

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
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera, false));
	}

	@Override
	public void onLoadResources() {
		this.mTexture = new Texture(256, 128);

		this.mHelicopterTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mTexture, this, "gfx/helicopter_tiled.png", 0, 0, 2, 2);	
		this.mBananaTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mTexture, this, "gfx/banana_tiled.png", 96, 0, 4, 2);	
		this.mFaceTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mTexture, this, "gfx/boxface_tiled.png", 96, 70, 2, 1);
		
		this.getEngine().getTextureManager().loadTexture(this.mTexture);
	}

	@Override
	public Scene onLoadScene() {
		this.getEngine().registerPreFrameHandler(new FPSCounter());
		
		final Scene scene = new Scene(1);
		scene.setBackgroundColor(0.09804f, 0.6274f, 0.8784f);

		/* Quickly twinkling face. */
		final AnimatedSprite face = new AnimatedSprite(150, 150, this.mFaceTextureRegion);
		face.animate(100);
		scene.getTopLayer().addEntity(face);

		/* Continuously flying helicopter. */
		final AnimatedSprite heli = new AnimatedSprite(550, 150, this.mHelicopterTextureRegion);
		heli.animate(new long[]{100, 100}, 1, 2, true);
		scene.getTopLayer().addEntity(heli);
		
		/* Continuously flying helicopter. */
		final AnimatedSprite heli2 = new AnimatedSprite(550, 300, this.mHelicopterTextureRegion.clone());
		heli2.animate(100);
		scene.getTopLayer().addEntity(heli2);
		
		/* Funny banana. */
		final AnimatedSprite banana = new AnimatedSprite(150, 300, this.mBananaTextureRegion);
		banana.animate(100);
		scene.getTopLayer().addEntity(banana);
		
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
