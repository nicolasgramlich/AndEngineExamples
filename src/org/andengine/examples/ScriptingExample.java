package org.andengine.examples;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.scripting.ScriptingEnvironment;
import org.andengine.extension.scripting.Test;
import org.andengine.ui.activity.SimpleLayoutGameActivity;
import org.andengine.util.debug.Debug;
import org.andengine.util.system.SystemUtils;

import android.content.pm.PackageManager.NameNotFoundException;
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

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

//	public static native String stringFromJNI();
//	public static native String unimplementedStringFromJNI();

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
		this.mEditTextCode.setText("p00p");
		this.findViewById(R.id.scriptingexample_code_apply).setOnClickListener(this);
	}

	@Override
	public void onCreateResources() {
		try {
			ScriptingEnvironment.init(SystemUtils.getApkFilePath(this), this.getEngine());
		} catch (NameNotFoundException e) {
			Debug.e(e);
		}
	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
		scene.setOnAreaTouchTraversalFrontToBack();
		scene.setBackground(new Background(1, 0, 0, 1));

		scene.attachChild((IEntity)Test.test());

		return scene;
	}

	@Override
	public void onClick(final View pView) {
		/* Do sth. */
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
