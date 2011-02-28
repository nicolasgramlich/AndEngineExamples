package org.anddev.andengine.examples.launcher;

import org.anddev.andengine.examples.AnalogOnScreenControlExample;
import org.anddev.andengine.examples.AnalogOnScreenControlsExample;
import org.anddev.andengine.examples.AnimatedSpritesExample;
import org.anddev.andengine.examples.AugmentedRealityExample;
import org.anddev.andengine.examples.AugmentedRealityHorizonExample;
import org.anddev.andengine.examples.AutoParallaxBackgroundExample;
import org.anddev.andengine.examples.BoundCameraExample;
import org.anddev.andengine.examples.ChangeableTextExample;
import org.anddev.andengine.examples.CollisionDetectionExample;
import org.anddev.andengine.examples.ColorKeyTextureSourceDecoratorExample;
import org.anddev.andengine.examples.CoordinateConversionExample;
import org.anddev.andengine.examples.CustomFontExample;
import org.anddev.andengine.examples.DigitalOnScreenControlExample;
import org.anddev.andengine.examples.EaseFunctionExample;
import org.anddev.andengine.examples.EntityModifierExample;
import org.anddev.andengine.examples.EntityModifierIrregularExample;
import org.anddev.andengine.examples.ImageFormatsExample;
import org.anddev.andengine.examples.LevelLoaderExample;
import org.anddev.andengine.examples.LineExample;
import org.anddev.andengine.examples.LoadTextureExample;
import org.anddev.andengine.examples.MenuExample;
import org.anddev.andengine.examples.ModPlayerExample;
import org.anddev.andengine.examples.MovingBallExample;
import org.anddev.andengine.examples.MultiTouchExample;
import org.anddev.andengine.examples.MultiplayerExample;
import org.anddev.andengine.examples.MusicExample;
import org.anddev.andengine.examples.ParticleSystemCoolExample;
import org.anddev.andengine.examples.ParticleSystemNexusExample;
import org.anddev.andengine.examples.ParticleSystemSimpleExample;
import org.anddev.andengine.examples.PathModifierExample;
import org.anddev.andengine.examples.PauseExample;
import org.anddev.andengine.examples.PhysicsCollisionFilteringExample;
import org.anddev.andengine.examples.PhysicsExample;
import org.anddev.andengine.examples.PhysicsFixedStepExample;
import org.anddev.andengine.examples.PhysicsJumpExample;
import org.anddev.andengine.examples.PhysicsMouseJointExample;
import org.anddev.andengine.examples.PhysicsRemoveExample;
import org.anddev.andengine.examples.PhysicsRevoluteJointExample;
import org.anddev.andengine.examples.PinchZoomExample;
import org.anddev.andengine.examples.R;
import org.anddev.andengine.examples.RectangleExample;
import org.anddev.andengine.examples.RepeatingSpriteBackgroundExample;
import org.anddev.andengine.examples.Rotation3DExample;
import org.anddev.andengine.examples.ScreenCaptureExample;
import org.anddev.andengine.examples.SoundExample;
import org.anddev.andengine.examples.SplitScreenExample;
import org.anddev.andengine.examples.SpriteExample;
import org.anddev.andengine.examples.SpriteRemoveExample;
import org.anddev.andengine.examples.StrokeFontExample;
import org.anddev.andengine.examples.SubMenuExample;
import org.anddev.andengine.examples.TMXTiledMapExample;
import org.anddev.andengine.examples.TextExample;
import org.anddev.andengine.examples.TextMenuExample;
import org.anddev.andengine.examples.TextureOptionsExample;
import org.anddev.andengine.examples.TickerTextExample;
import org.anddev.andengine.examples.TouchDragExample;
import org.anddev.andengine.examples.UnloadResourcesExample;
import org.anddev.andengine.examples.UpdateTextureExample;
import org.anddev.andengine.examples.XMLLayoutExample;
import org.anddev.andengine.examples.ZoomExample;
import org.anddev.andengine.examples.app.cityradar.CityRadarActivity;
import org.anddev.andengine.examples.benchmark.AnimationBenchmark;
import org.anddev.andengine.examples.benchmark.EntityModifierBenchmark;
import org.anddev.andengine.examples.benchmark.ParticleSystemBenchmark;
import org.anddev.andengine.examples.benchmark.PhysicsBenchmark;
import org.anddev.andengine.examples.benchmark.SpriteBenchmark;
import org.anddev.andengine.examples.benchmark.TickerTextBenchmark;
import org.anddev.andengine.examples.game.pong.PongGameActivity;
import org.anddev.andengine.examples.game.racer.RacerGameActivity;
import org.anddev.andengine.examples.game.snake.SnakeGameActivity;
import org.anddev.andengine.ui.activity.BaseGameActivity;

/**
 * @author Nicolas Gramlich
 * @since 20:42:27 - 16.06.2010
 */
enum Example {
	// ===========================================================
	// Elements
	// ===========================================================

	ANALOGONSCREENCONTROL(AnalogOnScreenControlExample.class, R.string.example_analogonscreencontrol),
	ANALOGONSCREENCONTROLS(AnalogOnScreenControlsExample.class, R.string.example_analogonscreencontrols),
	ANIMATEDSPRITES(AnimatedSpritesExample.class, R.string.example_animatedsprites),
	AUGMENTEDREALITY(AugmentedRealityExample.class, R.string.example_augmentedreality),
	AUGMENTEDREALITYHORIZON(AugmentedRealityHorizonExample.class, R.string.example_augmentedrealityhorizon),
	AUTOPARALLAXBACKGROUND(AutoParallaxBackgroundExample.class, R.string.example_autoparallaxbackground),
	BOUNDCAMERA(BoundCameraExample.class, R.string.example_boundcamera),
	CHANGEABLETEXT(ChangeableTextExample.class, R.string.example_changeabletext),
	COLLISIONDETECTION(CollisionDetectionExample.class, R.string.example_collisiondetection),
	COLORKEYTEXTURESOURCEDECORATOR(ColorKeyTextureSourceDecoratorExample.class, R.string.example_colorkeytexturesourcedecorator),
	COORDINATECONVERSION(CoordinateConversionExample.class, R.string.example_coordinateconversion),
	CUSTOMFONT(CustomFontExample.class, R.string.example_customfont),
	DIGITALONSCREENCONTROL(DigitalOnScreenControlExample.class, R.string.example_digitalonscreencontrol),
	EASEFUNCTION(EaseFunctionExample.class, R.string.example_easefunction),
	IMAGEFORMATS(ImageFormatsExample.class, R.string.example_imageformats),
	LEVELLOADER(LevelLoaderExample.class, R.string.example_levelloader),
	LINE(LineExample.class, R.string.example_line),
	LOADTEXTURE(LoadTextureExample.class, R.string.example_loadtexture),
	MENU(MenuExample.class, R.string.example_menu),
	MODPLAYER(ModPlayerExample.class, R.string.example_modplayer),
	MOVINGBALL(MovingBallExample.class, R.string.example_movingball),
	MULTIPLAYER(MultiplayerExample.class, R.string.example_multiplayer),
	MULTITOUCH(MultiTouchExample.class, R.string.example_multitouch),
	MUSIC(MusicExample.class, R.string.example_music),
	PAUSE(PauseExample.class, R.string.example_pause),
	PATHMODIFIER(PathModifierExample.class, R.string.example_pathmodifier),
	PARTICLESYSTEMNEXUS(ParticleSystemNexusExample.class, R.string.example_particlesystemnexus),
	PARTICLESYSTEMCOOL(ParticleSystemCoolExample.class, R.string.example_particlesystemcool),
	PARTICLESYSTEMSIMPLE(ParticleSystemSimpleExample.class, R.string.example_particlesystemsimple),
	PHYSICSCOLLISIONFILTERING(PhysicsCollisionFilteringExample.class, R.string.example_physicscollisionfiltering),
	PHYSICS(PhysicsExample.class, R.string.example_physics),
	PHYSICSFIXEDSTEP(PhysicsFixedStepExample.class, R.string.example_physicsfixedstep),
	PHYSICSMOUSEJOINT(PhysicsMouseJointExample.class, R.string.example_physicsmousejoint),
	PHYSICSJUMP(PhysicsJumpExample.class, R.string.example_physicsjump),
	PHYSICSREVOLUTEJOINT(PhysicsRevoluteJointExample.class, R.string.example_physicsrevolutejoint),
	PHYSICSREMOVE(PhysicsRemoveExample.class, R.string.example_physicsremove),
	PINCHZOOM(PinchZoomExample.class, R.string.example_pinchzoom),
	RECTANGLE(RectangleExample.class, R.string.example_rectangle),
	REPEATINGSPRITEBACKGROUND(RepeatingSpriteBackgroundExample.class, R.string.example_repeatingspritebackground),
	ROTATION3D(Rotation3DExample.class, R.string.example_rotation3d),
	ENTITYMODIFIER(EntityModifierExample.class, R.string.example_entitymodifier),
	ENTITYMODIFIERIRREGULAR(EntityModifierIrregularExample.class, R.string.example_entitymodifierirregular),
	SCREENCAPTURE(ScreenCaptureExample.class, R.string.example_screencapture),
	SOUND(SoundExample.class, R.string.example_sound),
	SPLITSCREEN(SplitScreenExample.class, R.string.example_splitscreen),
	SPRITE(SpriteExample.class, R.string.example_sprite),
	SPRITEREMOVE(SpriteRemoveExample.class, R.string.example_spriteremove),
	STROKEFONT(StrokeFontExample.class, R.string.example_strokefont),
	SUBMENU(SubMenuExample.class, R.string.example_submenu),
	TEXT(TextExample.class, R.string.example_text),
	TEXTMENU(TextMenuExample.class, R.string.example_textmenu),
	TEXTUREOPTIONS(TextureOptionsExample.class, R.string.example_textureoptions),
	TMXTILEDMAP(TMXTiledMapExample.class, R.string.example_tmxtiledmap),
	TICKERTEXT(TickerTextExample.class, R.string.example_tickertext),
	TOUCHDRAG(TouchDragExample.class, R.string.example_touchdrag),
	UNLOADRESOURCES(UnloadResourcesExample.class, R.string.example_unloadresources),
	UPDATETEXTURE(UpdateTextureExample.class, R.string.example_updatetexture),
	XMLLAYOUT(XMLLayoutExample.class, R.string.example_xmllayout),
	ZOOM(ZoomExample.class, R.string.example_zoom),

	BENCHMARK_ANIMATION(AnimationBenchmark.class, R.string.example_benchmark_animation),
	BENCHMARK_PARTICLESYSTEM(ParticleSystemBenchmark.class, R.string.example_benchmark_particlesystem),
	BENCHMARK_PHYSICS(PhysicsBenchmark.class, R.string.example_benchmark_physics),
	BENCHMARK_ENTITYMODIFIER(EntityModifierBenchmark.class, R.string.example_benchmark_entitymodifier),
	BENCHMARK_SPRITE(SpriteBenchmark.class, R.string.example_benchmark_sprite),
	BENCHMARK_TICKERTEXT(TickerTextBenchmark.class, R.string.example_benchmark_tickertext),

	APP_CITYRADAR(CityRadarActivity.class, R.string.example_app_cityradar),

	GAME_PONG(PongGameActivity.class, R.string.example_game_pong),
	GAME_SNAKE(SnakeGameActivity.class, R.string.example_game_snake),
	GAME_RACER(RacerGameActivity.class, R.string.example_game_racer);
	
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	public final Class<? extends BaseGameActivity> CLASS;
	public final int NAMERESID;

	// ===========================================================
	// Constructors
	// ===========================================================

	private Example(final Class<? extends BaseGameActivity> pExampleClass, final int pNameResID) {
		this.CLASS = pExampleClass;
		this.NAMERESID = pNameResID;
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

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}