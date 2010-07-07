package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.SingleSceneSplitScreenEngine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.camera.ChaseCamera;
import org.anddev.andengine.engine.options.SplitScreenEngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.extension.physics.box2d.Box2DPhysicsSpace;
import org.anddev.andengine.extension.physics.box2d.adt.DynamicPhysicsBody;
import org.anddev.andengine.extension.physics.box2d.adt.PhysicsShape;
import org.anddev.andengine.extension.physics.box2d.adt.StaticPhysicsBody;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
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
public class SplitScreenExample extends BaseExample implements IAccelerometerListener, IOnSceneTouchListener {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 400;
	private static final int CAMERA_HEIGHT = 480;

	// ===========================================================
	// Fields
	// ===========================================================

	private Texture mTexture;

	private TiledTextureRegion mBoxFaceTextureRegion;

	private Box2DPhysicsSpace mPhysicsSpace;
	private int mFaceCount;
	private ChaseCamera mChaseCamera;

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
		Toast.makeText(this, "Touch the screen to add boxes.", Toast.LENGTH_LONG).show();
		final Camera firstCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		final ChaseCamera secondCamera = new ChaseCamera(0, 0, CAMERA_WIDTH / 2, CAMERA_HEIGHT / 2, null);
		this.mChaseCamera = secondCamera;
		return new SingleSceneSplitScreenEngine(new SplitScreenEngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH * 2, CAMERA_HEIGHT), firstCamera, secondCamera, false));
	}

	@Override
	public void onLoadResources() {
		this.mTexture = new Texture(64, 32, TextureOptions.BILINEAR);
		this.mBoxFaceTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mTexture, this, "gfx/boxface_tiled.png", 0, 0, 2, 1); // 64x32
		this.mEngine.getTextureManager().loadTexture(this.mTexture);

		this.enableAccelerometerSensor(this);
	}

	@Override
	public Scene onLoadScene() {
		this.mPhysicsSpace = new Box2DPhysicsSpace();
		this.mPhysicsSpace.createWorld(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		this.mPhysicsSpace.setGravity(0, 2 * SensorManager.GRAVITY_EARTH);

		final Scene scene = new Scene(2);
		scene.setBackgroundColor(0, 0, 0);
		scene.setOnSceneTouchListener(this);

		this.mEngine.registerPostFrameHandler(new FPSLogger());

		final Rectangle ground = new Rectangle(0, CAMERA_HEIGHT - 1, CAMERA_WIDTH, 1);
		scene.getLayer(0).addEntity(ground);
		this.mPhysicsSpace.addStaticBody(new StaticPhysicsBody(ground, 0, 0.5f, 0.5f, PhysicsShape.RECTANGLE));

		final Rectangle roof = new Rectangle(0, 0, CAMERA_WIDTH, 2);
		scene.getLayer(0).addEntity(roof);
		this.mPhysicsSpace.addStaticBody(new StaticPhysicsBody(roof, 0, 0.5f, 0.5f, PhysicsShape.RECTANGLE));

		final Rectangle left = new Rectangle(0, 0, 1, CAMERA_HEIGHT);
		scene.getLayer(0).addEntity(left);
		this.mPhysicsSpace.addStaticBody(new StaticPhysicsBody(left, 0, 0.5f, 0.5f, PhysicsShape.RECTANGLE));

		final Rectangle right = new Rectangle(CAMERA_WIDTH - 1, 0, 1, CAMERA_HEIGHT);
		scene.getLayer(0).addEntity(right);
		this.mPhysicsSpace.addStaticBody(new StaticPhysicsBody(right, 0, 0.5f, 0.5f, PhysicsShape.RECTANGLE));

		this.mEngine.registerPreFrameHandler(this.mPhysicsSpace);

		return scene;
	}

	private void addFace(final float pX, final float pY) {
		final Scene scene = this.mEngine.getScene();

		final AnimatedSprite face = new AnimatedSprite(pX, pY, this.mBoxFaceTextureRegion);

		face.animate(new long[]{100,100}, 0, 1, true);

		//		face.addSpriteModifier(new SequenceModifier(new IModifierListener() {
		//			@Override
		//			public void onModifierFinished(final ISpriteModifier pSpriteModifier, final BaseSprite pBaseSprite) {
		//				Playground.this.runOnUiThread(new Runnable() {
		//					@Override
		//					public void run() {
		//						Toast.makeText(Playground.this, "Sequence ended.", Toast.LENGTH_LONG).show();
		//					}
		//				});
		//			}
		//		},
		//		new RotateByModifier(5, 90),
		//		new DelayModifier(2),
		//		new AlphaModifier(3, 1, 0),
		//		new AlphaModifier(3, 0, 1),
		//		new ScaleModifier(3, 1, 0.5f),
		//		new ScaleModifier(3, 0.5f, 5),
		//		new ScaleModifier(3, 5, 1),
		//		new RotateModifier(5, 45, 90),
		//		new RotateByModifier(5, -90)));

		scene.getLayer(1).addEntity(face);

		//		if(this.mFaceCount == 0) {
		//			this.mPhysicsSpace.addDynamicBody(new DynamicPhysicsBody(face, 1, 0.5f, 0.5f, PhysicsShape.RECTANGLE, false, new ICollisionCallback() {
		//				@Override
		//				public boolean onCollision(final StaticEntity pCheckEntity, final StaticEntity pTargetStaticEntity) {
		//					Debug.d("KLONK");
		//					return false;
		//				}
		//			}));
		//		} else {
		this.mPhysicsSpace.addDynamicBody(new DynamicPhysicsBody(face, 1, 0.5f, 0.5f, PhysicsShape.RECTANGLE, false));

		if(this.mFaceCount == 0){
			this.mChaseCamera.setChaseEntity(face);
		}
		//		}
		//
		this.mFaceCount++;
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
