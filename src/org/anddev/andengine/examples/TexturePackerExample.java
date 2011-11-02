package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.Background;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.sprite.UncoloredSprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.examples.spritesheets.TexturePackerExampleSpritesheet;
import org.anddev.andengine.extension.texturepacker.opengl.texture.util.texturepacker.TexturePack;
import org.anddev.andengine.extension.texturepacker.opengl.texture.util.texturepacker.TexturePackLoader;
import org.anddev.andengine.extension.texturepacker.opengl.texture.util.texturepacker.TexturePackTextureRegionLibrary;
import org.anddev.andengine.extension.texturepacker.opengl.texture.util.texturepacker.exception.TexturePackParseException;
import org.anddev.andengine.opengl.texture.ITexture;
import org.anddev.andengine.opengl.texture.TextureManager;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.util.Debug;

/**
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 9:55:51 - 02.08.2011
 */
public class TexturePackerExample extends BaseExample {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mCamera;
	private ITexture mSpritesheetTexture;
	private TexturePackTextureRegionLibrary mSpritesheetTexturePackTextureRegionLibrary;

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
		try {
			final TexturePack spritesheetTexturePack = new TexturePackLoader(this, "gfx/spritesheets/").loadFromAsset(this, "texturepackerexample.xml");
			this.mSpritesheetTexture = spritesheetTexturePack.getTexture();
			this.mSpritesheetTexturePackTextureRegionLibrary = spritesheetTexturePack.getTexturePackTextureRegionLibrary();
			TextureManager.loadTexture(this.mSpritesheetTexture);
		} catch (final TexturePackParseException e) {
			Debug.e(e);
		}
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
		scene.setBackground(new Background(1, 1, 1));

		TextureRegion faceTextureRegion = this.mSpritesheetTexturePackTextureRegionLibrary.get(TexturePackerExampleSpritesheet.TEST_ID);
		/* Calculate the coordinates for the face, so its centered on the camera. */
		final int centerX = (CAMERA_WIDTH - faceTextureRegion.getWidth()) / 2;
		final int centerY = (CAMERA_HEIGHT - faceTextureRegion.getHeight()) / 2;

		/* Create the face and add it to the scene. */
		Sprite entity = new Sprite(centerX, centerY, faceTextureRegion);
		entity.setScale(20);
		scene.attachChild(entity);

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
