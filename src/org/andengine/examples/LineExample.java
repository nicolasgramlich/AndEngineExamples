package org.andengine.examples;

import java.util.Random;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.util.FPSLogger;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 11:54:51 - 03.04.2010
 */
public class LineExample extends BaseExample {
	// ===========================================================
	// Constants
	// ===========================================================

	/* Initializing the Random generator produces a comparable result over different versions. */
	private static final long RANDOM_SEED = 1234567890;

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	private static final int LINE_COUNT = 100;

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mCamera;

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

	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
		scene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));

		final Random random = new Random(RANDOM_SEED);

		for(int i = 0; i < LINE_COUNT; i++) {
			final float x1 = random.nextFloat() * CAMERA_WIDTH;
			final float x2 = random.nextFloat() * CAMERA_WIDTH;
			final float y1 = random.nextFloat() * CAMERA_HEIGHT;
			final float y2 = random.nextFloat() * CAMERA_HEIGHT;
			final float lineWidth = random.nextFloat() * 5;

			final Line line = new Line(x1, y1, x2, y2, lineWidth);

			line.setColor(random.nextFloat(), random.nextFloat(), random.nextFloat());

			scene.attachChild(line);
		}

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
