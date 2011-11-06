package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.modifier.LoopEntityModifier;
import org.anddev.andengine.entity.modifier.RotationModifier;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.Background;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.ITextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.render.RenderTexture;
import org.anddev.andengine.opengl.util.GLState;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 11:54:51 - 03.04.2010
 */
public class SpriteExample extends BaseExample {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mCamera;
	private BitmapTextureAtlas mBitmapTextureAtlas;
	private ITextureRegion mFaceTextureRegion;

	private boolean mRenderTextureInitialized;
	private final RenderTexture mRenderTextureA = new RenderTexture(512, 512);
	private final RenderTexture mRenderTextureB = new RenderTexture(512, 512);
	private Sprite mRenderTextureASprite;
	private Sprite mRenderTextureBSprite;

	private RenderTexture mPrimaryRenderTexture;
	private RenderTexture mSecondaryRenderTexture;

	private Sprite mPrimaryRenderTextureSprite;
	private Sprite mSecondaryRenderTextureSprite;

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
		this.mCamera = new Camera(0, 0, SpriteExample.CAMERA_WIDTH, SpriteExample.CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(SpriteExample.CAMERA_WIDTH, SpriteExample.CAMERA_HEIGHT), this.mCamera)) {
			@Override
			public void onDrawFrame() throws InterruptedException {
				final int surfaceWidth = this.mCamera.getSurfaceWidth();
				final int surfaceHeight = this.mCamera.getSurfaceHeight();

				boolean firstFrame = false;
				if(!SpriteExample.this.mRenderTextureInitialized) {
					firstFrame = true;

					SpriteExample.this.mRenderTextureInitialized = true;
					SpriteExample.this.mRenderTextureA.init();
					SpriteExample.this.mRenderTextureB.init();

					final ITextureRegion renderTextureATextureRegion = TextureRegionFactory.extractFromTexture(SpriteExample.this.mRenderTextureA);
					SpriteExample.this.mRenderTextureASprite = new Sprite(0, 0, surfaceWidth, surfaceHeight, renderTextureATextureRegion);
					SpriteExample.this.mRenderTextureASprite.setScaleY(-1);

					final ITextureRegion renderTextureBTextureRegion = TextureRegionFactory.extractFromTexture(SpriteExample.this.mRenderTextureB);
					SpriteExample.this.mRenderTextureBSprite = new Sprite(0, 0, surfaceWidth, surfaceHeight, renderTextureBTextureRegion);
					SpriteExample.this.mRenderTextureBSprite.setScaleY(-1);

					SpriteExample.this.mPrimaryRenderTexture = SpriteExample.this.mRenderTextureA;
					SpriteExample.this.mPrimaryRenderTextureSprite = SpriteExample.this.mRenderTextureASprite;
					SpriteExample.this.mSecondaryRenderTexture = SpriteExample.this.mRenderTextureB;
					SpriteExample.this.mSecondaryRenderTextureSprite = SpriteExample.this.mRenderTextureBSprite;
				}

				SpriteExample.this.mPrimaryRenderTexture.begin();
				{
					/* Draw current frame. */
					super.onDrawFrame();

					/* Draw previous frame with reduced alpha. */
					if(!firstFrame){
						GLState.pushProjectionGLMatrix();
						GLState.orthoProjectionGLMatrixf(0, surfaceWidth, surfaceHeight, 0, -1, 1);
						SpriteExample.this.mSecondaryRenderTextureSprite.setAlpha(0.975f);
						SpriteExample.this.mSecondaryRenderTextureSprite.onDraw(this.mCamera);
						GLState.popProjectionGLMatrix();
					}
				}
				SpriteExample.this.mPrimaryRenderTexture.end();

				/* Draw combined frame with full alpha. */
				{
					GLState.pushProjectionGLMatrix();
					GLState.orthoProjectionGLMatrixf(0, surfaceWidth, surfaceHeight, 0, -1, 1);
					SpriteExample.this.mPrimaryRenderTextureSprite.setAlpha(1);
					SpriteExample.this.mPrimaryRenderTextureSprite.onDraw(this.mCamera);
					GLState.popProjectionGLMatrix();
				}

				final Sprite tmpSprite = SpriteExample.this.mPrimaryRenderTextureSprite;
				SpriteExample.this.mPrimaryRenderTextureSprite = SpriteExample.this.mSecondaryRenderTextureSprite;
				SpriteExample.this.mSecondaryRenderTextureSprite = tmpSprite;

				final RenderTexture tmpRenderTexture = SpriteExample.this.mPrimaryRenderTexture;
				SpriteExample.this.mPrimaryRenderTexture = SpriteExample.this.mSecondaryRenderTexture;
				SpriteExample.this.mSecondaryRenderTexture = tmpRenderTexture;
			}
		};
	}

	@Override
	public void onLoadResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		this.mBitmapTextureAtlas = new BitmapTextureAtlas(32, 32, TextureOptions.NEAREST_PREMULTIPLYALPHA);
		this.mFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "face_box.png", 0, 0);
		this.mBitmapTextureAtlas.load();
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();

		/* Calculate the coordinates for the face, so its centered on the camera. */
		final int centerX = (SpriteExample.CAMERA_WIDTH - this.mFaceTextureRegion.getWidth()) / 2;
		final int centerY = (SpriteExample.CAMERA_HEIGHT - this.mFaceTextureRegion.getHeight()) / 2;

		/* Create the face and add it to the scene. */
		final Sprite face = new Sprite(centerX, centerY, this.mFaceTextureRegion);
		face.registerEntityModifier(new LoopEntityModifier(new RotationModifier(3, 0, 360)));
		face.setScale(10);
		scene.attachChild(face);

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
}
