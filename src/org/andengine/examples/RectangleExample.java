package org.andengine.examples;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.util.FPSLogger;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.color.Color;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 11:54:51 - 03.04.2010
 */
public class RectangleExample extends SimpleBaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	private static final int RECTANGLE_SIZE = 180;

	// ===========================================================
	// Fields
	// ===========================================================

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
		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
	}

	@Override
	public void onCreateResources() {

	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
		scene.getBackground().setColor(Color.BLACK);

		/* Create the rectangles. */
		final Entity rectangleGroup = new Entity(CAMERA_WIDTH / 2, CAMERA_HEIGHT / 2);

		rectangleGroup.attachChild(this.makeColoredRectangle(-RECTANGLE_SIZE / 2, -RECTANGLE_SIZE / 2, Color.RED));
		rectangleGroup.attachChild(this.makeColoredRectangle(RECTANGLE_SIZE / 2, -RECTANGLE_SIZE / 2, Color.GREEN));
		rectangleGroup.attachChild(this.makeColoredRectangle(RECTANGLE_SIZE / 2, RECTANGLE_SIZE / 2, Color.BLUE));
		rectangleGroup.attachChild(this.makeColoredRectangle(-RECTANGLE_SIZE / 2, RECTANGLE_SIZE / 2, Color.YELLOW));

		scene.attachChild(rectangleGroup);

		return scene;
	}

	private Rectangle makeColoredRectangle(final float pX, final float pY, final Color pColor) {
		final Rectangle coloredRect = new Rectangle(pX, pY, RECTANGLE_SIZE, RECTANGLE_SIZE, this.getVertexBufferObjectManager());
		coloredRect.setColor(pColor);
		return coloredRect;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
