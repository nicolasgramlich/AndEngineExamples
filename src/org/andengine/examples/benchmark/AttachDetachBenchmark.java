package org.andengine.examples.benchmark;

import java.io.IOException;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.batch.SpriteGroup;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.bitmap.AssetBitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.opengl.GLES20;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 20:24:17 - 27.06.2010
 */
public class AttachDetachBenchmark extends BaseBenchmark {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	private static final int SPRITE_COUNT = 2000;
	private static final float ATTACH_DETACH_DELAY = 0.005f;
	
	// ===========================================================
	// Fields
	// ===========================================================

	private ITexture mFaceTexture;
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
		return ENTITYMODIFIERBENCHMARK_ID;
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
	public EngineOptions onCreateEngineOptions() {
		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
	}

	@Override
	public void onCreateResources() throws IOException {
		this.mFaceTexture = new AssetBitmapTexture(this.getTextureManager(), this.getAssets(), "gfx/face_box.png", TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mFaceTextureRegion = TextureRegionFactory.extractFromTexture(this.mFaceTexture);
		this.mFaceTexture.load();
	}

	@Override
	public Scene onCreateScene() {
		final Scene scene = new Scene();
		scene.getBackground().setColor(0.09804f, 0.6274f, 0.8784f);

//		this.drawUsingSprites(scene);
//		this.drawUsingSpritesWithSharedVertexBuffer(scene);
		this.drawUsingSpriteBatch(scene);

		return scene;
	}

	// ===========================================================
	// Methods
	// ===========================================================

//	private void drawUsingSprites(final Scene pScene) {
//		final VertexBufferObjectManager vertexBufferObjectManager = this.getVertexBufferObjectManager();
//		for(int i = 0; i < SPRITE_COUNT; i++) {
//			final Sprite face = new Sprite((CAMERA_WIDTH - 32) * this.mRandom.nextFloat(), (CAMERA_HEIGHT - 32) * this.mRandom.nextFloat(), this.mFaceTextureRegion, vertexBufferObjectManager);
//			face.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
//
//			pScene.attachChild(face);
//		}
//	}

//	private void drawUsingSpritesWithSharedVertexBuffer(final Scene pScene) {
//		/* As we are creating quite a lot of the same Sprites, we can let them share a VertexBuffer to significantly increase performance. */
//		final ISpriteVertexBufferObject sharedVertexBuffer = new Sprite.LowMemorySpriteVertexBufferObject(this.getVertexBufferObjectManager(), Sprite.SPRITE_SIZE, DrawType.STATIC, true, Sprite.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT);
//
//		for(int i = 0; i < SPRITE_COUNT; i++) {
//			final Sprite face = new Sprite((CAMERA_WIDTH - 32) * this.mRandom.nextFloat(), (CAMERA_HEIGHT - 32) * this.mRandom.nextFloat(), this.mFaceTextureRegion, sharedVertexBuffer);
//			face.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
//
//			pScene.attachChild(face);
//		}
//	}

	private void drawUsingSpriteBatch(final Scene pScene) {
		final VertexBufferObjectManager vertexBufferObjectManager = this.getVertexBufferObjectManager();
		final SpriteGroup spriteGroup = new SpriteGroup(this.mFaceTexture, AttachDetachBenchmark.SPRITE_COUNT, vertexBufferObjectManager);
		spriteGroup.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		for(int i = 0; i < AttachDetachBenchmark.SPRITE_COUNT; i++) {
			final Sprite face = new Sprite((AttachDetachBenchmark.CAMERA_WIDTH - 32) * this.mRandom.nextFloat(), (AttachDetachBenchmark.CAMERA_HEIGHT - 32) * this.mRandom.nextFloat(), this.mFaceTextureRegion, vertexBufferObjectManager);
			spriteGroup.attachChild(face);
		}
		pScene.registerUpdateHandler(new TimerHandler(ATTACH_DETACH_DELAY, true, new ITimerCallback() {
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {
				final int randomChildIndex = AttachDetachBenchmark.this.mRandom.nextInt(spriteGroup.getChildCount());
				final Sprite child = (Sprite)spriteGroup.getChildByIndex(randomChildIndex);
				child.detachSelf();
				spriteGroup.attachChild(child);
			}
		}));

		pScene.attachChild(spriteGroup);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
