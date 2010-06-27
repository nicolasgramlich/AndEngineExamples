package org.anddev.andengine.examples.benchmark;

import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.Scene;
import org.anddev.andengine.entity.primitives.Rectangle;
import org.anddev.andengine.entity.shape.modifier.AlphaModifier;
import org.anddev.andengine.entity.shape.modifier.DelayModifier;
import org.anddev.andengine.entity.shape.modifier.ParallelModifier;
import org.anddev.andengine.entity.shape.modifier.RotateByModifier;
import org.anddev.andengine.entity.shape.modifier.RotateModifier;
import org.anddev.andengine.entity.shape.modifier.ScaleModifier;
import org.anddev.andengine.entity.shape.modifier.SequenceModifier;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;

/**
 * @author Nicolas Gramlich
 * @since 20:24:17 - 27.06.2010
 */
public class ShapeModifierBenchmark extends BaseBenchmark {
	// ===========================================================
	// Constants
	// ===========================================================
	
	/* Initializing the Random generator produces a comparable result over different versions. */
	private static final long RANDOM_SEED = 1234567890;

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;
	
	private static final int SPRITE_COUNT = 100;

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
	protected float getBenchmarkDuration() {
		return 10;
	}

	@Override
	protected float getBenchmarkStartOffset() {
		return 2;
	}

	@Override
	public Engine onLoadEngine() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera, false));
	}

	@Override
	public void onLoadResources() {
		this.mTexture = new Texture(64, 32, TextureOptions.BILINEAR);
		this.mFaceTextureRegion = TextureRegionFactory.createFromAsset(this.mTexture, this, "gfx/boxface.png", 0, 0);

		this.getEngine().getTextureManager().loadTexture(this.mTexture);
	}

	@Override
	public Scene onLoadScene() {
		this.getEngine().registerPreFrameHandler(new FPSLogger());

		final Scene scene = new Scene(1);
		scene.setBackgroundColor(0.09804f, 0.6274f, 0.8784f);

		final SequenceModifier shapeModifier = new SequenceModifier(
			new RotateByModifier(2, 90),
			new AlphaModifier(1.5f, 1, 0),
			new AlphaModifier(1.5f, 0, 1),
			new ScaleModifier(2.5f, 1, 0.5f),
			new DelayModifier(0.5f),
			new ParallelModifier(
					new ScaleModifier(2f, 0.5f, 5),
					new RotateByModifier(2, 90)
			),
			new ParallelModifier(
					new ScaleModifier(2f, 5, 1),
					new RotateModifier(2f, 180, 0)
			)
		);

		final Random random = new Random(RANDOM_SEED);

		for(int i = 0; i < SPRITE_COUNT; i++) {
			final Rectangle rect = new Rectangle((CAMERA_WIDTH - 32) * random.nextFloat(), (CAMERA_HEIGHT - 32) * random.nextFloat(), 32, 32);
			rect.setColor(1, 0, 0);
			rect.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
	
			final Sprite face = new Sprite((CAMERA_WIDTH - 32) * random.nextFloat(), (CAMERA_HEIGHT - 32) * random.nextFloat(), this.mFaceTextureRegion);
	
			face.addShapeModifier(shapeModifier.clone());
			rect.addShapeModifier(shapeModifier.clone());
	
			scene.getTopLayer().addEntity(face);
			scene.getTopLayer().addEntity(rect);
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
