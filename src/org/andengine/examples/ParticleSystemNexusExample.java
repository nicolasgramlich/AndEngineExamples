package org.andengine.examples;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.particle.SpriteParticleSystem;
import org.andengine.entity.particle.emitter.PointParticleEmitter;
import org.andengine.entity.particle.initializer.AccelerationInitializer;
import org.andengine.entity.particle.initializer.BlendFunctionInitializer;
import org.andengine.entity.particle.initializer.ColorInitializer;
import org.andengine.entity.particle.initializer.RotationInitializer;
import org.andengine.entity.particle.initializer.VelocityInitializer;
import org.andengine.entity.particle.modifier.AlphaModifier;
import org.andengine.entity.particle.modifier.ColorModifier;
import org.andengine.entity.particle.modifier.ExpireModifier;
import org.andengine.entity.particle.modifier.ScaleModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;

import android.opengl.GLES20;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 16:44:30 - 29.06.2010
 */
public class ParticleSystemNexusExample extends BaseExample {
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
	private BitmapTextureAtlas mBitmapTextureAtlas;
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

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(ParticleSystemNexusExample.CAMERA_WIDTH, ParticleSystemNexusExample.CAMERA_HEIGHT), this.mCamera);
	}

	@Override
	protected void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		this.mBitmapTextureAtlas = new BitmapTextureAtlas(32, 32, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mParticleTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "particle_fire.png", 0, 0);

		this.mEngine.getTextureManager().loadTexture(this.mBitmapTextureAtlas);
	}

	@Override
	protected Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
		scene.setBackground(new Background(0.0f, 0.0f, 0.0f));

		/* LowerLeft to LowerRight Particle System. */
		{
			final SpriteParticleSystem particleSystem = new SpriteParticleSystem(new PointParticleEmitter(-32, ParticleSystemNexusExample.CAMERA_HEIGHT - 32), ParticleSystemNexusExample.RATE_MIN, ParticleSystemNexusExample.RATE_MAX, ParticleSystemNexusExample.PARTICLES_MAX, this.mParticleTextureRegion);
			particleSystem.addParticleInitializer(new BlendFunctionInitializer<Sprite>(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE));
			particleSystem.addParticleInitializer(new VelocityInitializer<Sprite>(35, 45, 0, -10));
			particleSystem.addParticleInitializer(new AccelerationInitializer<Sprite>(5, -11));
			particleSystem.addParticleInitializer(new RotationInitializer<Sprite>(0.0f, 360.0f));
			particleSystem.addParticleInitializer(new ColorInitializer<Sprite>(1.0f, 1.0f, 0.0f));

			particleSystem.addParticleModifier(new ScaleModifier<Sprite>(0.5f, 2.0f, 0, 5));
			particleSystem.addParticleModifier(new ExpireModifier<Sprite>(6.5f));
			particleSystem.addParticleModifier(new ColorModifier<Sprite>(1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 2.5f, 5.5f));
			particleSystem.addParticleModifier(new AlphaModifier<Sprite>(1.0f, 0.0f, 2.5f, 6.5f));

			scene.attachChild(particleSystem);
		}

		/* LowerRight to LowerLeft Particle System. */
		{
			final SpriteParticleSystem particleSystem = new SpriteParticleSystem(new PointParticleEmitter(ParticleSystemNexusExample.CAMERA_WIDTH, ParticleSystemNexusExample.CAMERA_HEIGHT - 32), ParticleSystemNexusExample.RATE_MIN, ParticleSystemNexusExample.RATE_MAX, ParticleSystemNexusExample.PARTICLES_MAX, this.mParticleTextureRegion);
			particleSystem.addParticleInitializer(new BlendFunctionInitializer<Sprite>(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE));
			particleSystem.addParticleInitializer(new VelocityInitializer<Sprite>(-35, -45, 0, -10));
			particleSystem.addParticleInitializer(new AccelerationInitializer<Sprite>(-5, -11));
			particleSystem.addParticleInitializer(new RotationInitializer<Sprite>(0.0f, 360.0f));
			particleSystem.addParticleInitializer(new ColorInitializer<Sprite>(0.0f, 1.0f, 0.0f));

			particleSystem.addParticleModifier(new ScaleModifier<Sprite>(0.5f, 2.0f, 0, 5));
			particleSystem.addParticleModifier(new ExpireModifier<Sprite>(6.5f));
			particleSystem.addParticleModifier(new ColorModifier<Sprite>(0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 2.5f, 5.5f));
			particleSystem.addParticleModifier(new AlphaModifier<Sprite>(1.0f, 0.0f, 2.5f, 6.5f));

			scene.attachChild(particleSystem);
		}

		/* UpperLeft to UpperRight Particle System. */
		{
			final SpriteParticleSystem particleSystem = new SpriteParticleSystem(new PointParticleEmitter(-32, 0), ParticleSystemNexusExample.RATE_MIN, ParticleSystemNexusExample.RATE_MAX, ParticleSystemNexusExample.PARTICLES_MAX, this.mParticleTextureRegion);
			particleSystem.addParticleInitializer(new BlendFunctionInitializer<Sprite>(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE));
			particleSystem.addParticleInitializer(new VelocityInitializer<Sprite>(35, 45, 0, 10));
			particleSystem.addParticleInitializer(new AccelerationInitializer<Sprite>(5, 11));
			particleSystem.addParticleInitializer(new RotationInitializer<Sprite>(0.0f, 360.0f));
			particleSystem.addParticleInitializer(new ColorInitializer<Sprite>(0.0f, 0.0f, 1.0f));

			particleSystem.addParticleModifier(new ScaleModifier<Sprite>(0.5f, 2.0f, 0, 5));
			particleSystem.addParticleModifier(new ExpireModifier<Sprite>(6.5f));
			particleSystem.addParticleModifier(new ColorModifier<Sprite>(0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 2.5f, 5.5f));
			particleSystem.addParticleModifier(new AlphaModifier<Sprite>(1.0f, 0.0f, 2.5f, 6.5f));

			scene.attachChild(particleSystem);
		}

		/* UpperRight to UpperLeft Particle System. */
		{
			final SpriteParticleSystem particleSystem = new SpriteParticleSystem(new PointParticleEmitter(ParticleSystemNexusExample.CAMERA_WIDTH, 0), ParticleSystemNexusExample.RATE_MIN, ParticleSystemNexusExample.RATE_MAX, ParticleSystemNexusExample.PARTICLES_MAX, this.mParticleTextureRegion);
			particleSystem.addParticleInitializer(new BlendFunctionInitializer<Sprite>(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE));
			particleSystem.addParticleInitializer(new VelocityInitializer<Sprite>(-35, -45, 0, 10));
			particleSystem.addParticleInitializer(new AccelerationInitializer<Sprite>(-5, 11));
			particleSystem.addParticleInitializer(new RotationInitializer<Sprite>(0.0f, 360.0f));
			particleSystem.addParticleInitializer(new ColorInitializer<Sprite>(1.0f, 0.0f, 0.0f));

			particleSystem.addParticleModifier(new ScaleModifier<Sprite>(0.5f, 2.0f, 0, 5));
			particleSystem.addParticleModifier(new ExpireModifier<Sprite>(6.5f));
			particleSystem.addParticleModifier(new ColorModifier<Sprite>(1.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 2.5f, 5.5f));
			particleSystem.addParticleModifier(new AlphaModifier<Sprite>(1.0f, 0.0f, 2.5f, 6.5f));

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
