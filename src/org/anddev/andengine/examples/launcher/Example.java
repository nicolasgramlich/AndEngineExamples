package org.anddev.andengine.examples.launcher;

import org.anddev.andengine.examples.AnimatedSpritesExample;
import org.anddev.andengine.examples.AugmentedRealityExample;
import org.anddev.andengine.examples.AugmentedRealityHorizonExample;
import org.anddev.andengine.examples.CustomFontExample;
import org.anddev.andengine.examples.ImageFormatsExample;
import org.anddev.andengine.examples.LineExample;
import org.anddev.andengine.examples.MenuExample;
import org.anddev.andengine.examples.MovingBallExample;
import org.anddev.andengine.examples.MultiplayerExample;
import org.anddev.andengine.examples.MusicExample;
import org.anddev.andengine.examples.ParticleSystemNexusExample;
import org.anddev.andengine.examples.ParticleSystemCoolExample;
import org.anddev.andengine.examples.ParticleSystemSimpleExample;
import org.anddev.andengine.examples.PathModifierExample;
import org.anddev.andengine.examples.PauseExample;
import org.anddev.andengine.examples.PhysicsExample;
import org.anddev.andengine.examples.PhysicsJumpExample;
import org.anddev.andengine.examples.PhysicsRemoveExample;
import org.anddev.andengine.examples.R;
import org.anddev.andengine.examples.RectangleExample;
import org.anddev.andengine.examples.ShapeModifierExample;
import org.anddev.andengine.examples.SoundExample;
import org.anddev.andengine.examples.SplitScreenExample;
import org.anddev.andengine.examples.SpriteExample;
import org.anddev.andengine.examples.SpriteRemoveExample;
import org.anddev.andengine.examples.SubMenuExample;
import org.anddev.andengine.examples.TextExample;
import org.anddev.andengine.examples.TextureOptionsExample;
import org.anddev.andengine.examples.TickerTextExample;
import org.anddev.andengine.examples.TouchDragExample;
import org.anddev.andengine.examples.TouchDragManyExample;
import org.anddev.andengine.examples.UnloadTextureExample;
import org.anddev.andengine.examples.ZoomExample;
import org.anddev.andengine.examples.benchmark.AnimationBenchmark;
import org.anddev.andengine.examples.benchmark.ParticleSystemBenchmark;
import org.anddev.andengine.examples.benchmark.ShapeModifierBenchmark;
import org.anddev.andengine.examples.benchmark.SpriteBenchmark;
import org.anddev.andengine.examples.benchmark.TickerTextBenchmark;
import org.anddev.andengine.ui.activity.BaseGameActivity;

/**
 * @author Nicolas Gramlich
 * @since 20:42:27 - 16.06.2010
 */
enum Example {
	// ===========================================================
	// Elements
	// ===========================================================

	ANIMATEDSPRITES(AnimatedSpritesExample.class, R.string.example_animatedsprites),
	AUGMENTEDREALITY(AugmentedRealityExample.class, R.string.example_augmentedreality),
	AUGMENTEDREALITYHORIZON(AugmentedRealityHorizonExample.class, R.string.example_augmentedrealityhorizon),
	CUSTOMFONT(CustomFontExample.class, R.string.example_customfont),
	IMAGEFORMATS(ImageFormatsExample.class, R.string.example_imageformats),
	LINE(LineExample.class, R.string.example_line),
	MENU(MenuExample.class, R.string.example_menu),
	MOVINGBALL(MovingBallExample.class, R.string.example_movingball),
	MULTIPLAYER(MultiplayerExample.class, R.string.example_multiplayer),
	MUSIC(MusicExample.class, R.string.example_music),
	PAUSE(PauseExample.class, R.string.example_pause),
	PATHMODIFIER(PathModifierExample.class, R.string.example_pathmodifier),
	PARTICLESYSTEMNEXUS(ParticleSystemNexusExample.class, R.string.example_particlesystemnexus),
	PARTICLESYSTEMCOOL(ParticleSystemCoolExample.class, R.string.example_particlesystemcool),
	PARTICLESYSTEMSIMPLE(ParticleSystemSimpleExample.class, R.string.example_particlesystemsimple),
	PHYSICS(PhysicsExample.class, R.string.example_physics),
	PHYSICSJUMP(PhysicsJumpExample.class, R.string.example_physicsjump),
	PHYSICSREMOVE(PhysicsRemoveExample.class, R.string.example_physicsremove),
	RECTANGLE(RectangleExample.class, R.string.example_rectangle),
	SHAPEMODIFIER(ShapeModifierExample.class, R.string.example_shapemodifier),
	SOUND(SoundExample.class, R.string.example_sound),
	SPLITSCREEN(SplitScreenExample.class, R.string.example_splitscreen),
	SPRITE(SpriteExample.class, R.string.example_sprite),
	SPRITEREMOVE(SpriteRemoveExample.class, R.string.example_spriteremove),
	SUBMENU(SubMenuExample.class, R.string.example_submenu),
	TEXT(TextExample.class, R.string.example_text),
	TEXTUREOPTIONS(TextureOptionsExample.class, R.string.example_textureoptions),
	TICKERTEXT(TickerTextExample.class, R.string.example_tickertext),
	TOUCHDRAG(TouchDragExample.class, R.string.example_touchdrag),
	TOUCHDRAGMANY(TouchDragManyExample.class, R.string.example_touchdragmany),
	UNLOADTEXTURE(UnloadTextureExample.class, R.string.example_unloadtexture),
	ZOOM(ZoomExample.class, R.string.example_zoom),

	BENCHMARK_ANIMATION(AnimationBenchmark.class, R.string.example_benchmark_animation),
	BENCHMARK_PARTICLESYSTEM(ParticleSystemBenchmark.class, R.string.example_benchmark_particlesystem),
	BENCHMARK_SHAPEMODIFIER(ShapeModifierBenchmark.class, R.string.example_benchmark_shapemodifier),
	BENCHMARK_SPRITE(SpriteBenchmark.class, R.string.example_benchmark_sprite),
	BENCHMARK_TICKERTEXT(TickerTextBenchmark.class, R.string.example_benchmark_tickertext);
	
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