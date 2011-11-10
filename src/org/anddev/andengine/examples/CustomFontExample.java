package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.Background;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.ITexture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;

import android.graphics.Color;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
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
		final ITexture droidFontTexture = new BitmapTextureAtlas(256, 256, TextureOptions.BILINEAR);
		final ITexture kingdomOfHeartsFontTexture = new BitmapTextureAtlas(256, 256, TextureOptions.BILINEAR);
		final ITexture neverwinterNightsFontTexture = new BitmapTextureAtlas(256, 256, TextureOptions.BILINEAR);
		final ITexture plokFontTexture = new BitmapTextureAtlas(256, 256, TextureOptions.BILINEAR);
		final ITexture unrealTournamentFontTexture = new BitmapTextureAtlas(256, 256, TextureOptions.BILINEAR);

		FontFactory.setAssetBasePath("font/");
		this.mDroidFont = FontFactory.createFromAsset(droidFontTexture, this, "Droid.ttf", FONT_SIZE, true, Color.BLACK).load();
		this.mKingdomOfHeartsFont = FontFactory.createFromAsset(kingdomOfHeartsFontTexture, this, "KingdomOfHearts.ttf", FONT_SIZE + 20, true, Color.BLACK).load();
		this.mNeverwinterNightsFont = FontFactory.createFromAsset(neverwinterNightsFontTexture, this, "NeverwinterNights.ttf", FONT_SIZE, true, Color.BLACK).load();
		this.mPlokFont = FontFactory.createFromAsset(plokFontTexture, this, "Plok.ttf", FONT_SIZE, true, Color.BLACK).load();
		this.mUnrealTournamenFont = FontFactory.createFromAsset(unrealTournamentFontTexture, this, "UnrealTournament.ttf", FONT_SIZE, true, Color.BLACK).load();
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
		scene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));

		scene.attachChild(new Text(230, 30, this.mDroidFont, "Droid Font"));
		scene.attachChild(new Text(160, 120, this.mKingdomOfHeartsFont, "Kingdom Of Hearts Font"));
		scene.attachChild(new Text(110, 210, this.mNeverwinterNightsFont, "Neverwinter Nights Font"));
		scene.attachChild(new Text(140, 300, this.mPlokFont, "Plok Font"));
		scene.attachChild(new Text(25, 390, this.mUnrealTournamenFont, "Unreal Tournament Font"));

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
