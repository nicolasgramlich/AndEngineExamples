package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.atlas.bitmap.source.AssetBitmapTextureAtlasSource;
import org.anddev.andengine.opengl.texture.atlas.bitmap.source.decorator.ColorKeyBitmapTextureAtlasSourceDecorator;
import org.anddev.andengine.opengl.texture.atlas.bitmap.source.decorator.shape.RectangleBitmapTextureAtlasSourceDecoratorShape;
import org.anddev.andengine.opengl.texture.region.ITextureRegion;

import android.graphics.Color;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 11:54:51 - 03.04.2010
 */
public class ColorKeyTextureSourceDecoratorExample extends BaseExample {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 480;
	private static final int CAMERA_HEIGHT = 320;

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mCamera;

	private BitmapTextureAtlas mBitmapTextureAtlas;

	private ITextureRegion mChromaticCircleTextureRegion;
	private ITextureRegion mChromaticCircleColorKeyedTextureRegion;

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
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(256, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		/* The actual AssetTextureSource. */
		final AssetBitmapTextureAtlasSource baseTextureSource = new AssetBitmapTextureAtlasSource(this, "gfx/chromatic_circle.png");

		this.mChromaticCircleTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromSource(this.mBitmapTextureAtlas, baseTextureSource, 0, 0);

		/* We will remove both the red and the green segment of the chromatic circle,
		 * by nesting two ColorKeyTextureSourceDecorators around the actual baseTextureSource. */
		final int colorKeyRed = Color.rgb(255, 0, 51); // Red segment
		final int colorKeyGreen = Color.rgb(0, 179, 0); // Green segment
		final ColorKeyBitmapTextureAtlasSourceDecorator colorKeyBitmapTextureAtlasSource = new ColorKeyBitmapTextureAtlasSourceDecorator(new ColorKeyBitmapTextureAtlasSourceDecorator(baseTextureSource, RectangleBitmapTextureAtlasSourceDecoratorShape.getDefaultInstance(), colorKeyRed), RectangleBitmapTextureAtlasSourceDecoratorShape.getDefaultInstance(), colorKeyGreen);

		this.mChromaticCircleColorKeyedTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromSource(this.mBitmapTextureAtlas, colorKeyBitmapTextureAtlasSource, 128, 0);

		this.mEngine.getTextureManager().loadTexture(this.mBitmapTextureAtlas);
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();

		final int centerX = (CAMERA_WIDTH - this.mChromaticCircleTextureRegion.getWidth()) / 2;
		final int centerY = (CAMERA_HEIGHT - this.mChromaticCircleTextureRegion.getHeight()) / 2;

		final Sprite chromaticCircle = new Sprite(centerX - 80, centerY, this.mChromaticCircleTextureRegion);

		final Sprite chromaticCircleColorKeyed = new Sprite(centerX + 80, centerY, this.mChromaticCircleColorKeyedTextureRegion);

		scene.attachChild(chromaticCircle);
		scene.attachChild(chromaticCircleColorKeyed);

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
