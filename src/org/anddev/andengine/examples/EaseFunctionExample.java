package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.camera.hud.HUD;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.modifier.MoveModifier;
import org.anddev.andengine.entity.primitive.Line;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.util.modifier.ease.EaseBackIn;
import org.anddev.andengine.util.modifier.ease.EaseBackInOut;
import org.anddev.andengine.util.modifier.ease.EaseBackOut;
import org.anddev.andengine.util.modifier.ease.EaseBounceIn;
import org.anddev.andengine.util.modifier.ease.EaseBounceInOut;
import org.anddev.andengine.util.modifier.ease.EaseBounceOut;
import org.anddev.andengine.util.modifier.ease.EaseCircularIn;
import org.anddev.andengine.util.modifier.ease.EaseCircularInOut;
import org.anddev.andengine.util.modifier.ease.EaseCircularOut;
import org.anddev.andengine.util.modifier.ease.EaseCubicIn;
import org.anddev.andengine.util.modifier.ease.EaseCubicInOut;
import org.anddev.andengine.util.modifier.ease.EaseCubicOut;
import org.anddev.andengine.util.modifier.ease.EaseElasticIn;
import org.anddev.andengine.util.modifier.ease.EaseElasticInOut;
import org.anddev.andengine.util.modifier.ease.EaseElasticOut;
import org.anddev.andengine.util.modifier.ease.EaseExponentialIn;
import org.anddev.andengine.util.modifier.ease.EaseExponentialInOut;
import org.anddev.andengine.util.modifier.ease.EaseExponentialOut;
import org.anddev.andengine.util.modifier.ease.EaseLinear;
import org.anddev.andengine.util.modifier.ease.EaseQuadIn;
import org.anddev.andengine.util.modifier.ease.EaseQuadInOut;
import org.anddev.andengine.util.modifier.ease.EaseQuadOut;
import org.anddev.andengine.util.modifier.ease.EaseQuartIn;
import org.anddev.andengine.util.modifier.ease.EaseQuartInOut;
import org.anddev.andengine.util.modifier.ease.EaseQuartOut;
import org.anddev.andengine.util.modifier.ease.EaseQuintIn;
import org.anddev.andengine.util.modifier.ease.EaseQuintInOut;
import org.anddev.andengine.util.modifier.ease.EaseQuintOut;
import org.anddev.andengine.util.modifier.ease.EaseSineIn;
import org.anddev.andengine.util.modifier.ease.EaseSineInOut;
import org.anddev.andengine.util.modifier.ease.EaseSineOut;
import org.anddev.andengine.util.modifier.ease.EaseStrongIn;
import org.anddev.andengine.util.modifier.ease.EaseStrongInOut;
import org.anddev.andengine.util.modifier.ease.EaseStrongOut;
import org.anddev.andengine.util.modifier.ease.IEaseFunction;

import android.graphics.Color;
import android.graphics.Typeface;

/**
 * @author Nicolas Gramlich
 * @since 15:12:16 - 30.07.2010
 */
public class EaseFunctionExample extends BaseExample {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mCamera;

	private Texture mFontTexture;
	private Font mFont;

	private Texture mTexture;
	private TextureRegion mFaceTextureRegion;
	private TextureRegion mNextTextureRegion;

	private static final IEaseFunction[][] EASEFUNCTIONS = new IEaseFunction[][]{
		new IEaseFunction[] {
				EaseLinear.getInstance(),
				EaseLinear.getInstance(),
				EaseLinear.getInstance()
		},
		new IEaseFunction[] {
				EaseBackIn.getInstance(),
				EaseBackOut.getInstance(),
				EaseBackInOut.getInstance()
		},
		new IEaseFunction[] {
				EaseBounceIn.getInstance(),
				EaseBounceOut.getInstance(),
				EaseBounceInOut.getInstance()
		},
		new IEaseFunction[] {
				EaseCircularIn.getInstance(),
				EaseCircularOut.getInstance(),
				EaseCircularInOut.getInstance()
		},
		new IEaseFunction[] {
				EaseCubicIn.getInstance(),
				EaseCubicOut.getInstance(),
				EaseCubicInOut.getInstance()
		},
		new IEaseFunction[] {
				EaseElasticIn.getInstance(),
				EaseElasticOut.getInstance(),
				EaseElasticInOut.getInstance()
		},
		new IEaseFunction[] {
				EaseExponentialIn.getInstance(),
				EaseExponentialOut.getInstance(),
				EaseExponentialInOut.getInstance()
		},
		new IEaseFunction[] {
				EaseQuadIn.getInstance(),
				EaseQuadOut.getInstance(),
				EaseQuadInOut.getInstance()
		},
		new IEaseFunction[] {
				EaseQuartIn.getInstance(),
				EaseQuartOut.getInstance(),
				EaseQuartInOut.getInstance()
		},
		new IEaseFunction[] {
				EaseQuintIn.getInstance(),
				EaseQuintOut.getInstance(),
				EaseQuintInOut.getInstance()
		},
		new IEaseFunction[] {
				EaseSineIn.getInstance(),
				EaseSineOut.getInstance(),
				EaseSineInOut.getInstance()
		},
		new IEaseFunction[] {
				EaseStrongIn.getInstance(),
				EaseStrongOut.getInstance(),
				EaseStrongInOut.getInstance()
		}
	};

	private int mCurrentEaseFunctionSet = 0;

	private final Sprite[] mFaces = new Sprite[3];
	private final ChangeableText[] mEaseFunctionNameTexts = new ChangeableText[3];

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
		/* The font. */
		this.mFontTexture = new Texture(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		this.mFont = new Font(this.mFontTexture, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32, true, Color.WHITE);

		this.mEngine.getTextureManager().loadTexture(this.mFontTexture);
		this.mEngine.getFontManager().loadFont(this.mFont);

		/* The textures. */
		this.mTexture = new Texture(256, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mNextTextureRegion = TextureRegionFactory.createFromAsset(this.mTexture, this, "gfx/next.png", 0, 0);
		this.mFaceTextureRegion = TextureRegionFactory.createFromAsset(this.mTexture, this, "gfx/badge.png", 97, 0);

		this.mEngine.getTextureManager().loadTexture(this.mTexture);
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene(1);

		final HUD hud = new HUD();

		final Sprite nextSprite = new Sprite(CAMERA_WIDTH - 100 - this.mNextTextureRegion.getWidth(), 0, this.mNextTextureRegion){
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionDown()) {
					EaseFunctionExample.this.next();
				}
				return true;
			};
		};
		final Sprite previousSprite = new Sprite(100, 0, this.mNextTextureRegion.clone()){
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionDown()) {
					EaseFunctionExample.this.previous();
				}
				return true;
			};
		};
		previousSprite.getTextureRegion().setFlippedHorizontal(true);

		hud.getLastChild().attachChild(nextSprite);
		hud.getLastChild().attachChild(previousSprite);

		hud.registerTouchArea(nextSprite);
		hud.registerTouchArea(previousSprite);

		this.mCamera.setHUD(hud);

		/* Create the sprites that will be moving. */

		this.mFaces[0] = new Sprite(0, CAMERA_HEIGHT - 300, this.mFaceTextureRegion);
		this.mFaces[1] = new Sprite(0, CAMERA_HEIGHT - 200, this.mFaceTextureRegion);
		this.mFaces[2] = new Sprite(0, CAMERA_HEIGHT - 100, this.mFaceTextureRegion);

		this.mEaseFunctionNameTexts[0] = new ChangeableText(0, CAMERA_HEIGHT - 250, this.mFont, "Function", 20);
		this.mEaseFunctionNameTexts[1] = new ChangeableText(0, CAMERA_HEIGHT - 150, this.mFont, "Function", 20);
		this.mEaseFunctionNameTexts[2] = new ChangeableText(0, CAMERA_HEIGHT - 50, this.mFont, "Function", 20);

		final IEntity lastChild = scene.getLastChild();
		lastChild.attachChild(this.mFaces[0]);
		lastChild.attachChild(this.mFaces[1]);
		lastChild.attachChild(this.mFaces[2]);
		lastChild.attachChild(this.mEaseFunctionNameTexts[0]);
		lastChild.attachChild(this.mEaseFunctionNameTexts[1]);
		lastChild.attachChild(this.mEaseFunctionNameTexts[2]);
		lastChild.attachChild(new Line(0, CAMERA_HEIGHT - 110, CAMERA_WIDTH, CAMERA_HEIGHT - 110));
		lastChild.attachChild(new Line(0, CAMERA_HEIGHT - 210, CAMERA_WIDTH, CAMERA_HEIGHT - 210));
		lastChild.attachChild(new Line(0, CAMERA_HEIGHT - 310, CAMERA_WIDTH, CAMERA_HEIGHT - 310));

		return scene;
	}

	@Override
	public void onLoadComplete() {
		this.reanimate();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void next() {
		this.mCurrentEaseFunctionSet++;
		this.mCurrentEaseFunctionSet %= EASEFUNCTIONS.length;
		this.reanimate();
	}

	public void previous() {
		this.mCurrentEaseFunctionSet--;
		if(this.mCurrentEaseFunctionSet < 0) {
			this.mCurrentEaseFunctionSet += EASEFUNCTIONS.length;
		}

		this.reanimate();
	}

	private void reanimate() {
		this.runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				final IEaseFunction[] currentEaseFunctionsSet = EASEFUNCTIONS[EaseFunctionExample.this.mCurrentEaseFunctionSet];
				final ChangeableText[] easeFunctionNameTexts = EaseFunctionExample.this.mEaseFunctionNameTexts;
				final Sprite[] faces = EaseFunctionExample.this.mFaces;

				for(int i = 0; i < 3; i++) {
					easeFunctionNameTexts[i].setText(currentEaseFunctionsSet[i].getClass().getSimpleName());
					final Sprite face = faces[i];
					face.clearEntityModifiers();

					final float y = face.getY();
					face.setPosition(0, y);
					face.registerEntityModifier(new MoveModifier(3, 0, CAMERA_WIDTH - face.getWidth(), y, y, currentEaseFunctionsSet[i]));
				}
			}
		});
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
