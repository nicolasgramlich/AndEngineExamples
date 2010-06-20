package org.anddev.andengine.examples;

import org.anddev.andengine.ui.activity.BaseGameActivity;

/**
 * @author Nicolas Gramlich
 * @since 18:20:40 - 18.06.2010
 */
public class ManyTouchDragExample { // extends BaseGameActivity {
//	// ===========================================================
//	// Constants
//	// ===========================================================
//
//	private static final int CAMERA_WIDTH = 720;
//	private static final int CAMERA_HEIGHT = 480;
//
//	// ===========================================================
//	// Fields
//	// ===========================================================
//
//	private Camera mCamera;
//	private Texture mCardDeckTexture;
//
//	protected CardSprite mSelectedCardSprite;
//
//	private HashMap<Card, TextureRegion> mCardTotextureRegionMap;
//	private RunnableHandler mRemoveRunnableHandler;
//
//	// ===========================================================
//	// Constructors
//	// ===========================================================
//
//	// ===========================================================
//	// Getter & Setter
//	// ===========================================================
//
//	// ===========================================================
//	// Methods for/from SuperClass/Interfaces
//	// ===========================================================
//
//	@Override
//	public Engine onLoadEngine() {
//		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
//		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera, false));
//	}
//
//	@Override
//	public void onLoadResources() {
//		this.mCardDeckTexture = new Texture(1024, 512);
//
//		TextureRegionFactory.createFromAsset(this.mCardDeckTexture, this, "gfx/carddeck.png", 0, 0);
//
//		this.mCardTotextureRegionMap = new HashMap<Card, TextureRegion>();
//
//		for(final Card card : Card.values()) {
//			final TextureRegion cardTextureRegion = TextureRegionFactory.extractFromTexture(this.mCardDeckTexture, card.getTexturePositionX(), card.getTexturePositionY(), Constants.CARD_WIDTH, Constants.CARD_HEIGHT);
//			this.mCardTotextureRegionMap.put(card, cardTextureRegion);
//		}
//
//		this.getEngine().getTextureManager().loadTexture(this.mCardDeckTexture);
//	}
//
//	@Override
//	public Scene onLoadScene() {
//		this.getEngine().registerPreFrameHandler(new FPSCounter());
//
//		final Scene scene = new Scene(1);
//
//		this.addCard(scene, Card.CLUB_ACE);
//		this.addCard(scene, Card.CLUB_ONE);
//		this.addCard(scene, Card.CLUB_TWO);
//		this.addCard(scene, Card.CLUB_THREE);
//
//		scene.setBackgroundColor(0.09804f, 0.6274f, 0.8784f);
//
//		scene.setOnSceneTouchListener(new IOnSceneTouchListener() {
//			@Override
//			public boolean onSceneTouchEvent(final Scene pScene, final MotionEvent pSceneMotionEvent) {
//				return updateSelectedCardPosition(pSceneMotionEvent);
//			}
//		});
//
//		scene.setOnAreaTouchListener(new IOnAreaTouchListener() {
//			@Override
//			public boolean onAreaTouched(final ITouchArea pTouchArea, final MotionEvent pSceneMotionEvent) {
//				if(pSceneMotionEvent.getAction() == MotionEvent.ACTION_DOWN) {
//					ManyTouchDragExample.this.mSelectedCardSprite = (CardSprite) pTouchArea;
//					ManyTouchDragExample.this.mSelectedCardSprite.setScale(1.2f);
//					return true;
//				} else {
//					return updateSelectedCardPosition(pSceneMotionEvent);
//				}
//			}
//		});
//
//		return scene;
//	}
//
//	public void removeCardSprite(final CardSprite pCardSprite) {
//		this.mRemoveRunnableHandler.addRunnable(new Runnable() {
//			@Override
//			public void run() {
//				final Scene scene = ManyTouchDragExample.this.mEngine.getScene();
//				scene.unregisterTouchArea(pCardSprite);
//				scene.getTopLayer().removeEntity(pCardSprite);
//			}
//		});
//	}
//
//	@Override
//	public void onLoadComplete() {
//
//	}
//
//	// ===========================================================
//	// Methods
//	// ===========================================================
//
//	private boolean updateSelectedCardPosition(final MotionEvent pSceneMotionEvent) {
//		if(ManyTouchDragExample.this.mSelectedCardSprite != null) {
//			if(pSceneMotionEvent.getAction() == MotionEvent.ACTION_MOVE) {
//				ManyTouchDragExample.this.mSelectedCardSprite.setPosition(pSceneMotionEvent.getX() - Constants.CARD_WIDTH / 2, pSceneMotionEvent.getY() - Constants.CARD_HEIGHT / 2);
//			} else if(pSceneMotionEvent.getAction() == MotionEvent.ACTION_UP) {
//				ManyTouchDragExample.this.mSelectedCardSprite.setScale(1.0f);
//				ManyTouchDragExample.this.mSelectedCardSprite = null;
//			}
//			return true;
//		} else {
//			return false;
//		}
//	}
//
//	public void addCard(final Scene pScene, final Card pCard) {
//		final CardSprite cardSprite = new CardSprite((float)Math.random() * CAMERA_WIDTH, (float)Math.random() * CAMERA_HEIGHT, this.mCardTotextureRegionMap.get(pCard));
//
//		pScene.getTopLayer().addEntity(cardSprite);
//		pScene.registerTouchArea(cardSprite);
//	}
//
//	// ===========================================================
//	// Inner and Anonymous Classes
//	// ===========================================================
}
