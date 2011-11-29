package org.andengine.examples;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.StrokeFont;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;

import android.graphics.Color;
import android.graphics.Typeface;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 22:49:43 - 26.07.2010
 */
public class StrokeFontExample extends BaseExample {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	private static final int FONT_SIZE = 48;

	// ===========================================================
	// Fields
	// ===========================================================

	private Font mFont;
	private StrokeFont mStrokeFont;
	private StrokeFont mStrokeOnlyFont;

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
		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
	}

	@Override
	public void onCreateResources() {
		final ITexture fontTexture = new BitmapTextureAtlas(256, 256, TextureOptions.BILINEAR);
		final ITexture strokeFontTexture = new BitmapTextureAtlas(256, 256, TextureOptions.BILINEAR);
		final ITexture strokeOnlyFontTexture = new BitmapTextureAtlas(256, 256, TextureOptions.BILINEAR);

		this.mFont = new Font(fontTexture, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), FONT_SIZE, true, Color.BLACK).load();
		this.mStrokeFont = new StrokeFont(strokeFontTexture, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), FONT_SIZE, true, Color.BLACK, 2, Color.WHITE).load();
		this.mStrokeOnlyFont = new StrokeFont(strokeOnlyFontTexture, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), FONT_SIZE, true, Color.BLACK, 2, Color.WHITE, true).load();
	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
		scene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));

		final Text textNormal = new Text(100, 100, this.mFont, "Just some normal Text.");
		final Text textStroke = new Text(100, 200, this.mStrokeFont, "Text with fill and stroke.");
		final Text textStrokeOnly = new Text(100, 300, this.mStrokeOnlyFont, "Text with stroke only.");

		scene.attachChild(textNormal);
		scene.attachChild(textStroke);
		scene.attachChild(textStrokeOnly);

		return scene;
	}

	@Override
	public void onGameCreated() {

	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
