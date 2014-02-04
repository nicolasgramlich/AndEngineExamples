package org.andengine.examples;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.Text.TextOptions;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.detector.SurfaceGestureDetector.SurfaceGestureDetectorAdapter;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.HorizontalAlign;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

/**
 * (c) 2012 Ryan Antiquiera
 *
 * @author Ryan Antiquiera
 * @since 15:44:58 - 03.14.2012
 */
public class SurfaceGestureExample extends SimpleBaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	// ===========================================================
	// Fields
	// ===========================================================

	private SurfaceGestureDetectorAdapter mSGDA;
	private Font mFont;
	private Scene mScene;

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
	protected void onCreate(final Bundle pSavedInstanceState) {
		super.onCreate(pSavedInstanceState);
		
        this.mSGDA = new SurfaceGestureDetectorAdapter(){

            @Override
            protected boolean onSingleTap() {
            	onSurfaceGesture("Tap");
                return false;
            }

            @Override
            protected boolean onSwipeDown() {
            	onSurfaceGesture("Swipe\nDown");
                return false;
            }

            @Override
            protected boolean onSwipeLeft() {
            	onSurfaceGesture("Swipe\nLeft");
                return false;
            }

            @Override
            protected boolean onSwipeRight() {
            	onSurfaceGesture("Swipe\nRight");
                return false;
            }

            @Override
            protected boolean onSwipeUp() {
            	onSurfaceGesture("Swipe\nUp");
                return false;
            }

            @Override
            protected boolean onDoubleTap() {
            	onSurfaceGesture("Double\nTap");
                return false;
            }
           
        };
        
        this.mSGDA.setEnabled(true);
	}

	@Override
	protected void onCreateResources() {
		/* Load the font we are going to use. */
		FontFactory.setAssetBasePath("font/");
		this.mFont = FontFactory.createFromAsset(this.getFontManager(), this.getTextureManager(), 512, 512, TextureOptions.BILINEAR, this.getAssets(), "Plok.ttf", 32, true, Color.WHITE);
		this.mFont.load();		
	}

	@Override
	public EngineOptions onCreateEngineOptions() {
		Toast.makeText(this, "Try tap, double tap, and swipe!", Toast.LENGTH_LONG).show();

		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
	}

	@Override
	protected Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		this.mScene = new Scene();
		this.mScene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));

		this.mScene.setOnSceneTouchListener(this.mSGDA);

		return this.mScene;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	void onSurfaceGesture(CharSequence text) {
		final Text gestureText = new Text(0, 0, this.mFont, text, new TextOptions(HorizontalAlign.CENTER), this.getVertexBufferObjectManager());
		gestureText.setPosition((CAMERA_WIDTH - gestureText.getWidth()) * 0.5f, (CAMERA_HEIGHT - gestureText.getHeight()) * 0.5f);
		gestureText.setScale(0.0f);
		gestureText.registerEntityModifier(new ScaleModifier(0.5f, 0.0f, 2f));
		this.mScene.attachChild(gestureText);
		
		this.mScene.registerUpdateHandler(new TimerHandler(1.5f, new ITimerCallback() {
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {
				SurfaceGestureExample.this.mScene.unregisterUpdateHandler(pTimerHandler);
				SurfaceGestureExample.this.mScene.detachChild(gestureText);
			}
		}));
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
