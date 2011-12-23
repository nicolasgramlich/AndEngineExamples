package org.andengine.examples;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.augmentedreality.BaseAugmentedRealityGameActivity;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;

import android.widget.Toast;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 11:54:51 - 03.04.2010
 */
//public class AugmentedRealityExample extends BaseAugmentedRealityGameActivity {
//	// ===========================================================
//	// Constants
//	// ===========================================================
//
//	private static final int CAMERA_WIDTH = 720;
//	private static final int CAMERA_HEIGHT = 480;
//
//	// ===========================================================
//	// Fields
//	// ===========================================================
//
//	private BitmapTextureAtlas mBitmapTextureAtlas;
//	private ITextureRegion mFaceTextureRegion;
//
//	// ===========================================================
//	// Constructors
//	// ===========================================================
//
//	// ===========================================================
//	// Getter & Setter
//	// ===========================================================
//
//	// ===========================================================
//	// Methods for/from SuperClass/Interfaces
//	// ===========================================================
//
//	@Override
//	public EngineOptions onCreateEngineOptions() {
//		Toast.makeText(this, "If you don't see a sprite moving over the screen, try starting this while already being in Landscape orientation!!", Toast.LENGTH_LONG).show();
//
//		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
//
//		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
//	}
//
//	@Override
//	public void onCreateResources() {
//		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
//
//		this.mBitmapTextureAtlas = new BitmapTextureAtlas(32, 32, TextureOptions.BILINEAR);
//		this.mFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "face_box.png", 0, 0);
//		this.mBitmapTextureAtlas.load(this.getTextureManager());
//	}
//
//	@Override
//	public Scene onCreateScene() {
//		this.mEngine.registerUpdateHandler(new FPSLogger());
//
//		final Scene scene = new Scene();
//		//		scene.setBackgroundEnabled(false);
//		scene.setBackground(new Background(0.0f, 0.0f, 0.0f, 0.0f));
//
//		final int centerX = (CAMERA_WIDTH - this.mFaceTextureRegion.getWidth()) / 2;
//		final int centerY = (CAMERA_HEIGHT - this.mFaceTextureRegion.getHeight()) / 2;
//		final Sprite face = new Sprite(centerX, centerY, this.mFaceTextureRegion);
//		face.registerEntityModifier(new MoveModifier(30, 0, CAMERA_WIDTH - face.getWidth(), 0, CAMERA_HEIGHT - face.getHeight()));
//		scene.attachChild(face);
//
//		return scene;
//	}
//
//	@Override
//	public void onGameCreated() {
//
//	}
//
//	// ===========================================================
//	// Methods
//	// ===========================================================
//
//	// ===========================================================
//	// Inner and Anonymous Classes
//	// ===========================================================
//}
