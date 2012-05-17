package org.andengine.examples;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.Entity;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.util.FPSLogger;
import org.andengine.entity.util.ScreenCapture;
import org.andengine.entity.util.ScreenCapture.IScreenCaptureCallback;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.FileUtils;
import org.andengine.util.adt.color.Color;

import android.widget.Toast;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 11:54:51 - 03.04.2010
 */
public class ScreenCaptureExample extends SimpleBaseGameActivity {
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
		Toast.makeText(this, "Touch the screen to capture it (screenshot).", Toast.LENGTH_LONG).show();

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
		final ScreenCapture screenCapture = new ScreenCapture();

		scene.getBackground().setColor(Color.BLACK);

		/* Create three lines that will form an arrow pointing to the eye. */
		final Line arrowLineMain = new Line(0, 0, 0, 0, 3, this.getVertexBufferObjectManager());
		final Line arrowLineWingLeft = new Line(0, 0, 0, 0, 3, this.getVertexBufferObjectManager());
		final Line arrowLineWingRight = new Line(0, 0, 0, 0, 3, this.getVertexBufferObjectManager());

		arrowLineMain.setColor(1, 0, 1);
		arrowLineWingLeft.setColor(1, 0, 1);
		arrowLineWingRight.setColor(1, 0, 1);

		scene.attachChild(arrowLineMain);
		scene.attachChild(arrowLineWingLeft);
		scene.attachChild(arrowLineWingRight);

		/* Create the rectangles. */
		final Entity rectangleGroup = new Entity(CAMERA_WIDTH / 2, CAMERA_HEIGHT / 2);
		rectangleGroup.registerEntityModifier(new LoopEntityModifier(new ParallelEntityModifier(
				new SequenceEntityModifier(
						new ScaleModifier(10, 1, 0.5f),
						new ScaleModifier(10, 0.5f, 1)
				),
				new RotationModifier(20, 0, 360))
		));

		rectangleGroup.attachChild(this.makeColoredRectangle(-RECTANGLE_SIZE / 2, -RECTANGLE_SIZE / 2, Color.RED));
		rectangleGroup.attachChild(this.makeColoredRectangle(RECTANGLE_SIZE / 2, -RECTANGLE_SIZE / 2, Color.GREEN));
		rectangleGroup.attachChild(this.makeColoredRectangle(RECTANGLE_SIZE / 2, RECTANGLE_SIZE / 2, Color.BLUE));
		rectangleGroup.attachChild(this.makeColoredRectangle(-RECTANGLE_SIZE / 2, RECTANGLE_SIZE / 2, Color.YELLOW));

		scene.attachChild(rectangleGroup);

		/* Attaching the ScreenCapture to the end. */
		scene.attachChild(screenCapture);
		scene.setOnSceneTouchListener(new IOnSceneTouchListener() {
			@Override
			public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
				if(pSceneTouchEvent.isActionDown()) {
					final int viewWidth = ScreenCaptureExample.this.mRenderSurfaceView.getWidth();
					final int viewHeight = ScreenCaptureExample.this.mRenderSurfaceView.getHeight();

					FileUtils.ensureDirectoriesExistOnExternalStorage(ScreenCaptureExample.this, "");

					screenCapture.capture(0, viewHeight / 2, viewWidth, viewHeight / 2, FileUtils.getAbsolutePathOnExternalStorage(ScreenCaptureExample.this, "Screen_" + System.currentTimeMillis() + ".png"), new IScreenCaptureCallback() {
						@Override
						public void onScreenCaptured(final String pFilePath) {
							ScreenCaptureExample.this.toastOnUiThread("Screenshot: '" + pFilePath + "' taken!");
						}
  
						@Override
						public void onScreenCaptureFailed(final String pFilePath, final Exception pException) {
							ScreenCaptureExample.this.toastOnUiThread("FAILED capturing screenshot: '" + pFilePath + "' !", Toast.LENGTH_LONG);
						}
					});
				}
				return true;
			}
		});
		
		return scene;
	}

	private Rectangle makeColoredRectangle(final float pX, final float pY, final Color pColor) {
		final VertexBufferObjectManager vertexBufferObjectManager = this.getVertexBufferObjectManager();
		final Rectangle coloredRect = new Rectangle(pX, pY, RECTANGLE_SIZE, RECTANGLE_SIZE, vertexBufferObjectManager);
		coloredRect.setColor(pColor);

		final Rectangle subRectangle = new Rectangle(RECTANGLE_SIZE / 2, RECTANGLE_SIZE / 2, RECTANGLE_SIZE / 2, RECTANGLE_SIZE / 2, vertexBufferObjectManager);
		subRectangle.registerEntityModifier(new LoopEntityModifier(new RotationModifier(5, 0, 360)));

		coloredRect.attachChild(subRectangle);

		return coloredRect;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
