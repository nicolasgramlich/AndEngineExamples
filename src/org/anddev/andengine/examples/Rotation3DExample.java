package org.anddev.andengine.examples;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.modifier.LoopEntityModifier;
import org.anddev.andengine.entity.modifier.RotationModifier;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.util.GLHelper;

/**
 * @author Nicolas Gramlich
 * @since 10:10:10 - 10.10.2010
 */
public class Rotation3DExample extends BaseExample {
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
	private TextureRegion mFaceTextureRegion;

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
		this.mCamera.setZClippingPlanes(-100, 100);
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera));
	}

	@Override
	public void onLoadResources() {
		this.mTexture = new Texture(32, 32, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mFaceTextureRegion = TextureRegionFactory.createFromAsset(this.mTexture, this, "gfx/face_box.png", 0, 0);

		this.mEngine.getTextureManager().loadTexture(this.mTexture);
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene(1);
		scene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));

		/* Calculate the coordinates for the face, so its centered on the camera. */
		final int centerX = (CAMERA_WIDTH - this.mFaceTextureRegion.getWidth()) / 2;
		final int centerY = (CAMERA_HEIGHT - this.mFaceTextureRegion.getHeight()) / 2;

		/* Create the face and add it to the scene. */
		final Sprite face = new Sprite(centerX, centerY, this.mFaceTextureRegion) {
			@Override
			protected void applyRotation(final GL10 pGL) {
				/* Disable culling so we can see the backside of this sprite. */
				GLHelper.disableCulling(pGL);

				final float rotation = this.mRotation;

				if(rotation != 0) {
					final float rotationCenterX = this.mRotationCenterX;
					final float rotationCenterY = this.mRotationCenterY;

					pGL.glTranslatef(rotationCenterX, rotationCenterY, 0);
					/* Note we are applying rotation around the y-axis and not the z-axis anymore! */
					pGL.glRotatef(rotation, 0, 1, 0);
					pGL.glTranslatef(-rotationCenterX, -rotationCenterY, 0);
				}
			}

			@Override
			protected void drawVertices(final GL10 pGL, final Camera pCamera) {
				super.drawVertices(pGL, pCamera);

				/* Enable culling as 'normal' entities profit from culling. */
				GLHelper.enableCulling(pGL);
			}
		};
		face.registerEntityModifier(new LoopEntityModifier(new RotationModifier(6, 0, 360)));
		scene.getLastChild().attachChild(face);

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
