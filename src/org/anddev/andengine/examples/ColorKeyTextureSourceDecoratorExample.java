package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.source.AssetTextureSource;
import org.anddev.andengine.opengl.texture.source.decorator.ColorKeyTextureSourceDecorator;
import org.anddev.andengine.opengl.texture.source.decorator.shape.RectangleTextureSourceDecoratorShape;

import android.graphics.Color;

/**
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

	private Texture mTexture;

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
		this.mTexture = new Texture(256, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		/* The actual AssetTextureSource. */
		final AssetTextureSource baseTextureSource = new AssetTextureSource(this, "gfx/chromatic_circle.png");

		this.mChromaticCircleTextureRegion = TextureRegionFactory.createFromSource(this.mTexture, baseTextureSource, 0, 0);

		/* We will remove both the red and the green segment of the chromatic circle,
		 * by nesting two ColorKeyTextureSourceDecorators around the actual baseTextureSource. */
		final int colorKeyRed = Color.rgb(255, 0, 51); // Red segment
		final int colorKeyGreen = Color.rgb(0, 179, 0); // Green segment
		final ColorKeyTextureSourceDecorator colorKeyedTextureSource = new ColorKeyTextureSourceDecorator(new ColorKeyTextureSourceDecorator(baseTextureSource, RectangleTextureSourceDecoratorShape.getDefaultInstance(), colorKeyRed), RectangleTextureSourceDecoratorShape.getDefaultInstance(), colorKeyGreen);

		this.mChromaticCircleColorKeyedTextureRegion = TextureRegionFactory.createFromSource(this.mTexture, colorKeyedTextureSource, 128, 0);

		this.mEngine.getTextureManager().loadTexture(this.mTexture);
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene(1);

		final int centerX = (CAMERA_WIDTH - this.mChromaticCircleTextureRegion.getWidth()) / 2;
		final int centerY = (CAMERA_HEIGHT - this.mChromaticCircleTextureRegion.getHeight()) / 2;

		final Sprite chromaticCircle = new Sprite(centerX - 80, centerY, this.mChromaticCircleTextureRegion);

		final Sprite chromaticCircleColorKeyed = new Sprite(centerX + 80, centerY, this.mChromaticCircleColorKeyedTextureRegion);

		final IEntity lastChild = scene.getLastChild();
		lastChild.attachChild(chromaticCircle);
		lastChild.attachChild(chromaticCircleColorKeyed);

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
