package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.ITextureRegion;
import org.anddev.andengine.util.pool.RunnablePoolItem;
import org.anddev.andengine.util.pool.RunnablePoolUpdateHandler;

import android.widget.Toast;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 14:56:22 - 15.06.2011
 */
public class RunnablePoolUpdateHandlerExample extends BaseExample implements IOnSceneTouchListener {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	private static final int FACE_COUNT = 2;

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mCamera;
	private BitmapTextureAtlas mBitmapTextureAtlas;
	private ITextureRegion mFaceTextureRegion;

	private int mTargetFaceIndex = 0;
	private final Sprite[] mFaces = new Sprite[FACE_COUNT];

	private final RunnablePoolUpdateHandler<FaceRotateRunnablePoolItem> mFaceRotateRunnablePoolUpdateHandler = new RunnablePoolUpdateHandler<FaceRotateRunnablePoolItem>() {
		@Override
		protected FaceRotateRunnablePoolItem onAllocatePoolItem() {
			return new FaceRotateRunnablePoolItem();
		}
	};

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
		Toast.makeText(this, "Touch the screen to rotate the sprites using RunnablePoolItems.", Toast.LENGTH_LONG).show();
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera));
	}

	@Override
	public void onLoadResources() {
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(32, 32, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "gfx/face_box.png", 0, 0);

		this.mEngine.getTextureManager().loadTexture(this.mBitmapTextureAtlas);
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
		scene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));

		scene.registerUpdateHandler(this.mFaceRotateRunnablePoolUpdateHandler);

		/* Calculate the coordinates for the face, so its centered on the camera. */
		final int centerX = (CAMERA_WIDTH - this.mFaceTextureRegion.getWidth()) / 2;
		final int centerY = (CAMERA_HEIGHT - this.mFaceTextureRegion.getHeight()) / 2;

		this.mFaces[0] = new Sprite(centerX - 50, centerY, this.mFaceTextureRegion);
		this.mFaces[1] = new Sprite(centerX + 50, centerY, this.mFaceTextureRegion);
		scene.attachChild(this.mFaces[0]);
		scene.attachChild(this.mFaces[1]);

		scene.setOnSceneTouchListener(this);

		return scene;
	}

	@Override
	public void onLoadComplete() {

	}

	@Override
	public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
		if(pSceneTouchEvent.isActionDown()) {
			this.mTargetFaceIndex = (this.mTargetFaceIndex + 1) % FACE_COUNT;

			final FaceRotateRunnablePoolItem faceRotateRunnablePoolItem = this.mFaceRotateRunnablePoolUpdateHandler.obtainPoolItem();
			faceRotateRunnablePoolItem.setTargetFace(this.mFaces[this.mTargetFaceIndex]);
			this.mFaceRotateRunnablePoolUpdateHandler.postPoolItem(faceRotateRunnablePoolItem);
		}
		return true;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public class FaceRotateRunnablePoolItem extends RunnablePoolItem {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		private Sprite mTargetFace;

		// ===========================================================
		// Constructors
		// ===========================================================

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		public void setTargetFace(final Sprite pTargetFace) {
			this.mTargetFace = pTargetFace;
		}

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		@Override
		public void run() {
			this.mTargetFace.setRotation(this.mTargetFace.getRotation() + 45);
		}

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
