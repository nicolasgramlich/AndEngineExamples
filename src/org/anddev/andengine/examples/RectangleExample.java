package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.Scene;
import org.anddev.andengine.entity.primitives.Rectangle;
import org.anddev.andengine.entity.util.FPSLogger;

/**
 * @author Nicolas Gramlich
 * @since 11:54:51 - 03.04.2010
 */
public class RectangleExample extends BaseExample {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

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
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera, false));
	}

	@Override
	public void onLoadResources() {

	}

	@Override
	public Scene onLoadScene() {
		this.getEngine().registerPreFrameHandler(new FPSLogger());

		final Scene scene = new Scene(1);
		scene.setBackgroundColor(0, 0, 0);
		//		scene.setBackgroundColor(0.09804f, 0.6274f, 0.8784f);

		final Rectangle rect1 = new Rectangle(180, 60, 180, 180);
		rect1.setColor(1, 0, 0);
		final Rectangle rect2 = new Rectangle(360, 60, 180, 180);
		rect2.setColor(0, 1, 0);
		final Rectangle rect3 = new Rectangle(180, 240, 180, 180);
		rect3.setColor(0, 0, 1);
		final Rectangle rect4 = new Rectangle(360, 240, 180, 180);
		rect4.setColor(1, 1, 0);

		scene.getTopLayer().addEntity(rect1);
		scene.getTopLayer().addEntity(rect2);
		scene.getTopLayer().addEntity(rect3);
		scene.getTopLayer().addEntity(rect4);

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
