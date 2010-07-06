package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.Scene;
import org.anddev.andengine.entity.shape.IModifierListener;
import org.anddev.andengine.entity.shape.IShapeModifier;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.entity.shape.modifier.ParallelModifier;
import org.anddev.andengine.entity.shape.modifier.RotationByModifier;
import org.anddev.andengine.entity.shape.modifier.RotationModifier;
import org.anddev.andengine.entity.shape.modifier.ScaleModifier;
import org.anddev.andengine.entity.shape.modifier.SequenceModifier;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import android.widget.Toast;

/**
 * @author Nicolas Gramlich
 * @since 21:42:39 - 06.07.2010
 */
public class ShapeModifierIrregularExample extends BaseExample {
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
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera, false));
	}

	@Override
	public void onLoadResources() {
		this.mTexture = new Texture(64, 32, TextureOptions.BILINEAR);
		this.mFaceTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mTexture, this, "gfx/boxface_tiled.png", 0, 0, 2, 1);

		this.getEngine().getTextureManager().loadTexture(this.mTexture);
	}

	@Override
	public Scene onLoadScene() {
		this.getEngine().registerPreFrameHandler(new FPSLogger());

		final Scene scene = new Scene(1);
		scene.setBackgroundColor(0.09804f, 0.6274f, 0.8784f);

		final int x = (CAMERA_WIDTH - this.mFaceTextureRegion.getWidth()) / 2;
		final int y = (CAMERA_HEIGHT - this.mFaceTextureRegion.getHeight()) / 2;
		
		final AnimatedSprite face1Reference = new AnimatedSprite(x - 100, y, this.mFaceTextureRegion);
		final AnimatedSprite face2Reference = new AnimatedSprite(x + 100, y, this.mFaceTextureRegion);

		final AnimatedSprite face1 = new AnimatedSprite(x - 100, y, this.mFaceTextureRegion);
		face1.setRotationCenter(0, 0);
		face1.setScaleCenter(0, 0);
		face1.animate(100);

		final AnimatedSprite face2 = new AnimatedSprite(x + 100, y, this.mFaceTextureRegion);
		face2.animate(100);

		final SequenceModifier shapeModifier = new SequenceModifier(
				new IModifierListener() {
					@Override
					public void onModifierFinished(final IShapeModifier pShapeModifier, final Shape pShape) {
						ShapeModifierIrregularExample.this.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(ShapeModifierIrregularExample.this, "Sequence ended.", Toast.LENGTH_LONG).show();
							}
						});
					}
				},
				new ScaleModifier(2, 1, 1.25f, 1, 2.5f),
				new ParallelModifier(
					new ScaleModifier(3, 1.25f, 5, 2.5f, 5),
					new RotationByModifier(3, 90)
				),
				new ParallelModifier(
					new ScaleModifier(3, 5, 1),
					new RotationModifier(3, 180, 0)
				)
		);

		face1.addShapeModifier(shapeModifier);
		face2.addShapeModifier(shapeModifier.clone());

		scene.getTopLayer().addEntity(face1);
		scene.getTopLayer().addEntity(face2);
		scene.getTopLayer().addEntity(face1Reference);
		scene.getTopLayer().addEntity(face2Reference);

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
