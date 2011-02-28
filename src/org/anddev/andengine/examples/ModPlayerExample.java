package org.anddev.andengine.examples;

import java.util.concurrent.Callable;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnAreaTouchListener;
import org.anddev.andengine.entity.scene.Scene.ITouchArea;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.util.Callback;
import org.anddev.andengine.util.FileUtils;
import org.helllabs.android.xmp.ModPlayer;

import android.widget.Toast;

/**
 * @author Nicolas Gramlich
 * @since 15:51:47 - 13.06.2010
 */
public class ModPlayerExample extends BaseExample {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	private static final String SAMPLE_MOD_DIRECTORY = "mfx/";
	private static final String SAMPLE_MOD_FILENAME = "lepeltheme.mod";

	// ===========================================================
	// Fields
	// ===========================================================

	private Texture mTexture;
	private TextureRegion mILove8BitTextureRegion;

	private final ModPlayer mModPlayer = ModPlayer.getInstance();

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
		Toast.makeText(this, "Touch the image to toggle the playback of this awesome 8-bit style .MOD music.", Toast.LENGTH_LONG).show();
		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera));
	}

	@Override
	public void onLoadResources() {
		this.mTexture = new Texture(128, 128, TextureOptions.DEFAULT);
		TextureRegionFactory.setAssetBasePath("gfx/");
		this.mILove8BitTextureRegion = TextureRegionFactory.createFromAsset(this.mTexture, this, "i_love_8_bit.png", 0, 0);

		if(FileUtils.isFileExistingOnExternalStorage(this, SAMPLE_MOD_DIRECTORY + SAMPLE_MOD_FILENAME)) {
			this.startPlayingMod();
		} else {
			this.doAsync(R.string.dialog_modplayerexample_loading_to_external_title, R.string.dialog_modplayerexample_loading_to_external_message, new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					FileUtils.ensureDirectoriesExistOnExternalStorage(ModPlayerExample.this, SAMPLE_MOD_DIRECTORY);
					FileUtils.copyToExternalStorage(ModPlayerExample.this, SAMPLE_MOD_DIRECTORY + SAMPLE_MOD_FILENAME, SAMPLE_MOD_DIRECTORY + SAMPLE_MOD_FILENAME);
					return null;
				}
			}, new Callback<Void>() {
				@Override
				public void onCallback(final Void pCallbackValue) {
					ModPlayerExample.this.startPlayingMod();
				}
			});
		}

		this.mEngine.getTextureManager().loadTexture(this.mTexture);
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene(1);
		scene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));

		final int x = (CAMERA_WIDTH - this.mILove8BitTextureRegion.getWidth()) / 2;
		final int y = (CAMERA_HEIGHT - this.mILove8BitTextureRegion.getHeight()) / 2;

		final Sprite iLove8Bit = new Sprite(x, y, this.mILove8BitTextureRegion);
		scene.getLastChild().attachChild(iLove8Bit);

		scene.registerTouchArea(iLove8Bit);
		scene.setOnAreaTouchListener(new IOnAreaTouchListener() {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final ITouchArea pTouchArea, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionDown()) {
					ModPlayerExample.this.mModPlayer.pause();
				}

				return true;
			}
		});

		return scene;
	}

	@Override
	public void onLoadComplete() {

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ModPlayerExample.this.mModPlayer.stop();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void startPlayingMod() {
		this.mModPlayer.play(FileUtils.getAbsolutePathOnExternalStorage(this, SAMPLE_MOD_DIRECTORY + SAMPLE_MOD_FILENAME));
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
