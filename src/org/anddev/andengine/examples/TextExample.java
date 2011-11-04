package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.primitive.Line;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.Background;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.font.BitmapFont;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.ITexture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.bitmap.BitmapTexture.BitmapTextureFormat;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.util.HorizontalAlign;

import android.graphics.Typeface;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 11:54:51 - 03.04.2010
 */
public class TextExample extends BaseExample {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mCamera;
	private Font mFont;
	private BitmapFont mBitmapFont;

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
		final ITexture fontTexture = new BitmapTextureAtlas(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mFont = new Font(fontTexture, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32).load();
		
		this.mBitmapFont = new BitmapFont(this, "font/BitmapFont.fnt", TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mBitmapFont.loadTextures();
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
//		scene.attachChild(new Sprite(0, 0, TextureRegionFactory.extractFromTexture(this.mFont.getTexture())));
		scene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));

		final Text text = new Text(100, 100, this.mFont, "Hello Font!\nYou can even have multilined text!", HorizontalAlign.CENTER);
		final Text bitmapText = new Text(100, 200, this.mBitmapFont, "Hello BitmapFont!\nYou can even have multilined text!", HorizontalAlign.CENTER);
		scene.attachChild(new Line(100, 100, 500, 100));
		scene.attachChild(new Line(100, 100, 100, 500));
		scene.attachChild(new Line(100, 200, 500, 200));
		
		scene.attachChild(text);
		scene.attachChild(bitmapText);
		
//		final Text textCenter = new Text(100, 60, this.mFont, "Hello AndEngine!\nYou can even have multilined text!", HorizontalAlign.CENTER);
//		final Text textLeft = new Text(100, 200, this.mFont, "Also left aligned!\nLorem ipsum dolor sit amat...", HorizontalAlign.LEFT);
//		final Text textRight = new Text(100, 340, this.mFont, "And right aligned!\nLorem ipsum dolor sit amat...", HorizontalAlign.RIGHT);
//
//		scene.attachChild(textCenter);
//		scene.attachChild(textLeft);
//		scene.attachChild(textRight);

		
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
