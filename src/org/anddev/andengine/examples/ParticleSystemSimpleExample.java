package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.SmoothEngine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.particle.ParticleSystem;
import org.anddev.andengine.entity.particle.modifier.AccelerationInitializer;
import org.anddev.andengine.entity.particle.modifier.ExpireModifier;
import org.anddev.andengine.entity.particle.modifier.RotationInitializer;
import org.anddev.andengine.entity.particle.modifier.ScaleModifier;
import org.anddev.andengine.entity.particle.modifier.VelocityInitializer;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;

/**
 * @author Nicolas Gramlich
 * @since 11:54:51 - 03.04.2010
 */
public class ParticleSystemSimpleExample extends BaseExample {
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
		return new SmoothEngine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera));
	}

	@Override
	public void onLoadResources() {
		this.mTexture = new Texture(32, 32, TextureOptions.BILINEAR);

		this.mFaceTextureRegion = TextureRegionFactory.createFromAsset(this.mTexture, this, "gfx/boxface.png", 0, 0);

		this.mEngine.getTextureManager().loadTexture(this.mTexture);
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene(1);
		scene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));

		final ParticleSystem particleSystem = new ParticleSystem(0, CAMERA_HEIGHT, 0, 0, 8, 12, 200, this.mFaceTextureRegion);
		
		particleSystem.addParticleInitializer(new VelocityInitializer(20, 30, -80, -120));
		particleSystem.addParticleInitializer(new AccelerationInitializer(10, 20));
		particleSystem.addParticleInitializer(new RotationInitializer(0.0f, 360.0f));
		
		particleSystem.addParticleModifier(new ScaleModifier(1.0f, 2.0f, 0, 5));
		particleSystem.addParticleModifier(new ExpireModifier(12, 12));
		
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
