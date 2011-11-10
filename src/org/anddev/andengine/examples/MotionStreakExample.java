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
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.region.ITextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.render.RenderTexture;
import org.anddev.andengine.opengl.util.GLState;
import org.anddev.andengine.util.modifier.ease.EaseQuadInOut;

import android.widget.Toast;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 16:55:18 - 06.11.2011
 */
public class MotionStreakExample extends BaseExample implements IOnSceneTouchListener {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mCamera;

	private boolean mMotionStreaking = true;

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
		this.mCamera = new Camera(0, 0, MotionStreakExample.CAMERA_WIDTH, MotionStreakExample.CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(MotionStreakExample.CAMERA_WIDTH, MotionStreakExample.CAMERA_HEIGHT), this.mCamera)) {
			private static final int RENDERTEXTURE_COUNT = 2;

			private boolean mRenderTextureInitialized;

			private final RenderTexture[] mRenderTextures = new RenderTexture[RENDERTEXTURE_COUNT];
			private final Sprite[] mRenderTextureSprites = new Sprite[RENDERTEXTURE_COUNT];

			private int mCurrentRenderTextureIndex = 0;

			@Override
			public void onDrawFrame() throws InterruptedException {
				final boolean firstFrame = !this.mRenderTextureInitialized;

				if(firstFrame) {
					this.initRenderTextures();
					this.mRenderTextureInitialized = true;
				}

				final int surfaceWidth = this.mCamera.getSurfaceWidth();
				final int surfaceHeight = this.mCamera.getSurfaceHeight();

				final int currentRenderTextureIndex = this.mCurrentRenderTextureIndex;
				final int otherRenderTextureIndex = (currentRenderTextureIndex + 1) % RENDERTEXTURE_COUNT;

				this.mRenderTextures[currentRenderTextureIndex].begin(false, true);
				{
					/* Draw current frame. */
					super.onDrawFrame();

					/* Draw previous frame with reduced alpha. */
					if(!firstFrame) {
						if(MotionStreakExample.this.mMotionStreaking) {
							this.mRenderTextureSprites[otherRenderTextureIndex].setAlpha(0.9f);
							this.mRenderTextureSprites[otherRenderTextureIndex].onDraw(this.mCamera);
						}
					}
				}
				this.mRenderTextures[currentRenderTextureIndex].end();

				/* Draw combined frame with full alpha. */
				{
					GLState.pushProjectionGLMatrix();
					GLState.orthoProjectionGLMatrixf(0, surfaceWidth, 0, surfaceHeight, -1, 1);
					{
						this.mRenderTextureSprites[otherRenderTextureIndex].setAlpha(1);
						this.mRenderTextureSprites[otherRenderTextureIndex].onDraw(this.mCamera);
					}
					GLState.popProjectionGLMatrix();
				}

				/* Flip RenderTextures. */
				this.mCurrentRenderTextureIndex = otherRenderTextureIndex;
			}

			private void initRenderTextures() {
				final int surfaceWidth = this.mCamera.getSurfaceWidth();
				final int surfaceHeight = this.mCamera.getSurfaceHeight();

				for(int i = 0; i <= 1; i++) {
					this.mRenderTextures[i] = new RenderTexture(surfaceWidth, surfaceHeight);
					this.mRenderTextures[i].init();

					final ITextureRegion renderTextureATextureRegion = TextureRegionFactory.extractFromTexture(this.mRenderTextures[i]);
					this.mRenderTextureSprites[i] = new Sprite(0, 0, renderTextureATextureRegion);
				}
			}
		};
	}

	@Override
	public void onLoadResources() {

	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		/* Create a nice scene with some rectangles. */
		final Scene scene = new Scene();

		final Entity rectangleGroup = new Entity(MotionStreakExample.CAMERA_WIDTH / 2, MotionStreakExample.CAMERA_HEIGHT / 2);

		rectangleGroup.attachChild(this.makeColoredRectangle(-180, -180, 1, 0, 0));
		rectangleGroup.attachChild(this.makeColoredRectangle(0, -180, 0, 1, 0));
		rectangleGroup.attachChild(this.makeColoredRectangle(0, 0, 0, 0, 1));
		rectangleGroup.attachChild(this.makeColoredRectangle(-180, 0, 1, 1, 0));

		/* Spin the rectangles. */
		rectangleGroup.registerEntityModifier(new LoopEntityModifier(new SequenceEntityModifier(new RotationModifier(10, 0, 7200, EaseQuadInOut.getInstance()), new DelayModifier(2))));

		scene.attachChild(rectangleGroup);
		
		/* TouchListener */
		scene.setOnSceneTouchListener(this);

		return scene;
	}

	@Override
	public void onLoadComplete() {

	}

	@Override
	public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
		if(pSceneTouchEvent.isActionDown()) {
			MotionStreakExample.this.mMotionStreaking = !MotionStreakExample.this.mMotionStreaking;

			MotionStreakExample.this.runOnUiThread(new Runnable(){
				@Override
				public void run() {
					Toast.makeText(MotionStreakExample.this, "MotionStreaking " + (MotionStreakExample.this.mMotionStreaking ? "enabled." : "disabled."), Toast.LENGTH_SHORT).show();
				}
			});
		}
		return true;
	}

	// ===========================================================
	// Methods
	// ===========================================================
	
	private Rectangle makeColoredRectangle(final float pX, final float pY, final float pRed, final float pGreen, final float pBlue) {
		final Rectangle coloredRect = new Rectangle(pX, pY, 180, 180);
		coloredRect.setColor(pRed, pGreen, pBlue);
		return coloredRect;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
