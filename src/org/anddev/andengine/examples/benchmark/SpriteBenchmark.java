package org.anddev.andengine.examples.benchmark;

import javax.microedition.khronos.opengles.GL11;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.ITextureRegion;

import android.opengl.GLES20;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 10:34:22 - 27.06.2010
 */
public class SpriteBenchmark extends BaseBenchmark {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	private static final int SPRITE_COUNT = 1000;

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mCamera;
	private BitmapTextureAtlas mBitmapTextureAtlas;
	private ITextureRegion mFaceTextureRegion;

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
		return SPRITEBENCHMARK_ID;
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
		final Scene scene = new Scene();
		scene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));

//		this.drawUsingSprites(scene);
//		this.drawUsingSpritesWithSharedVertexBuffer(scene);
		this.drawUsingSpriteBatch(scene);

		return scene;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void drawUsingSprites(final Scene scene) {
		for(int i = 0; i < SPRITE_COUNT; i++) {
			final Sprite face = new Sprite(this.mRandom.nextFloat() * (CAMERA_WIDTH - 32), this.mRandom.nextFloat() * (CAMERA_HEIGHT - 32), this.mFaceTextureRegion);
			face.setBlendFunction(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
			face.setIgnoreUpdate(true);
			scene.attachChild(face);
		}
	}

	private void drawUsingSpritesWithSharedVertexBuffer(final Scene scene) {
		/* As we are creating quite a lot of the same Sprites, we can let them share a VertexBuffer to significantly increase performance. */
		final RectangleVertexBuffer sharedVertexBuffer = new RectangleVertexBuffer(GL11.GL_STATIC_DRAW, true);
		sharedVertexBuffer.update(this.mFaceTextureRegion.getWidth(), this.mFaceTextureRegion.getHeight());
		
		for(int i = 0; i < SPRITE_COUNT; i++) {
			final Sprite face = new Sprite(this.mRandom.nextFloat() * (CAMERA_WIDTH - 32), this.mRandom.nextFloat() * (CAMERA_HEIGHT - 32), this.mFaceTextureRegion, sharedVertexBuffer);
			face.setBlendFunction(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
			face.setIgnoreUpdate(true);
			scene.attachChild(face);
		}
	}

	private void drawUsingSpriteBatch(final Scene scene) {
		final int width = this.mFaceTextureRegion.getWidth();
		final int height = this.mFaceTextureRegion.getHeight();

		final SpriteBatch spriteBatch = new SpriteBatch(this.mBitmapTextureAtlas, SPRITE_COUNT);
		spriteBatch.setBlendFunction(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		for(int i = 0; i < SPRITE_COUNT; i++) {
			final float x = this.mRandom.nextFloat() * (CAMERA_WIDTH - 32);
			final float y = this.mRandom.nextFloat() * (CAMERA_HEIGHT - 32);
			spriteBatch.draw(this.mFaceTextureRegion, x, y, width, height);
		}
		spriteBatch.submit();

		scene.attachChild(spriteBatch);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
