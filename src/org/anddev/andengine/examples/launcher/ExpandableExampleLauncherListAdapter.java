package org.anddev.andengine.examples.launcher;

import org.anddev.andengine.examples.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

/**
 * @author Nicolas Gramlich
 * @since 20:43:54 - 16.06.2010
 */
class ExpandableExampleLauncherListAdapter extends BaseExpandableListAdapter {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final ExampleGroup[] EXAMPLEGROUPS = {
		ExampleGroup.SIMPLE,
		ExampleGroup.MODIFIER_AND_ANIMATION,
		ExampleGroup.TOUCH,
		ExampleGroup.PARTICLESYSTEMS,
		ExampleGroup.MULTIPLAYER,
		ExampleGroup.PHYSICS,
		ExampleGroup.TEXT,
		ExampleGroup.AUDIO,
		ExampleGroup.ADVANCED,
		ExampleGroup.OTHERS,
		ExampleGroup.BENCHMARKS
	};

	private static final Example[][] EXAMPLES = {
		{ Example.LINE, Example.RECTANGLE, Example.SPRITE, Example.SPRITEREMOVE },
		{ Example.MOVINGBALL, Example.SHAPEMODIFIER, Example.SHAPEMODIFIERIRREGULAR, Example.PATHMODIFIER, Example.ANIMATEDSPRITES },
		{ Example.TOUCHDRAG, Example.TOUCHDRAGMANY },
		{ Example.PARTICLESYSTEMSIMPLE, Example.PARTICLESYSTEMCOOL, Example.PARTICLESYSTEMNEXUS },
		{ Example.MULTIPLAYER },
		{ Example.PHYSICS, Example.PHYSICSJUMP, Example.PHYSICSREMOVE },
		{ Example.TEXT, Example.TICKERTEXT, Example.CUSTOMFONT },
		{ Example.SOUND, Example.MUSIC },
		{ Example.SPLITSCREEN, Example.AUGMENTEDREALITY, Example.AUGMENTEDREALITYHORIZON },
		{ Example.PAUSE, Example.MENU, Example.SUBMENU, Example.ZOOM , Example.IMAGEFORMATS, Example.TEXTUREOPTIONS, Example.LOADTEXTURE, Example.UPDATETEXTURE, Example.UNLOADTEXTURE},
		{ Example.BENCHMARK_SPRITE, Example.BENCHMARK_SHAPEMODIFIER, Example.BENCHMARK_ANIMATION, Example.BENCHMARK_TICKERTEXT, Example.BENCHMARK_PARTICLESYSTEM, Example.BENCHMARK_PHYSICS }
	};

	// ===========================================================
	// Fields
	// ===========================================================

	private final Context mContext;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ExpandableExampleLauncherListAdapter(final Context pContext) {
		this.mContext = pContext;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public Example getChild(final int pGroupPosition, final int pChildPosition) {
		return EXAMPLES[pGroupPosition][pChildPosition];
	}

	@Override
	public long getChildId(final int pGroupPosition, final int pChildPosition) {
		return pChildPosition;
	}

	@Override
	public int getChildrenCount(final int pGroupPosition) {
		return EXAMPLES[pGroupPosition].length;
	}

	@Override
	public View getChildView(final int pGroupPosition, final int pChildPosition, final boolean pIsLastChild, final View pConvertView, final ViewGroup pParent) {
		final View childView;
		if (pConvertView != null){
			childView = pConvertView;
		}else{
			childView = LayoutInflater.from(this.mContext).inflate(R.layout.listrow_example, null);
		}

		((TextView)childView.findViewById(R.id.tv_listrow_example_name)).setText(this.getChild(pGroupPosition, pChildPosition).NAMERESID);
		return childView;
	}

	@Override
	public View getGroupView(final int pGroupPosition, final boolean pIsExpanded, final View pConvertView, final ViewGroup pParent) {
		final View groupView;
		if (pConvertView != null){
			groupView = pConvertView;
		}else{
			groupView = LayoutInflater.from(this.mContext).inflate(R.layout.listrow_examplegroup, null);
		}

		((TextView)groupView.findViewById(R.id.tv_listrow_examplegroup_name)).setText(this.getGroup(pGroupPosition).NAMERESID);
		return groupView;
	}

	@Override
	public ExampleGroup getGroup(final int pGroupPosition) {
		return EXAMPLEGROUPS[pGroupPosition];
	}

	@Override
	public int getGroupCount() {
		return EXAMPLEGROUPS.length;
	}

	@Override
	public long getGroupId(final int pGroupPosition) {
		return pGroupPosition;
	}

	@Override
	public boolean isChildSelectable(final int pGroupPosition, final int pChildPosition) {
		return true;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}