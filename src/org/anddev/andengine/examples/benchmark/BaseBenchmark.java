package org.anddev.andengine.examples.benchmark;

import org.anddev.andengine.entity.handler.timer.ITimerCallback;
import org.anddev.andengine.entity.handler.timer.TimerHandler;
import org.anddev.andengine.entity.util.FPSCounter;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

/**
 * @author Nicolas Gramlich
 * @since 10:38:36 - 27.06.2010
 */
public abstract class BaseBenchmark extends BaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int DIALOG_SHOW_RESULT = 1;

	// ===========================================================
	// Fields
	// ===========================================================

	private float mFPS;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================;

	protected void showResult(final float pFPS) {
		this.mFPS = pFPS;
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				BaseBenchmark.this.showDialog(DIALOG_SHOW_RESULT);
			}
		});
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract float getBenchmarkDuration();

	protected abstract float getBenchmarkStartOffset();

	@Override
	public void onLoadComplete() {
		this.mEngine.registerPostFrameHandler(new TimerHandler(this.getBenchmarkStartOffset(), new ITimerCallback() {
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {
				BaseBenchmark.this.mEngine.unregisterPostFrameHandler(pTimerHandler);
				BaseBenchmark.this.setUpBenchmarkHandling();
			}
		}));
	}

	protected void setUpBenchmarkHandling() {
		final FPSCounter fpsCounter = new FPSCounter();
		this.getEngine().registerPreFrameHandler(fpsCounter);

		this.getEngine().registerPostFrameHandler(new TimerHandler(this.getBenchmarkDuration(), new ITimerCallback() {
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {
				BaseBenchmark.this.showResult(fpsCounter.getFPS());
			}
		}));
	}

	@Override
	protected Dialog onCreateDialog(final int pID) {
		switch(pID) {
			case DIALOG_SHOW_RESULT:
				return new AlertDialog.Builder(this)
				.setTitle(this.getClass().getSimpleName() + "-Results")
				.setMessage(String.format("FPS: %.2d", this.mFPS))
				.setPositiveButton(android.R.string.ok, new OnClickListener() {
					@Override
					public void onClick(final DialogInterface pDialog, final int pWhich) {
						BaseBenchmark.this.finish();
					}
				})
				.create();
			default:
				return super.onCreateDialog(pID);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
