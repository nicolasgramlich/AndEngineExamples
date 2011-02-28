package org.anddev.andengine.examples.game.racer;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.anddev.andengine.engine.camera.hud.controls.AnalogOnScreenControl.IAnalogOnScreenControlListener;
import org.anddev.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.sprite.TiledSprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.anddev.andengine.extension.physics.box2d.PhysicsConnector;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.extension.physics.box2d.util.Vector2Pool;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.MathUtils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/**
 * @author Nicolas Gramlich
 * @since 22:43:20 - 15.07.2010
 */
public class RacerGameActivity  extends BaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int RACETRACK_WIDTH = 64;

	private static final int OBSTACLE_SIZE = 16;
	private static final int CAR_SIZE = 16;

	private static final int CAMERA_WIDTH = RACETRACK_WIDTH * 5;
	private static final int CAMERA_HEIGHT = RACETRACK_WIDTH * 3;

	private static final int LAYER_RACETRACK = 0;
	private static final int LAYER_BORDERS = LAYER_RACETRACK + 1;
	private static final int LAYER_CARS = LAYER_BORDERS + 1;
	private static final int LAYER_OBSTACLES = LAYER_CARS + 1;

	// ===========================================================
	// Fields
	// ===========================================================
	
	private Camera mCamera;

	private PhysicsWorld mPhysicsWorld;

	private Texture mVehiclesTexture;
	private TiledTextureRegion mVehiclesTextureRegion;
	
	private Texture mBoxTexture;
	private TextureRegion mBoxTextureRegion;

	private Texture mRacetrackTexture;
	private TextureRegion mRacetrackStraightTextureRegion;
	private TextureRegion mRacetrackCurveTextureRegion;

	private Texture mOnScreenControlTexture;
	private TextureRegion mOnScreenControlBaseTextureRegion;
	private TextureRegion mOnScreenControlKnobTextureRegion;

	private Body mCarBody;
	private TiledSprite mCar;

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
		TextureRegionFactory.setAssetBasePath("gfx/");

		this.mVehiclesTexture = new Texture(128, 16, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mVehiclesTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mVehiclesTexture, this, "vehicles.png", 0, 0, 6, 1);

		this.mRacetrackTexture = new Texture(128, 256, TextureOptions.REPEATING_BILINEAR_PREMULTIPLYALPHA);
		this.mRacetrackStraightTextureRegion = TextureRegionFactory.createFromAsset(this.mRacetrackTexture, this, "racetrack_straight.png", 0, 0);
		this.mRacetrackCurveTextureRegion = TextureRegionFactory.createFromAsset(this.mRacetrackTexture, this, "racetrack_curve.png", 0, 128);

		this.mOnScreenControlTexture = new Texture(256, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mOnScreenControlBaseTextureRegion = TextureRegionFactory.createFromAsset(this.mOnScreenControlTexture, this, "onscreen_control_base.png", 0, 0);
		this.mOnScreenControlKnobTextureRegion = TextureRegionFactory.createFromAsset(this.mOnScreenControlTexture, this, "onscreen_control_knob.png", 128, 0);

		this.mBoxTexture = new Texture(32, 32, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mBoxTextureRegion = TextureRegionFactory.createFromAsset(this.mBoxTexture, this, "box.png", 0, 0);

		this.mEngine.getTextureManager().loadTextures(this.mVehiclesTexture, this.mRacetrackTexture, this.mOnScreenControlTexture, this.mBoxTexture);
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene(4);
		scene.setBackground(new ColorBackground(0, 0, 0));

		this.mPhysicsWorld = new FixedStepPhysicsWorld(30, new Vector2(0, 0), false, 8, 1);

		this.initRacetrack(scene);
		this.initRacetrackBorders(scene);
		this.initCar(scene);
		this.initObstacles(scene);
		this.initOnScreenControls(scene);

		scene.registerUpdateHandler(this.mPhysicsWorld);

		return scene;
	}

	@Override
	public void onLoadComplete() {

	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void initOnScreenControls(final Scene pScene) {
		final AnalogOnScreenControl analogOnScreenControl = new AnalogOnScreenControl(0, CAMERA_HEIGHT - this.mOnScreenControlBaseTextureRegion.getHeight(), this.mCamera, this.mOnScreenControlBaseTextureRegion, this.mOnScreenControlKnobTextureRegion, 0.1f, new IAnalogOnScreenControlListener() {
			@Override
			public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl, final float pValueX, final float pValueY) {
				final Body carBody = RacerGameActivity.this.mCarBody;

				final Vector2 velocity = Vector2Pool.obtain(pValueX * 5, pValueY * 5);
				carBody.setLinearVelocity(velocity);
				Vector2Pool.recycle(velocity);
				
				final float rotationInRad = (float)Math.atan2(-pValueX, pValueY);
				carBody.setTransform(carBody.getWorldCenter(), rotationInRad);
				
				RacerGameActivity.this.mCar.setRotation(MathUtils.radToDeg(rotationInRad));
			}

			@Override
			public void onControlClick(AnalogOnScreenControl pAnalogOnScreenControl) {
				/* Nothing. */
			}
		});
		analogOnScreenControl.getControlBase().setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		analogOnScreenControl.getControlBase().setAlpha(0.5f);
//		analogOnScreenControl.getControlBase().setScaleCenter(0, 128);
//		analogOnScreenControl.getControlBase().setScale(0.75f);
//		analogOnScreenControl.getControlKnob().setScale(0.75f);
		analogOnScreenControl.refreshControlKnobPosition();
		
		pScene.setChildScene(analogOnScreenControl);
	}

	private void initCar(final Scene pScene) {
		this.mCar = new TiledSprite(20, 20, CAR_SIZE, CAR_SIZE, this.mVehiclesTextureRegion);
		this.mCar.setCurrentTileIndex(0);
		
		final FixtureDef carFixtureDef = PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f);
		this.mCarBody = PhysicsFactory.createBoxBody(this.mPhysicsWorld, this.mCar, BodyType.DynamicBody, carFixtureDef);
		
		this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(this.mCar, this.mCarBody, true, false));

		pScene.getChild(LAYER_CARS).attachChild(this.mCar);
	}
	
	private void initObstacles(final Scene pScene) {
		addObstacle(pScene, CAMERA_WIDTH / 2, RACETRACK_WIDTH / 2);
		addObstacle(pScene, CAMERA_WIDTH / 2, RACETRACK_WIDTH / 2);
		addObstacle(pScene, CAMERA_WIDTH / 2, CAMERA_HEIGHT - RACETRACK_WIDTH / 2);
		addObstacle(pScene, CAMERA_WIDTH / 2, CAMERA_HEIGHT - RACETRACK_WIDTH / 2);
	}

	private void addObstacle(final Scene pScene, final float pX, final float pY) {
		final Sprite box = new Sprite(pX, pY, OBSTACLE_SIZE, OBSTACLE_SIZE, this.mBoxTextureRegion);
		
		final FixtureDef boxFixtureDef = PhysicsFactory.createFixtureDef(0.1f, 0.5f, 0.5f);
		final Body boxBody = PhysicsFactory.createBoxBody(this.mPhysicsWorld, box, BodyType.DynamicBody, boxFixtureDef);
		boxBody.setLinearDamping(10);
		boxBody.setAngularDamping(10);
		
		this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(box, boxBody, true, true));

		pScene.getChild(LAYER_OBSTACLES).attachChild(box);
	}

	private void initRacetrack(final Scene pScene) {
		final IEntity racetrackEntity = pScene.getChild(LAYER_RACETRACK);

		/* Straights. */
		{
			final TextureRegion racetrackHorizontalStraightTextureRegion = this.mRacetrackStraightTextureRegion.clone();
			racetrackHorizontalStraightTextureRegion.setWidth(3 * this.mRacetrackStraightTextureRegion.getWidth());

			final TextureRegion racetrackVerticalStraightTextureRegion = this.mRacetrackStraightTextureRegion;

			/* Top Straight */
			racetrackEntity.attachChild(new Sprite(RACETRACK_WIDTH, 0, 3 * RACETRACK_WIDTH, RACETRACK_WIDTH, racetrackHorizontalStraightTextureRegion));
			/* Bottom Straight */
			racetrackEntity.attachChild(new Sprite(RACETRACK_WIDTH, CAMERA_HEIGHT - RACETRACK_WIDTH, 3 * RACETRACK_WIDTH, RACETRACK_WIDTH, racetrackHorizontalStraightTextureRegion));

			/* Left Straight */
			final Sprite leftVerticalStraight = new Sprite(0, RACETRACK_WIDTH, RACETRACK_WIDTH, RACETRACK_WIDTH, racetrackVerticalStraightTextureRegion);
			leftVerticalStraight.setRotation(90);
			racetrackEntity.attachChild(leftVerticalStraight);
			/* Right Straight */
			final Sprite rightVerticalStraight = new Sprite(CAMERA_WIDTH - RACETRACK_WIDTH, RACETRACK_WIDTH, RACETRACK_WIDTH, RACETRACK_WIDTH, racetrackVerticalStraightTextureRegion);
			rightVerticalStraight.setRotation(90);
			racetrackEntity.attachChild(rightVerticalStraight);
		}

		/* Edges */
		{
			final TextureRegion racetrackCurveTextureRegion = this.mRacetrackCurveTextureRegion;

			/* Upper Left */
			final Sprite upperLeftCurve = new Sprite(0, 0, RACETRACK_WIDTH, RACETRACK_WIDTH, racetrackCurveTextureRegion);
			upperLeftCurve.setRotation(90);
			racetrackEntity.attachChild(upperLeftCurve);

			/* Upper Right */
			final Sprite upperRightCurve = new Sprite(CAMERA_WIDTH - RACETRACK_WIDTH, 0, RACETRACK_WIDTH, RACETRACK_WIDTH, racetrackCurveTextureRegion);
			upperRightCurve.setRotation(180);
			racetrackEntity.attachChild(upperRightCurve);

			/* Lower Right */
			final Sprite lowerRightCurve = new Sprite(CAMERA_WIDTH - RACETRACK_WIDTH, CAMERA_HEIGHT - RACETRACK_WIDTH, RACETRACK_WIDTH, RACETRACK_WIDTH, racetrackCurveTextureRegion);
			lowerRightCurve.setRotation(270);
			racetrackEntity.attachChild(lowerRightCurve);

			/* Lower Left */
			final Sprite lowerLeftCurve = new Sprite(0, CAMERA_HEIGHT - RACETRACK_WIDTH, RACETRACK_WIDTH, RACETRACK_WIDTH, racetrackCurveTextureRegion);
			racetrackEntity.attachChild(lowerLeftCurve);
		}
	}


	private void initRacetrackBorders(final Scene pScene) {
		final Shape bottomOuter = new Rectangle(0, CAMERA_HEIGHT - 2, CAMERA_WIDTH, 2);
		final Shape topOuter = new Rectangle(0, 0, CAMERA_WIDTH, 2);
		final Shape leftOuter = new Rectangle(0, 0, 2, CAMERA_HEIGHT);
		final Shape rightOuter = new Rectangle(CAMERA_WIDTH - 2, 0, 2, CAMERA_HEIGHT);

		final Shape bottomInner = new Rectangle(RACETRACK_WIDTH, CAMERA_HEIGHT - 2 - RACETRACK_WIDTH, CAMERA_WIDTH - 2 * RACETRACK_WIDTH, 2);
		final Shape topInner = new Rectangle(RACETRACK_WIDTH, RACETRACK_WIDTH, CAMERA_WIDTH - 2 * RACETRACK_WIDTH, 2);
		final Shape leftInner = new Rectangle(RACETRACK_WIDTH, RACETRACK_WIDTH, 2, CAMERA_HEIGHT - 2 * RACETRACK_WIDTH);
		final Shape rightInner = new Rectangle(CAMERA_WIDTH - 2 - RACETRACK_WIDTH, RACETRACK_WIDTH, 2, CAMERA_HEIGHT - 2 * RACETRACK_WIDTH);

		final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, bottomOuter, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, topOuter, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, leftOuter, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, rightOuter, BodyType.StaticBody, wallFixtureDef);

		PhysicsFactory.createBoxBody(this.mPhysicsWorld, bottomInner, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, topInner, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, leftInner, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, rightInner, BodyType.StaticBody, wallFixtureDef);

		final IEntity firstChild = pScene.getChild(LAYER_BORDERS);
		firstChild.attachChild(bottomOuter);
		firstChild.attachChild(topOuter);
		firstChild.attachChild(leftOuter);
		firstChild.attachChild(rightOuter);

		firstChild.attachChild(bottomInner);
		firstChild.attachChild(topInner);
		firstChild.attachChild(leftInner);
		firstChild.attachChild(rightInner);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
