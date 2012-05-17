package org.andengine.examples;

import java.io.IOException;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.bitmap.AssetBitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.pool.RunnablePoolItem;
import org.andengine.util.adt.pool.RunnablePoolUpdateHandler;

import android.widget.Toast;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 14:56:22 - 15.06.2011
 */
public class RunnablePoolUpdateHandlerExample extends SimpleBaseGameActivity implements IOnSceneTouchListener {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	private static final int SPRITE_COUNT = 2;

	// ===========================================================
	// Fields
	// ===========================================================

	private ITexture mFaceTexture;
	private ITextureRegion mFaceTextureRegion;

	private int mTargetSpriteIndex = 0;
	private final Sprite[] mSprites = new Sprite[SPRITE_COUNT];

	private final RunnablePoolUpdateHandler<SpriteRotateRunnablePoolItem> mFaceRotateRunnablePoolUpdateHandler = new RunnablePoolUpdateHandler<SpriteRotateRunnablePoolItem>() {
		@Override
		protected SpriteRotateRunnablePoolItem onAllocatePoolItem() {
			return new SpriteRotateRunnablePoolItem();
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
	public EngineOptions onCreateEngineOptions() {
		Toast.makeText(this, "Touch the screen to rotate the sprites using RunnablePoolItems.", Toast.LENGTH_LONG).show();

		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
	}

	@Override
	public void onCreateResources() throws IOException {
		this.mFaceTexture = new AssetBitmapTexture(this.getTextureManager(), this.getAssets(), "gfx/face_box.png", TextureOptions.BILINEAR);
		this.mFaceTextureRegion = TextureRegionFactory.extractFromTexture(this.mFaceTexture);
		this.mFaceTexture.load();
	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
		scene.getBackground().setColor(0.09804f, 0.6274f, 0.8784f);

		scene.registerUpdateHandler(this.mFaceRotateRunnablePoolUpdateHandler);

		final float centerX = CAMERA_WIDTH / 2;
		final float centerY = CAMERA_HEIGHT / 2;

		final VertexBufferObjectManager vertexBufferObjectManager = this.getVertexBufferObjectManager();
		this.mSprites[0] = new Sprite(centerX - 50, centerY, this.mFaceTextureRegion, vertexBufferObjectManager);
		this.mSprites[1] = new Sprite(centerX + 50, centerY, this.mFaceTextureRegion, vertexBufferObjectManager);

		scene.attachChild(this.mSprites[0]);
		scene.attachChild(this.mSprites[1]);

		scene.setOnSceneTouchListener(this);

		return scene;
	}

	@Override
	public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
		if(pSceneTouchEvent.isActionDown()) {
			this.mTargetSpriteIndex = (this.mTargetSpriteIndex + 1) % SPRITE_COUNT;

			final SpriteRotateRunnablePoolItem spriteRotateRunnablePoolItem = this.mFaceRotateRunnablePoolUpdateHandler.obtainPoolItem();
			spriteRotateRunnablePoolItem.setTargetSprite(this.mSprites[this.mTargetSpriteIndex]);
			this.mFaceRotateRunnablePoolUpdateHandler.postPoolItem(spriteRotateRunnablePoolItem);
		}
		return true;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public class SpriteRotateRunnablePoolItem extends RunnablePoolItem {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		private Sprite mTargetSprite;

		// ===========================================================
		// Constructors
		// ===========================================================

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		public void setTargetSprite(final Sprite pTargetSprite) {
			this.mTargetSprite = pTargetSprite;
		}

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		@Override
		public void run() {
			this.mTargetSprite.setRotation(this.mTargetSprite.getRotation() + 45);
		}

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
