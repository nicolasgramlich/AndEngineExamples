package org.andengine.examples;

import java.io.IOException;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.scripting.AndEngineScriptingExtension;
import org.andengine.extension.scripting.Test;
import org.andengine.ui.activity.LayoutGameActivity;
import org.andengine.util.StreamUtils;
import org.andengine.util.debug.Debug;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 11:54:51 - 03.04.2010
 */
public class ScriptingExample extends LayoutGameActivity implements OnClickListener {
	// ===========================================================
	// Constants
	// ===========================================================

	static {
		System.loadLibrary("gnustl_shared");
		System.loadLibrary("andenginescriptingextension");
	}

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	// ===========================================================
	// Fields
	// ===========================================================

	private EditText mEditTextCode;

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
	protected int getLayoutID() {
		return R.layout.scriptingexample;
	}

	@Override
	protected int getRenderSurfaceViewID() {
		return R.id.scriptingexample_rendersurfaceview;
	}

	@Override
	public EngineOptions onCreateEngineOptions() {
		final Camera camera = new Camera(0, 0, ScriptingExample.CAMERA_WIDTH, ScriptingExample.CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, new RatioResolutionPolicy(ScriptingExample.CAMERA_WIDTH, ScriptingExample.CAMERA_HEIGHT), camera);
	}

	@Override
	public Engine onCreateEngine(final EngineOptions pEngineOptions) {
		return new Engine(pEngineOptions);
	}

	@Override
	protected void onSetContentView() {
		super.onSetContentView();

		this.mEditTextCode = (EditText) this.findViewById(R.id.scriptingexample_code);
		try {
			this.mEditTextCode.setText(StreamUtils.readFully(this.getAssets().open("js/scriptingexample.js")));
		} catch (final IOException e) {
			Debug.e(e);
		}

		this.findViewById(R.id.scriptingexample_code_apply).setOnClickListener(this);
	}

	@Override
	public void onCreateResources(final OnCreateResourcesCallback pOnCreateResourcesCallback) throws Exception {
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(final OnCreateSceneCallback pOnCreateSceneCallback) throws Exception {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
		scene.setOnAreaTouchTraversalFrontToBack();
		scene.setBackground(new Background(1, 1, 1, 1));

		pOnCreateSceneCallback.onCreateSceneFinished(scene);
	}

	@Override
	public void onPopulateScene(final Scene pScene, final OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	@Override
	public synchronized void onGameCreated() {
		super.onGameCreated();

		this.runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				AndEngineScriptingExtension.init(ScriptingExample.this, ScriptingExample.this.getEngine());

				final Sprite rectangle = (Sprite)Test.test();

				final Scene scene = ScriptingExample.this.getEngine().getScene();
				scene.attachChild(rectangle);
				scene.registerTouchArea(rectangle);
			}
		});
	}

	@Override
	public void onClick(final View pView) {
		final String script = this.mEditTextCode.getText().toString();

		this.runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				AndEngineScriptingExtension.runScript(script);
			}
		});
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
