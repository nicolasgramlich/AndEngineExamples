package org.andengine.examples;

import java.io.IOException;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.bitmap.AssetBitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.util.GLState;
import org.andengine.ui.activity.SimpleBaseGameActivity;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 10:10:10 - 10.10.2010
 */
public class Rotation3DExample extends SimpleBaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	// ===========================================================
	// Fields
	// ===========================================================

	private ITexture mFaceTexture;
	private ITextureRegion mFaceTextureRegion;

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
		final Camera camera = new Camera(0, 0, Rotation3DExample.CAMERA_WIDTH, Rotation3DExample.CAMERA_HEIGHT);
		camera.setZClippingPlanes(-100, 100);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, new RatioResolutionPolicy(Rotation3DExample.CAMERA_WIDTH, Rotation3DExample.CAMERA_HEIGHT), camera);
	}

	@Override
	public void onCreateResources() throws IOException {
		this.mFaceTexture = new AssetBitmapTexture(this.getTextureManager(), this.getAssets(), "gfx/face_box.png", TextureOptions.BILINEAR);
		this.mFaceTextureRegion = TextureRegionFactory.extractFromTexture(this.mFaceTexture);
		this.mFaceTexture.load();
	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
		scene.getBackground().setColor(0.09804f, 0.6274f, 0.8784f);

		final float centerX = Rotation3DExample.CAMERA_WIDTH / 2;
		final float centerY = Rotation3DExample.CAMERA_HEIGHT / 2;

		/* Create the sprite and add it to the scene. */
		final Sprite sprite = new Sprite(centerX, centerY, this.mFaceTextureRegion, this.getVertexBufferObjectManager()) {
			@Override
			protected void applyRotation(final GLState pGLState) {
				final float rotation = this.mRotation;

				if(rotation != 0) {
					final float localRotationCenterX = this.mLocalRotationCenterX;
					final float localRotationCenterY = this.mLocalRotationCenterY;

					pGLState.translateModelViewGLMatrixf(localRotationCenterX, localRotationCenterY, 0);
					/* Note we are applying rotation around the y-axis and not the z-axis anymore! */
					pGLState.rotateModelViewGLMatrixf(-rotation, 0, 1, 0);
					pGLState.translateModelViewGLMatrixf(-localRotationCenterX, -localRotationCenterY, 0);
				}
			}
		};
		sprite.registerEntityModifier(new LoopEntityModifier(new RotationModifier(6, 0, 360)));
		scene.attachChild(sprite);

		return scene;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
