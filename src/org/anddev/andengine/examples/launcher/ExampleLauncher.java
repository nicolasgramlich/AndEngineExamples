package org.anddev.andengine.examples.launcher;


import android.app.ExpandableListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

public class ExampleLauncher extends ExpandableListActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	
	private ExpandableExampleLauncherListAdapter mExpandableExampleLauncherListAdapter;

	// ===========================================================
	// Constructors
	// ===========================================================

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.mExpandableExampleLauncherListAdapter = new ExpandableExampleLauncherListAdapter(this);
		
		this.setListAdapter(this.mExpandableExampleLauncherListAdapter);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public boolean onChildClick(final ExpandableListView pParent, final View pV, final int pGroupPosition, final int pChildPosition, final long pId) {
		final Example example = this.mExpandableExampleLauncherListAdapter.getChild(pGroupPosition, pChildPosition);
		
		this.startActivity(new Intent(this, example.CLASS));
		
		return super.onChildClick(pParent, pV, pGroupPosition, pChildPosition, pId);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}