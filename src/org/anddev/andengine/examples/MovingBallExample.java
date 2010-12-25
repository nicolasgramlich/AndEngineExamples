package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.physics.PhysicsHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

/**
 * @author Nicolas Gramlich
 * @since 11:54:51 - 03.04.2010
 */
public class MovingBallExample extends BaseExample {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	private static final float DEMO_VELOCITY = 100.0f;

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mCamera;

	private Texture mTexture;
	private TiledTextureRegion mFaceTextureRegion;

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
		this.mTexture = new Texture(64, 32, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mFaceTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mTexture, this, "gfx/face_circle_tiled.png", 0, 0, 2, 1);

		this.mEngine.getTextureManager().loadTexture(this.mTexture);
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene(1);
		scene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));

		final int centerX = (CAMERA_WIDTH - this.mFaceTextureRegion.getWidth()) / 2;
		final int centerY = (CAMERA_HEIGHT - this.mFaceTextureRegion.getHeight()) / 2;
		final Ball ball = new Ball(centerX, centerY, this.mFaceTextureRegion);
		final PhysicsHandler physicsHandler = new PhysicsHandler(ball);
		ball.registerUpdateHandler(physicsHandler);
		physicsHandler.setVelocity(DEMO_VELOCITY, DEMO_VELOCITY);

		scene.getLastChild().attachChild(ball);

		return scene;
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

	private static class Ball extends AnimatedSprite {
		private final PhysicsHandler mPhysicsHandler;

		public Ball(final float pX, final float pY, final TiledTextureRegion pTextureRegion) {
			super(pX, pY, pTextureRegion);
			this.mPhysicsHandler = new PhysicsHandler(this);
			this.registerUpdateHandler(this.mPhysicsHandler);
		}

		@Override
		protected void onManagedUpdate(final float pSecondsElapsed) {
			if(this.mX < 0) {
				this.mPhysicsHandler.setVelocityX(DEMO_VELOCITY);
			} else if(this.mX + this.getWidth() > CAMERA_WIDTH) {
				this.mPhysicsHandler.setVelocityX(-DEMO_VELOCITY);
			}

			if(this.mY < 0) {
				this.mPhysicsHandler.setVelocityY(DEMO_VELOCITY);
			} else if(this.mY + this.getHeight() > CAMERA_HEIGHT) {
				this.mPhysicsHandler.setVelocityY(-DEMO_VELOCITY);
			}

			super.onManagedUpdate(pSecondsElapsed);
		}
	}
}
