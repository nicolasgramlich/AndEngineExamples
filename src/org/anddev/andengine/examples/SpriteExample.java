package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.source.AssetTextureSource;
import org.anddev.andengine.opengl.texture.source.ColorKeyTextureSourceDecorator;
import org.anddev.andengine.opengl.texture.source.ITextureSource;
import org.anddev.andengine.opengl.texture.source.OutlineTextureSourceDecorator;

import android.graphics.Color;

/**
 * @author Nicolas Gramlich
 * @since 11:54:51 - 03.04.2010
 */
public class SpriteExample extends BaseExample {
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
	private TextureRegion mFaceFilteredTextureRegion;
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
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera));
	}

	@Override
	public void onLoadResources() {
		this.mTexture = new Texture(128, 32, TextureOptions.BILINEAR);
		final ITextureSource faceTextureSource = new AssetTextureSource(this, "gfx/face_box.png");
		this.mFaceTextureRegion = TextureRegionFactory.createFromSource(this.mTexture, faceTextureSource, 0, 0);
		this.mFaceFilteredTextureRegion = TextureRegionFactory.createFromSource(this.mTexture, new ColorKeyTextureSourceDecorator(faceTextureSource, Color.parseColor("#A7C1E7")), 33, 0);
		
//		this.mFaceTextureRegion = TextureRegionFactory.createFromSource(this.mTexture, new OutlineTextureSourceDecorator(new ColorKeyTextureSourceDecorator(baseTextureSource, Color.parseColor("#A7C1E7")), Color.GREEN), 0, 0);

		this.mEngine.getTextureManager().loadTexture(this.mTexture);
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene(1);
		scene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));

		/* Calculate the coordinates for the face, so its centered on the camera. */
		final int centerX = (CAMERA_WIDTH - this.mFaceFilteredTextureRegion.getWidth()) / 2;
		final int centerY = (CAMERA_HEIGHT - this.mFaceFilteredTextureRegion.getHeight()) / 2;

		final Sprite face = new Sprite(centerX - 64, centerY, this.mFaceTextureRegion);
		face.setScale(2);
		scene.getTopLayer().addEntity(face);
		
		final Sprite faceFiltered = new Sprite(centerX + 64, centerY, this.mFaceFilteredTextureRegion);
		faceFiltered.setScale(2);
		scene.getTopLayer().addEntity(faceFiltered);

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
