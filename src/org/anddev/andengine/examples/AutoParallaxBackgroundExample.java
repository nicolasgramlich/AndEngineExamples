package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.AutoParallaxBackground;
import org.anddev.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.ITextureRegion;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 19:58:39 - 19.07.2010
 */
public class AutoParallaxBackgroundExample extends BaseExample {
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
	private TiledTextureRegion mPlayerTextureRegion;
	private TiledTextureRegion mEnemyTextureRegion;

	private BitmapTextureAtlas mAutoParallaxBackgroundTexture;

	private ITextureRegion mParallaxLayerBack;
	private ITextureRegion mParallaxLayerMid;
	private ITextureRegion mParallaxLayerFront;

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
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(256, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mPlayerTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "gfx/player.png", 0, 0, 3, 4);
		this.mEnemyTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "gfx/enemy.png", 73, 0, 3, 4);

		this.mAutoParallaxBackgroundTexture = new BitmapTextureAtlas(1024, 1024, TextureOptions.DEFAULT);
		this.mParallaxLayerFront = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mAutoParallaxBackgroundTexture, this, "gfx/parallax_background_layer_front.png", 0, 0);
		this.mParallaxLayerBack = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mAutoParallaxBackgroundTexture, this, "gfx/parallax_background_layer_back.png", 0, 188);
		this.mParallaxLayerMid = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mAutoParallaxBackgroundTexture, this, "gfx/parallax_background_layer_mid.png", 0, 669);

		this.mEngine.getTextureManager().loadTextures(this.mBitmapTextureAtlas, this.mAutoParallaxBackgroundTexture);
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
		final AutoParallaxBackground autoParallaxBackground = new AutoParallaxBackground(0, 0, 0, 5);
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(0.0f, new Sprite(0, CAMERA_HEIGHT - this.mParallaxLayerBack.getHeight(), this.mParallaxLayerBack)));
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-5.0f, new Sprite(0, 80, this.mParallaxLayerMid)));
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-10.0f, new Sprite(0, CAMERA_HEIGHT - this.mParallaxLayerFront.getHeight(), this.mParallaxLayerFront)));
		scene.setBackground(autoParallaxBackground);

		/* Calculate the coordinates for the face, so its centered on the camera. */
		final int playerX = (CAMERA_WIDTH - this.mPlayerTextureRegion.getTileWidth()) / 2;
		final int playerY = CAMERA_HEIGHT - this.mPlayerTextureRegion.getTileHeight() - 5;

		/* Create two sprits and add it to the scene. */
		final AnimatedSprite player = new AnimatedSprite(playerX, playerY, this.mPlayerTextureRegion);
		player.setScaleCenterY(this.mPlayerTextureRegion.getTileHeight());
		player.setScale(2);
		player.animate(new long[]{200, 200, 200}, 3, 5, true);

		final AnimatedSprite enemy = new AnimatedSprite(playerX - 80, playerY, this.mEnemyTextureRegion);
		enemy.setScaleCenterY(this.mEnemyTextureRegion.getTileHeight());
		enemy.setScale(2);
		enemy.animate(new long[]{200, 200, 200}, 3, 5, true);

		scene.attachChild(player);
		scene.attachChild(enemy);

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
