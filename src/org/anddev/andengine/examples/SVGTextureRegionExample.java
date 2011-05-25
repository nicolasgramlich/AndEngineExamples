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

import android.graphics.Color;

import com.larvalabs.svgandroid.adt.ISVGColorMapper;

/**
 * @author Nicolas Gramlich
 * @since 13:58:12 - 21.05.2011
 */
public class SVGTextureRegionExample extends BaseExample {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	private static final int SIZE = 128;
	
	private static final int COUNT = 6;
	private static final int COLUMNS = 3;
	private static final int ROWS = (int)Math.ceil((float)COUNT / COLUMNS);

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
		SVGTextureRegionFactory.setAssetBasePath("gfx/");

		this.mSVGTestTextureRegions = new TextureRegion[COUNT];
		int i = 0;
		this.mSVGTestTextureRegions[i++] = SVGTextureRegionFactory.createFromAsset(this.mBuildableTexture, this, "chick.svg", 16, 16);
		this.mSVGTestTextureRegions[i++] = SVGTextureRegionFactory.createFromAsset(this.mBuildableTexture, this, "chick.svg", 64, 64);
		this.mSVGTestTextureRegions[i++] = SVGTextureRegionFactory.createFromAsset(this.mBuildableTexture, this, "chick.svg", 256, 256);
		this.mSVGTestTextureRegions[i++] = SVGTextureRegionFactory.createFromAsset(this.mBuildableTexture, this, "badge.svg", 16, 16);
		this.mSVGTestTextureRegions[i++] = SVGTextureRegionFactory.createFromAsset(this.mBuildableTexture, this, "badge.svg", 64, 64);
		this.mSVGTestTextureRegions[i++] = SVGTextureRegionFactory.createFromAsset(this.mBuildableTexture, this, "badge.svg", 256, 256, new ISVGColorMapper() {
			@Override
			public Integer mapColor(Integer pColor) {
				if(pColor == null) {
					return null;
				} else {
					/* Swap red and green channel. */
					return Color.argb(0, Color.green(pColor), Color.red(pColor), Color.blue(pColor));
				}
			}
		});

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
		
		for(int i = 0; i < COUNT; i++) {
			final int row = i / COLUMNS;
			final int column = i % COLUMNS;
			
			final float centerX = this.mCamera.getWidth() / (COLUMNS + 1) * (column + 1);
			final float centerY = this.mCamera.getHeight() / (ROWS + 1) * (row + 1);
			
			final float x = centerX - SIZE * 0.5f;
			final float y = centerY - SIZE * 0.5f;
			scene.attachChild(new Sprite(x, y, SIZE, SIZE, this.mSVGTestTextureRegions[i]));
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
