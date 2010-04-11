package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.FPSCounter;
import org.anddev.andengine.entity.Scene;
import org.anddev.andengine.entity.shape.IModifierListener;
import org.anddev.andengine.entity.shape.IShapeModifier;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.entity.shape.modifier.AlphaModifier;
import org.anddev.andengine.entity.shape.modifier.DelayModifier;
import org.anddev.andengine.entity.shape.modifier.RotateByModifier;
import org.anddev.andengine.entity.shape.modifier.RotateModifier;
import org.anddev.andengine.entity.shape.modifier.ScaleModifier;
import org.anddev.andengine.entity.shape.modifier.SequenceModifier;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureManager;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import android.widget.Toast;

/**
 * @author Nicolas Gramlich
 * @since 11:54:51 - 03.04.2010
 */
public class SpriteModifierExample extends BaseExampleGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mCamera;
	private Texture mTexture;
	private TiledTextureRegion mFaceTextureRegion;

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
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera));
	}

	@Override
	public void onLoadResources() {
		this.mTexture = new Texture(64, 32);
		this.mFaceTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mTexture, this, "gfx/boxface_tiled.png", 0, 0, 2, 1);		
		
		TextureManager.loadTexture(this.mTexture);
	}

	@Override
	public Scene onLoadScene() {
		this.getEngine().registerPreFrameHandler(new FPSCounter());
		
		final Scene scene = new Scene(1);
		scene.setBackgroundColor(0.09804f, 0.6274f, 0.8784f);

		final int x = (CAMERA_WIDTH - this.mFaceTextureRegion.getWidth()) / 2;
		final int y = (CAMERA_HEIGHT - this.mFaceTextureRegion.getHeight()) / 2;
		final AnimatedSprite face = new AnimatedSprite(x, y, this.mFaceTextureRegion);
//		face.animate(100);
		
		face.addShapeModifier(new SequenceModifier(new IModifierListener() {
			@Override
			public void onModifierFinished(final IShapeModifier pShapeModifier, final Shape pShape) {
				SpriteModifierExample.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(SpriteModifierExample.this, "Sequence ended.", Toast.LENGTH_LONG).show();
					}
				});
			}
		},
		new RotateByModifier(5, 90),
		new DelayModifier(1),
		new AlphaModifier(3, 1, 0),
		new AlphaModifier(3, 0, 1),
		new ScaleModifier(3, 1, 0.5f),
		new ScaleModifier(3, 0.5f, 5),
		new ScaleModifier(3, 5, 1),
		new RotateModifier(5, 45, 90),
		new RotateByModifier(5, -90)));

		scene.getTopLayer().addEntity(face);

		return scene;
	}

	@Override
	public void onLoadComplete() {

	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
