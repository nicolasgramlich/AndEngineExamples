package org.anddev.andengine.examples.benchmark;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.modifier.AlphaModifier;
import org.anddev.andengine.entity.modifier.DelayModifier;
import org.anddev.andengine.entity.modifier.ParallelEntityModifier;
import org.anddev.andengine.entity.modifier.RotationByModifier;
import org.anddev.andengine.entity.modifier.RotationModifier;
import org.anddev.andengine.entity.modifier.ScaleModifier;
import org.anddev.andengine.entity.modifier.SequenceEntityModifier;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.vertex.RectangleVertexBuffer;

/**
 * @author Nicolas Gramlich
 * @since 20:24:17 - 27.06.2010
 */
public class EntityModifierBenchmark extends BaseBenchmark {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	private static final int SPRITE_COUNT = 200;

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mCamera;
	private Texture mTexture;
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
	public Engine onLoadEngine() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera));
	}

	@Override
	public void onLoadResources() {
		this.mTexture = new Texture(32, 32, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mFaceTextureRegion = TextureRegionFactory.createFromAsset(this.mTexture, this, "gfx/face_box.png", 0, 0);

		this.mEngine.getTextureManager().loadTexture(this.mTexture);
	}

	@Override
	public Scene onLoadScene() {
		final Scene scene = new Scene(1);
		scene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));

		final SequenceEntityModifier EntityModifier = new SequenceEntityModifier(
				new RotationByModifier(2, 90),
				new AlphaModifier(1.5f, 1, 0),
				new AlphaModifier(1.5f, 0, 1),
				new ScaleModifier(2.5f, 1, 0.5f),
				new DelayModifier(0.5f),
				new ParallelEntityModifier(
					new ScaleModifier(2f, 0.5f, 5),
					new RotationByModifier(2, 90)
				),
				new ParallelEntityModifier(
					new ScaleModifier(2f, 5, 1),
					new RotationModifier(2f, 180, 0)
				)
		);

		/* As we are creating quite a lot of the same Sprites, we can let them share a VertexBuffer to significantly increase performance. */
		final RectangleVertexBuffer sharedVertexBuffer = new RectangleVertexBuffer(GL11.GL_DYNAMIC_DRAW);
		sharedVertexBuffer.update(this.mFaceTextureRegion.getWidth(), this.mFaceTextureRegion.getHeight());

		for(int i = 0; i < SPRITE_COUNT; i++) {
			final Rectangle rect = new Rectangle((CAMERA_WIDTH - 32) * this.mRandom.nextFloat(), (CAMERA_HEIGHT - 32) * this.mRandom.nextFloat(), 32, 32, sharedVertexBuffer);
			rect.setColor(1, 0, 0);
			rect.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

			final Sprite face = new Sprite((CAMERA_WIDTH - 32) * this.mRandom.nextFloat(), (CAMERA_HEIGHT - 32) * this.mRandom.nextFloat(), this.mFaceTextureRegion, sharedVertexBuffer);

			face.registerEntityModifier(EntityModifier.clone());
			rect.registerEntityModifier(EntityModifier.clone());

			scene.getLastChild().attachChild(face);
			scene.getLastChild().attachChild(rect);
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
