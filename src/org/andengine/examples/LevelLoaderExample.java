package org.andengine.examples;

import java.io.IOException;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.SAXUtils;
import org.andengine.util.level.EntityLoader;
import org.andengine.util.level.constants.LevelConstants;
import org.andengine.util.level.simple.SimpleLevelEntityLoaderData;
import org.andengine.util.level.simple.SimpleLevelLoader;
import org.andengine.util.level.simple.SimpleLevelLoaderResult;
import org.xml.sax.Attributes;

import android.widget.Toast;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 17:16:10 - 11.10.2010
 */
public class LevelLoaderExample extends SimpleBaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 480;
	private static final int CAMERA_HEIGHT = 320;

	private static final String TAG_ENTITY = "entity";
	private static final String TAG_ENTITY_ATTRIBUTE_X = "x";
	private static final String TAG_ENTITY_ATTRIBUTE_Y = "y";
	private static final String TAG_ENTITY_ATTRIBUTE_WIDTH = "width";
	private static final String TAG_ENTITY_ATTRIBUTE_HEIGHT = "height";
	private static final String TAG_ENTITY_ATTRIBUTE_TYPE = "type";

	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_BOX = "box";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_CIRCLE = "circle";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_TRIANGLE = "triangle";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_HEXAGON = "hexagon";

	// ===========================================================
	// Fields
	// ===========================================================

	private BitmapTextureAtlas mBitmapTextureAtlas;

	private TiledTextureRegion mBoxFaceTextureRegion;
	private TiledTextureRegion mCircleFaceTextureRegion;
	private TiledTextureRegion mTriangleFaceTextureRegion;
	private TiledTextureRegion mHexagonFaceTextureRegion;

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
		Toast.makeText(this, "Loading level...", Toast.LENGTH_SHORT).show();

		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
	}

	@Override
	public void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 64, 128, TextureOptions.BILINEAR);
		this.mBoxFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "face_box_tiled.png", 0, 0, 2, 1); // 64x32
		this.mCircleFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "face_circle_tiled.png", 0, 32, 2, 1); // 64x32
		this.mTriangleFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "face_triangle_tiled.png", 0, 64, 2, 1); // 64x32
		this.mHexagonFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "face_hexagon_tiled.png", 0, 96, 2, 1); // 64x32
		this.mBitmapTextureAtlas.load();
	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final SimpleLevelLoader levelLoader = new SimpleLevelLoader(this.getVertexBufferObjectManager());

		levelLoader.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(LevelConstants.TAG_LEVEL) {
			@Override
			public IEntity onLoadEntity(final String pEntityName, final IEntity pParent, final Attributes pAttributes, final SimpleLevelEntityLoaderData pSimpleLevelEntityLoaderData) throws IOException {
				final int width = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_WIDTH);
				final int height = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_HEIGHT);
				
				LevelLoaderExample.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(LevelLoaderExample.this, "Loaded level with width=" + width + " and height=" + height + ".", Toast.LENGTH_LONG).show();
					}
				});
				
				final Scene scene = new Scene();
				scene.setBackground(new Background(0, 0, 0));
				return scene;
			}
		});

		levelLoader.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(TAG_ENTITY) {
			@Override
			public IEntity onLoadEntity(final String pEntityName, final IEntity pParent, final Attributes pAttributes, final SimpleLevelEntityLoaderData pSimpleLevelEntityLoaderData) throws IOException {
				final int x = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_X);
				final int y = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_Y);
				final int width = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_WIDTH);
				final int height = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_HEIGHT);
				final String type = SAXUtils.getAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_TYPE);

				final VertexBufferObjectManager vertexBufferObjectManager = pSimpleLevelEntityLoaderData.getVertexBufferObjectManager();

				final AnimatedSprite face;
				if(type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_BOX)) {
					face = new AnimatedSprite(x, y, width, height, LevelLoaderExample.this.mBoxFaceTextureRegion, vertexBufferObjectManager);
				} else if(type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_CIRCLE)) {
					face = new AnimatedSprite(x, y, width, height, LevelLoaderExample.this.mCircleFaceTextureRegion, vertexBufferObjectManager);
				} else if(type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_TRIANGLE)) {
					face = new AnimatedSprite(x, y, width, height, LevelLoaderExample.this.mTriangleFaceTextureRegion, vertexBufferObjectManager);
				} else if(type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_HEXAGON)) {
					face = new AnimatedSprite(x, y, width, height, LevelLoaderExample.this.mHexagonFaceTextureRegion, vertexBufferObjectManager);
				} else {
					throw new IllegalArgumentException();
				}

				face.animate(200);

				return face;
			}
		});

		final SimpleLevelLoaderResult simpleLevelLoaderResult = levelLoader.loadLevelFromAsset(this.getAssets(), "level/example.lvl");

		return simpleLevelLoaderResult.getScene();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}