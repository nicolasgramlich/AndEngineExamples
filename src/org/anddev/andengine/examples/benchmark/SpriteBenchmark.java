package org.anddev.andengine.examples.benchmark;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.sprite.batch.SpriteBatch;
import org.anddev.andengine.entity.sprite.batch.SpriteBatch.SpriteBatchMesh;
import org.anddev.andengine.opengl.shader.ShaderProgram;
import org.anddev.andengine.opengl.shader.util.constants.ShaderPrograms;
import org.anddev.andengine.opengl.texture.ITexture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.ITextureRegion;
import org.anddev.andengine.opengl.util.GLHelper;
import org.anddev.andengine.opengl.vbo.VertexBufferObjectAttributes;
import org.anddev.andengine.opengl.vbo.VertexBufferObjectAttributesBuilder;

import android.opengl.GLES20;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 10:34:22 - 27.06.2010
 */
public class SpriteBenchmark extends BaseBenchmark {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	private static final int SPRITE_COUNT = 5000;

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mCamera;
	private BitmapTextureAtlas mBitmapTextureAtlas;
	private ITextureRegion mFaceTextureRegion;

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
	protected int getBenchmarkID() {
		return BaseBenchmark.SPRITEBENCHMARK_ID;
	}

	@Override
	protected float getBenchmarkStartOffset() {
		return 2;
	}

	@Override
	protected float getBenchmarkDuration() {
		return 10;
	}

	@Override
	public Engine onLoadEngine() {
		this.mCamera = new Camera(0, 0, SpriteBenchmark.CAMERA_WIDTH, SpriteBenchmark.CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(SpriteBenchmark.CAMERA_WIDTH, SpriteBenchmark.CAMERA_HEIGHT), this.mCamera));
	}

	@Override
	public void onLoadResources() {
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(32, 32, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		this.mFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "face_box.png", 0, 0);

		this.mEngine.getTextureManager().loadTexture(this.mBitmapTextureAtlas);
	}

	@Override
	public Scene onLoadScene() {
		final Scene scene = new Scene();
		scene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));

//		this.drawUsingSprites(scene);
//		this.drawUsingSpritesWithSharedVertexBuffer(scene);
		this.drawUsingSpriteBatch(scene);

		return scene;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void drawUsingSprites(final Scene pScene) {
		for(int i = 0; i < SpriteBenchmark.SPRITE_COUNT; i++) {
			final Sprite face = new Sprite(this.mRandom.nextFloat() * (SpriteBenchmark.CAMERA_WIDTH - 32), this.mRandom.nextFloat() * (SpriteBenchmark.CAMERA_HEIGHT - 32), this.mFaceTextureRegion);
			face.setBlendFunction(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
			face.setIgnoreUpdate(true);
			pScene.attachChild(face);
		}
	}

//	private void drawUsingSpritesWithSharedVertexBuffer(final Scene pScene) {
//		/* As we are creating quite a lot of the same Sprites, we can let them share a VertexBuffer to significantly increase performance. */
//		final RectangleVertexBuffer sharedVertexBuffer = new RectangleVertexBuffer(GL11.GL_STATIC_DRAW, true);
//		sharedVertexBuffer.update(this.mFaceTextureRegion.getWidth(), this.mFaceTextureRegion.getHeight());
//
//		for(int i = 0; i < SPRITE_COUNT; i++) {
//			final Sprite face = new Sprite(this.mRandom.nextFloat() * (CAMERA_WIDTH - 32), this.mRandom.nextFloat() * (CAMERA_HEIGHT - 32), this.mFaceTextureRegion, sharedVertexBuffer);
//			face.setBlendFunction(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
//			face.setIgnoreUpdate(true);
//			pScene.attachChild(face);
//		}
//	}

	private void drawUsingSpriteBatch(final Scene pScene) {
		final int width = this.mFaceTextureRegion.getWidth();
		final int height = this.mFaceTextureRegion.getHeight();

		final SpriteBatchWithoutColor spriteBatch = new SpriteBatchWithoutColor(this.mBitmapTextureAtlas, SpriteBenchmark.SPRITE_COUNT);
//		final SpriteBatch spriteBatch = new SpriteBatch(this.mBitmapTextureAtlas, SpriteBenchmark.SPRITE_COUNT);

		spriteBatch.setBlendFunction(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		for(int i = 0; i < SpriteBenchmark.SPRITE_COUNT; i++) {
			final float x = this.mRandom.nextFloat() * (SpriteBenchmark.CAMERA_WIDTH - 32);
			final float y = this.mRandom.nextFloat() * (SpriteBenchmark.CAMERA_HEIGHT - 32);
			spriteBatch.draw(this.mFaceTextureRegion, x, y, width, height);
//			spriteBatch.draw(this.mFaceTextureRegion, x, y, width, height, 1, 1, 1, 1);
		}
		spriteBatch.submit();

		pScene.attachChild(spriteBatch);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	private static class SpriteBatchWithoutColor extends SpriteBatch {
		// ===========================================================
		// Constants
		// ===========================================================

		public static final int VERTEX_INDEX_X = 0;
		public static final int VERTEX_INDEX_Y = SpriteBatchWithoutColor.VERTEX_INDEX_X + 1;
		public static final int TEXTURECOORDINATES_INDEX_U = SpriteBatchWithoutColor.VERTEX_INDEX_Y + 1;
		public static final int TEXTURECOORDINATES_INDEX_V = SpriteBatchWithoutColor.TEXTURECOORDINATES_INDEX_U + 1;

		public static final int VERTEX_SIZE = 2 + 2;
		public static final int VERTICES_PER_SPRITE = 6;
		public static final int SPRITE_SIZE = SpriteBatchWithoutColor.VERTEX_SIZE * SpriteBatchWithoutColor.VERTICES_PER_SPRITE;

		public static final VertexBufferObjectAttributes VERTEXBUFFEROBJECTATTRIBUTES_WITHOUT_COLOR = new VertexBufferObjectAttributesBuilder(2)
			.add(ShaderPrograms.ATTRIBUTE_POSITION, 2, GLES20.GL_FLOAT, false)
			.add(ShaderPrograms.ATTRIBUTE_TEXTURECOORDINATES, 2, GLES20.GL_FLOAT, false)
			.build();
		
		public static final String VERTEXSHADER_COLOR_TEXTURECOORDINATES =
				"uniform mat4 " + ShaderPrograms.UNIFORM_MODELVIEWPROJECTIONMATRIX + ";\n" +
				"attribute vec4 " + ShaderPrograms.ATTRIBUTE_POSITION + ";\n" +
				"attribute vec2 " + ShaderPrograms.ATTRIBUTE_TEXTURECOORDINATES + ";\n" +
				"varying vec2 " + ShaderPrograms.VARYING_TEXTURECOORDINATES + ";\n" +
				"void main() {\n" +
				"   " + ShaderPrograms.VARYING_TEXTURECOORDINATES + " = " + ShaderPrograms.ATTRIBUTE_TEXTURECOORDINATES + ";\n" +
				"   gl_Position = " + ShaderPrograms.UNIFORM_MODELVIEWPROJECTIONMATRIX + " * " + ShaderPrograms.ATTRIBUTE_POSITION + ";\n" +
				"}";
		
		public static final String FRAGMENTSHADER_COLOR_TEXTURECOORDINATES = "precision lowp float;\n" + 
				"uniform sampler2D " + ShaderPrograms.UNIFORM_TEXTURE_0 + ";\n" +
				"varying mediump vec2 " + ShaderPrograms.VARYING_TEXTURECOORDINATES + ";\n" +
				"void main() {\n" +
				"  gl_FragColor = texture2D(" + ShaderPrograms.UNIFORM_TEXTURE_0 + ", " + ShaderPrograms.VARYING_TEXTURECOORDINATES + ");\n" +
				"}";
		
		// ===========================================================
		// Fields
		// ===========================================================

		private final SpriteBatchMeshWithoutColor mSpriteBatchMeshWithoutColor;

		// ===========================================================
		// Constructors
		// ===========================================================

		private SpriteBatchWithoutColor(final ITexture pTexture, final int pCapacity) {
			super(pTexture, pCapacity, new SpriteBatchMeshWithoutColor(pCapacity, GLES20.GL_STATIC_DRAW, true, SpriteBatchWithoutColor.VERTEXBUFFEROBJECTATTRIBUTES_WITHOUT_COLOR));
			this.mSpriteBatchMeshWithoutColor = (SpriteBatchMeshWithoutColor) this.mSpriteBatchMesh;
			
			this.setShaderProgram(new ShaderProgram(VERTEXSHADER_COLOR_TEXTURECOORDINATES, FRAGMENTSHADER_COLOR_TEXTURECOORDINATES) {
				private int mUniformModelViewPositionMatrixLocation = ShaderProgram.LOCATION_INVALID;
				private int mUniformTexture0Location = ShaderProgram.LOCATION_INVALID;
			
				@Override
				protected void onCompiled() {
					this.mUniformModelViewPositionMatrixLocation = this.getUniformLocation(ShaderPrograms.UNIFORM_MODELVIEWPROJECTIONMATRIX);
					this.mUniformTexture0Location = this.getUniformLocation(ShaderPrograms.UNIFORM_TEXTURE_0);
				};
			
				@Override
				public void bind(final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
					super.bind(pVertexBufferObjectAttributes);
					GLES20.glUniformMatrix4fv(this.mUniformModelViewPositionMatrixLocation, 1, false, GLHelper.getModelViewProjectionMatrix(), 0);
					GLES20.glUniform1i(this.mUniformTexture0Location, 0);
				};
			});
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		public void draw(final ITextureRegion pTextureRegion, final float pX, final float pY, final int pWidth, final int pHeight) {
			this.mSpriteBatchMeshWithoutColor.addWithoutColor(pTextureRegion, pX, pY, pWidth, pHeight);
			this.mIndex++;
		}

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}

	public static class SpriteBatchMeshWithoutColor extends SpriteBatchMesh {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		// ===========================================================
		// Constructors
		// ===========================================================

		public SpriteBatchMeshWithoutColor(final int pCapacity, final int pDrawType, final boolean pManaged, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
			super(pCapacity * SpriteBatchWithoutColor.SPRITE_SIZE, pDrawType, pManaged, pVertexBufferObjectAttributes);
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public void addWithoutColor(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight) {
			final float x1 = pX;
			final float y1 = pY;
			final float x2 = pX + pWidth;
			final float y2 = pY + pHeight;
			final float u = pTextureRegion.getU();
			final float v = pTextureRegion.getV();
			final float u2 = pTextureRegion.getU2();
			final float v2 = pTextureRegion.getV2();

			final float[] bufferData = this.mVertexBufferObject.getBufferData();
			final int index = this.mIndex;
			bufferData[index + 0 * SpriteBatchWithoutColor.VERTEX_SIZE + SpriteBatchWithoutColor.VERTEX_INDEX_X] = x1;
			bufferData[index + 0 * SpriteBatchWithoutColor.VERTEX_SIZE + SpriteBatchWithoutColor.VERTEX_INDEX_Y] = y1;
			bufferData[index + 0 * SpriteBatchWithoutColor.VERTEX_SIZE + SpriteBatchWithoutColor.TEXTURECOORDINATES_INDEX_U] = u;
			bufferData[index + 0 * SpriteBatchWithoutColor.VERTEX_SIZE + SpriteBatchWithoutColor.TEXTURECOORDINATES_INDEX_V] = v;

			bufferData[index + 1 * SpriteBatchWithoutColor.VERTEX_SIZE + SpriteBatchWithoutColor.VERTEX_INDEX_X] = x1;
			bufferData[index + 1 * SpriteBatchWithoutColor.VERTEX_SIZE + SpriteBatchWithoutColor.VERTEX_INDEX_Y] = y2;
			bufferData[index + 1 * SpriteBatchWithoutColor.VERTEX_SIZE + SpriteBatchWithoutColor.TEXTURECOORDINATES_INDEX_U] = u;
			bufferData[index + 1 * SpriteBatchWithoutColor.VERTEX_SIZE + SpriteBatchWithoutColor.TEXTURECOORDINATES_INDEX_V] = v2;

			bufferData[index + 2 * SpriteBatchWithoutColor.VERTEX_SIZE + SpriteBatchWithoutColor.VERTEX_INDEX_X] = x2;
			bufferData[index + 2 * SpriteBatchWithoutColor.VERTEX_SIZE + SpriteBatchWithoutColor.VERTEX_INDEX_Y] = y1;
			bufferData[index + 2 * SpriteBatchWithoutColor.VERTEX_SIZE + SpriteBatchWithoutColor.TEXTURECOORDINATES_INDEX_U] = u2;
			bufferData[index + 2 * SpriteBatchWithoutColor.VERTEX_SIZE + SpriteBatchWithoutColor.TEXTURECOORDINATES_INDEX_V] = v;

			bufferData[index + 3 * SpriteBatchWithoutColor.VERTEX_SIZE + SpriteBatchWithoutColor.VERTEX_INDEX_X] = x2;
			bufferData[index + 3 * SpriteBatchWithoutColor.VERTEX_SIZE + SpriteBatchWithoutColor.VERTEX_INDEX_Y] = y1;
			bufferData[index + 3 * SpriteBatchWithoutColor.VERTEX_SIZE + SpriteBatchWithoutColor.TEXTURECOORDINATES_INDEX_U] = u2;
			bufferData[index + 3 * SpriteBatchWithoutColor.VERTEX_SIZE + SpriteBatchWithoutColor.TEXTURECOORDINATES_INDEX_V] = v;

			bufferData[index + 4 * SpriteBatchWithoutColor.VERTEX_SIZE + SpriteBatchWithoutColor.VERTEX_INDEX_X] = x1;
			bufferData[index + 4 * SpriteBatchWithoutColor.VERTEX_SIZE + SpriteBatchWithoutColor.VERTEX_INDEX_Y] = y2;
			bufferData[index + 4 * SpriteBatchWithoutColor.VERTEX_SIZE + SpriteBatchWithoutColor.TEXTURECOORDINATES_INDEX_U] = u;
			bufferData[index + 4 * SpriteBatchWithoutColor.VERTEX_SIZE + SpriteBatchWithoutColor.TEXTURECOORDINATES_INDEX_V] = v2;

			bufferData[index + 5 * SpriteBatchWithoutColor.VERTEX_SIZE + SpriteBatchWithoutColor.VERTEX_INDEX_X] = x2;
			bufferData[index + 5 * SpriteBatchWithoutColor.VERTEX_SIZE + SpriteBatchWithoutColor.VERTEX_INDEX_Y] = y2;
			bufferData[index + 5 * SpriteBatchWithoutColor.VERTEX_SIZE + SpriteBatchWithoutColor.TEXTURECOORDINATES_INDEX_U] = u2;
			bufferData[index + 5 * SpriteBatchWithoutColor.VERTEX_SIZE + SpriteBatchWithoutColor.TEXTURECOORDINATES_INDEX_V] = v2;

			this.mIndex += SpriteBatchWithoutColor.SPRITE_SIZE;
		}

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
