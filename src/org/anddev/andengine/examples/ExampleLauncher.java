package org.anddev.andengine.examples;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ExampleLauncher extends ListActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	final Example[] EXAMPLES = Example.values();

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	/** Called when the activity is first created. */
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Create an ArrayAdapter, that will actually make the Strings above appear in the ListView
		this.setListAdapter(new ArrayAdapter<Example>(this, android.R.layout.simple_list_item_1, this.EXAMPLES));
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onListItemClick(final ListView pListView, final View pView, final int pPosition, final long pId) {
		super.onListItemClick(pListView, pView, pPosition, pId);
		this.startActivity(new Intent(this, this.EXAMPLES[pPosition].CLASS));
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	private static enum Example {
		LINE(LineExample.class, "Line Example"),
		RECTANGLE(RectangleExample.class, "Rectangle Example"),
		SPRITE(SpriteExample.class, "Sprite Example"),
		SHAPEMODIFIER(ShapeModifierExample.class, "ShapeModifier Example"),
		SPRITES(SpritesExample.class, "Sprites Example"),
		ANIMATEDSPRITES(AnimatedSpritesExample.class, "Animated Sprites Example"),
		TEXTUREOPTIONS(TextureOptionsExample.class, "TextureOptions Example"),
		PAUSE(PauseExample.class, "Pause Example"),
		MENU(MenuExample.class, "Menu Example"),
		SUBMENU(SubMenuExample.class, "SubMenu Example"),
		TEXT(TextExample.class, "Text Example"),
		TICKERTEXT(TickerTextExample.class, "TickerText Example"),
		PARTICLESYSTEM(ParticleSystemExample.class, "ParticleSystem Example"),
		PHYSICS(PhysicsExample.class, "Physics Example"),
		SPLITSCREEN(SplitScreenExample.class, "SplitScreen Example"),
		AUGMENTEDREALITY(AugmentedRealityExample.class, "AugmentedReality Example"),
		AUGMENTEDREALITYHORIZON(AugmentedRealityHorizonExample.class, "AugmentedReality Horizon Example"),
		UNLOADTEXTURE(UnloadTextureExample.class, "Unload Texture Example"),
		TOUCHDRAG(TouchDragExample.class, "TouchDrag Example"),
		SOUND(SoundExample.class, "Sound Example"),
		MUSIC(MusicExample.class, "Music Example");

		public final Class<? extends Activity> CLASS;
		public final String NAME;

		private Example(final Class<? extends Activity> pExampleClass, final String pExampleName) {
			this.CLASS = pExampleClass;
			this.NAME = pExampleName;
		}

		@Override
		public String toString() {
			return this.NAME;
		}
	}
}