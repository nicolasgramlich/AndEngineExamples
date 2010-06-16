package org.anddev.andengine.examples.launcher;

import org.anddev.andengine.examples.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

	private static final int DIALOG_FIRST_APP_LAUNCH = 0;

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
		
		if(this.isFirstTime("first.app.launch")) {
			this.showDialog(DIALOG_FIRST_APP_LAUNCH);
		}
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	@Override
	protected Dialog onCreateDialog(int pId) {
		switch(pId) {
			case DIALOG_FIRST_APP_LAUNCH:
				return new AlertDialog.Builder(this)
					.setTitle(R.string.dialog_first_app_launch_title)
					.setMessage(R.string.dialog_first_app_launch_message)
					.setIcon(android.R.drawable.ic_dialog_info)
					.setPositiveButton(android.R.string.ok, null)
					.create();
			default:
				return super.onCreateDialog(pId);
		}
	}

	@Override
	public boolean onChildClick(final ExpandableListView pParent, final View pV, final int pGroupPosition, final int pChildPosition, final long pId) {
		final Example example = this.mExpandableExampleLauncherListAdapter.getChild(pGroupPosition, pChildPosition);
		
		this.startActivity(new Intent(this, example.CLASS));
		
		return super.onChildClick(pParent, pV, pGroupPosition, pChildPosition, pId);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public boolean isFirstTime(final String pKey){
		final SharedPreferences prefs = this.getPreferences(Context.MODE_PRIVATE);
		if(prefs.getBoolean(pKey, true)){
			prefs.edit().putBoolean(pKey, false).commit();
			return true;
		}
		return false;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}