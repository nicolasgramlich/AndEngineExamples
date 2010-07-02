package org.anddev.andengine.examples.benchmark;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.Scene;
import org.anddev.andengine.entity.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.handler.timer.ITimerCallback;
import org.anddev.andengine.entity.handler.timer.TimerHandler;
import org.anddev.andengine.entity.primitives.Rectangle;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.extension.physics.box2d.Box2DPhysicsSpace;
import org.anddev.andengine.extension.physics.box2d.adt.DynamicPhysicsBody;
import org.anddev.andengine.extension.physics.box2d.adt.PhysicsShape;
import org.anddev.andengine.extension.physics.box2d.adt.StaticPhysicsBody;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import android.hardware.SensorManager;
import android.view.MotionEvent;

/**
 * @author Nicolas Gramlich
 * @since 18:47:08 - 19.03.2010
 */
public class PhysicsBenchmark extends BaseBenchmark implements IOnSceneTouchListener {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;
	
	private static final int COUNT_HORIZONTAL = 17;
	private static final int COUNT_VERTICAL = 15;

	// ===========================================================
	// Fields
	// ===========================================================

	private Texture mTexture;

	private TiledTextureRegion mBoxFaceTextureRegion;
	private TiledTextureRegion mCircleFaceTextureRegion;

	private Box2DPhysicsSpace mPhysicsSpace;
	private int mFaceCount = 0;

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
	protected int getBenchmarkID() {
		return PHYSICSBENCHMARK_ID;
	}

	@Override
	protected float getBenchmarkStartOffset() {
		return 2;
	}

	@Override
	protected float getBenchmarkDuration() {
		return 20;
	}

	@Override
	public Engine onLoadEngine() {
		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera, false));
	}

	@Override
	public void onLoadResources() {
		this.mTexture = new Texture(64, 64, TextureOptions.BILINEAR);
		TextureRegionFactory.setAssetBasePath("gfx/");
		this.mBoxFaceTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mTexture, this, "boxface_tiled.png", 0, 0, 2, 1); // 64x32
		this.mCircleFaceTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mTexture, this, "circleface_tiled.png", 0, 32, 2, 1); // 64x32
		this.mEngine.getTextureManager().loadTexture(this.mTexture);
	}

	@Override
	public Scene onLoadScene() {
		this.mPhysicsSpace = new Box2DPhysicsSpace();
		this.mPhysicsSpace.createWorld(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		this.mPhysicsSpace.setGravity(0, SensorManager.GRAVITY_EARTH);

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
		
		for(int x = 1; x < COUNT_HORIZONTAL; x++) {
			for(int y = 1; y < COUNT_VERTICAL; y++) {
				final float pX = (((float)CAMERA_WIDTH) / COUNT_HORIZONTAL) * x + y;
				final float pY = (((float)CAMERA_HEIGHT) / COUNT_VERTICAL) * y;
				this.addFace(scene, pX, pY);
			}
		}

		scene.registerPreFrameHandler(new TimerHandler(2, new ITimerCallback() {
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {
				scene.unregisterPreFrameHandler(pTimerHandler);
				scene.registerPreFrameHandler(PhysicsBenchmark.this.mPhysicsSpace);
				scene.registerPreFrameHandler(new TimerHandler(10, new ITimerCallback() {
					@Override
					public void onTimePassed(TimerHandler pTimerHandler) {
						PhysicsBenchmark.this.mPhysicsSpace.setGravity(0, -SensorManager.GRAVITY_EARTH);
					}
				}));
			}
		}));

		return scene;
	}

	private void addFace(final Scene pScene, final float pX, final float pY) {
		this.mFaceCount++;

		final AnimatedSprite face;

		if(this.mFaceCount % 2 == 1) {
			face = new AnimatedSprite(pX - 16, pY - 16, this.mBoxFaceTextureRegion);
			this.mPhysicsSpace.addDynamicBody(new DynamicPhysicsBody(face, 1, 0.5f, 0.5f, PhysicsShape.RECTANGLE, false));
		} else {
			face = new AnimatedSprite(pX - 16, pY - 16, this.mCircleFaceTextureRegion);
			this.mPhysicsSpace.addDynamicBody(new DynamicPhysicsBody(face, 1, 0.5f, 0.5f, PhysicsShape.CIRCLE, false));
		}
		pScene.getTopLayer().addEntity(face);
	}

	@Override
	public boolean onSceneTouchEvent(final Scene pScene, final MotionEvent pSceneMotionEvent) {
		if(this.mPhysicsSpace != null) {
			if(pSceneMotionEvent.getAction() == MotionEvent.ACTION_DOWN) {
				this.addFace(pScene, pSceneMotionEvent.getX(), pSceneMotionEvent.getY());
				return true;
			}
		}
		return false;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
