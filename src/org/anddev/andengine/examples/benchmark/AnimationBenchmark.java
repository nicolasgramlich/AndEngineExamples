package org.anddev.andengine.examples.benchmark;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.Background;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.sprite.batch.SpriteBatch.SpriteBatchMesh;
import org.anddev.andengine.entity.sprite.batch.SpriteGroup;
import org.anddev.andengine.opengl.shader.PositionTextureCoordinatesShaderProgram;
import org.anddev.andengine.opengl.shader.util.constants.ShaderProgramConstants;
import org.anddev.andengine.opengl.texture.ITexture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.ITextureRegion;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.opengl.vbo.VertexBufferObject.DrawType;
import org.anddev.andengine.opengl.vbo.VertexBufferObjectAttributes;
import org.anddev.andengine.opengl.vbo.VertexBufferObjectAttributesBuilder;

import android.opengl.GLES20;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 11:28:45 - 28.06.2010
 */
public class AnimationBenchmark extends BaseBenchmark {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;
	
	private static final int SPRITE_COUNT = 1000;

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mCamera;

	private BitmapTextureAtlas mBitmapTextureAtlas;

	private TiledTextureRegion mSnapdragonTextureRegion;
	private TiledTextureRegion mHelicopterTextureRegion;
	private TiledTextureRegion mBananaTextureRegion;
	private TiledTextureRegion mFaceTextureRegion;

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
		return ANIMATIONBENCHMARK_ID;
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
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		final EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera);
		return new Engine(engineOptions);
	}

	@Override
	public void onLoadResources() {
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(512, 256, TextureOptions.BILINEAR);

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		this.mSnapdragonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "snapdragon_tiled.png", 0, 0, 4, 3);
		this.mHelicopterTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "helicopter_tiled.png", 400, 0, 2, 2);
		this.mBananaTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "banana_tiled.png", 0, 180, 4, 2);
		this.mFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "face_box_tiled.png", 132, 180, 2, 1);

		this.mBitmapTextureAtlas.load();
	}

	@Override
	public Scene onLoadScene() {
		final Scene scene = new Scene();
		scene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));
		
//		this.drawUsingSprites(scene);
//		this.drawUsingSpritesWithSharedVertexBuffer(scene);
		this.drawUsingSpriteBatch(scene); 

		return scene;
	}

	private void drawUsingSprites(Scene pScene) {
		for(int i = 0; i < SPRITE_COUNT; i++) {
			/* Quickly twinkling face. */
			final AnimatedSprite face = new AnimatedSprite(this.mRandom.nextFloat() * (CAMERA_WIDTH - 32), this.mRandom.nextFloat() * (CAMERA_HEIGHT - 32), this.mFaceTextureRegion.deepCopy()); //, faceSharedVertexBuffer);
			face.animate(50 + this.mRandom.nextInt(100));
			pScene.attachChild(face);

			/* Continuously flying helicopter. */
			final AnimatedSprite helicopter = new AnimatedSprite(this.mRandom.nextFloat() * (CAMERA_WIDTH - 48), this.mRandom.nextFloat() * (CAMERA_HEIGHT - 48), this.mHelicopterTextureRegion.deepCopy()); //, helicopterSharedVertexBuffer);
			helicopter.animate(new long[] { 50 + this.mRandom.nextInt(100), 50 + this.mRandom.nextInt(100) }, 1, 2, true);
			pScene.attachChild(helicopter);

			/* Snapdragon. */
			final AnimatedSprite snapdragon = new AnimatedSprite(this.mRandom.nextFloat() * (CAMERA_WIDTH - 100), this.mRandom.nextFloat() * (CAMERA_HEIGHT - 60), this.mSnapdragonTextureRegion.deepCopy()); //, snapdragonSharedVertexBuffer);
			snapdragon.animate(50 + this.mRandom.nextInt(100));
			pScene.attachChild(snapdragon);

			/* Funny banana. */
			final AnimatedSprite banana = new AnimatedSprite(this.mRandom.nextFloat() * (CAMERA_WIDTH - 32), this.mRandom.nextFloat() * (CAMERA_HEIGHT - 32), this.mBananaTextureRegion.deepCopy()); //, bananaSharedVertexBuffer);
			banana.animate(50 + this.mRandom.nextInt(100));
			pScene.attachChild(banana);
		}
	}

	private void drawUsingSpritesWithSharedVertexBuffer(Scene pScene) {
		/* As we are creating quite a lot of the same Sprites, we can let them share a VertexBuffer to significantly increase performance. */
//		final RectangleVertexBuffer faceSharedVertexBuffer = new RectangleVertexBuffer(GL11.GL_DYNAMIC_DRAW, true);
//		faceSharedVertexBuffer.update(this.mFaceTextureRegion.getWidth(), this.mFaceTextureRegion.getHeight());
//
//		final RectangleVertexBuffer helicopterSharedVertexBuffer = new RectangleVertexBuffer(GL11.GL_DYNAMIC_DRAW, true);
//		helicopterSharedVertexBuffer.update(this.mHelicopterTextureRegion.getWidth(), this.mHelicopterTextureRegion.getHeight());
//
//		final RectangleVertexBuffer snapdragonSharedVertexBuffer = new RectangleVertexBuffer(GL11.GL_DYNAMIC_DRAW, true);
//		snapdragonSharedVertexBuffer.update(this.mSnapdragonTextureRegion.getWidth(), this.mSnapdragonTextureRegion.getHeight());
//
//		final RectangleVertexBuffer bananaSharedVertexBuffer = new RectangleVertexBuffer(GL11.GL_DYNAMIC_DRAW, true);
//		bananaSharedVertexBuffer.update(this.mBananaTextureRegion.getWidth(), this.mBananaTextureRegion.getHeight());
//		
//		for(int i = 0; i < SPRITE_COUNT; i++) {
//			/* Quickly twinkling face. */
//			final AnimatedSprite face = new AnimatedSprite(this.mRandom.nextFloat() * (CAMERA_WIDTH - 32), this.mRandom.nextFloat() * (CAMERA_HEIGHT - 32), this.mFaceTextureRegion.deepCopy()); //, faceSharedVertexBuffer);
//			face.animate(50 + this.mRandom.nextInt(100));
//			pScene.attachChild(face);
//
//			/* Continuously flying helicopter. */
//			final AnimatedSprite helicopter = new AnimatedSprite(this.mRandom.nextFloat() * (CAMERA_WIDTH - 48), this.mRandom.nextFloat() * (CAMERA_HEIGHT - 48), this.mHelicopterTextureRegion.deepCopy()); //, helicopterSharedVertexBuffer);
//			helicopter.animate(new long[] { 50 + this.mRandom.nextInt(100), 50 + this.mRandom.nextInt(100) }, 1, 2, true);
//			pScene.attachChild(helicopter);
//
//			/* Snapdragon. */
//			final AnimatedSprite snapdragon = new AnimatedSprite(this.mRandom.nextFloat() * (CAMERA_WIDTH - 100), this.mRandom.nextFloat() * (CAMERA_HEIGHT - 60), this.mSnapdragonTextureRegion.deepCopy()); //, snapdragonSharedVertexBuffer);
//			snapdragon.animate(50 + this.mRandom.nextInt(100));
//			pScene.attachChild(snapdragon);
//
//			/* Funny banana. */
//			final AnimatedSprite banana = new AnimatedSprite(this.mRandom.nextFloat() * (CAMERA_WIDTH - 32), this.mRandom.nextFloat() * (CAMERA_HEIGHT - 32), this.mBananaTextureRegion.deepCopy()); //, bananaSharedVertexBuffer);
//			banana.animate(50 + this.mRandom.nextInt(100));
//			pScene.attachChild(banana);
//		}
	}

	private void drawUsingSpriteBatch(Scene pScene) {
//		final SpriteGroup spriteGroup = new SpriteGroup(this.mBitmapTextureAtlas, 4 * SPRITE_COUNT);
		final SpriteGroupWithoutColor spriteGroup = new SpriteGroupWithoutColor(this.mBitmapTextureAtlas, 4 * SPRITE_COUNT, DrawType.DYNAMIC);
		for(int i = 0; i < SPRITE_COUNT; i++) {
			/* Quickly twinkling face. */
			final AnimatedSprite face = new AnimatedSprite(this.mRandom.nextFloat() * (CAMERA_WIDTH - 32), this.mRandom.nextFloat() * (CAMERA_HEIGHT - 32), this.mFaceTextureRegion.deepCopy()); //, faceSharedVertexBuffer);
			face.animate(50 + this.mRandom.nextInt(100));
			spriteGroup.attachChild(face);

			/* Continuously flying helicopter. */
			final AnimatedSprite helicopter = new AnimatedSprite(this.mRandom.nextFloat() * (CAMERA_WIDTH - 48), this.mRandom.nextFloat() * (CAMERA_HEIGHT - 48), this.mHelicopterTextureRegion.deepCopy()); //, helicopterSharedVertexBuffer);
			helicopter.animate(new long[] { 50 + this.mRandom.nextInt(100), 50 + this.mRandom.nextInt(100) }, 1, 2, true);
			spriteGroup.attachChild(helicopter);

			/* Snapdragon. */
			final AnimatedSprite snapdragon = new AnimatedSprite(this.mRandom.nextFloat() * (CAMERA_WIDTH - 100), this.mRandom.nextFloat() * (CAMERA_HEIGHT - 60), this.mSnapdragonTextureRegion.deepCopy()); //, snapdragonSharedVertexBuffer);
			snapdragon.animate(50 + this.mRandom.nextInt(100));
			spriteGroup.attachChild(snapdragon);

			/* Funny banana. */
			final AnimatedSprite banana = new AnimatedSprite(this.mRandom.nextFloat() * (CAMERA_WIDTH - 32), this.mRandom.nextFloat() * (CAMERA_HEIGHT - 32), this.mBananaTextureRegion.deepCopy()); //, bananaSharedVertexBuffer);
			banana.animate(50 + this.mRandom.nextInt(100));
			spriteGroup.attachChild(banana);
		}
		
		pScene.attachChild(spriteGroup);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	private static class SpriteGroupWithoutColor extends SpriteGroup {
		// ===========================================================
		// Constants
		// ===========================================================

		public static final int VERTEX_INDEX_X = 0;
		public static final int VERTEX_INDEX_Y = SpriteGroupWithoutColor.VERTEX_INDEX_X + 1;
		public static final int TEXTURECOORDINATES_INDEX_U = SpriteGroupWithoutColor.VERTEX_INDEX_Y + 1;
		public static final int TEXTURECOORDINATES_INDEX_V = SpriteGroupWithoutColor.TEXTURECOORDINATES_INDEX_U + 1;

		public static final int VERTEX_SIZE = 2 + 2;
		public static final int VERTICES_PER_SPRITE = 6;
		public static final int SPRITE_SIZE = SpriteGroupWithoutColor.VERTEX_SIZE * SpriteGroupWithoutColor.VERTICES_PER_SPRITE;

		public static final VertexBufferObjectAttributes VERTEXBUFFEROBJECTATTRIBUTES_WITHOUT_COLOR = new VertexBufferObjectAttributesBuilder(2)
			.add(ShaderProgramConstants.ATTRIBUTE_POSITION_LOCATION, ShaderProgramConstants.ATTRIBUTE_POSITION, 2, GLES20.GL_FLOAT, false)
			.add(ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES_LOCATION, ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES, 2, GLES20.GL_FLOAT, false)
			.build();
		
		// ===========================================================
		// Fields
		// ===========================================================

		private final SpriteBatchMeshWithoutColor mSpriteBatchMeshWithoutColor;

		// ===========================================================
		// Constructors
		// ===========================================================

		private SpriteGroupWithoutColor(final ITexture pTexture, final int pCapacity, DrawType pDrawType) {
			super(pTexture, pCapacity, new SpriteBatchMeshWithoutColor(pCapacity, pDrawType, true, SpriteGroupWithoutColor.VERTEXBUFFEROBJECTATTRIBUTES_WITHOUT_COLOR));
			this.mSpriteBatchMeshWithoutColor = (SpriteBatchMeshWithoutColor) this.mMesh;

			this.setShaderProgram(PositionTextureCoordinatesShaderProgram.getInstance());
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================
		
		@Override
		public void drawWithoutChecks(final Sprite pSprite) {
			this.mSpriteBatchMeshWithoutColor.addWithoutColor(pSprite.getTextureRegion(), pSprite.getX(), pSprite.getY(), pSprite.getWidth(), pSprite.getHeight());
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

		public SpriteBatchMeshWithoutColor(final int pCapacity, final DrawType pDrawType, final boolean pManaged, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
			super(pCapacity * SpriteGroupWithoutColor.SPRITE_SIZE, pDrawType, pManaged, pVertexBufferObjectAttributes);
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
			final int bufferDataOffset = this.mBufferDataOffset;
			bufferData[bufferDataOffset + 0 * SpriteGroupWithoutColor.VERTEX_SIZE + SpriteGroupWithoutColor.VERTEX_INDEX_X] = x1;
			bufferData[bufferDataOffset + 0 * SpriteGroupWithoutColor.VERTEX_SIZE + SpriteGroupWithoutColor.VERTEX_INDEX_Y] = y1;
			bufferData[bufferDataOffset + 0 * SpriteGroupWithoutColor.VERTEX_SIZE + SpriteGroupWithoutColor.TEXTURECOORDINATES_INDEX_U] = u;
			bufferData[bufferDataOffset + 0 * SpriteGroupWithoutColor.VERTEX_SIZE + SpriteGroupWithoutColor.TEXTURECOORDINATES_INDEX_V] = v;

			bufferData[bufferDataOffset + 1 * SpriteGroupWithoutColor.VERTEX_SIZE + SpriteGroupWithoutColor.VERTEX_INDEX_X] = x1;
			bufferData[bufferDataOffset + 1 * SpriteGroupWithoutColor.VERTEX_SIZE + SpriteGroupWithoutColor.VERTEX_INDEX_Y] = y2;
			bufferData[bufferDataOffset + 1 * SpriteGroupWithoutColor.VERTEX_SIZE + SpriteGroupWithoutColor.TEXTURECOORDINATES_INDEX_U] = u;
			bufferData[bufferDataOffset + 1 * SpriteGroupWithoutColor.VERTEX_SIZE + SpriteGroupWithoutColor.TEXTURECOORDINATES_INDEX_V] = v2;

			bufferData[bufferDataOffset + 2 * SpriteGroupWithoutColor.VERTEX_SIZE + SpriteGroupWithoutColor.VERTEX_INDEX_X] = x2;
			bufferData[bufferDataOffset + 2 * SpriteGroupWithoutColor.VERTEX_SIZE + SpriteGroupWithoutColor.VERTEX_INDEX_Y] = y1;
			bufferData[bufferDataOffset + 2 * SpriteGroupWithoutColor.VERTEX_SIZE + SpriteGroupWithoutColor.TEXTURECOORDINATES_INDEX_U] = u2;
			bufferData[bufferDataOffset + 2 * SpriteGroupWithoutColor.VERTEX_SIZE + SpriteGroupWithoutColor.TEXTURECOORDINATES_INDEX_V] = v;

			bufferData[bufferDataOffset + 3 * SpriteGroupWithoutColor.VERTEX_SIZE + SpriteGroupWithoutColor.VERTEX_INDEX_X] = x2;
			bufferData[bufferDataOffset + 3 * SpriteGroupWithoutColor.VERTEX_SIZE + SpriteGroupWithoutColor.VERTEX_INDEX_Y] = y1;
			bufferData[bufferDataOffset + 3 * SpriteGroupWithoutColor.VERTEX_SIZE + SpriteGroupWithoutColor.TEXTURECOORDINATES_INDEX_U] = u2;
			bufferData[bufferDataOffset + 3 * SpriteGroupWithoutColor.VERTEX_SIZE + SpriteGroupWithoutColor.TEXTURECOORDINATES_INDEX_V] = v;

			bufferData[bufferDataOffset + 4 * SpriteGroupWithoutColor.VERTEX_SIZE + SpriteGroupWithoutColor.VERTEX_INDEX_X] = x1;
			bufferData[bufferDataOffset + 4 * SpriteGroupWithoutColor.VERTEX_SIZE + SpriteGroupWithoutColor.VERTEX_INDEX_Y] = y2;
			bufferData[bufferDataOffset + 4 * SpriteGroupWithoutColor.VERTEX_SIZE + SpriteGroupWithoutColor.TEXTURECOORDINATES_INDEX_U] = u;
			bufferData[bufferDataOffset + 4 * SpriteGroupWithoutColor.VERTEX_SIZE + SpriteGroupWithoutColor.TEXTURECOORDINATES_INDEX_V] = v2;

			bufferData[bufferDataOffset + 5 * SpriteGroupWithoutColor.VERTEX_SIZE + SpriteGroupWithoutColor.VERTEX_INDEX_X] = x2;
			bufferData[bufferDataOffset + 5 * SpriteGroupWithoutColor.VERTEX_SIZE + SpriteGroupWithoutColor.VERTEX_INDEX_Y] = y2;
			bufferData[bufferDataOffset + 5 * SpriteGroupWithoutColor.VERTEX_SIZE + SpriteGroupWithoutColor.TEXTURECOORDINATES_INDEX_U] = u2;
			bufferData[bufferDataOffset + 5 * SpriteGroupWithoutColor.VERTEX_SIZE + SpriteGroupWithoutColor.TEXTURECOORDINATES_INDEX_V] = v2;

			this.mBufferDataOffset += SpriteGroupWithoutColor.SPRITE_SIZE;
		}

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
