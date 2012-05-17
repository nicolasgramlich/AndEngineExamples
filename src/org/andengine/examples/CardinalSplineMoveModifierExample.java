package org.andengine.examples;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.modifier.CardinalSplineMoveModifier;
import org.andengine.entity.modifier.CardinalSplineMoveModifier.CardinalSplineMoveModifierConfig;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.util.FPSLogger;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.math.MathUtils;
import org.andengine.util.modifier.ease.EaseLinear;

import android.opengl.GLES20;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 11:54:51 - 03.04.2010
 */
public class CardinalSplineMoveModifierExample extends SimpleBaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	private static final int COUNT = 200;
	private static final float DURATION = 2;
	private static final float SIZE = 25;

	private static final float[] CONTROLPOINT_1_XS = {
		2f * (CAMERA_WIDTH / 4f),
		1f * (CAMERA_WIDTH / 4f),
		1.5f * (CAMERA_WIDTH / 4f),
		2f * (CAMERA_WIDTH / 4f)
	};

	private static final float[] CONTROLPOINT_2_XS = {
		2f * (CAMERA_WIDTH / 4f),
		3f * (CAMERA_WIDTH / 4f),
		2.5f * (CAMERA_WIDTH / 4f),
		2f * (CAMERA_WIDTH / 4f)
	};

	private static final float[] CONTROLPOINT_YS = {
		0.5f * (CAMERA_HEIGHT / 4f),
		2f * (CAMERA_HEIGHT / 4f),
		3f * (CAMERA_HEIGHT / 4f),
		2.5f * (CAMERA_HEIGHT / 4f),
	};

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
		scene.getBackground().setColor(0, 0, 0);

		for(int i = 0; i < COUNT; i++) {
			final float tension = MathUtils.random(-0.5f, 0.5f);
			this.addRectangleWithTension(scene, tension, MathUtils.random(0, DURATION * 2f));
		}

		return scene;
	}

	private void addRectangleWithTension(final Scene pScene, final float pTension, float pDelay) {
		final Rectangle rectangle = new Rectangle(-SIZE, -SIZE, SIZE, SIZE, this.getVertexBufferObjectManager());
		rectangle.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE);
		if(pTension < 0) {
			rectangle.setColor(1 - pTension, 0, 0, 0.5f);
		} else {
			rectangle.setColor(pTension, 0, 0, 0.5f);
		}

		final CardinalSplineMoveModifierConfig cardinalSplineMoveModifierConfig1 = new CardinalSplineMoveModifierConfig(CardinalSplineMoveModifierExample.CONTROLPOINT_1_XS.length, pTension);
		final CardinalSplineMoveModifierConfig cardinalSplineMoveModifierConfig2 = new CardinalSplineMoveModifierConfig(CardinalSplineMoveModifierExample.CONTROLPOINT_1_XS.length, pTension);

		for(int i = 0; i < CardinalSplineMoveModifierExample.CONTROLPOINT_1_XS.length; i++) {
			cardinalSplineMoveModifierConfig1.setControlPoint(i, CardinalSplineMoveModifierExample.CONTROLPOINT_1_XS[i], CardinalSplineMoveModifierExample.CONTROLPOINT_YS[i]);
			cardinalSplineMoveModifierConfig2.setControlPoint(i, CardinalSplineMoveModifierExample.CONTROLPOINT_2_XS[i], CardinalSplineMoveModifierExample.CONTROLPOINT_YS[i]);
		}

		rectangle.registerEntityModifier(
			new SequenceEntityModifier(
				new DelayModifier(pDelay),
				new LoopEntityModifier(
					new SequenceEntityModifier(
						new ParallelEntityModifier(
							new CardinalSplineMoveModifier(CardinalSplineMoveModifierExample.DURATION, cardinalSplineMoveModifierConfig1, EaseLinear.getInstance()),
							new RotationModifier(CardinalSplineMoveModifierExample.DURATION, -45, -315)
						),
						new ParallelEntityModifier(
							new CardinalSplineMoveModifier(CardinalSplineMoveModifierExample.DURATION, cardinalSplineMoveModifierConfig2, EaseLinear.getInstance()),
							new RotationModifier(CardinalSplineMoveModifierExample.DURATION, 45, 315)
						)
					)
				)
			)
		);

		pScene.attachChild(rectangle);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
