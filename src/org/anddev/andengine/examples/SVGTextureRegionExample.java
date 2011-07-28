package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.extension.svg.adt.ISVGColorMapper;
import org.anddev.andengine.extension.svg.adt.SVGDirectColorMapper;
import org.anddev.andengine.extension.svg.opengl.texture.atlas.bitmap.SVGBitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.anddev.andengine.opengl.texture.buildable.builder.BlackPawnTextureBuilder;
import org.anddev.andengine.opengl.texture.buildable.builder.ITextureBuilder.TextureAtlasSourcePackingException;
import org.anddev.andengine.opengl.texture.region.BaseTextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.util.Debug;

import android.graphics.Color;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
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

	private static final int COUNT = 12;
	private static final int COLUMNS = 4;
	private static final int ROWS = (int)Math.ceil((float)COUNT / COLUMNS);

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mCamera;
	private BuildableBitmapTextureAtlas mBuildableBitmapTextureAtlas;
	private BaseTextureRegion[] mSVGTestTextureRegions;

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
		this.mBuildableBitmapTextureAtlas = new BuildableBitmapTextureAtlas(1024, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		SVGBitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		this.mSVGTestTextureRegions = new BaseTextureRegion[COUNT];
		int i = 0;
		this.mSVGTestTextureRegions[i++] = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBuildableBitmapTextureAtlas, this, "chick.svg", 16, 16);
		this.mSVGTestTextureRegions[i++] = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBuildableBitmapTextureAtlas, this, "chick.svg", 32, 32);
		this.mSVGTestTextureRegions[i++] = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBuildableBitmapTextureAtlas, this, "chick.svg", 64, 64);
		this.mSVGTestTextureRegions[i++] = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBuildableBitmapTextureAtlas, this, "chick.svg", 128, 128);
		this.mSVGTestTextureRegions[i++] = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBuildableBitmapTextureAtlas, this, "badge.svg", 16, 16);
		this.mSVGTestTextureRegions[i++] = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBuildableBitmapTextureAtlas, this, "badge.svg", 64, 64);
		this.mSVGTestTextureRegions[i++] = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBuildableBitmapTextureAtlas, this, "badge.svg", 128, 128, new ISVGColorMapper() {
			@Override
			public Integer mapColor(final Integer pColor) {
				if(pColor == null) {
					return null;
				} else {
					/* Swap blue and green channel. */
					return Color.argb(0, Color.red(pColor), Color.blue(pColor), Color.green(pColor));
				}
			}
		});
		this.mSVGTestTextureRegions[i++] = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBuildableBitmapTextureAtlas, this, "badge.svg", 256, 256, new ISVGColorMapper() {
			@Override
			public Integer mapColor(final Integer pColor) {
				if(pColor == null) {
					return null;
				} else {
					/* Swap red and green channel. */
					return Color.argb(0, Color.green(pColor), Color.red(pColor), Color.blue(pColor));
				}
			}
		});
		this.mSVGTestTextureRegions[i++] = SVGBitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBuildableBitmapTextureAtlas, this, "pacdroid.svg", 64, 64, 2, 2);
		this.mSVGTestTextureRegions[i++] = SVGBitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBuildableBitmapTextureAtlas, this, "pacdroid.svg", 256, 256, 2, 2);
		final SVGDirectColorMapper angryPacDroidSVGColorMapper = new SVGDirectColorMapper();
		angryPacDroidSVGColorMapper.addColorMapping(0xA7CA4A, 0xEA872A);
		angryPacDroidSVGColorMapper.addColorMapping(0xC1DA7F, 0xFAA15F);
		this.mSVGTestTextureRegions[i++] = SVGBitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBuildableBitmapTextureAtlas, this, "pacdroid.svg", 256, 256, angryPacDroidSVGColorMapper, 2, 2);
		this.mSVGTestTextureRegions[i++] = SVGBitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBuildableBitmapTextureAtlas, this, "pacdroid_apples.svg", 256, 256, 2, 2);

		try {
			this.mBuildableBitmapTextureAtlas.build(new BlackPawnTextureBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(1));
		} catch (final TextureAtlasSourcePackingException e) {
			Debug.e(e);
		}

		this.mEngine.getTextureManager().loadTexture(this.mBuildableBitmapTextureAtlas);
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
		scene.setBackground(new ColorBackground(0.5f, 0.5f, 0.5f));

		for(int i = 0; i < COUNT; i++) {
			final int row = i / COLUMNS;
			final int column = i % COLUMNS;

			final float centerX = this.mCamera.getWidth() / (COLUMNS + 1) * (column + 1);
			final float centerY = this.mCamera.getHeight() / (ROWS + 1) * (row + 1);

			final float x = centerX - SIZE * 0.5f;
			final float y = centerY - SIZE * 0.5f;
			final BaseTextureRegion baseTextureRegion = this.mSVGTestTextureRegions[i];
			if(baseTextureRegion instanceof TextureRegion) {
				final TextureRegion textureRegion = (TextureRegion)baseTextureRegion;
				scene.attachChild(new Sprite(x, y, SIZE, SIZE, textureRegion));
			} else if(baseTextureRegion instanceof TiledTextureRegion) {
				final TiledTextureRegion tiledTextureRegion = (TiledTextureRegion)baseTextureRegion;
				final AnimatedSprite animatedSprite = new AnimatedSprite(x, y, SIZE, SIZE, tiledTextureRegion);
				animatedSprite.animate(500);
				scene.attachChild(animatedSprite);
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
