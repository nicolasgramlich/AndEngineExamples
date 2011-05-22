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
import org.anddev.andengine.extension.svg.opengl.texture.region.SVGTextureRegionFactory;
import org.anddev.andengine.opengl.texture.BuildableTexture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.builder.BlackPawnTextureBuilder;
import org.anddev.andengine.opengl.texture.builder.ITextureBuilder.TextureSourcePackingException;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.util.Debug;

/**
 * @author Nicolas Gramlich
 * @since 13:58:12 - 21.05.2011
 */
public class SVGTextureRegionExample extends BaseExample {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 480;
	private static final int CAMERA_HEIGHT = 320;
	private static final int SVG_TEST_COUNT = 4;

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mCamera;
	private BuildableTexture mBuildableTexture;
	private TextureRegion[] mSVGTestTextureRegions;

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
		this.mBuildableTexture = new BuildableTexture(1024, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		this.mSVGTestTextureRegions = new TextureRegion[SVG_TEST_COUNT];
		this.mSVGTestTextureRegions[0] = SVGTextureRegionFactory.createFromAsset(this.mBuildableTexture, this, "gfx/svg_test_simple_0.svg");
//		this.mSVGTestTextureRegions[1] = SVGTextureRegionFactory.createFromAsset(this.mBuildableTexture, this, "gfx/svg_test_simple_1.svg");
//		this.mSVGTestTextureRegions[2] = SVGTextureRegionFactory.createFromAsset(this.mBuildableTexture, this, "gfx/svg_test_bunny_ai.svg");
//		this.mSVGTestTextureRegions[3] = SVGTextureRegionFactory.createFromAsset(this.mBuildableTexture, this, "gfx/svg_test_bunny_inkscape.svg");

		try {
			this.mBuildableTexture.build(new BlackPawnTextureBuilder(1));
		} catch (final TextureSourcePackingException e) {
			Debug.e(e);
		}

		this.mEngine.getTextureManager().loadTexture(this.mBuildableTexture);
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene(0);
		scene.setBackground(new ColorBackground(1, 1, 1));

		final int size = 100;
		final float centerY = this.mCamera.getHeight() * 0.5f;
		for(int i = 0; i < SVG_TEST_COUNT; i++) {
			final float centerX = this.mCamera.getWidth() / (SVG_TEST_COUNT + 1) * (i + 1);
			final TextureRegion textureRegion = this.mSVGTestTextureRegions[i];
			if(textureRegion != null) {
				scene.attachChild(new Sprite(centerX - size * 0.5f, centerY - size * 0.5f, size, size, textureRegion));
			}
		}

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
