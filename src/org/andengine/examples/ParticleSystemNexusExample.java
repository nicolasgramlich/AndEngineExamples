package org.andengine.examples;

import java.io.IOException;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.Entity;
import org.andengine.entity.particle.BatchedPseudoSpriteParticleSystem;
import org.andengine.entity.particle.emitter.PointParticleEmitter;
import org.andengine.entity.particle.initializer.AccelerationParticleInitializer;
import org.andengine.entity.particle.initializer.ColorParticleInitializer;
import org.andengine.entity.particle.initializer.ExpireParticleInitializer;
import org.andengine.entity.particle.initializer.RotationParticleInitializer;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.AlphaParticleModifier;
import org.andengine.entity.particle.modifier.ColorParticleModifier;
import org.andengine.entity.particle.modifier.ScaleParticleModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.bitmap.AssetBitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.color.Color;

import android.opengl.GLES20;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 16:44:30 - 29.06.2010
 */
public class ParticleSystemNexusExample extends SimpleBaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 480;
	private static final int CAMERA_HEIGHT = 320;
	private static final float RATE_MIN = 8;
	private static final float RATE_MAX = 12;
	private static final int PARTICLES_MAX = 200;

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mCamera;
	private ITexture mParticleTexture;
	private ITextureRegion mParticleTextureRegion;

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
		this.mCamera = new Camera(0, 0, ParticleSystemNexusExample.CAMERA_WIDTH, ParticleSystemNexusExample.CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, new RatioResolutionPolicy(ParticleSystemNexusExample.CAMERA_WIDTH, ParticleSystemNexusExample.CAMERA_HEIGHT), this.mCamera);
	}

	@Override
	protected void onCreateResources() throws IOException {
		this.mParticleTexture = new AssetBitmapTexture(this.getTextureManager(), this.getAssets(), "gfx/particle_fire.png", TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mParticleTextureRegion = TextureRegionFactory.extractFromTexture(this.mParticleTexture);
		this.mParticleTexture.load();
	}

	@Override
	protected Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
		scene.getBackground().setColor(Color.BLACK);

		/* LowerLeft to LowerRight Particle System. */
		{
			final BatchedPseudoSpriteParticleSystem particleSystem = new BatchedPseudoSpriteParticleSystem(new PointParticleEmitter(-32, ParticleSystemNexusExample.CAMERA_HEIGHT - 32), ParticleSystemNexusExample.RATE_MIN, ParticleSystemNexusExample.RATE_MAX, ParticleSystemNexusExample.PARTICLES_MAX, this.mParticleTextureRegion, this.getVertexBufferObjectManager());
			particleSystem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE);
			particleSystem.addParticleInitializer(new VelocityParticleInitializer<Entity>(35, 45, 0, -10));
			particleSystem.addParticleInitializer(new AccelerationParticleInitializer<Entity>(5, -11));
			particleSystem.addParticleInitializer(new RotationParticleInitializer<Entity>(0.0f, 360.0f));
			particleSystem.addParticleInitializer(new ColorParticleInitializer<Entity>(1.0f, 1.0f, 0.0f));
			particleSystem.addParticleInitializer(new ExpireParticleInitializer<Entity>(6.5f));

			particleSystem.addParticleModifier(new ScaleParticleModifier<Entity>(0, 5, 0.5f, 2.0f));
			particleSystem.addParticleModifier(new ColorParticleModifier<Entity>(2.5f, 5.5f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f));
			particleSystem.addParticleModifier(new AlphaParticleModifier<Entity>(2.5f, 6.5f, 1.0f, 0.0f));

			scene.attachChild(particleSystem);
		}

		/* LowerRight to LowerLeft Particle System. */
		{
			final BatchedPseudoSpriteParticleSystem particleSystem = new BatchedPseudoSpriteParticleSystem(new PointParticleEmitter(ParticleSystemNexusExample.CAMERA_WIDTH, ParticleSystemNexusExample.CAMERA_HEIGHT - 32), ParticleSystemNexusExample.RATE_MIN, ParticleSystemNexusExample.RATE_MAX, ParticleSystemNexusExample.PARTICLES_MAX, this.mParticleTextureRegion, this.getVertexBufferObjectManager());
			particleSystem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE);
			particleSystem.addParticleInitializer(new VelocityParticleInitializer<Entity>(-35, -45, 0, -10));
			particleSystem.addParticleInitializer(new AccelerationParticleInitializer<Entity>(-5, -11));
			particleSystem.addParticleInitializer(new RotationParticleInitializer<Entity>(0.0f, 360.0f));
			particleSystem.addParticleInitializer(new ColorParticleInitializer<Entity>(0.0f, 1.0f, 0.0f));
			particleSystem.addParticleInitializer(new ExpireParticleInitializer<Entity>(6.5f));

			particleSystem.addParticleModifier(new ScaleParticleModifier<Entity>(0, 5, 0.5f, 2.0f));
			particleSystem.addParticleModifier(new ColorParticleModifier<Entity>(2.5f, 5.5f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f));
			particleSystem.addParticleModifier(new AlphaParticleModifier<Entity>(2.5f, 6.5f, 1.0f, 0.0f));

			scene.attachChild(particleSystem);
		}

		/* UpperLeft to UpperRight Particle System. */
		{
			final BatchedPseudoSpriteParticleSystem particleSystem = new BatchedPseudoSpriteParticleSystem(new PointParticleEmitter(-32, 0), ParticleSystemNexusExample.RATE_MIN, ParticleSystemNexusExample.RATE_MAX, ParticleSystemNexusExample.PARTICLES_MAX, this.mParticleTextureRegion, this.getVertexBufferObjectManager());
			particleSystem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE);
			particleSystem.addParticleInitializer(new VelocityParticleInitializer<Entity>(35, 45, 0, 10));
			particleSystem.addParticleInitializer(new AccelerationParticleInitializer<Entity>(5, 11));
			particleSystem.addParticleInitializer(new RotationParticleInitializer<Entity>(0.0f, 360.0f));
			particleSystem.addParticleInitializer(new ColorParticleInitializer<Entity>(0.0f, 0.0f, 1.0f));
			particleSystem.addParticleInitializer(new ExpireParticleInitializer<Entity>(6.5f));

			particleSystem.addParticleModifier(new ScaleParticleModifier<Entity>(0, 5, 0.5f, 2.0f));
			particleSystem.addParticleModifier(new ColorParticleModifier<Entity>(2.5f, 5.5f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f));
			particleSystem.addParticleModifier(new AlphaParticleModifier<Entity>(2.5f, 6.5f, 1.0f, 0.0f));

			scene.attachChild(particleSystem);
		}

		/* UpperRight to UpperLeft Particle System. */
		{
			final BatchedPseudoSpriteParticleSystem particleSystem = new BatchedPseudoSpriteParticleSystem(new PointParticleEmitter(ParticleSystemNexusExample.CAMERA_WIDTH, 0), ParticleSystemNexusExample.RATE_MIN, ParticleSystemNexusExample.RATE_MAX, ParticleSystemNexusExample.PARTICLES_MAX, this.mParticleTextureRegion, this.getVertexBufferObjectManager());
			particleSystem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE);
			particleSystem.addParticleInitializer(new VelocityParticleInitializer<Entity>(-35, -45, 0, 10));
			particleSystem.addParticleInitializer(new AccelerationParticleInitializer<Entity>(-5, 11));
			particleSystem.addParticleInitializer(new RotationParticleInitializer<Entity>(0.0f, 360.0f));
			particleSystem.addParticleInitializer(new ColorParticleInitializer<Entity>(1.0f, 0.0f, 0.0f));
			particleSystem.addParticleInitializer(new ExpireParticleInitializer<Entity>(6.5f));

			particleSystem.addParticleModifier(new ScaleParticleModifier<Entity>(0, 5, 0.5f, 2.0f));
			particleSystem.addParticleModifier(new ColorParticleModifier<Entity>(2.5f, 5.5f, 1.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f));
			particleSystem.addParticleModifier(new AlphaParticleModifier<Entity>(2.5f, 6.5f, 1.0f, 0.0f));

			scene.attachChild(particleSystem);
		}

		return scene;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
