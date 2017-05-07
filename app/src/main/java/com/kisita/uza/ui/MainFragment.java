package com.kisita.uza.ui;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.kisita.uza.MainActivity;
import com.kisita.uza.R;
import com.kisita.uza.custom.CustomFragment;


/**
 * The Class MainFragment is the base fragment that shows the list of various
 * products. You can add your code to do whatever you want related to products
 * for your app.
 */
public class MainFragment extends CustomFragment
{
	private static final String TAG = "### MainFragment";
	/** The product list. */
	private FragmentPagerAdapter mPagerAdapter;

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.main_container, null);
		if (getActivity() instanceof MainActivity)
		{
			((MainActivity) getActivity()).toolbar.setTitle(getResources().getString(R.string.all_sales));
			((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			//((MainActivity) getActivity()).getToggle().setDrawerIndicatorEnabled(true);

		}

		setHasOptionsMenu(true);
		setupView(v);

		return v;
	}

	/* (non-Javadoc)
	 * @see com.whatshere.custom.CustomFragment#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v)
	{
		Log.i(TAG,"fucking click");
		super.onClick(v);
	}

	/**
	 * Setup the view components for this fragment. You write your code for
	 * initializing the views, setting the adapters, touch and click listeners
	 * etc.
	 * 
	 * @param v
	 *            the base view of fragment
	 */
	private void setupView(View v)
	{
		initPager(v);
	}

	/**
	 * Inits the pager view.
	 * 
	 * @param v
	 *            the root view
	 */
	private void initPager(View v)
	{
		mPagerAdapter = new PageAdapter(getFragmentManager());
		ViewPager pager = (ViewPager) v.findViewById(R.id.pager);
		pager.setPageMargin(10);
		pager.setAdapter(mPagerAdapter);
	}

	/**
	 * The Class PageAdapter is adapter class for ViewPager and it simply holds
	 * a RecyclerView with dummy images. You need to write logic for loading
	 * actual images.
	 */
	private class PageAdapter extends FragmentPagerAdapter
	{


		public PageAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return new OnSaleFragment();
		}

		@Override
		public int getCount() {
			return 5;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			return super.instantiateItem(container, position);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			if (position == 0)
				return "MEN";
			if (position == 1)
				return "WOMEN";
			if (position == 2)
				return "KIDS";
			if (position == 3)
				return "TRADITIONAL";
			if (position == 4)
				return "SPECIAL";
			return "Page-" + position;
		}
	}
}
