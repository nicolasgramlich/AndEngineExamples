package org.anddev.andengine.examples.game.racer;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.anddev.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.anddev.andengine.engine.camera.hud.controls.BaseOnScreenControl.OnScreenControlListener;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.layer.ILayer;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.sprite.TiledSprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.extension.physics.box2d.PhysicsConnector;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.MathUtils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

/**
 * @author Nicolas Gramlich
 * @since 22:43:20 - 15.07.2010
 */
public class RacerGameActivity  extends BaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 768;
	private static final int CAMERA_HEIGHT = 384;

	private static final int INSET = 128;

	private static final int LAYER_RACETRACK = 0;
	private static final int LAYER_BORDERS = LAYER_RACETRACK + 1;
	private static final int LAYER_CARS = LAYER_BORDERS + 1;

	// ===========================================================
	// Fields
	// ===========================================================
	private Camera mCamera;

	private PhysicsWorld mPhysicsWorld;

	private Texture mVehiclesTexture;
	private TiledTextureRegion mVehiclesTextureRegion;

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

		this.mVehiclesTexture = new Texture(128, 16, TextureOptions.BILINEAR);
		this.mVehiclesTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mVehiclesTexture, this, "vehicles.png", 0, 0, 6, 1);

		this.mRacetrackTexture = new Texture(128, 256, TextureOptions.REPEATING_BILINEAR);
		this.mRacetrackStraightTextureRegion = TextureRegionFactory.createFromAsset(this.mRacetrackTexture, this, "racetrack_straight.png", 0, 0);
		this.mRacetrackCurveTextureRegion = TextureRegionFactory.createFromAsset(this.mRacetrackTexture, this, "racetrack_curve.png", 0, 128);

		this.mOnScreenControlTexture = new Texture(256, 128, TextureOptions.BILINEAR);
		this.mOnScreenControlBaseTextureRegion = TextureRegionFactory.createFromAsset(this.mOnScreenControlTexture, this, "onscreen_control_base.png", 0, 0);
		this.mOnScreenControlKnobTextureRegion = TextureRegionFactory.createFromAsset(this.mOnScreenControlTexture, this, "onscreen_control_knob.png", 128, 0);


		this.mEngine.getTextureManager().loadTextures(this.mVehiclesTexture, this.mRacetrackTexture, this.mOnScreenControlTexture);
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerPostFrameHandler(new FPSLogger());

		final Scene scene = new Scene(3);
		scene.setBackgroundColor(0, 0, 0);

		this.mPhysicsWorld = new PhysicsWorld(new Vector2(0, 0), false);

		this.initRacetrack(scene);
		this.initRacetrackBorders(scene);
		this.initCar(scene);
		this.initOnScreenControls(scene);

		scene.registerPreFrameHandler(this.mPhysicsWorld);

		return scene;
	}

	private void initOnScreenControls(final Scene pScene) {
		final AnalogOnScreenControl analogOnScreenControl = new AnalogOnScreenControl(0, CAMERA_HEIGHT - this.mOnScreenControlBaseTextureRegion.getHeight(), this.mCamera, this.mOnScreenControlBaseTextureRegion, this.mOnScreenControlKnobTextureRegion, 0.1f, new OnScreenControlListener() {
			private Vector2 mTemp = new Vector2();

			@Override
			public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl, final float pValueX, final float pValueY) {
				this.mTemp.set(pValueX * 200, pValueY * 200);
				RacerGameActivity.this.mCarBody.setLinearVelocity(this.mTemp);
				RacerGameActivity.this.mCar.setRotation(MathUtils.radToDeg((float)Math.atan2(-pValueX, pValueY)));
			}
		}){
			@Override
			protected void onHandleControlBaseLeft() {
				/* Nothing. */
			}
			
			@Override
			protected void onHandleControlKnobReleased() {
				/* Nothing. */
			}
		};
		analogOnScreenControl.getControlBase().setAlpha(0.5f);

		pScene.setChildScene(analogOnScreenControl);
	}

	@Override
	public void onLoadComplete() {

	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void initCar(final Scene pScene) {
		this.mCar = new TiledSprite(20, 20, 32, 32, this.mVehiclesTextureRegion);
		this.mCar.setCurrentTileIndex(0);
		final FixtureDef carFixtureDef = new FixtureDef();
		carFixtureDef.restitution = 0;
		this.mCarBody = PhysicsFactory.createBoxBody(this.mPhysicsWorld, this.mCar, BodyType.DynamicBody, carFixtureDef);
		this.mCar.setUpdatePhysics(false);
		this.mCarBody.setAngularDamping(10);
		this.mCarBody.setLinearDamping(10);

		pScene.getLayer(LAYER_CARS).addEntity(this.mCar);
		this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(this.mCar, this.mCarBody, true, false, true, false));
	}


	private void initRacetrack(final Scene pScene) {
		final ILayer racetrackLayer = pScene.getLayer(LAYER_RACETRACK);

		/* Straights. */
		{
			final TextureRegion racetrackHorizontalStraightTextureRegion = this.mRacetrackStraightTextureRegion.clone();
			racetrackHorizontalStraightTextureRegion.setWidth(512);

			final TextureRegion racetrackVerticalStraightTextureRegion = this.mRacetrackStraightTextureRegion;

			/* Top Straight */
			racetrackLayer.addEntity(new Sprite(INSET, 0, racetrackHorizontalStraightTextureRegion));
			/* Bottom Straight */
			racetrackLayer.addEntity(new Sprite(INSET, CAMERA_HEIGHT - INSET, racetrackHorizontalStraightTextureRegion));

			/* Left Straight */
			final Sprite leftVerticalStraight = new Sprite(0, INSET, racetrackVerticalStraightTextureRegion);
			leftVerticalStraight.setRotation(90);
			racetrackLayer.addEntity(leftVerticalStraight);
			/* Right Straight */
			final Sprite rightVerticalStraight = new Sprite(CAMERA_WIDTH - INSET, INSET, racetrackVerticalStraightTextureRegion);
			rightVerticalStraight.setRotation(90);
			racetrackLayer.addEntity(rightVerticalStraight);
		}

		/* Edges */
		{
			final TextureRegion racetrackCurveTextureRegion = this.mRacetrackCurveTextureRegion;

			/* Upper Left */
			final Sprite upperLeftCurve = new Sprite(0, 0, racetrackCurveTextureRegion);
			upperLeftCurve.setRotation(90);
			racetrackLayer.addEntity(upperLeftCurve);

			/* Upper Right */
			final Sprite upperRightCurve = new Sprite(CAMERA_WIDTH - INSET, 0, racetrackCurveTextureRegion);
			upperRightCurve.setRotation(180);
			racetrackLayer.addEntity(upperRightCurve);

			/* Lower Right */
			final Sprite lowerRightCurve = new Sprite(CAMERA_WIDTH - INSET, CAMERA_HEIGHT - INSET, racetrackCurveTextureRegion);
			lowerRightCurve.setRotation(270);
			racetrackLayer.addEntity(lowerRightCurve);

			/* Lower Left */
			final Sprite lowerLeftCurve = new Sprite(0, CAMERA_HEIGHT - INSET, racetrackCurveTextureRegion);
			racetrackLayer.addEntity(lowerLeftCurve);
		}
	}


	private void initRacetrackBorders(final Scene pScene) {
		final Shape bottomOuter = new Rectangle(0, CAMERA_HEIGHT - 2, CAMERA_WIDTH, 2);
		final Shape topOuter = new Rectangle(0, 0, CAMERA_WIDTH, 2);
		final Shape leftOuter = new Rectangle(0, 0, 2, CAMERA_HEIGHT);
		final Shape rightOuter = new Rectangle(CAMERA_WIDTH - 2, 0, 2, CAMERA_HEIGHT);

		final Shape bottomInner = new Rectangle(INSET, CAMERA_HEIGHT - 2 - INSET, CAMERA_WIDTH - 2 * INSET, 2);
		final Shape topInner = new Rectangle(INSET, INSET, CAMERA_WIDTH - 2 * INSET, 2);
		final Shape leftInner = new Rectangle(INSET, INSET, 2, CAMERA_HEIGHT - 2 * INSET);
		final Shape rightInner = new Rectangle(CAMERA_WIDTH - 2 - INSET, INSET, 2, CAMERA_HEIGHT - 2 * INSET);

		final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, bottomOuter, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, topOuter, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, leftOuter, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, rightOuter, BodyType.StaticBody, wallFixtureDef);

		PhysicsFactory.createBoxBody(this.mPhysicsWorld, bottomInner, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, topInner, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, leftInner, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, rightInner, BodyType.StaticBody, wallFixtureDef);

		final ILayer bottomLayer = pScene.getLayer(LAYER_BORDERS);
		bottomLayer.addEntity(bottomOuter);
		bottomLayer.addEntity(topOuter);
		bottomLayer.addEntity(leftOuter);
		bottomLayer.addEntity(rightOuter);

		bottomLayer.addEntity(bottomInner);
		bottomLayer.addEntity(topInner);
		bottomLayer.addEntity(leftInner);
		bottomLayer.addEntity(rightInner);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
