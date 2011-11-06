package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.Entity;
import org.anddev.andengine.entity.modifier.DelayModifier;
import org.anddev.andengine.entity.modifier.LoopEntityModifier;
import org.anddev.andengine.entity.modifier.RotationModifier;
import org.anddev.andengine.entity.modifier.SequenceEntityModifier;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.entity.util.ScreenCapture;
import org.anddev.andengine.entity.util.ScreenCapture.IScreenCaptureCallback;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.region.ITextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.render.RenderTexture;
import org.anddev.andengine.opengl.util.GLState;
import org.anddev.andengine.util.FileUtils;
import org.anddev.andengine.util.modifier.ease.EaseQuadInOut;

import android.widget.Toast;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
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
		this.mCamera = new Camera(0, 0, RectangleExample.CAMERA_WIDTH, RectangleExample.CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(RectangleExample.CAMERA_WIDTH, RectangleExample.CAMERA_HEIGHT), this.mCamera)) {
			private boolean mRenderTextureInitialized;
			private RenderTexture mRenderTextureA;
			private RenderTexture mRenderTextureB;
			private Sprite mRenderTextureASprite;
			private Sprite mRenderTextureBSprite;

			private RenderTexture mPrimaryRenderTexture;
			private RenderTexture mSecondaryRenderTexture;

			private Sprite mPrimaryRenderTextureSprite;
			private Sprite mSecondaryRenderTextureSprite;

			@Override
			public void onDrawFrame() throws InterruptedException {
				final boolean firstFrame = !this.mRenderTextureInitialized;

				final int surfaceWidth = this.mCamera.getSurfaceWidth();
				final int surfaceHeight = this.mCamera.getSurfaceHeight();

				if(!this.mRenderTextureInitialized) {
					this.mRenderTextureInitialized = true;
					
					this.mRenderTextureA = new RenderTexture(surfaceWidth, surfaceHeight);
					this.mRenderTextureA.init();
					this.mRenderTextureB = new RenderTexture(surfaceWidth, surfaceHeight);
					this.mRenderTextureB.init();

					final ITextureRegion renderTextureATextureRegion = TextureRegionFactory.extractFromTexture(this.mRenderTextureA);
					this.mRenderTextureASprite = new Sprite(0, 0, surfaceWidth, surfaceHeight, renderTextureATextureRegion);

					final ITextureRegion renderTextureBTextureRegion = TextureRegionFactory.extractFromTexture(this.mRenderTextureB);
					this.mRenderTextureBSprite = new Sprite(0, 0, surfaceWidth, surfaceHeight, renderTextureBTextureRegion);

					this.mPrimaryRenderTexture = this.mRenderTextureA;
					this.mPrimaryRenderTextureSprite = this.mRenderTextureASprite;
					this.mSecondaryRenderTexture = this.mRenderTextureB;
					this.mSecondaryRenderTextureSprite = this.mRenderTextureBSprite;
				}

				this.mPrimaryRenderTexture.begin();
				{
					/* Draw current frame. */
					super.onDrawFrame();

					/* Draw previous frame with reduced alpha. */
					if(!firstFrame) {
						GLState.pushProjectionGLMatrix();
						GLState.orthoProjectionGLMatrixf(0, surfaceWidth, 0, surfaceHeight, -1, 1);
						{
							this.mSecondaryRenderTextureSprite.setAlpha(0.975f);
							this.mSecondaryRenderTextureSprite.onDraw(this.mCamera);
						}
						GLState.popProjectionGLMatrix();
					}
				}
				this.mPrimaryRenderTexture.end();

				/* Draw combined frame with full alpha. */
				{
					GLState.pushProjectionGLMatrix();
					GLState.orthoProjectionGLMatrixf(0, surfaceWidth, 0, surfaceHeight, -1, 1);
					{
						this.mPrimaryRenderTextureSprite.setAlpha(1);
						this.mPrimaryRenderTextureSprite.onDraw(this.mCamera);
					}
					GLState.popProjectionGLMatrix();
				}

				/* Flip RenderTextures. */
				final Sprite tmpSprite = this.mPrimaryRenderTextureSprite;
				this.mPrimaryRenderTextureSprite = this.mSecondaryRenderTextureSprite;
				this.mSecondaryRenderTextureSprite = tmpSprite;

				final RenderTexture tmpRenderTexture = this.mPrimaryRenderTexture;
				this.mPrimaryRenderTexture = this.mSecondaryRenderTexture;
				this.mSecondaryRenderTexture = tmpRenderTexture;
			}
		};
	}

	@Override
	public void onLoadResources() {

	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();

		final ScreenCapture screenCapture = new ScreenCapture();
		scene.attachChild(screenCapture);
		scene.setOnSceneTouchListener(new IOnSceneTouchListener() {
			@Override
			public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
				if(pSceneTouchEvent.isActionDown()) {
					screenCapture.capture(180, 60, 360, 360, FileUtils.getAbsolutePathOnExternalStorage(RectangleExample.this, "Screen_" + System.currentTimeMillis() + ".png"), new IScreenCaptureCallback() {
						@Override
						public void onScreenCaptured(final String pFilePath) {
							RectangleExample.this.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(RectangleExample.this, "Screenshot: " + pFilePath + " taken!", Toast.LENGTH_SHORT).show();
								}
							});
						}

						@Override
						public void onScreenCaptureFailed(final String pFilePath, final Exception pException) {
							RectangleExample.this.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(RectangleExample.this, "FAILED capturing Screenshot: " + pFilePath + " !", Toast.LENGTH_SHORT).show();
								}
							});
						}
					});
				}
				return true;
			}
		});

		/* Create the rectangles. */
		final Rectangle rect1 = this.makeColoredRectangle(-180, -180, 1, 0, 0);
		final Rectangle rect2 = this.makeColoredRectangle(0, -180, 0, 1, 0);
		final Rectangle rect3 = this.makeColoredRectangle(0, 0, 0, 0, 1);
		final Rectangle rect4 = this.makeColoredRectangle(-180, 0, 1, 1, 0);

		final Entity rectangleGroup = new Entity(CAMERA_WIDTH / 2, CAMERA_HEIGHT / 2);
		rectangleGroup.registerEntityModifier(new LoopEntityModifier(new SequenceEntityModifier(new RotationModifier(10, 0, 7200, EaseQuadInOut.getInstance()), new DelayModifier(2))));

		rectangleGroup.attachChild(rect1);
		rectangleGroup.attachChild(rect2);
		rectangleGroup.attachChild(rect3);
		rectangleGroup.attachChild(rect4);

		scene.attachChild(rectangleGroup);

		return scene;
	}

	private Rectangle makeColoredRectangle(final float pX, final float pY, final float pRed, final float pGreen, final float pBlue) {
		final Rectangle coloredRect = new Rectangle(pX, pY, 180, 180);
		coloredRect.setColor(pRed, pGreen, pBlue);
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
