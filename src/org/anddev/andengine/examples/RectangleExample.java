package org.anddev.andengine.examples;

import static org.anddev.andengine.util.constants.Constants.VERTEX_INDEX_X;
import static org.anddev.andengine.util.constants.Constants.VERTEX_INDEX_Y;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.Entity;
import org.anddev.andengine.entity.modifier.LoopEntityModifier;
import org.anddev.andengine.entity.modifier.ParallelEntityModifier;
import org.anddev.andengine.entity.modifier.RotationModifier;
import org.anddev.andengine.entity.modifier.ScaleModifier;
import org.anddev.andengine.entity.modifier.SequenceEntityModifier;
import org.anddev.andengine.entity.primitive.Line;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
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
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera));
	}

	@Override
	public void onLoadResources() {

	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene(2);
		scene.setBackground(new ColorBackground(0, 0, 0));
		
		/* Create three lines that will form an arrow pointing to the eye. */
		final Line arrowLineMain = new Line(0, 0, 0, 0, 3);
		final Line arrowLineWingLeft = new Line(0, 0, 0, 0, 3);
		final Line arrowLineWingRight = new Line(0, 0, 0, 0, 3);

		arrowLineMain.setColor(1, 0, 1);
		arrowLineWingLeft.setColor(1, 0, 1);
		arrowLineWingRight.setColor(1, 0, 1);

		scene.getLastChild().attachChild(arrowLineMain);
		scene.getLastChild().attachChild(arrowLineWingLeft);
		scene.getLastChild().attachChild(arrowLineWingRight);

		/* Create thr rectangles. */
		final Rectangle rect1 = makeColoredRectangle(-180, -180, 1, 0, 0);
		final Rectangle rect2 = makeColoredRectangle(0, -180, 0, 1, 0);
		final Rectangle rect3 = makeColoredRectangle(0, 0, 0, 0, 1);
		
		final Rectangle rect4 = new Rectangle(-180, 0, 180, 180);
		rect4.setColor(1, 1, 0);

		final Rectangle subRectangle = new Rectangle(45, 45, 90, 90){
			@Override
			protected void onManagedUpdate(final float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				final float[] upperLeftCoordinates = this.convertLocalToSceneCoordinates(0, 0);
				final float upperLeftX = upperLeftCoordinates[VERTEX_INDEX_X];
				final float upperLeftY = upperLeftCoordinates[VERTEX_INDEX_Y];

				arrowLineMain.setPosition(upperLeftX, upperLeftY, upperLeftX, upperLeftY - 50);
				arrowLineWingLeft.setPosition(upperLeftX, upperLeftY, upperLeftX - 10, upperLeftY - 10);
				arrowLineWingRight.setPosition(upperLeftX, upperLeftY, upperLeftX + 10, upperLeftY - 10);
			}
		};
		subRectangle.addEntityModifier(new LoopEntityModifier(new RotationModifier(5, 0, 360)));
		
		rect4.attachChild(subRectangle);
		
		
		
		final Entity rectangleGroup = new Entity(CAMERA_WIDTH / 2, CAMERA_HEIGHT / 2);
		rectangleGroup.addEntityModifier(new LoopEntityModifier(new ParallelEntityModifier(
				new SequenceEntityModifier(
					new ScaleModifier(10, 1, 0.5f), 
					new ScaleModifier(10, 0.5f, 1)
				),
				new RotationModifier(20, 0, 360))
			));

		rectangleGroup.attachChild(rect1);
		rectangleGroup.attachChild(rect2);
		rectangleGroup.attachChild(rect3);
		rectangleGroup.attachChild(rect4);
		
		scene.getFirstChild().attachChild(rectangleGroup);

		return scene;
	}

	private Rectangle makeColoredRectangle(final float pX, final float pY, final float pRed, final float pGreen, final float pBlue) {
		final Rectangle coloredRect = new Rectangle(pX, pY, 180, 180);
		coloredRect.setColor(pRed, pGreen, pBlue);
		
		final Rectangle subRectangle = new Rectangle(45, 45, 90, 90);
		subRectangle.addEntityModifier(new LoopEntityModifier(new RotationModifier(3, 0, 360)));

		coloredRect.attachChild(subRectangle);
		
		return coloredRect;
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
