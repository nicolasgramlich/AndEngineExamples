package org.andengine.examples;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.IEntity;
import org.andengine.entity.TagEntityMatcher;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.cocosbuilder.CCBLevelLoader;
import org.andengine.extension.cocosbuilder.CCBLevelLoaderResult;
import org.andengine.extension.cocosbuilder.MemberVariableAssignmentCCBEntityLoaderListener;
import org.andengine.extension.cocosbuilder.entity.CCRotatingSprite;
import org.andengine.extension.cocosbuilder.entity.CCSprite;
import org.andengine.extension.cocosbuilder.loader.CCRotatingSpriteEntityLoader;
import org.andengine.ui.activity.SimpleBaseGameActivity;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 11:41:40 - 19.04.2012
 */
public class CCBLevelLoaderExample extends SimpleBaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 480;
	private static final int CAMERA_HEIGHT = 320;

	private static final int EXAMPLE_SPRITE_TAG = 1337;

	// ===========================================================
	// Fields
	// ===========================================================

	public CCRotatingSprite mCCRotatingSprite;

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
		final Camera camera = new Camera(0, 0, CCBLevelLoaderExample.CAMERA_WIDTH, CCBLevelLoaderExample.CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, new RatioResolutionPolicy(CCBLevelLoaderExample.CAMERA_WIDTH, CCBLevelLoaderExample.CAMERA_HEIGHT), camera);
	}

	@Override
	public void onCreateResources() {

	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();

		final CCBLevelLoader ccbLevelLoader = new CCBLevelLoader(this.getAssets(), "ccb/", this.getVertexBufferObjectManager(), this.getTextureManager(), this.getFontManager());
		/* Register EntityLoaders from extensions. */
		ccbLevelLoader.registerEntityLoader(new CCRotatingSpriteEntityLoader());

		/* When loading the CCBAEX file, we want to pick up member variables and assign them to the owner. */
		final Object owner = this;
		final MemberVariableAssignmentCCBEntityLoaderListener memberVariableAssignmentCCBEntityLoaderListener = new MemberVariableAssignmentCCBEntityLoaderListener(owner);

		/* Kick of parsing. */
		final CCBLevelLoaderResult ccbLevelLoaderResult = ccbLevelLoader.loadLevelFromAsset(this.getAssets(), "ccb/example.ccbaex", memberVariableAssignmentCCBEntityLoaderListener);

		/* Note: this.mCCRotatingSprite was magically assigned with the CCRotatingSprite entity from the CCBAEX file! */
		this.mCCRotatingSprite.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 2)));

		/* Grab the root entity of the loaded CCBAEX file. */
		final IEntity rootEntity = ccbLevelLoaderResult.getRootEntity();

		/* Query for a CCSprite by its tag and do something with it. */
		final CCSprite sprite = rootEntity.queryFirstForSubclass(new TagEntityMatcher(EXAMPLE_SPRITE_TAG));
		sprite.registerEntityModifier(new LoopEntityModifier(new RotationModifier(1, 0, 360)));

		/* Hook the root entity into the scene. */
		scene.attachChild(rootEntity);

		return scene;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
