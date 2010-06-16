package org.anddev.andengine.examples.launcher;

import org.anddev.andengine.examples.R;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;

/**
 * @author Nicolas Gramlich
 * @since 22:56:46 - 16.06.2010
 */
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
		
		this.setContentView(R.layout.list_examples);

		this.mExpandableExampleLauncherListAdapter = new ExpandableExampleLauncherListAdapter(this);
		
		this.setListAdapter(this.mExpandableExampleLauncherListAdapter);
		
		this.findViewById(R.id.btn_get_involved).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View pView) {
				ExampleLauncher.this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.andengine.org")));
			}
		});
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