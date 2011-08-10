package org.anddev.andengine.examples.launcher;

import org.anddev.andengine.examples.R;


/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 11:13:34 - 27.06.2010
 */
public enum ExampleGroup {
	// ===========================================================
	// Elements
	// ===========================================================

	SIMPLE(R.string.examplegroup_simple, 
			Example.LINE, Example.RECTANGLE, Example.SPRITE, Example.SPRITEREMOVE),
	MODIFIER_AND_ANIMATION(R.string.examplegroup_modifier_and_animation, 
			Example.MOVINGBALL, Example.ENTITYMODIFIER, Example.ENTITYMODIFIERIRREGULAR, Example.ANIMATEDSPRITES, Example.ROTATION3D ),
	TOUCH(R.string.examplegroup_touch, 
			Example.TOUCHDRAG, Example.MULTITOUCH, Example.PINCHZOOM),
//	PARTICLESYSTEM(R.string.examplegroup_particlesystems),
	MULTIPLAYER(R.string.examplegroup_multiplayer,
			Example.MULTIPLAYER, Example.MULTIPLAYERSERVERDISCOVERY, Example.MULTIPLAYERBLUETOOTH),
	PHYSICS(R.string.examplegroup_physics,
			Example.PHYSICS, Example.PHYSICSFIXEDSTEP, Example.PHYSICSCOLLISIONFILTERING, Example.PHYSICSJUMP, Example.PHYSICSREVOLUTEJOINT, Example.PHYSICSMOUSEJOINT, Example.PHYSICSREMOVE),
	TEXT(R.string.examplegroup_text, Example.TEXT),
	AUDIO(R.string.examplegroup_audio, 
			Example.SOUND, Example.MUSIC, Example.MODPLAYER),
	ADVANCED(R.string.examplegroup_advanced, 
			Example.SPLITSCREEN ), // Example.AUGMENTEDREALITY, Example.AUGMENTEDREALITYHORIZON),
	BACKGROUND(R.string.examplegroup_background, 
			Example.AUTOPARALLAXBACKGROUND),
	OTHER(R.string.examplegroup_other, 
			Example.SCREENCAPTURE, Example.PAUSE, Example.ZOOM , Example.IMAGEFORMATS, Example.PVRTEXTURE, Example.PVRCCZTEXTURE, Example.PVRGZTEXTURE, Example.ETC1TEXTURE, Example.COLORKEYTEXTURESOURCEDECORATOR, Example.LOADTEXTURE, Example.UPDATETEXTURE, Example.RUNNABLEPOOLUPDATEHANDLER, Example.SVGTEXTUREREGION, Example.LEVELLOADER),
//	APP(R.string.examplegroup_app),
//	GAME(R.string.examplegroup_game),
	BENCHMARK(R.string.examplegroup_benchmark, 
			Example.BENCHMARK_SPRITE, Example.BENCHMARK_ENTITYMODIFIER, Example.BENCHMARK_ANIMATION, Example.BENCHMARK_PHYSICS);

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	public final Example[] mExamples;
	public final int mNameResourceID;

	// ===========================================================
	// Constructors
	// ===========================================================

	private ExampleGroup(final int pNameResourceID, final Example ... pExamples) {
		this.mNameResourceID = pNameResourceID;
		this.mExamples = pExamples;
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
