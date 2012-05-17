package org.andengine.examples;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.animationpack.AnimationPack;
import org.andengine.util.animationpack.AnimationPackLoader;
import org.andengine.util.animationpack.AnimationPackTiledTextureRegion;
import org.andengine.util.animationpack.AnimationPackTiledTextureRegionLibrary;
import org.andengine.util.animationpack.exception.AnimationPackParseException;
import org.andengine.util.debug.Debug;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 00:57:44 - 04.05.2012
 */
public class AnimationPackExample extends SimpleBaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	// ===========================================================
	// Fields
	// ===========================================================

	private AnimationPack mAnimationPack;
	private AnimationPackTiledTextureRegionLibrary mAnimationPackAnimationDataLibrary;

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
		final Camera camera = new Camera(0, 0, AnimationPackExample.CAMERA_WIDTH, AnimationPackExample.CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, new RatioResolutionPolicy(AnimationPackExample.CAMERA_WIDTH, AnimationPackExample.CAMERA_HEIGHT), camera);
	}

	@Override
	public void onCreateResources() {
		try {
			final AnimationPackLoader animationPackLoader = new AnimationPackLoader(this.getAssets(), this.getTextureManager());

			this.mAnimationPack = animationPackLoader.loadFromAsset("anim/animations_1-6.xml", "anim/");
			this.mAnimationPackAnimationDataLibrary = this.mAnimationPack.getAnimationPackAnimationDataLibrary();
		} catch (final AnimationPackParseException e) {
			Debug.e(e);
		}
	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
		scene.getBackground().setColor(0.09804f, 0.6274f, 0.8784f);

		final float centerX = AnimationPackExample.CAMERA_WIDTH * 0.5f;
		final float centerY = AnimationPackExample.CAMERA_HEIGHT * 0.5f;

		final AnimationPackTiledTextureRegion animationPackTiledTextureRegion = this.mAnimationPackAnimationDataLibrary.get("animation_135");
		final AnimatedSprite animatedSprite = new AnimatedSprite(centerX, centerY, animationPackTiledTextureRegion, this.getVertexBufferObjectManager());
		animatedSprite.animate(animationPackTiledTextureRegion.getAnimationData());
		animatedSprite.setScale(4);

		scene.attachChild(animatedSprite);

		return scene;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
