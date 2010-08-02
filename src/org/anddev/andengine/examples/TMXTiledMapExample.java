package org.anddev.andengine.examples;

import java.util.ArrayList;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.BoundCamera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXLayer;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXLoader;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTileProperty;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTiledMap;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXLoader.ITMXTilePropertiesListener;
import org.anddev.andengine.entity.layer.tiled.tmx.util.exception.TMXLoadException;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.entity.shape.modifier.LoopModifier;
import org.anddev.andengine.entity.shape.modifier.PathModifier;
import org.anddev.andengine.entity.shape.modifier.PathModifier.IPathModifierListener;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.util.Debug;
import org.anddev.andengine.util.Path;

import android.widget.Toast;

/**
 * @author Nicolas Gramlich
 * @since 13:58:48 - 19.07.2010
 */
public class TMXTiledMapExample extends BaseExample {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 800;
	private static final int CAMERA_HEIGHT = 480;

	// ===========================================================
	// Fields
	// ===========================================================

	private BoundCamera mBoundChaseCamera;

	private Texture mTexture;
	private TiledTextureRegion mPlayerTextureRegion;
	private TMXTiledMap mTMXTiledMap;
	protected int mCactusCount;

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
		this.mBoundChaseCamera = new BoundCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mBoundChaseCamera));
	}

	@Override
	public void onLoadResources() {
		this.mTexture = new Texture(128, 128, TextureOptions.DEFAULT);
		this.mPlayerTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mTexture, this, "gfx/player.png", 0, 0, 3, 4);

		this.mEngine.getTextureManager().loadTexture(this.mTexture);
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene(2);

		try {
			final TMXLoader tmxLoader = new TMXLoader(this, this.mEngine.getTextureManager(), TextureOptions.BILINEAR, new ITMXTilePropertiesListener() {
				@Override
				public void onTMXTileWithPropertiesCreated(final TMXTiledMap pTMXTiledMap, final TMXLayer pTMXLayer, final ArrayList<TMXTileProperty> pTMXTileProperties, final int pTileRow, final int pTileColumn, final int pTileWidth, final int pTileHeight) {
					final int tmxTilePropertyCount = pTMXTileProperties.size();
					/* We are going to count the tiles that have the property "cactus=true" set. */ 
					for(int i = 0; i < tmxTilePropertyCount; i++) {
						TMXTileProperty tmxTileProperty = pTMXTileProperties.get(i);
						if(tmxTileProperty.getName().equals("cactus") && tmxTileProperty.getValue().equals("true")) {
							TMXTiledMapExample.this.mCactusCount++;
						}
					}
				}
			});
			this.mTMXTiledMap = tmxLoader.loadFromAsset(this, "tmx/desert.tmx");
			
			Toast.makeText(this, "Cactus count in this TMXTiledMap: " + this.mCactusCount, Toast.LENGTH_LONG).show();
		} catch (final TMXLoadException tmxe) {
			Debug.e(tmxe);
		}
		
		final TMXLayer tmxLayer = this.mTMXTiledMap.getTMXLayers().get(0);
		scene.getBottomLayer().addEntity(tmxLayer);
		this.mBoundChaseCamera.setBounds(0, tmxLayer.getWidth(), 0, tmxLayer.getHeight());
		this.mBoundChaseCamera.setBoundsEnabled(true);

		/* Calculate the coordinates for the face, so its centered on the camera. */
		final int centerX = (CAMERA_WIDTH - this.mPlayerTextureRegion.getTileWidth()) / 2;
		final int centerY = (CAMERA_HEIGHT - this.mPlayerTextureRegion.getTileHeight()) / 2;

		/* Create the sprite and add it to the scene. */
		final AnimatedSprite player = new AnimatedSprite(centerX, centerY, this.mPlayerTextureRegion);
		this.mBoundChaseCamera.setChaseShape(player);

		final Path path = new Path(5).to(0, 160).to(0, 500).to(600, 500).to(600, 160).to(0, 160);

		player.addShapeModifier(new LoopModifier(new PathModifier(30, path, null, new IPathModifierListener() {
			@Override
			public void onWaypointPassed(final PathModifier pPathModifier, final IShape pShape, final int pWaypointIndex) {
				switch(pWaypointIndex) {
					case 0:
						player.animate(new long[]{200, 200, 200}, 6, 8, true);
						break;
					case 1:
						player.animate(new long[]{200, 200, 200}, 3, 5, true);
						break;
					case 2:
						player.animate(new long[]{200, 200, 200}, 0, 2, true);
						break;
					case 3:
						player.animate(new long[]{200, 200, 200}, 9, 11, true);
						break;
				}
			}
		})));
		scene.getTopLayer().addEntity(player);

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
