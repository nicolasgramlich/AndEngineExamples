package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.primitive.Line;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.Background;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.font.BitmapFont;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
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
	private BitmapFont mBitmapFontWithKerning;

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
		this.mFont = FontFactory.create(256, 256, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32).load();
		
		this.mBitmapFont = new BitmapFont(this, "font/BitmapFont.fnt");
		this.mBitmapFont.loadTextures();
		this.mBitmapFontWithKerning = new BitmapFont(this, "font/BitmapFontWithKerning.fnt");
		this.mBitmapFontWithKerning.loadTextures();
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
//		scene.attachChild(new Sprite(0, 0, TextureRegionFactory.extractFromTexture(this.mFont.getTexture())));
		scene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));

		final Text text = new Text(100, 50, this.mFont, "Hello Font!\n1234567890!,\n.///////////\n.........pfnpsnbfka...", HorizontalAlign.CENTER);
		borderize(scene, text);
		
		final Text bitmapText = new Text(100, 200, this.mBitmapFont, "Hello BitmapFont!\n1234567890!,\n.///////////\n.........pfnpsnbfka...", HorizontalAlign.CENTER);
		borderize(scene, bitmapText);
		
		final Text bitmapTextWithKerning = new Text(100, 350, this.mBitmapFontWithKerning, "Hello BitmapFont!\n1234567890!,\n.///////////\n.........pfnpsnbfka...", HorizontalAlign.CENTER);
		borderize(scene, bitmapTextWithKerning);
		
		scene.attachChild(text);
		scene.attachChild(bitmapText);
		scene.attachChild(bitmapTextWithKerning);
		
//		final Text textCenter = new Text(100, 60, this.mFont, "Hello AndEngine!\nYou can even have multilined text!", HorizontalAlign.CENTER);
//		final Text textLeft = new Text(100, 200, this.mFont, "Also left aligned!\nLorem ipsum dolor sit amat...", HorizontalAlign.LEFT);
//		final Text textRight = new Text(100, 340, this.mFont, "And right aligned!\nLorem ipsum dolor sit amat...", HorizontalAlign.RIGHT);
//
//		scene.attachChild(textCenter);
//		scene.attachChild(textLeft);
//		scene.attachChild(textRight);

		return scene;
	}

	private void borderize(final Scene pScene, final Text pText) {
		final float left = pText.getX();
		final float top = pText.getY();
		final float right = left + pText.getWidth();
		final float bottom = top + pText.getHeight();
		pScene.attachChild(new Line(left, top, left, bottom)); // LEFT
		pScene.attachChild(new Line(right, top, right, bottom)); // RIGHT
		pScene.attachChild(new Line(left, top, right, top)); // TOP 
		pScene.attachChild(new Line(left, bottom, right, bottom)); // BOTTOM
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
