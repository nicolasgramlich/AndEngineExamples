package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;

import android.graphics.Color;

/**
 * @author Nicolas Gramlich
 * @since 11:54:51 - 03.04.2010
 */
public class CustomFontExample extends BaseExample {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	private static final int FONT_SIZE = 48;

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mCamera;


	private Font mDroidFont;
	private Font mPlokFont;
	private Font mNeverwinterNightsFont;
	private Font mUnrealTournamenFont;
	private Font mKingdomOfHeartsFont;

	private Texture mDroidFontTexture;
	private Texture mPlokFontTexture;
	private Texture mNeverwinterNightsFontTexture;
	private Texture mUnrealTournamentFontTexture;
	private Texture mKingdomOfHeartsFontTexture;

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
		/* The custom fonts. */
		this.mDroidFontTexture = new Texture(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mKingdomOfHeartsFontTexture = new Texture(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mNeverwinterNightsFontTexture = new Texture(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mPlokFontTexture = new Texture(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mUnrealTournamentFontTexture = new Texture(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		FontFactory.setAssetBasePath("font/");
		this.mDroidFont = FontFactory.createFromAsset(this.mDroidFontTexture, this, "Droid.ttf", FONT_SIZE, true, Color.BLACK);
		this.mKingdomOfHeartsFont = FontFactory.createFromAsset(this.mKingdomOfHeartsFontTexture, this, "KingdomOfHearts.ttf", FONT_SIZE + 20, true, Color.BLACK);
		this.mNeverwinterNightsFont = FontFactory.createFromAsset(this.mNeverwinterNightsFontTexture, this, "NeverwinterNights.ttf", FONT_SIZE, true, Color.BLACK);
		this.mPlokFont = FontFactory.createFromAsset(this.mPlokFontTexture, this, "Plok.ttf", FONT_SIZE, true, Color.BLACK);
		this.mUnrealTournamenFont = FontFactory.createFromAsset(this.mUnrealTournamentFontTexture, this, "UnrealTournament.ttf", FONT_SIZE, true, Color.BLACK);

		this.mEngine.getTextureManager().loadTextures(this.mDroidFontTexture, this.mKingdomOfHeartsFontTexture, this.mNeverwinterNightsFontTexture, this.mPlokFontTexture, this.mUnrealTournamentFontTexture);
		this.mEngine.getFontManager().loadFonts(this.mDroidFont, this.mKingdomOfHeartsFont, this.mNeverwinterNightsFont, this.mPlokFont, this.mUnrealTournamenFont);
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene(1);
		scene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));

		final IEntity lastChild = scene.getLastChild();
		lastChild.attachChild(new Text(230, 30, this.mDroidFont, "Droid Font"));
		lastChild.attachChild(new Text(160, 120, this.mKingdomOfHeartsFont, "Kingdom Of Hearts Font"));
		lastChild.attachChild(new Text(110, 210, this.mNeverwinterNightsFont, "Neverwinter Nights Font"));
		lastChild.attachChild(new Text(140, 300, this.mPlokFont, "Plok Font"));
		lastChild.attachChild(new Text(25, 390, this.mUnrealTournamenFont, "Unreal Tournament Font"));

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
