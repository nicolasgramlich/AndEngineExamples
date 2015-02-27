package org.andengine.examples;

import java.io.IOException;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.Entity;
import org.andengine.entity.particle.BatchedPseudoSpriteParticleSystem;
import org.andengine.entity.particle.emitter.CircleOutlineParticleEmitter;
import org.andengine.entity.particle.initializer.AlphaParticleInitializer;
import org.andengine.entity.particle.initializer.ColorParticleInitializer;
import org.andengine.entity.particle.initializer.ExpireParticleInitializer;
import org.andengine.entity.particle.initializer.RotationParticleInitializer;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.AlphaParticleModifier;
import org.andengine.entity.particle.modifier.ColorParticleModifier;
import org.andengine.entity.particle.modifier.ScaleParticleModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.bitmap.AssetBitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import android.opengl.GLES20;
import android.widget.Toast;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 11:54:51 - 03.04.2010
 */
public class ParticleSystemSimpleExample extends SimpleBaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

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
		Toast.makeText(this, "Touch the screen to move the particlesystem.", Toast.LENGTH_LONG).show();

		this.mCamera = new Camera(0, 0, ParticleSystemSimpleExample.CAMERA_WIDTH, ParticleSystemSimpleExample.CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, new RatioResolutionPolicy(ParticleSystemSimpleExample.CAMERA_WIDTH, ParticleSystemSimpleExample.CAMERA_HEIGHT), this.mCamera);
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

		final float centerX = ParticleSystemSimpleExample.CAMERA_WIDTH * 0.5f;
		final float centerY = ParticleSystemSimpleExample.CAMERA_HEIGHT * 0.5f;

		final CircleOutlineParticleEmitter particleEmitter = new CircleOutlineParticleEmitter(centerX, centerY, 80);
		final BatchedPseudoSpriteParticleSystem particleSystem = new BatchedPseudoSpriteParticleSystem(particleEmitter, 160, 160, 1000, this.mParticleTextureRegion, this.getVertexBufferObjectManager());

		scene.setOnSceneTouchListener(new IOnSceneTouchListener() {
			@Override
			public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
				particleEmitter.setCenter(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
				return true;
			}
		});
		particleSystem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE);

		particleSystem.addParticleInitializer(new ColorParticleInitializer<Entity>(1, 0, 0));
		particleSystem.addParticleInitializer(new AlphaParticleInitializer<Entity>(0));
//		particleSystem.addParticleInitializer(new BlendFunctionParticleInitializer<Entity>(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE));
		particleSystem.addParticleInitializer(new VelocityParticleInitializer<Entity>(-2, 2, 10, 20));
		particleSystem.addParticleInitializer(new RotationParticleInitializer<Entity>(0.0f, 360.0f));
		particleSystem.addParticleInitializer(new ExpireParticleInitializer<Entity>(6));

		particleSystem.addParticleModifier(new ScaleParticleModifier<Entity>(0, 5, 1.0f, 2.0f));
		particleSystem.addParticleModifier(new ColorParticleModifier<Entity>(0, 3, 1, 1, 0, 0.5f, 0, 0));
		particleSystem.addParticleModifier(new ColorParticleModifier<Entity>(4, 6, 1, 1, 0.5f, 1, 0, 1));
		particleSystem.addParticleModifier(new AlphaParticleModifier<Entity>(0, 1, 0, 1));
		particleSystem.addParticleModifier(new AlphaParticleModifier<Entity>(5, 6, 1, 0));

		scene.attachChild(particleSystem);

		return scene;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
