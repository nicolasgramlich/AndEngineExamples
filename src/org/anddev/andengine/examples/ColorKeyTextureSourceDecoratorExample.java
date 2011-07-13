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
import org.anddev.andengine.opengl.texture.bitmap.BitmapTexture;
import org.anddev.andengine.opengl.texture.bitmap.BitmapTextureRegionFactory;
import org.anddev.andengine.opengl.texture.bitmap.source.AssetBitmapTextureSource;
import org.anddev.andengine.opengl.texture.bitmap.source.decorator.ColorKeyBitmapTextureSourceDecorator;
import org.anddev.andengine.opengl.texture.bitmap.source.decorator.shape.RectangleBitmapTextureSourceDecoratorShape;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

import android.graphics.Color;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
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

	private BitmapTexture mBitmapTexture;

	private TextureRegion mChromaticCircleTextureRegion;
	private TextureRegion mChromaticCircleColorKeyedTextureRegion;

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
		this.mBitmapTexture = new BitmapTexture(256, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		/* The actual AssetTextureSource. */
		final AssetBitmapTextureSource baseTextureSource = new AssetBitmapTextureSource(this, "gfx/chromatic_circle.png");

		this.mChromaticCircleTextureRegion = BitmapTextureRegionFactory.createFromSource(this.mBitmapTexture, baseTextureSource, 0, 0);

		/* We will remove both the red and the green segment of the chromatic circle,
		 * by nesting two ColorKeyTextureSourceDecorators around the actual baseTextureSource. */
		final int colorKeyRed = Color.rgb(255, 0, 51); // Red segment
		final int colorKeyGreen = Color.rgb(0, 179, 0); // Green segment
		final ColorKeyBitmapTextureSourceDecorator colorKeyedTextureSource = new ColorKeyBitmapTextureSourceDecorator(new ColorKeyBitmapTextureSourceDecorator(baseTextureSource, RectangleBitmapTextureSourceDecoratorShape.getDefaultInstance(), colorKeyRed), RectangleBitmapTextureSourceDecoratorShape.getDefaultInstance(), colorKeyGreen);

		this.mChromaticCircleColorKeyedTextureRegion = BitmapTextureRegionFactory.createFromSource(this.mBitmapTexture, colorKeyedTextureSource, 128, 0);

		this.mEngine.getTextureManager().loadTexture(this.mBitmapTexture);
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
