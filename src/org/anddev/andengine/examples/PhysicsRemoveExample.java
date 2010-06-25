package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.Scene;
import org.anddev.andengine.entity.Scene.IOnAreaTouchListener;
import org.anddev.andengine.entity.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.Scene.ITouchArea;
import org.anddev.andengine.entity.handler.runnable.RunnableHandler;
import org.anddev.andengine.entity.primitives.Rectangle;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.util.FPSCounter;
import org.anddev.andengine.extension.physics.box2d.Box2DPhysicsSpace;
import org.anddev.andengine.extension.physics.box2d.adt.DynamicPhysicsBody;
import org.anddev.andengine.extension.physics.box2d.adt.PhysicsShape;
import org.anddev.andengine.extension.physics.box2d.adt.StaticPhysicsBody;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.sensor.accelerometer.AccelerometerData;
import org.anddev.andengine.sensor.accelerometer.IAccelerometerListener;

import android.hardware.SensorManager;
import android.view.MotionEvent;
import android.widget.Toast;

/**
 * @author Nicolas Gramlich
 * @since 18:47:08 - 19.03.2010
 */
public class PhysicsRemoveExample extends BaseExampleGameActivity implements IAccelerometerListener, IOnSceneTouchListener, IOnAreaTouchListener {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	// ===========================================================
	// Fields
	// ===========================================================

	private Texture mTexture;

	private TiledTextureRegion mBoxFaceTextureRegion;
	private TiledTextureRegion mCircleFaceTextureRegion;

	private Box2DPhysicsSpace mPhysicsSpace;
	private int mFaceCount = 0;
	private RunnableHandler mRemoveRunnableHandler;

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
		Toast.makeText(this, "Touch the screen to add objects. Touch an object to remove it.", Toast.LENGTH_LONG).show();
		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera, false));
	}

	@Override
	public void onLoadResources() {
		this.mTexture = new Texture(64, 64);
		TextureRegionFactory.setAssetBasePath("gfx/");
		this.mBoxFaceTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mTexture, this, "boxface_tiled.png", 0, 0, 2, 1); // 64x32
		this.mCircleFaceTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mTexture, this, "circleface_tiled.png", 0, 32, 2, 1); // 64x32
		this.mEngine.getTextureManager().loadTexture(this.mTexture);

		this.enableAccelerometerSensor(this);
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerPostFrameHandler(new FPSCounter());

		this.mPhysicsSpace = new Box2DPhysicsSpace();
		this.mPhysicsSpace.createWorld(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		this.mPhysicsSpace.setGravity(0, 2 * SensorManager.GRAVITY_EARTH);

		final Scene scene = new Scene(2);
		scene.setBackgroundColor(0, 0, 0);
		scene.setOnSceneTouchListener(this);

		final Rectangle ground = new Rectangle(0, CAMERA_HEIGHT - 1, CAMERA_WIDTH, 1);
		scene.getBottomLayer().addEntity(ground);
		this.mPhysicsSpace.addStaticBody(new StaticPhysicsBody(ground, 0, 0.5f, 0.5f, PhysicsShape.RECTANGLE));

		final Rectangle roof = new Rectangle(0, 0, CAMERA_WIDTH, 2);
		scene.getBottomLayer().addEntity(roof);
		this.mPhysicsSpace.addStaticBody(new StaticPhysicsBody(roof, 0, 0.5f, 0.5f, PhysicsShape.RECTANGLE));

		final Rectangle left = new Rectangle(0, 0, 1, CAMERA_HEIGHT);
		scene.getBottomLayer().addEntity(left);
		this.mPhysicsSpace.addStaticBody(new StaticPhysicsBody(left, 0, 0.5f, 0.5f, PhysicsShape.RECTANGLE));

		final Rectangle right = new Rectangle(CAMERA_WIDTH - 1, 0, 1, CAMERA_HEIGHT);
		scene.getBottomLayer().addEntity(right);
		this.mPhysicsSpace.addStaticBody(new StaticPhysicsBody(right, 0, 0.5f, 0.5f, PhysicsShape.RECTANGLE));

		scene.registerPreFrameHandler(this.mPhysicsSpace);

		scene.setOnAreaTouchListener(this);

		this.mRemoveRunnableHandler = new RunnableHandler();
		scene.registerPostFrameHandler(this.mRemoveRunnableHandler);

		return scene;
	}

	private void addFace(final float pX, final float pY) {
		this.mFaceCount++;

		final AnimatedSprite face;

		if(this.mFaceCount % 2 == 1){
			face = new AnimatedSprite(pX, pY, this.mBoxFaceTextureRegion);
			this.mPhysicsSpace.addDynamicBody(new DynamicPhysicsBody(face, 1, 0.5f, 0.5f, PhysicsShape.RECTANGLE, false));
		} else {
			face = new AnimatedSprite(pX, pY, this.mCircleFaceTextureRegion);
			this.mPhysicsSpace.addDynamicBody(new DynamicPhysicsBody(face, 1, 0.5f, 0.5f, PhysicsShape.CIRCLE, false));
		}

		final Scene scene = this.mEngine.getScene();
		face.animate(new long[]{200,200}, 0, 1, true);
		scene.registerTouchArea(face);
		scene.getTopLayer().addEntity(face);
	}

	@Override
	public boolean onAreaTouched(final ITouchArea pTouchArea, final MotionEvent pSceneMotionEvent) {
		if(pSceneMotionEvent.getAction() == MotionEvent.ACTION_DOWN) {
			this.mRemoveRunnableHandler.postRunnable(new Runnable() {
				@Override
				public void run() {
					final AnimatedSprite face = (AnimatedSprite)pTouchArea;
					final Scene scene = PhysicsRemoveExample.this.mEngine.getScene();

					PhysicsRemoveExample.this.mPhysicsSpace.removeDynamicBodyByShape(face);
					scene.unregisterTouchArea(face);
					scene.getTopLayer().removeEntity(face);
				}
			});
		}

		return false;
	}

	public void onLoadComplete() {

	}

	@Override
	public boolean onSceneTouchEvent(final Scene pScene, final MotionEvent pSceneMotionEvent) {
		if(this.mPhysicsSpace != null) {
			if(pSceneMotionEvent.getAction() == MotionEvent.ACTION_DOWN) {
				this.addFace(pSceneMotionEvent.getX(), pSceneMotionEvent.getY());
				return true;
			}
		}
		return false;
	}

	@Override
	public void onAccelerometerChanged(final AccelerometerData pAccelerometerData) {
		this.mPhysicsSpace.setGravity(4 * pAccelerometerData.getY(), 4 * pAccelerometerData.getX());
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
