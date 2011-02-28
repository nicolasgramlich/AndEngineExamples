package org.anddev.andengine.examples;

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
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.entity.util.ScreenCapture;
import org.anddev.andengine.entity.util.ScreenCapture.IScreenCaptureCallback;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.util.FileUtils;

import android.widget.Toast;

/**
 * @author Nicolas Gramlich
 * @since 11:54:51 - 03.04.2010
 */
public class ScreenCaptureExample extends BaseExample {
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
		Toast.makeText(this, "Touch the screen to capture it (screenshot).", Toast.LENGTH_LONG).show();
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
		final ScreenCapture screenCapture = new ScreenCapture();
		scene.attachChild(screenCapture);
		scene.setOnSceneTouchListener(new IOnSceneTouchListener() {
			@Override
			public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
				if(pSceneTouchEvent.isActionDown()) {
					final int viewWidth = ScreenCaptureExample.this.mRenderSurfaceView.getWidth();
					final int viewHeight = ScreenCaptureExample.this.mRenderSurfaceView.getHeight();
					screenCapture.capture(viewWidth, viewHeight, FileUtils.getAbsolutePathOnExternalStorage(ScreenCaptureExample.this, "Screen_" + System.currentTimeMillis() + ".png"), new IScreenCaptureCallback() {
						@Override
						public void onScreenCaptured(final String pFilePath) {
							ScreenCaptureExample.this.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(ScreenCaptureExample.this, "Screenshot: " + pFilePath + " taken!", Toast.LENGTH_SHORT).show();
								}
							});
						}

						@Override
						public void onScreenCaptureFailed(final String pFilePath, final Exception pException) {
							ScreenCaptureExample.this.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(ScreenCaptureExample.this, "FAILED capturing Screenshot: " + pFilePath + " !", Toast.LENGTH_SHORT).show();
								}
							});
						}
					});
				}
				return true;
			}
		});

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

		/* Create the rectangles. */
		final Rectangle rect1 = this.makeColoredRectangle(-180, -180, 1, 0, 0);
		final Rectangle rect2 = this.makeColoredRectangle(0, -180, 0, 1, 0);
		final Rectangle rect3 = this.makeColoredRectangle(0, 0, 0, 0, 1);
		final Rectangle rect4 = this.makeColoredRectangle(-180, 0, 1, 1, 0);

		final Entity rectangleGroup = new Entity(CAMERA_WIDTH / 2, CAMERA_HEIGHT / 2);
		rectangleGroup.registerEntityModifier(new LoopEntityModifier(new ParallelEntityModifier(
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
		subRectangle.registerEntityModifier(new LoopEntityModifier(new RotationModifier(3, 0, 360)));

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
