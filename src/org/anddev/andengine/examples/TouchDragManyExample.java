package org.anddev.andengine.examples;

import java.util.HashMap;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.Scene;
import org.anddev.andengine.entity.Scene.IOnAreaTouchListener;
import org.anddev.andengine.entity.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.Scene.ITouchArea;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSCounter;
import org.anddev.andengine.examples.adt.card.Card;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.view.MotionEvent;

/**
 * @author Nicolas Gramlich
 * @since 18:20:40 - 18.06.2010
 */
public class TouchDragManyExample extends BaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mCamera;
	private Texture mCardDeckTexture;

	protected Sprite mSelectedSprite;

	private HashMap<Card, TextureRegion> mCardTotextureRegionMap;

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
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera, false));
	}

	@Override
	public void onLoadResources() {
		this.mCardDeckTexture = new Texture(1024, 512, TextureOptions.BILINEAR);

		TextureRegionFactory.createFromAsset(this.mCardDeckTexture, this, "gfx/carddeck_tiled.png", 0, 0);

		this.mCardTotextureRegionMap = new HashMap<Card, TextureRegion>();

		/* Extract the TextureRegion of each card in the whole deck. */
		for(final Card card : Card.values()) {
			final TextureRegion cardTextureRegion = TextureRegionFactory.extractFromTexture(this.mCardDeckTexture, card.getTexturePositionX(), card.getTexturePositionY(), Card.CARD_WIDTH, Card.CARD_HEIGHT);
			this.mCardTotextureRegionMap.put(card, cardTextureRegion);
		}

		this.getEngine().getTextureManager().loadTexture(this.mCardDeckTexture);
	}

	@Override
	public Scene onLoadScene() {
		this.getEngine().registerPreFrameHandler(new FPSCounter());

		final Scene scene = new Scene(1);
		scene.setOnAreaTouchTraversalFrontToBack();

		this.addCard(scene, Card.CLUB_ACE, 200, 100);
		this.addCard(scene, Card.HEART_ACE, 200, 260);
		this.addCard(scene, Card.DIAMOND_ACE, 440, 100);
		this.addCard(scene, Card.SPADE_ACE, 440, 260);

		scene.setBackgroundColor(0.09804f, 0.6274f, 0.8784f);

		scene.setOnSceneTouchListener(new IOnSceneTouchListener() {
			@Override
			public boolean onSceneTouchEvent(final Scene pScene, final MotionEvent pSceneMotionEvent) {
				return updateSelectedCardPosition(pSceneMotionEvent);
			}
		});

		scene.setOnAreaTouchListener(new IOnAreaTouchListener() {
			@Override
			public boolean onAreaTouched(final ITouchArea pTouchArea, final MotionEvent pSceneMotionEvent) {
				if(pSceneMotionEvent.getAction() == MotionEvent.ACTION_DOWN) {
					TouchDragManyExample.this.mSelectedSprite = (Sprite) pTouchArea;
					TouchDragManyExample.this.mSelectedSprite.setScale(1.2f);
					return true;
				} else {
					return updateSelectedCardPosition(pSceneMotionEvent);
				}
			}
		});

		return scene;
	}

	@Override
	public void onLoadComplete() {

	}

	// ===========================================================
	// Methods
	// ===========================================================

	private boolean updateSelectedCardPosition(final MotionEvent pSceneMotionEvent) {
		if(TouchDragManyExample.this.mSelectedSprite != null) {
			if(pSceneMotionEvent.getAction() == MotionEvent.ACTION_MOVE) {
				TouchDragManyExample.this.mSelectedSprite.setPosition(pSceneMotionEvent.getX() - Card.CARD_WIDTH / 2, pSceneMotionEvent.getY() - Card.CARD_HEIGHT / 2);
			} else if(pSceneMotionEvent.getAction() == MotionEvent.ACTION_UP) {
				TouchDragManyExample.this.mSelectedSprite.setScale(1.0f);
				TouchDragManyExample.this.mSelectedSprite = null;
			}
			return true;
		} else {
			return false;
		}
	}

	private void addCard(final Scene pScene, final Card pCard, final int pX, final int pY) {
		final Sprite sprite = new Sprite(pX, pY, this.mCardTotextureRegionMap.get(pCard));

		pScene.getTopLayer().addEntity(sprite);
		pScene.registerTouchArea(sprite);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
