package com.android.ground.ground.controller.person.main;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;

import java.util.ArrayList;

public class TabsAdapter extends FragmentPagerAdapter implements
		OnTabChangeListener, OnPageChangeListener {
	private final Context mContext;
	private final TabHost mTabHost;
	private final ViewPager mViewPager;
	private final FragmentManager mFragmentManager;
	private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();
	private final static String FIELD_KEY_PREFIX = "tabinfo";


	static final class TabInfo {
		private final String tag;
		private final Class<?> clss;
		private final Bundle args;
		private Fragment fragment;

		TabInfo(String _tag, Class<?> _class, Bundle _args) {
			tag = _tag;
			clss = _class;
			args = _args;
		}
	}

	static class DummyTabFactory implements TabHost.TabContentFactory {
		private final Context mContext;

		public DummyTabFactory(Context context) {
			mContext = context;
		}

		@Override
		public View createTabContent(String tag) {
			Log.d("TabsAdapter", "createTabContent");
			View v = new View(mContext);
			v.setMinimumWidth(0);
			v.setMinimumHeight(0);
			return v;
		}
	}

	public TabsAdapter(FragmentActivity activity, TabHost tabHost,
					   ViewPager pager) {
			this(activity, activity.getSupportFragmentManager(), tabHost, pager);
	}

	public TabsAdapter(Context context, FragmentManager fragmentManager, TabHost tabHost, ViewPager pager) {
		super(fragmentManager);
		Log.d("TabsAdapter", "TabsAdapter");
		mContext = context;
		mFragmentManager = fragmentManager;
		mTabHost = tabHost;
		mViewPager = pager;
		mTabHost.setOnTabChangedListener(this);
		mViewPager.setAdapter(this);
		mViewPager.addOnPageChangeListener(this);
	}

	public void onRestoreInstanceState(Bundle savedInstanceState) {
		for (TabInfo info : mTabs) {
			Log.d("TabsAdapter", "onRestoreInstanceState");
			String keyfield = FIELD_KEY_PREFIX + info.tag;
			info.fragment = mFragmentManager.getFragment(savedInstanceState, keyfield);
		}
	}

	public void onSaveInstanceState(Bundle outState) {
		for (TabInfo info : mTabs) {
			Log.d("TabsAdapter", "onSaveInstanceState");
			String keyfield = FIELD_KEY_PREFIX + info.tag;
			if (info.fragment != null) {
				mFragmentManager.putFragment(outState, keyfield, info.fragment);
			}
		}
	}

	public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
		Log.d("TabsAdapter", "addTab");
		tabSpec.setContent(new DummyTabFactory(mContext));
		String tag = tabSpec.getTag();

		TabInfo info = new TabInfo(tag, clss, args);
		mTabs.add(info);
		mTabHost.addTab(tabSpec);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mTabs.size();
	}

	@Override
	public Fragment getItem(int position) {
		Log.d("TabsAdapter", "getItem");
		TabInfo info = mTabs.get(position);
		info.fragment = Fragment.instantiate(mContext, info.clss.getName(),
				info.args);
		return info.fragment;
	}

	OnTabChangeListener mTabChangeListener;

	public void setOnTabChangedListener(OnTabChangeListener listener) {
		Log.d("TabsAdapter", "setOnTabChangedListener");
		mTabChangeListener = listener;
	}

	@Override
	public void onTabChanged(String tabId) {
		Log.d("TabsAdapter", "onTabChanged");
		int position = mTabHost.getCurrentTab();
		mViewPager.setCurrentItem(position);
		if (mTabChangeListener != null) {
			mTabChangeListener.onTabChanged(tabId);
		}
	}

	public Fragment getTabFragment(int position) {
		Log.d("TabsAdapter", "getTabFragment");
		TabInfo info = mTabs.get(position);
		if (info != null) {
			return info.fragment;
		}
		return null;
	}

	public Fragment getCurrentTabFragment() {
		Log.d("TabsAdapter", "getCurrentTabFragment");
		return getTabFragment(mTabHost.getCurrentTab());
	}

	OnPageChangeListener mPageChangeListener;

	public void setOnPageChangeListener(OnPageChangeListener listener) {
		Log.d("TabsAdapter", "setOnPageChangeListener");
		mPageChangeListener = listener;
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {

		if (mPageChangeListener != null) {
			mPageChangeListener.onPageScrolled(position, positionOffset,
					positionOffsetPixels);
		}
	}

	@Override
	public void onPageSelected(int position) {
		Log.d("TabsAdapter", "onPageSelected");
		TabWidget widget = mTabHost.getTabWidget();
		int oldFocusability = widget.getDescendantFocusability();
		widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
		mTabHost.setCurrentTab(position);
		widget.setDescendantFocusability(oldFocusability);
		if (mPageChangeListener != null) {
			mPageChangeListener.onPageSelected(position);
		}
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		Log.d("TabsAdapter", "onPageScrollStateChanged");
		if (mPageChangeListener != null) {
			mPageChangeListener.onPageScrollStateChanged(state);
		}
	}
	private FragmentTransaction mCurTransaction = null;
	private Fragment mCurrentPrimaryItem = null;
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		Log.d("TabsAdapter", "instantiateItem");
		if (mCurTransaction == null) {
			mCurTransaction = mFragmentManager.beginTransaction();
			Log.d("TabsAdapter", mCurTransaction.toString());
		}

		final long itemId = position;

		// Do we already have this fragment?
		String name = makeFragmentName(container.getId(), itemId);
		Fragment fragment = mFragmentManager.findFragmentByTag(name);
		fragment = null;
		if (fragment != null) {
			Log.d("TabsAdapter", "instantiateItem :  attach");
			mCurTransaction.attach(fragment);
		} else {
			fragment = getItem(position);
			Log.d("TabsAdapter", "instantiateItem : add");
			mCurTransaction.add(container.getId(), fragment,
					makeFragmentName(container.getId(), itemId));
		}
		if (fragment != mCurrentPrimaryItem) {
			fragment.setMenuVisibility(false);
			fragment.setUserVisibleHint(false);
		}

		return fragment;
	}
	private static String makeFragmentName(int viewId, long id) {
		return "android:switcher:" + viewId + ":" + id;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		if (mCurTransaction == null) {
			mCurTransaction = mFragmentManager.beginTransaction();
		}
		mCurTransaction.detach((Fragment)object);
	}

	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		Fragment fragment = (Fragment)object;
		if (fragment != mCurrentPrimaryItem) {
			if (mCurrentPrimaryItem != null) {
				mCurrentPrimaryItem.setMenuVisibility(false);
				mCurrentPrimaryItem.setUserVisibleHint(false);
			}
			if (fragment != null) {
				fragment.setMenuVisibility(true);
				fragment.setUserVisibleHint(true);
			}
			mCurrentPrimaryItem = fragment;
		}
	}

	@Override
	public void finishUpdate(ViewGroup container) {
		if (mCurTransaction != null) {
			mCurTransaction.commitAllowingStateLoss();
			mCurTransaction = null;
			mFragmentManager.executePendingTransactions();
		}
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return ((Fragment)object).getView() == view;
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void restoreState(Parcelable state, ClassLoader loader) {
	}

	/**
	 * Return a unique identifier for the item at the given position.
	 *
	 * <p>The default implementation returns the given position.
	 * Subclasses should override this method if the positions of items can change.</p>
	 *
	 * @param position Position within this adapter
	 * @return Unique identifier for the item at position
	 */
	public long getItemId(int position) {
		return position;
	}


}


