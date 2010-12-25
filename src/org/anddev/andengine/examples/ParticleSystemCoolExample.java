package org.anddev.andengine.examples;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.particle.ParticleSystem;
import org.anddev.andengine.entity.particle.emitter.PointParticleEmitter;
import org.anddev.andengine.entity.particle.initializer.AccelerationInitializer;
import org.anddev.andengine.entity.particle.initializer.ColorInitializer;
import org.anddev.andengine.entity.particle.initializer.RotationInitializer;
import org.anddev.andengine.entity.particle.initializer.VelocityInitializer;
import org.anddev.andengine.entity.particle.modifier.AlphaModifier;
import org.anddev.andengine.entity.particle.modifier.ColorModifier;
import org.anddev.andengine.entity.particle.modifier.ExpireModifier;
import org.anddev.andengine.entity.particle.modifier.ScaleModifier;
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
public class ParticleSystemCoolExample extends BaseExample {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 480;
	private static final int CAMERA_HEIGHT = 320;

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mCamera;
	private Texture mTexture;
	private TextureRegion mParticleTextureRegion;

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
		this.mTexture = new Texture(32, 32, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		this.mParticleTextureRegion = TextureRegionFactory.createFromAsset(this.mTexture, this, "gfx/particle_fire.png", 0, 0);

		this.mEngine.getTextureManager().loadTexture(this.mTexture);
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene(1);
		scene.setBackground(new ColorBackground(0.0f, 0.0f, 0.0f));

		/* Left to right Particle System. */
		{
			final ParticleSystem particleSystem = new ParticleSystem(new PointParticleEmitter(0, CAMERA_HEIGHT), 6, 10, 200, this.mParticleTextureRegion);
			particleSystem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE);

			particleSystem.addParticleInitializer(new VelocityInitializer(15, 22, -60, -90));
			particleSystem.addParticleInitializer(new AccelerationInitializer(5, 15));
			particleSystem.addParticleInitializer(new RotationInitializer(0.0f, 360.0f));
			particleSystem.addParticleInitializer(new ColorInitializer(1.0f, 0.0f, 0.0f));

			particleSystem.addParticleModifier(new ScaleModifier(0.5f, 2.0f, 0, 5));
			particleSystem.addParticleModifier(new ExpireModifier(11.5f));
			particleSystem.addParticleModifier(new AlphaModifier(1.0f, 0.0f, 2.5f, 3.5f));
			particleSystem.addParticleModifier(new AlphaModifier(0.0f, 1.0f, 3.5f, 4.5f));
			particleSystem.addParticleModifier(new ColorModifier(1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 11.5f));
			particleSystem.addParticleModifier(new AlphaModifier(1.0f, 0.0f, 4.5f, 11.5f));

			scene.getLastChild().attachChild(particleSystem);
		}

		/* Right to left Particle System. */
		{
			final ParticleSystem particleSystem = new ParticleSystem(new PointParticleEmitter(CAMERA_WIDTH - 32, CAMERA_HEIGHT), 8, 12, 200, this.mParticleTextureRegion);
			particleSystem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE);

			particleSystem.addParticleInitializer(new VelocityInitializer(-15, -22, -60, -90));
			particleSystem.addParticleInitializer(new AccelerationInitializer(-5, 15));
			particleSystem.addParticleInitializer(new RotationInitializer(0.0f, 360.0f));
			particleSystem.addParticleInitializer(new ColorInitializer(0.0f, 0.0f, 1.0f));

			particleSystem.addParticleModifier(new ScaleModifier(0.5f, 2.0f, 0, 5));
			particleSystem.addParticleModifier(new ExpireModifier(11.5f));
			particleSystem.addParticleModifier(new AlphaModifier(1.0f, 0.0f, 2.5f, 3.5f));
			particleSystem.addParticleModifier(new AlphaModifier(0.0f, 1.0f, 3.5f, 4.5f));
			particleSystem.addParticleModifier(new ColorModifier(0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 11.5f));
			particleSystem.addParticleModifier(new AlphaModifier(1.0f, 0.0f, 4.5f, 11.5f));

			scene.getLastChild().attachChild(particleSystem);
		}

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
