package org.anddev.andengine.examples;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.FPSCounter;
import org.anddev.andengine.entity.Scene;
import org.anddev.andengine.entity.particle.ParticleSystem;
import org.anddev.andengine.entity.particle.modifier.AccelerationModifier;
import org.anddev.andengine.entity.particle.modifier.ExpireModifier;
import org.anddev.andengine.entity.particle.modifier.VelocityModifier;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.TextureRegion;
import org.anddev.andengine.opengl.texture.TextureRegionFactory;
import org.anddev.andengine.ui.activity.BaseGameActivity;

/**
 * @author Nicolas Gramlich
 * @since 11:54:51 - 03.04.2010
 */
public class ParticleSystemExample extends BaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================
	
	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mCamera;
	private Texture mTexture;
	private TextureRegion mFaceTextureRegion;

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
		this.mTexture = new Texture(32, 32, new TextureOptions(GL10.GL_NEAREST, GL10.GL_LINEAR, GL10.GL_MODULATE, GL10.GL_CLAMP_TO_EDGE, GL10.GL_CLAMP_TO_EDGE));
		
		this.mFaceTextureRegion = TextureRegionFactory.createFromAsset(this.mTexture, this, "gfx/boxface.png", 0, 0);		
		
		this.getEngine().loadTexture(this.mTexture);
	}

	@Override
	public Scene onLoadScene() {
		this.getEngine().registerPreFrameHandler(new FPSCounter());
		
		final Scene scene = new Scene(1);
		scene.setBackgroundColor(0.09804f, 0.6274f, 0.8784f);

		final ParticleSystem particleSystem = new ParticleSystem(0, CAMERA_HEIGHT, 0, 0, 5, 10, 200, this.mFaceTextureRegion);
		particleSystem.addParticleModifier(new VelocityModifier(20, 30, -80, -120));
		particleSystem.addParticleModifier(new AccelerationModifier(10, 20));
		particleSystem.addParticleModifier(new ExpireModifier(15, 20));
		scene.getTopLayer().addEntity(particleSystem);

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
