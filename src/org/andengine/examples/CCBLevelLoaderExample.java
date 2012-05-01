package org.andengine.examples;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.cocosbuilder.CCBLevelLoader;
import org.andengine.extension.cocosbuilder.CCBLevelLoaderResult;
import org.andengine.ui.activity.SimpleBaseGameActivity;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 11:41:40 - 19.04.2012
 */
public class CCBLevelLoaderExample extends SimpleBaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 480;
	private static final int CAMERA_HEIGHT = 320;

	// ===========================================================
	// Fields
	// ===========================================================

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
		final Camera camera = new Camera(0, 0, CCBLevelLoaderExample.CAMERA_WIDTH, CCBLevelLoaderExample.CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, new RatioResolutionPolicy(CCBLevelLoaderExample.CAMERA_WIDTH, CCBLevelLoaderExample.CAMERA_HEIGHT), camera);
	}

	@Override
	public void onCreateResources() {

	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();

		final CCBLevelLoader ccbLevelLoader = new CCBLevelLoader(this.getAssets(), "ccb/", this.getVertexBufferObjectManager(), this.getTextureManager(), this.getFontManager());
		CCBLevelLoaderResult ccbLevelLoaderResult = ccbLevelLoader.loadLevelFromAsset(this.getAssets(), "ccb/example.ccbaex");

		scene.attachChild(ccbLevelLoaderResult.getRootEntity());

		return scene;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
