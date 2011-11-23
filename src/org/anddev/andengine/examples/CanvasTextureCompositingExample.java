package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.camera.CameraFactory;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.anddev.andengine.entity.modifier.LoopEntityModifier;
import org.anddev.andengine.entity.modifier.RotationModifier;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.Background;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.atlas.bitmap.source.EmptyBitmapTextureAtlasSource;
import org.anddev.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.anddev.andengine.opengl.texture.atlas.bitmap.source.decorator.BaseBitmapTextureAtlasSourceDecorator;
import org.anddev.andengine.opengl.texture.region.ITextureRegion;
import org.anddev.andengine.util.modifier.IModifier.DeepCopyNotSupportedException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 11:54:51 - 03.04.2010
 */
public class CanvasTextureCompositingExample extends BaseExample {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mCamera;
	private BitmapTextureAtlas mBitmapTextureAtlas;
	private ITextureRegion mDecoratedBalloonTextureRegion;

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
		this.mCamera = CameraFactory.createPixelPerfectCamera(this, 0, 0);
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new FillResolutionPolicy(), this.mCamera));
	}

	@Override
	public void onLoadResources() {
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(190, 190, TextureOptions.BILINEAR);
		
		final IBitmapTextureAtlasSource baseTextureSource = new EmptyBitmapTextureAtlasSource(190, 190);
		final IBitmapTextureAtlasSource decoratedTextureAtlasSource = new BaseBitmapTextureAtlasSourceDecorator(baseTextureSource) {
			@Override
			protected void onDecorateBitmap(Canvas pCanvas) throws Exception {
				this.mPaint.setColorFilter(new LightingColorFilter(Color.argb(128, 128, 128, 255), Color.TRANSPARENT));
				final Bitmap balloon = BitmapFactory.decodeStream(CanvasTextureCompositingExample.this.getAssets().open("gfx/texturecompositing/balloon.png"));
				pCanvas.drawBitmap(balloon, 0, 0, this.mPaint);
				this.mPaint.setColorFilter(null);

				this.mPaint.setXfermode(new PorterDuffXfermode(Mode.SRC_ATOP));
				final Bitmap alphamask = BitmapFactory.decodeStream(CanvasTextureCompositingExample.this.getAssets().open("gfx/texturecompositing/alphamask.png"));
				pCanvas.drawBitmap(alphamask, 0, 0, this.mPaint);
				this.mPaint.setXfermode(null);

				final Bitmap zynga = BitmapFactory.decodeStream(CanvasTextureCompositingExample.this.getAssets().open("gfx/texturecompositing/zynga.png"));
				pCanvas.drawBitmap(zynga, 0, 0, this.mPaint);
			}
			
			@Override
			public BaseBitmapTextureAtlasSourceDecorator deepCopy() {
				throw new DeepCopyNotSupportedException();
			}
		};

		this.mDecoratedBalloonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromSource(this.mBitmapTextureAtlas, decoratedTextureAtlasSource, 0, 0);
		this.mBitmapTextureAtlas.load();
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
		scene.setBackground(new Background(0.5f, 0.5f, 0.5f));

		/* Calculate the coordinates for the face, so its centered on the camera. */
		final int centerX = -this.mDecoratedBalloonTextureRegion.getWidth() / 2;
		final int centerY = -this.mDecoratedBalloonTextureRegion.getHeight() / 2;

		/* Create the balloon and add it to the scene. */
		final Sprite balloon = new Sprite(centerX, centerY, this.mDecoratedBalloonTextureRegion);
		balloon.registerEntityModifier(new LoopEntityModifier(new RotationModifier(60, 0, 360)));
		scene.attachChild(balloon);

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
