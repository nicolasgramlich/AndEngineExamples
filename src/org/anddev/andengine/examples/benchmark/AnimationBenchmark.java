package org.anddev.andengine.examples.benchmark;

import javax.microedition.khronos.opengles.GL11;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.opengl.vertex.RectangleVertexBuffer;

/**
 * @author Nicolas Gramlich
 * @since 11:28:45 - 28.06.2010
 */
public class AnimationBenchmark extends BaseBenchmark {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	private static final int SPRITE_COUNT = 150;

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mCamera;

	private Texture mTexture;

	private TiledTextureRegion mSnapdragonTextureRegion;
	private TiledTextureRegion mHelicopterTextureRegion;
	private TiledTextureRegion mBananaTextureRegion;
	private TiledTextureRegion mFaceTextureRegion;

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
	protected int getBenchmarkID() {
		return ANIMATIONBENCHMARK_ID;
	}

	@Override
	protected float getBenchmarkStartOffset() {
		return 2;
	}

	@Override
	protected float getBenchmarkDuration() {
		return 10;
	}

	@Override
	public Engine onLoadEngine() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		final EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera);
		engineOptions.getRenderOptions().disableExtensionVertexBufferObjects();
		return new Engine(engineOptions);
	}

	@Override
	public void onLoadResources() {
		this.mTexture = new Texture(512, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		TextureRegionFactory.setAssetBasePath("gfx/");
		this.mSnapdragonTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mTexture, this, "snapdragon_tiled.png", 0, 0, 4, 3);
		this.mHelicopterTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mTexture, this, "helicopter_tiled.png", 400, 0, 2, 2);
		this.mBananaTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mTexture, this, "banana_tiled.png", 0, 180, 4, 2);
		this.mFaceTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mTexture, this, "face_box_tiled.png", 132, 180, 2, 1);

		this.mEngine.getTextureManager().loadTexture(this.mTexture);
	}

	@Override
	public Scene onLoadScene() {
		final Scene scene = new Scene(1);
		scene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));

		/* As we are creating quite a lot of the same Sprites, we can let them share a VertexBuffer to significantly increase performance. */
		final RectangleVertexBuffer faceSharedVertexBuffer = new RectangleVertexBuffer(GL11.GL_DYNAMIC_DRAW);
		faceSharedVertexBuffer.update(this.mFaceTextureRegion.getTileWidth(), this.mFaceTextureRegion.getTileHeight());

		final RectangleVertexBuffer helicopterSharedVertexBuffer = new RectangleVertexBuffer(GL11.GL_DYNAMIC_DRAW);
		helicopterSharedVertexBuffer.update(this.mHelicopterTextureRegion.getTileWidth(), this.mHelicopterTextureRegion.getTileHeight());

		final RectangleVertexBuffer snapdragonSharedVertexBuffer = new RectangleVertexBuffer(GL11.GL_DYNAMIC_DRAW);
		snapdragonSharedVertexBuffer.update(this.mSnapdragonTextureRegion.getTileWidth(), this.mSnapdragonTextureRegion.getTileHeight());

		final RectangleVertexBuffer bananaSharedVertexBuffer = new RectangleVertexBuffer(GL11.GL_DYNAMIC_DRAW);
		bananaSharedVertexBuffer.update(this.mBananaTextureRegion.getTileWidth(), this.mBananaTextureRegion.getTileHeight());

		for(int i = 0; i < SPRITE_COUNT; i++) {
			/* Quickly twinkling face. */
			final AnimatedSprite face = new AnimatedSprite(this.mRandom.nextFloat() * (CAMERA_WIDTH - 32), this.mRandom.nextFloat() * (CAMERA_HEIGHT - 32), this.mFaceTextureRegion.clone(), faceSharedVertexBuffer);
			face.animate(50 + this.mRandom.nextInt(100));
			scene.getLastChild().attachChild(face);

			/* Continuously flying helicopter. */
			final AnimatedSprite helicopter = new AnimatedSprite(this.mRandom.nextFloat() * (CAMERA_WIDTH - 48), this.mRandom.nextFloat() * (CAMERA_HEIGHT - 48), this.mHelicopterTextureRegion.clone(), helicopterSharedVertexBuffer);
			helicopter.animate(new long[] { 50 + this.mRandom.nextInt(100), 50 + this.mRandom.nextInt(100) }, 1, 2, true);
			scene.getLastChild().attachChild(helicopter);

			/* Snapdragon. */
			final AnimatedSprite snapdragon = new AnimatedSprite(this.mRandom.nextFloat() * (CAMERA_WIDTH - 100), this.mRandom.nextFloat() * (CAMERA_HEIGHT - 60), this.mSnapdragonTextureRegion.clone(), snapdragonSharedVertexBuffer);
			snapdragon.animate(50 + this.mRandom.nextInt(100));
			scene.getLastChild().attachChild(snapdragon);

			/* Funny banana. */
			final AnimatedSprite banana = new AnimatedSprite(this.mRandom.nextFloat() * (CAMERA_WIDTH - 32), this.mRandom.nextFloat() * (CAMERA_HEIGHT - 32), this.mBananaTextureRegion.clone(), bananaSharedVertexBuffer);
			banana.animate(50 + this.mRandom.nextInt(100));
			scene.getLastChild().attachChild(banana);
		}

		return scene;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
