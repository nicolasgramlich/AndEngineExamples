package org.andengine.examples;

import java.io.IOException;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.UncoloredSprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ClickDetector;
import org.andengine.input.touch.detector.ClickDetector.IClickDetectorListener;
import org.andengine.opengl.shader.PositionTextureCoordinatesShaderProgram;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.shader.constants.ShaderProgramConstants;
import org.andengine.opengl.shader.exception.ShaderProgramException;
import org.andengine.opengl.shader.exception.ShaderProgramLinkException;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.bitmap.AssetBitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.texture.render.RenderTexture;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import android.opengl.GLES20;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 16:55:18 - 06.11.2011
 */
public class RadialBlurExample extends SimpleBaseGameActivity implements IOnSceneTouchListener, IClickDetectorListener {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mCamera;
	private ITexture mBadgeTexture;
	private ITextureRegion mBadgeTextureRegion;

	private boolean mRadialBlurring = true;
	private float mRadialBlurCenterX = 0.5f;
	private float mRadialBlurCenterY = 0.5f;
	private ClickDetector mClickDetector;

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
		this.mCamera = new Camera(0, 0, RadialBlurExample.CAMERA_WIDTH, RadialBlurExample.CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, new RatioResolutionPolicy(RadialBlurExample.CAMERA_WIDTH, RadialBlurExample.CAMERA_HEIGHT), this.mCamera);
	}

	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) {
		return new Engine(pEngineOptions) {
			private boolean mRenderTextureInitialized;

			private RenderTexture mRenderTexture;
			private UncoloredSprite mRenderTextureSprite;

			@Override
			public void onDrawFrame(final GLState pGLState) throws InterruptedException {
				final boolean firstFrame = !this.mRenderTextureInitialized;

				if(firstFrame) {
					this.initRenderTextures(pGLState);
					this.mRenderTextureInitialized = true;
				}

				final int surfaceWidth = this.mCamera.getSurfaceWidth();
				final int surfaceHeight = this.mCamera.getSurfaceHeight();

				this.mRenderTexture.begin(pGLState);
				{
					/* Draw current frame. */
					super.onDrawFrame(pGLState);
				}
				this.mRenderTexture.end(pGLState);

				/* Draw rendered texture with custom shader. */
				{
					pGLState.pushProjectionGLMatrix();
					pGLState.orthoProjectionGLMatrixf(0, surfaceWidth, 0, surfaceHeight, -1, 1);
					{
						this.mRenderTextureSprite.onDraw(pGLState, this.mCamera);
					}
					pGLState.popProjectionGLMatrix();
				}
			}

			private void initRenderTextures(final GLState pGLState) {
				final int surfaceWidth = this.mCamera.getSurfaceWidth();
				final int surfaceHeight = this.mCamera.getSurfaceHeight();

				this.mRenderTexture = new RenderTexture(RadialBlurExample.this.getTextureManager(), surfaceWidth, surfaceHeight);
				this.mRenderTexture.init(pGLState);

				final ITextureRegion renderTextureTextureRegion = TextureRegionFactory.extractFromTexture(this.mRenderTexture);
				this.mRenderTextureSprite = new UncoloredSprite(0, 0, renderTextureTextureRegion, this.getVertexBufferObjectManager()) {
					@Override
					protected void preDraw(final GLState pGLState, final Camera pCamera) {
						if(RadialBlurExample.this.mRadialBlurring) {
							this.setShaderProgram(RadialBlurShaderProgram.getInstance());
						} else {
							this.setShaderProgram(PositionTextureCoordinatesShaderProgram.getInstance());
						}
						super.preDraw(pGLState, pCamera);

						GLES20.glUniform2f(RadialBlurShaderProgram.sUniformRadialBlurCenterLocation, RadialBlurExample.this.mRadialBlurCenterX, 1 - RadialBlurExample.this.mRadialBlurCenterY);
					}
				};
				this.mRenderTextureSprite.setOffsetCenter(0, 0);
				this.mRenderTextureSprite.setScaleY(-1);
			}
		};
	}

	@Override
	protected void onCreateResources() throws IOException {
		this.mBadgeTexture = new AssetBitmapTexture(this.getTextureManager(), this.getAssets(), "gfx/badge_large.png", TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mBadgeTextureRegion = TextureRegionFactory.extractFromTexture(this.mBadgeTexture);
		this.mBadgeTexture.load();

		this.getShaderProgramManager().loadShaderProgram(RadialBlurShaderProgram.getInstance());
	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();

		final float centerX = RadialBlurExample.CAMERA_WIDTH / 2;
		final float centerY = RadialBlurExample.CAMERA_HEIGHT / 2;

		/* Create the sprite and add it to the scene. */
		final Sprite sprite = new Sprite(centerX, centerY, this.mBadgeTextureRegion, this.getVertexBufferObjectManager());
		scene.attachChild(sprite);

		/* TouchListener */
		this.mClickDetector = new ClickDetector(this);
		scene.setOnSceneTouchListener(this);

		return scene;
	}

	@Override
	public void onClick(final ClickDetector pClickDetector, final int pPointerID, final float pSceneX, final float pSceneY) {
		this.mRadialBlurring = !this.mRadialBlurring;
	}

	@Override
	public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
		this.mClickDetector.onSceneTouchEvent(pScene, pSceneTouchEvent);

		this.mRadialBlurCenterX = pSceneTouchEvent.getMotionEvent().getX() / this.mCamera.getSurfaceWidth();
		this.mRadialBlurCenterY = pSceneTouchEvent.getMotionEvent().getY() / this.mCamera.getSurfaceHeight();

		return true;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static class RadialBlurShaderProgram extends ShaderProgram {
		// ===========================================================
		// Constants
		// ===========================================================

		private static RadialBlurShaderProgram INSTANCE;

		public static final String VERTEXSHADER =
			"uniform mat4 " + ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX + ";\n" +
			"attribute vec4 " + ShaderProgramConstants.ATTRIBUTE_POSITION + ";\n" +
			"attribute vec2 " + ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES + ";\n" +
			"varying vec2 " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ";\n" +
			"void main() {\n" +
			"	" + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + " = " + ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES + ";\n" +
			"	gl_Position = " + ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX + " * " + ShaderProgramConstants.ATTRIBUTE_POSITION + ";\n" +
			"}";

		private static final String UNIFORM_RADIALBLUR_CENTER = "u_radialblur_center";

		public static final String FRAGMENTSHADER =
			"precision lowp float;\n" +

			"uniform sampler2D " + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ";\n" +
			"varying mediump vec2 " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ";\n" +

			"uniform vec2 " + RadialBlurShaderProgram.UNIFORM_RADIALBLUR_CENTER + ";\n" +

			"const float sampleShare = (1.0 / 11.0);\n" +
			"const float sampleDist = 1.0;\n" +
			"const float sampleStrength = 1.25;\n" +

			"void main() {\n" +
			/* The actual (unburred) sample. */
			"	vec4 color = texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ");\n" +

			/* Calculate direction towards center of the blur. */
			"	vec2 direction = " + RadialBlurShaderProgram.UNIFORM_RADIALBLUR_CENTER + " - " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ";\n" +

			/* Calculate the distance to the center of the blur. */
			"	float distance = sqrt(direction.x * direction.x + direction.y * direction.y);\n" +

			/* Normalize the direction (reuse the distance). */
			"	direction = direction / distance;\n" +

			"	vec4 sum = color * sampleShare;\n" +
			/* Take 10 additional samples along the direction towards the center of the blur. */
			"	vec2 directionSampleDist = direction * sampleDist;\n" +
			"	sum += texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + " - 0.08 * directionSampleDist) * sampleShare;\n" +
			"	sum += texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + " - 0.05 * directionSampleDist) * sampleShare;\n" +
			"	sum += texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + " - 0.03 * directionSampleDist) * sampleShare;\n" +
			"	sum += texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + " - 0.02 * directionSampleDist) * sampleShare;\n" +
			"	sum += texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + " - 0.01 * directionSampleDist) * sampleShare;\n" +
			"	sum += texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + " + 0.01 * directionSampleDist) * sampleShare;\n" +
			"	sum += texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + " + 0.02 * directionSampleDist) * sampleShare;\n" +
			"	sum += texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + " + 0.03 * directionSampleDist) * sampleShare;\n" +
			"	sum += texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + " + 0.05 * directionSampleDist) * sampleShare;\n" +
			"	sum += texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + " + 0.08 * directionSampleDist) * sampleShare;\n" +

			/* Weighten the blur effect with the distance to the center of the blur (further out is blurred more). */
			"	float t = sqrt(distance) * sampleStrength;\n" +
			"	t = clamp(t, 0.0, 1.0);\n" + // 0 <= t >= 1

			/* Blend the original color with the averaged pixels. */
			"	gl_FragColor = mix(color, sum, t);\n" +
			"}";

		// ===========================================================
		// Fields
		// ===========================================================

		public static int sUniformModelViewPositionMatrixLocation = ShaderProgramConstants.LOCATION_INVALID;
		public static int sUniformTexture0Location = ShaderProgramConstants.LOCATION_INVALID;
		public static int sUniformRadialBlurCenterLocation = ShaderProgramConstants.LOCATION_INVALID;

		// ===========================================================
		// Constructors
		// ===========================================================

		private RadialBlurShaderProgram() {
			super(RadialBlurShaderProgram.VERTEXSHADER, RadialBlurShaderProgram.FRAGMENTSHADER);
		}

		public static RadialBlurShaderProgram getInstance() {
			if(RadialBlurShaderProgram.INSTANCE == null) {
				RadialBlurShaderProgram.INSTANCE = new RadialBlurShaderProgram();
			}
			return RadialBlurShaderProgram.INSTANCE;
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		@Override
		protected void link(final GLState pGLState) throws ShaderProgramLinkException {
			GLES20.glBindAttribLocation(this.mProgramID, ShaderProgramConstants.ATTRIBUTE_POSITION_LOCATION, ShaderProgramConstants.ATTRIBUTE_POSITION);
			GLES20.glBindAttribLocation(this.mProgramID, ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES_LOCATION, ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES);

			super.link(pGLState);

			RadialBlurShaderProgram.sUniformModelViewPositionMatrixLocation = this.getUniformLocation(ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX);
			RadialBlurShaderProgram.sUniformTexture0Location = this.getUniformLocation(ShaderProgramConstants.UNIFORM_TEXTURE_0);

			RadialBlurShaderProgram.sUniformRadialBlurCenterLocation = this.getUniformLocation(RadialBlurShaderProgram.UNIFORM_RADIALBLUR_CENTER);
		}

		@Override
		public void bind(final GLState pGLState, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
			GLES20.glDisableVertexAttribArray(ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION);

			super.bind(pGLState, pVertexBufferObjectAttributes);

			GLES20.glUniformMatrix4fv(RadialBlurShaderProgram.sUniformModelViewPositionMatrixLocation, 1, false, pGLState.getModelViewProjectionGLMatrix(), 0);
			GLES20.glUniform1i(RadialBlurShaderProgram.sUniformTexture0Location, 0);
		}

		@Override
		public void unbind(final GLState pGLState) throws ShaderProgramException {
			GLES20.glEnableVertexAttribArray(ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION);

			super.unbind(pGLState);
		}

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
