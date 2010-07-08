package org.anddev.andengine.examples.launcher;

import org.anddev.andengine.examples.R;


/**
 * @author Nicolas Gramlich
 * @since 11:13:34 - 27.06.2010
 */
public enum ExampleGroup {
	// ===========================================================
	// Elements
	// ===========================================================
	
	SIMPLE(R.string.examplegroup_simple),
	MODIFIER_AND_ANIMATION(R.string.examplegroup_modifier_and_animation),
	TOUCH(R.string.examplegroup_touch),
	PARTICLESYSTEMS(R.string.examplegroup_particlesystems),
	MULTIPLAYER(R.string.examplegroup_multiplayer),
	PHYSICS(R.string.examplegroup_physics),
	TEXT(R.string.examplegroup_text),
	AUDIO(R.string.examplegroup_audio),
	ADVANCED(R.string.examplegroup_advanced),
	OTHERS(R.string.examplegroup_others),
	GAMES(R.string.examplegroup_games),
	BENCHMARKS(R.string.examplegroup_benchmarks);

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	public final int NAMERESID;

	// ===========================================================
	// Constructors
	// ===========================================================

	private ExampleGroup(final int pNameResID) {
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
