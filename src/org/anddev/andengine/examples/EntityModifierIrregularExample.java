package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.anddev.andengine.entity.modifier.ParallelEntityModifier;
import org.anddev.andengine.entity.modifier.RotationByModifier;
import org.anddev.andengine.entity.modifier.RotationModifier;
import org.anddev.andengine.entity.modifier.ScaleModifier;
import org.anddev.andengine.entity.modifier.SequenceEntityModifier;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.util.modifier.IModifier;

import android.widget.Toast;

/**
 * @author Nicolas Gramlich
 * @since 21:42:39 - 06.07.2010
 */
public class EntityModifierIrregularExample extends BaseExample {
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
		Toast.makeText(this, "Shapes can have variable rotation and scale centers.", Toast.LENGTH_LONG).show();
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera));
	}

	@Override
	public void onLoadResources() {
		this.mTexture = new Texture(64, 32, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mFaceTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mTexture, this, "gfx/face_box_tiled.png", 0, 0, 2, 1);

		this.mEngine.getTextureManager().loadTexture(this.mTexture);
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene(1);
		scene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));

		final int centerX = (CAMERA_WIDTH - this.mFaceTextureRegion.getWidth()) / 2;
		final int centerY = (CAMERA_HEIGHT - this.mFaceTextureRegion.getHeight()) / 2;

		final AnimatedSprite face1 = new AnimatedSprite(centerX - 100, centerY, this.mFaceTextureRegion);
		face1.setRotationCenter(0, 0);
		face1.setScaleCenter(0, 0);
		face1.animate(100);

		final AnimatedSprite face2 = new AnimatedSprite(centerX + 100, centerY, this.mFaceTextureRegion);
		face2.animate(100);

		final SequenceEntityModifier EntityModifier = new SequenceEntityModifier(
				new IEntityModifierListener() {
					@Override
					public void onModifierFinished(final IModifier<IEntity> pEntityModifier, final IEntity pEntity) {
						EntityModifierIrregularExample.this.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(EntityModifierIrregularExample.this, "Sequence ended.", Toast.LENGTH_LONG).show();
							}
						});
					}
				},
				new ScaleModifier(2, 1.0f, 0.75f, 1.0f, 2.0f),
				new ScaleModifier(2, 0.75f, 2.0f, 2.0f, 1.25f),
				new ParallelEntityModifier(
						new ScaleModifier(3, 2.0f, 5.0f, 1.25f, 5.0f),
						new RotationByModifier(3, 180)
				),
				new ParallelEntityModifier(
						new ScaleModifier(3, 5, 1),
						new RotationModifier(3, 180, 0)
				)
		);

		face1.registerEntityModifier(EntityModifier);
		face2.registerEntityModifier(EntityModifier.clone());

		scene.getLastChild().attachChild(face1);
		scene.getLastChild().attachChild(face2);

		/* Create some not-modified sprites, that act as fixed references to the modified ones. */
		final AnimatedSprite face1Reference = new AnimatedSprite(centerX - 100, centerY, this.mFaceTextureRegion);
		final AnimatedSprite face2Reference = new AnimatedSprite(centerX + 100, centerY, this.mFaceTextureRegion);

		scene.getLastChild().attachChild(face1Reference);
		scene.getLastChild().attachChild(face2Reference);

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
