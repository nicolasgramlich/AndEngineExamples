package org.andengine.examples;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import android.graphics.Color;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 11:54:51 - 03.04.2010
 */
public class CustomFontExample extends SimpleBaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	private static final int FONT_SIZE = 48;

	// ===========================================================
	// Fields
	// ===========================================================

	private Font mDroidFont;
	private Font mPlokFont;
	private Font mNeverwinterNightsFont;
	private Font mUnrealTournamenFont;
	private Font mKingdomOfHeartsFont;

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
		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
	}

	@Override
	public void onCreateResources() {
		/* The custom fonts. */
		FontFactory.setAssetBasePath("font/");
		this.mDroidFont = FontFactory.createFromAsset(this.getFontManager(), this.getTextureManager(), 256, 256, TextureOptions.BILINEAR, this.getAssets(), "Droid.ttf", FONT_SIZE, true, Color.BLACK);
		this.mDroidFont.load();

		this.mKingdomOfHeartsFont = FontFactory.createFromAsset(this.getFontManager(), this.getTextureManager(), 256, 256, TextureOptions.BILINEAR, this.getAssets(), "KingdomOfHearts.ttf", FONT_SIZE + 20, true, Color.BLACK);
		this.mKingdomOfHeartsFont.load();

		this.mNeverwinterNightsFont = FontFactory.createFromAsset(this.getFontManager(), this.getTextureManager(), 256, 256, TextureOptions.BILINEAR, this.getAssets(), "NeverwinterNights.ttf", FONT_SIZE, true, Color.BLACK);
		this.mNeverwinterNightsFont.load();

		this.mPlokFont = FontFactory.createFromAsset(this.getFontManager(), this.getTextureManager(), 256, 256, TextureOptions.BILINEAR, this.getAssets(), "Plok.ttf", FONT_SIZE, true, Color.BLACK);
		this.mPlokFont.load();

		this.mUnrealTournamenFont = FontFactory.createFromAsset(this.getFontManager(), this.getTextureManager(), 256, 256, TextureOptions.BILINEAR, this.getAssets(), "UnrealTournament.ttf", FONT_SIZE, true, Color.BLACK);
		this.mUnrealTournamenFont.load();
	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
		scene.getBackground().setColor(0.09804f, 0.6274f, 0.8784f);

		final float centerX = CAMERA_WIDTH / 2;
		final VertexBufferObjectManager vertexBufferObjectManager = this.getVertexBufferObjectManager();
		scene.attachChild(new Text(centerX, 400, this.mDroidFont, "Droid Font", vertexBufferObjectManager));
		scene.attachChild(new Text(centerX, 310, this.mKingdomOfHeartsFont, "Kingdom Of Hearts Font", vertexBufferObjectManager));
		scene.attachChild(new Text(centerX, 220, this.mNeverwinterNightsFont, "Neverwinter Nights Font", vertexBufferObjectManager));
		scene.attachChild(new Text(centerX, 130, this.mPlokFont, "Plok Font", vertexBufferObjectManager));
		scene.attachChild(new Text(centerX, 40, this.mUnrealTournamenFont, "Unreal Tournament Font", vertexBufferObjectManager));

		return scene;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
