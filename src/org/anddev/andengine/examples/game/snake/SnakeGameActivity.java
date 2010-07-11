package org.anddev.andengine.examples.game.snake;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.anddev.andengine.engine.camera.hud.controls.DigitalOnScreenControl;
import org.anddev.andengine.engine.camera.hud.controls.BaseOnScreenControl.OnScreenControlListener;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.examples.game.snake.adt.Direction;
import org.anddev.andengine.examples.game.snake.entity.Snake;
import org.anddev.andengine.examples.game.snake.util.constants.SnakeConstants;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.MathUtils;

/**
 * @author Nicolas Gramlich
 * @since 02:26:05 - 08.07.2010
 */
public class SnakeGameActivity extends BaseGameActivity implements SnakeConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = CELLS_HORIZONTAL * CELL_WIDTH; // 640
	private static final int CAMERA_HEIGHT = CELLS_VERTICAL * CELL_HEIGHT; // 480

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mCamera;
	
	private Texture mTexture;	
	private TextureRegion mTailPartTextureRegion;
	private TextureRegion mHeadTextureRegion;

	private Texture mBackgroundTexture;
	private TextureRegion mBackgroundTextureRegion;

	private Texture mOnScreenControlTexture;
	private TextureRegion mOnScreenControlBaseTextureRegion;
	private TextureRegion mOnScreenControlKnobTextureRegion;

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
		this.mTexture = new Texture(64, 64, TextureOptions.BILINEAR);
		this.mHeadTextureRegion = TextureRegionFactory.createFromAsset(this.mTexture, this, "gfx/snake_head.png", 0, 0);
		this.mTailPartTextureRegion = TextureRegionFactory.createFromAsset(this.mTexture, this, "gfx/snake_tailpart.png", 32, 0);

		this.mBackgroundTexture = new Texture(1024, 512, TextureOptions.DEFAULT);
		this.mBackgroundTextureRegion = TextureRegionFactory.createFromAsset(this.mBackgroundTexture, this, "gfx/background_forest.png", 0, 0);

		this.mOnScreenControlTexture = new Texture(256, 128, TextureOptions.BILINEAR);
		this.mOnScreenControlBaseTextureRegion = TextureRegionFactory.createFromAsset(this.mOnScreenControlTexture, this, "gfx/analog_onscreen_control_base.png", 0, 0);
		this.mOnScreenControlKnobTextureRegion = TextureRegionFactory.createFromAsset(this.mOnScreenControlTexture, this, "gfx/analog_onscreen_control_knob.png", 128, 0);

		this.mEngine.getTextureManager().loadTextures(this.mBackgroundTexture, this.mTexture, this.mOnScreenControlTexture);
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerPreFrameHandler(new FPSLogger());

		final Scene scene = new Scene(2);
//		scene.setBackgroundColor(0.09804f, 0.6274f, 0.8784f);
		
		scene.getBottomLayer().addEntity(new Sprite(0, 0, this.mBackgroundTextureRegion));

		/* Create the face and add it to the scene. */
		final Snake snake = new Snake(Direction.RIGHT, 0, CELLS_VERTICAL / 2, this.mHeadTextureRegion, this.mTailPartTextureRegion);
		snake.grow();
		scene.getTopLayer().addEntity(snake);
		
		final DigitalOnScreenControl digitalOnScreenControl = new DigitalOnScreenControl(0, CAMERA_HEIGHT - this.mOnScreenControlBaseTextureRegion.getHeight(), this.mCamera, this.mOnScreenControlBaseTextureRegion, this.mOnScreenControlKnobTextureRegion, 0.1f, new OnScreenControlListener() {
			@Override
			public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl, final float pValueX, final float pValueY) {
				if(pValueX == 1) {
					snake.setDirection(Direction.RIGHT);
				} else if(pValueX == -1) {
					snake.setDirection(Direction.LEFT);
				} else if(pValueY == 1) {
					snake.setDirection(Direction.DOWN);
				} else if(pValueY == -1) {
					snake.setDirection(Direction.UP);
				} 
			}
		});

		scene.setChildScene(digitalOnScreenControl, false, false);

		scene.registerPreFrameHandler(new TimerHandler(0.5f, 
			new ITimerCallback() {
				@Override
				public void onTimePassed(final TimerHandler pTimerHandler) {
					pTimerHandler.reset();
					
					// TODO Check if Snake ate food
					if(MathUtils.RANDOM.nextFloat() > 0.75f) {
						snake.grow();
					}
					
					// TODO Check if move is possible.
					snake.move(); // IntoDirection(SnakeGameActivity.this.mDirection);
				}
			})
		);

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
