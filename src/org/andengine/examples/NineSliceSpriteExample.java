package org.andengine.examples;

import java.io.IOException;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.NineSliceSprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.bitmap.AssetBitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 15:12:58 - 01.05.2012
 */
public class NineSliceSpriteExample extends SimpleBaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 480;
	private static final int CAMERA_HEIGHT = 320;

	// ===========================================================
	// Fields
	// ===========================================================

	private ITextureRegion mNineSliceTextureRegion;
	private ITextureRegion mNineSliceButtonTextureRegion;
	private ITexture mNinesliceButtonTexture;
	private ITexture mNinesliceTexture;

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
		final Camera camera = new Camera(0, 0, NineSliceSpriteExample.CAMERA_WIDTH, NineSliceSpriteExample.CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, new RatioResolutionPolicy(NineSliceSpriteExample.CAMERA_WIDTH, NineSliceSpriteExample.CAMERA_HEIGHT), camera);
	}

	@Override
	public void onCreateResources() throws IOException {
		this.mNinesliceTexture = new AssetBitmapTexture(this.getTextureManager(), this.getAssets(), "gfx/nineslice.png", TextureOptions.BILINEAR);
		this.mNineSliceTextureRegion = TextureRegionFactory.extractFromTexture(this.mNinesliceTexture);
		this.mNinesliceTexture.load();

		this.mNinesliceButtonTexture = new AssetBitmapTexture(this.getTextureManager(), this.getAssets(), "gfx/button_nineslice.png", TextureOptions.BILINEAR);
		this.mNineSliceButtonTextureRegion = TextureRegionFactory.extractFromTexture(this.mNinesliceButtonTexture);
		this.mNinesliceButtonTexture.load();
	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
		scene.getBackground().setColor(0.09804f, 0.6274f, 0.8784f);

		final VertexBufferObjectManager vertexBufferObjectManager = this.getVertexBufferObjectManager();
		scene.attachChild(new NineSliceSprite(NineSliceSpriteExample.CAMERA_WIDTH * 0.33f, NineSliceSpriteExample.CAMERA_HEIGHT * 0.33f, 32, 32, this.mNineSliceTextureRegion, 2, 2, 2, 2, vertexBufferObjectManager));
		scene.attachChild(new NineSliceSprite(NineSliceSpriteExample.CAMERA_WIDTH * 0.66f, NineSliceSpriteExample.CAMERA_HEIGHT * 0.33f, 128, 64, this.mNineSliceTextureRegion, 4, 4, 4, 4, vertexBufferObjectManager));
		scene.attachChild(new NineSliceSprite(NineSliceSpriteExample.CAMERA_WIDTH * 0.33f, NineSliceSpriteExample.CAMERA_HEIGHT * 0.66f, 128, 128, this.mNineSliceButtonTextureRegion, 21, 38, 53, 19, vertexBufferObjectManager));
		scene.attachChild(new NineSliceSprite(NineSliceSpriteExample.CAMERA_WIDTH * 0.66f, NineSliceSpriteExample.CAMERA_HEIGHT * 0.66f, 196, 64, this.mNineSliceButtonTextureRegion, 21, 38, 53, 19, vertexBufferObjectManager));

		return scene;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
