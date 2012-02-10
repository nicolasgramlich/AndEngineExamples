package org.andengine.examples;

import java.io.IOException;

import org.andengine.engine.Engine.EngineLock;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.util.FPSLogger;
import org.andengine.ui.activity.SimpleLayoutGameActivity;
import org.andengine.util.StreamUtils;
import org.andengine.util.debug.Debug;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 11:54:51 - 03.04.2010
 */
public class ScriptingExample extends SimpleLayoutGameActivity implements OnClickListener {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	// ===========================================================
	// Fields
	// ===========================================================

	private EditText mEditTextCode;

	private Context mJavascriptContext;
	private Scriptable mJavascriptScope;

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
	protected void onSetContentView() {
		super.onSetContentView();

		this.mEditTextCode = (EditText) this.findViewById(R.id.scriptingexample_code);
		try {
			this.mEditTextCode.setText(StreamUtils.readFully(this.getAssets().open("js/scriptingexample_rhino.js")));
		} catch (final IOException e) {
			Debug.e(e);
		}
		this.findViewById(R.id.scriptingexample_code_apply).setOnClickListener(this);
	}

	@Override
	public void onCreateResources() {
		this.runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				final EngineLock engineLock = ScriptingExample.this.mEngine.getEngineLock();
				engineLock.lock();

				ScriptingExample.this.mJavascriptContext = Context.enter();

				/* Turn off optimization (bytecode-generation). */
				ScriptingExample.this.mJavascriptContext.setOptimizationLevel(-1);

				ScriptingExample.this.mJavascriptScope = new ImporterTopLevel(ScriptingExample.this.mJavascriptContext);

				// Set a global variable that holds the activity instance.
				ScriptableObject.putProperty(ScriptingExample.this.mJavascriptScope, "mContext", Context.javaToJS(ScriptingExample.this, ScriptingExample.this.mJavascriptScope));

				engineLock.unlock();
			}
		});
	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
		scene.setOnAreaTouchTraversalFrontToBack();
		scene.setBackground(new Background(1, 1, 1, 1));

		return scene;
	}

	@Override
	public void onDestroyResources() throws Exception {
//		this.runOnUpdateThread(new Runnable() {
//			@Override
//			public void run() {
//				final EngineLock engineLock = ScriptingExample.this.mEngine.getEngineLock();
//				engineLock.lock();
//
//				Context.exit();
//
//				engineLock.unlock();
//			}
//		});

		super.onDestroyResources();
	}

	@Override
	public void onClick(final View pView) {
		switch(pView.getId()) {
			case R.id.scriptingexample_code_apply:
				this.applyCodeOnUpdateThread();
		}
	}

	void applyCodeOnUpdateThread() {
		final String code = this.mEditTextCode.getText().toString();

		this.runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				try {
					ScriptingExample.this.mJavascriptContext.evaluateString(ScriptingExample.this.mJavascriptScope, code, "<code>", 1, null);
				} catch (final Throwable t) {
					ScriptingExample.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(ScriptingExample.this, t.getMessage(), Toast.LENGTH_SHORT).show();
						}
					});
					Debug.e(t);
				}
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
