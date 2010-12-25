package org.anddev.andengine.examples.app.cityradar;

import java.util.ArrayList;
import java.util.HashMap;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.camera.hud.HUD;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.modifier.LoopEntityModifier;
import org.anddev.andengine.entity.modifier.RotationModifier;
import org.anddev.andengine.entity.primitive.Line;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.examples.adt.cityradar.City;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.BuildableTexture;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.builder.BlackPawnTextureBuilder;
import org.anddev.andengine.opengl.texture.builder.ITextureBuilder.TextureSourcePackingException;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.sensor.location.ILocationListener;
import org.anddev.andengine.sensor.location.LocationProviderStatus;
import org.anddev.andengine.sensor.location.LocationSensorOptions;
import org.anddev.andengine.sensor.orientation.IOrientationListener;
import org.anddev.andengine.sensor.orientation.OrientationData;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.Debug;
import org.anddev.andengine.util.MathUtils;
import org.anddev.andengine.util.modifier.ease.EaseLinear;

import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

public class CityRadarActivity extends BaseGameActivity implements IOrientationListener, ILocationListener {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final boolean USE_MOCK_LOCATION = false;
	private static final boolean USE_ACTUAL_LOCATION = !USE_MOCK_LOCATION;

	private static final int CAMERA_WIDTH = 480;
	private static final int CAMERA_HEIGHT = 800;

	private static final int GRID_SIZE = 80;

	private static final int LAYER_COUNT = 1;
	private static final int LAYER_CITIES = 0;

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mCamera;

	private BuildableTexture mBuildableTexture;

	private TextureRegion mRadarPointTextureRegion;
	private TextureRegion mRadarTextureRegion;

	private Texture mFontTexture;
	private Font mFont;

	private Location mUserLocation;

	private final ArrayList<City> mCities = new ArrayList<City>();
	private final HashMap<City, Sprite> mCityToCitySpriteMap = new HashMap<City, Sprite>();
	private final HashMap<City, Text> mCityToCityNameTextMap = new HashMap<City, Text>();

	// ===========================================================
	// Constructors
	// ===========================================================

	public CityRadarActivity() {
		this.mCities.add(new City("London", 51.509, -0.118));
		this.mCities.add(new City("New York", 40.713, -74.006));
//		this.mCities.add(new City("Paris", 48.857, 2.352));
		this.mCities.add(new City("Beijing", 39.929, 116.388));
		this.mCities.add(new City("Sydney", -33.850, 151.200));
		this.mCities.add(new City("Berlin", 52.518, 13.408));
		this.mCities.add(new City("Rio", -22.908, -43.196));
		this.mCities.add(new City("New Delhi", 28.636, 77.224));
		this.mCities.add(new City("Cape Town", -33.926, 18.424));

		this.mUserLocation = new Location(LocationManager.GPS_PROVIDER);

		if(USE_MOCK_LOCATION) {
			this.mUserLocation.setLatitude(51.518);
			this.mUserLocation.setLongitude(13.408);
		}
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public org.anddev.andengine.engine.Engine onLoadEngine() {
		this.mCamera = new Camera(0, 0, CityRadarActivity.CAMERA_WIDTH, CityRadarActivity.CAMERA_HEIGHT);
		return new org.anddev.andengine.engine.Engine(new EngineOptions(true, ScreenOrientation.PORTRAIT, new FillResolutionPolicy(), this.mCamera));
	}

	@Override
	public void onLoadResources() {
		/* Init font. */
		this.mFontTexture = new Texture(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mFont = new Font(this.mFontTexture, Typeface.DEFAULT, 12, true, Color.WHITE);

		this.mEngine.getFontManager().loadFont(this.mFont);
		this.mEngine.getTextureManager().loadTexture(this.mFontTexture);

		/* Init TextureRegions. */
		this.mBuildableTexture = new BuildableTexture(512, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		this.mRadarTextureRegion = TextureRegionFactory.createFromAsset(this.mBuildableTexture, this, "gfx/radar.png");
		this.mRadarPointTextureRegion = TextureRegionFactory.createFromAsset(this.mBuildableTexture, this, "gfx/radarpoint.png");

		try {
			this.mBuildableTexture.build(new BlackPawnTextureBuilder(1));
		} catch (final TextureSourcePackingException e) {
			Debug.e(e);
		}

		this.mEngine.getTextureManager().loadTexture(this.mBuildableTexture);
	}

	@Override
	public Scene onLoadScene() {
		final Scene scene = new Scene(LAYER_COUNT);

		final HUD hud = new HUD();
		this.mCamera.setHUD(hud);
		
		/* BACKGROUND */
		this.initBackground(hud.getFirstChild());

		/* CITIES */
		this.initCitySprites(scene.getChild(CityRadarActivity.LAYER_CITIES));

		return scene;
	}

	private void initCitySprites(final IEntity pEntity) {
		final int cityCount = this.mCities.size();

		for(int i = 0; i < cityCount; i++) {
			final City city = this.mCities.get(i);

			final Sprite citySprite = new Sprite(CityRadarActivity.CAMERA_WIDTH / 2, CityRadarActivity.CAMERA_HEIGHT / 2, this.mRadarPointTextureRegion);
			citySprite.setColor(0, 0.5f, 0, 1f);

			final Text cityNameText = new Text(CAMERA_WIDTH / 2, CAMERA_HEIGHT / 2, this.mFont, city.getName()) {
				@Override
				protected void onManagedDraw(final GL10 pGL, final Camera pCamera) {
					/* This ensures that the name of the city is always 'pointing down'. */
					this.setRotation(-CityRadarActivity.this.mCamera.getRotation());
					super.onManagedDraw(pGL, pCamera);
				}
			};
			cityNameText.setRotationCenterY(- citySprite.getHeight() / 2);

			this.mCityToCityNameTextMap.put(city, cityNameText);
			this.mCityToCitySpriteMap.put(city, citySprite);

			pEntity.attachChild(citySprite);
			pEntity.attachChild(cityNameText);
		}
	}

	private void initBackground(final IEntity pEntity) {
		/* Vertical Grid lines. */
		for(int i = CityRadarActivity.GRID_SIZE / 2; i < CityRadarActivity.CAMERA_WIDTH; i += CityRadarActivity.GRID_SIZE) {
			final Line line = new Line(i, 0, i, CityRadarActivity.CAMERA_HEIGHT);
			line.setColor(0, 0.5f, 0, 1f);
			pEntity.attachChild(line);
		}

		/* Horizontal Grid lines. */
		for(int i = CityRadarActivity.GRID_SIZE / 2; i < CityRadarActivity.CAMERA_HEIGHT; i += CityRadarActivity.GRID_SIZE) {
			final Line line = new Line(0, i, CityRadarActivity.CAMERA_WIDTH, i);
			line.setColor(0, 0.5f, 0, 1f);
			pEntity.attachChild(line);
		}

		/* Vertical Grid lines. */
		final Sprite radarSprite = new Sprite(CityRadarActivity.CAMERA_WIDTH / 2 - this.mRadarTextureRegion.getWidth(), CityRadarActivity.CAMERA_HEIGHT / 2 - this.mRadarTextureRegion.getHeight(), this.mRadarTextureRegion);
		radarSprite.setColor(0, 1f, 0, 1f);
		radarSprite.setRotationCenter(radarSprite.getWidth(), radarSprite.getHeight());
		radarSprite.registerEntityModifier(new LoopEntityModifier(new RotationModifier(3, 0, 360, EaseLinear.getInstance())));
		pEntity.attachChild(radarSprite);

		/* Title. */
		final Text titleText = new Text(0, 0, this.mFont, "-- CityRadar --");
		titleText.setPosition(CAMERA_WIDTH / 2 - titleText.getWidth() / 2, titleText.getHeight() + 35);
		titleText.setScale(2);
		titleText.setScaleCenterY(0);
		pEntity.attachChild(titleText);
	}

	@Override
	public void onLoadComplete() {
		this.refreshCitySprites();
	}

	@Override
	protected void onResume() {
		super.onResume();

		this.enableOrientationSensor(this);

		final LocationSensorOptions locationSensorOptions = new LocationSensorOptions();
		locationSensorOptions.setAccuracy(Criteria.ACCURACY_COARSE);
		locationSensorOptions.setMinimumTriggerTime(0);
		locationSensorOptions.setMinimumTriggerDistance(0);
		this.enableLocationSensor(this, locationSensorOptions);
	}

	@Override
	protected void onPause() {
		super.onPause();
		this.mEngine.disableOrientationSensor(this);
		this.mEngine.disableLocationSensor(this);
	}

	@Override
	public void onOrientationChanged(final OrientationData pOrientationData) {
		this.mCamera.setRotation(-pOrientationData.getYaw());
	}

	@Override
	public void onLocationChanged(final Location pLocation) {
		if(USE_ACTUAL_LOCATION) {
			this.mUserLocation = pLocation;
		}
		this.refreshCitySprites();
	}

	@Override
	public void onLocationLost() {
	}

	@Override
	public void onLocationProviderDisabled() {
	}

	@Override
	public void onLocationProviderEnabled() {
	}

	@Override
	public void onLocationProviderStatusChanged(final LocationProviderStatus pLocationProviderStatus, final Bundle pBundle) {
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void refreshCitySprites() {
		final double userLatitudeRad = MathUtils.degToRad((float) this.mUserLocation.getLatitude());
		final double userLongitudeRad = MathUtils.degToRad((float) this.mUserLocation.getLongitude());

		final int cityCount = this.mCities.size();

		double maxDistance = Double.MIN_VALUE;

		/* Calculate the distances and bearings of the cities to the location of the user. */
		for(int i = 0; i < cityCount; i++) {
			final City city = this.mCities.get(i);

			final double cityLatitudeRad = MathUtils.degToRad((float) city.getLatitude());
			final double cityLongitudeRad = MathUtils.degToRad((float) city.getLongitude());

			city.setDistanceToUser(GeoMath.calculateDistance(userLatitudeRad, userLongitudeRad, cityLatitudeRad, cityLongitudeRad));
			city.setBearingToUser(GeoMath.calculateBearing(userLatitudeRad, userLongitudeRad, cityLatitudeRad, cityLongitudeRad));

			maxDistance = Math.max(maxDistance, city.getDistanceToUser());
		}

		/* Calculate a scaleRatio so that all cities are visible at all times. */
		final double scaleRatio = (CityRadarActivity.CAMERA_WIDTH / 2) / maxDistance * 0.93f;

		for(int i = 0; i < cityCount; i++) {
			final City city = this.mCities.get(i);

			final Sprite citySprite = this.mCityToCitySpriteMap.get(city);
			final Text cityNameText = this.mCityToCityNameTextMap.get(city);

			final float bearingInRad = MathUtils.degToRad(90 - (float) city.getBearingToUser());

			final float x = (float) (CityRadarActivity.CAMERA_WIDTH / 2 + city.getDistanceToUser() * scaleRatio * Math.cos(bearingInRad));
			final float y = (float) (CityRadarActivity.CAMERA_HEIGHT / 2 - city.getDistanceToUser() * scaleRatio * Math.sin(bearingInRad));

			citySprite.setPosition(x - citySprite.getWidth() / 2, y - citySprite.getHeight() / 2);

			final float textX = x - cityNameText.getWidth() / 2;
			final float textY = y + citySprite.getHeight() / 2;

			cityNameText.setPosition(textX, textY);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	/**
	 * Note: Formulas taken from <a href="http://www.movable-type.co.uk/scripts/latlong.html">here</a>.
	 */
	private static class GeoMath {
		// ===========================================================
		// Constants
		// ===========================================================

		private static final double RADIUS_EARTH_METERS = 6371000;

		// ===========================================================
		// Fields
		// ===========================================================

		// ===========================================================
		// Constructors
		// ===========================================================

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		/**
		 * @return the distance in meters.
		 */
		public static double calculateDistance(final double pLatitude1, final double pLongitude1, final double pLatitude2, final double pLongitude2) {
			return Math.acos(Math.sin(pLatitude1) * Math.sin(pLatitude2) + Math.cos(pLatitude1) * Math.cos(pLatitude2) * Math.cos(pLongitude2 - pLongitude1)) * RADIUS_EARTH_METERS;
		}

		/**
		 * @return the bearing in degrees.
		 */
		public static double calculateBearing(final double pLatitude1, final double pLongitude1, final double pLatitude2, final double pLongitude2) {
			final double y = Math.sin(pLongitude2 - pLongitude1) * Math.cos(pLatitude2);
			final double x = Math.cos(pLatitude1) * Math.sin(pLatitude2) - Math.sin(pLatitude1) * Math.cos(pLatitude2) * Math.cos(pLongitude2 - pLongitude1);
			final float bearing = MathUtils.radToDeg((float) Math.atan2(y, x));
			return (bearing + 360) % 360;
		}

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}