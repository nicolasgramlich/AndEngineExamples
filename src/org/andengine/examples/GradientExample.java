package org.andengine.examples;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.primitive.Gradient;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ClickDetector;
import org.andengine.input.touch.detector.ClickDetector.IClickDetectorListener;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.color.Color;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 11:54:51 - 03.04.2010
 */
public class GradientExample extends SimpleBaseGameActivity implements IOnSceneTouchListener, IClickDetectorListener {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	// ===========================================================
	// Fields
	// ===========================================================

	private Gradient mGradient;
	private ClickDetector mClickDetector;

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
	public EngineOptions onCreateEngineOptions() {
		final Camera camera = new Camera(0, 0, GradientExample.CAMERA_WIDTH, GradientExample.CAMERA_HEIGHT);
		camera.setCenter(0, 0);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, new RatioResolutionPolicy(GradientExample.CAMERA_WIDTH, GradientExample.CAMERA_HEIGHT), camera);
	}

	@Override
	public void onCreateResources() {

	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
		scene.setOnSceneTouchListener(this);
		this.mClickDetector = new ClickDetector(this);

		/* No need for a background color, since our gradient is fullscreen. */
		scene.getBackground().setColorEnabled(false);

		this.mGradient = new Gradient(0, 0, GradientExample.CAMERA_WIDTH, GradientExample.CAMERA_HEIGHT, this.getVertexBufferObjectManager());
		this.mGradient.setGradient(Color.RED, Color.BLUE, 1, 0);
		this.mGradient.setGradientFitToBounds(true);
		this.mGradient.setGradientDitherEnabled(true);

		scene.attachChild(this.mGradient);

		return scene;
	}

	@Override
	public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
		this.mClickDetector.onSceneTouchEvent(pScene, pSceneTouchEvent);

		this.mGradient.setGradientVector(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());

		return true;
	}

	@Override
	public void onClick(final ClickDetector pClickDetector, final int pPointerID, final float pSceneX, final float pSceneY) {
		this.mGradient.setGradientFitToBounds(!this.mGradient.isGradientFitToBounds());

		this.toastOnUiThread("Gradient fit to bounds: '" + this.mGradient.isGradientFitToBounds() + "'.");
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
