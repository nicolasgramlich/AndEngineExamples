package org.anddev.andengine.examples;

import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.view.Menu;
import android.view.MenuItem;

/**
 * @author Nicolas Gramlich
 * @since 22:10:28 - 11.04.2010
 */
public abstract class BaseExampleGameActivity extends BaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int MENU_TRACE = Menu.FIRST;

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

	@Override
	public boolean onCreateOptionsMenu(final Menu pMenu) {
		pMenu.add(Menu.NONE, MENU_TRACE, Menu.NONE, "Start Method Tracing");
		return super.onCreateOptionsMenu(pMenu);
	}

	@Override
	public boolean onPrepareOptionsMenu(final Menu pMenu) {
		pMenu.findItem(MENU_TRACE).setTitle(this.getEngine().isMethodTracing() ? "Stop Method Tracing" : "Start Method Tracing");
		return super.onPrepareOptionsMenu(pMenu);
	}

	@Override
	public boolean onMenuItemSelected(final int pFeatureId, final MenuItem pItem) {
		switch(pItem.getItemId()) {
			case MENU_TRACE:
				if(this.getEngine().isMethodTracing()) {
					this.getEngine().stopMethodTracing();
				} else {
					this.getEngine().startMethodTracing("AndEngine_" + System.currentTimeMillis() + ".trace");
				}
				return true;
			default:
				return super.onMenuItemSelected(pFeatureId, pItem);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
