package org.anddev.andengine.examples;

import java.io.IOException;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.level.LevelLoader;
import org.anddev.andengine.level.LevelLoader.IEntityLoader;
import org.anddev.andengine.level.util.constants.LevelConstants;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.util.Debug;
import org.anddev.andengine.util.SAXUtils;
import org.xml.sax.Attributes;

import android.widget.Toast;

/**
 * @author Nicolas Gramlich
 * @since 17:16:10 - 11.10.2010
 */
public class LevelLoaderExample extends BaseExample {
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

	private Texture mTexture;

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
	public Engine onLoadEngine() {
		Toast.makeText(this, "Loading level...", Toast.LENGTH_SHORT).show();
		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera));
	}

	@Override
	public void onLoadResources() {
		/* Textures. */
		this.mTexture = new Texture(64, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		TextureRegionFactory.setAssetBasePath("gfx/");

		/* TextureRegions. */
		this.mBoxFaceTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mTexture, this, "face_box_tiled.png", 0, 0, 2, 1); // 64x32
		this.mCircleFaceTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mTexture, this, "face_circle_tiled.png", 0, 32, 2, 1); // 64x32
		this.mTriangleFaceTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mTexture, this, "face_triangle_tiled.png", 0, 64, 2, 1); // 64x32
		this.mHexagonFaceTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mTexture, this, "face_hexagon_tiled.png", 0, 96, 2, 1); // 64x32

		this.mEngine.getTextureManager().loadTexture(this.mTexture);
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene(1);
		scene.setBackground(new ColorBackground(0, 0, 0));

		final LevelLoader levelLoader = new LevelLoader();
		levelLoader.setAssetBasePath("level/");

		levelLoader.registerEntityLoader(LevelConstants.TAG_LEVEL, new IEntityLoader() {
			@Override
			public void onLoadEntity(final String pEntityName, final Attributes pAttributes) {
				final int width = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_WIDTH);
				final int height = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_HEIGHT);
				Toast.makeText(LevelLoaderExample.this, "Loaded level with width=" + width + " and height=" + height + ".", Toast.LENGTH_LONG).show();
			}
		});

		levelLoader.registerEntityLoader(TAG_ENTITY, new IEntityLoader() {
			@Override
			public void onLoadEntity(final String pEntityName, final Attributes pAttributes) {
				final int x = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_X);
				final int y = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_Y);
				final int width = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_WIDTH);
				final int height = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_HEIGHT);
				final String type = SAXUtils.getAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_TYPE);

				LevelLoaderExample.this.addFace(scene, x, y, width, height, type);
			}
		});

		try {
			levelLoader.loadLevelFromAsset(this, "example.lvl");
		} catch (final IOException e) {
			Debug.e(e);
		}

		return scene;
	}

	@Override
	public void onLoadComplete() {

	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void addFace(final Scene pScene, final float pX, final float pY, final int pWidth, final int pHeight, final String pType) {
		final AnimatedSprite face;

		if(pType.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_BOX)) {
			face = new AnimatedSprite(pX, pY, pWidth, pHeight, this.mBoxFaceTextureRegion);
		} else if(pType.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_CIRCLE)) {
			face = new AnimatedSprite(pX, pY, pWidth, pHeight, this.mCircleFaceTextureRegion);
		} else if(pType.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_TRIANGLE)) {
			face = new AnimatedSprite(pX, pY, pWidth, pHeight, this.mTriangleFaceTextureRegion);
		} else if(pType.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_HEXAGON)) {
			face = new AnimatedSprite(pX, pY, pWidth, pHeight, this.mHexagonFaceTextureRegion);
		} else {
			throw new IllegalArgumentException();
		}

		face.animate(200);

		pScene.getLastChild().attachChild(face);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}