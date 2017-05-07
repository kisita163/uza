package com.kisita.uza;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import com.kisita.uza.custom.CustomActivity;
import com.kisita.uza.ui.OnSaleFragment;


/**
 * The Activity MainActivity will launched after the Login and it is the
 * Home/Base activity of the app which holds all the Fragments and also show the
 * Sliding Navigation drawer. You can write your code for displaying actual
 * items on Drawer layout.
 */
@SuppressLint("InlinedApi")
public class MainActivity extends CustomActivity
{
	/** The toolbar. */
	public Toolbar toolbar;

	private FragmentPagerAdapter mPagerAdapter;

	private ViewPager mViewPager;

	/* (non-Javadoc)
	 * @see com.newsfeeder.custom.CustomActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		toolbar = (Toolbar) findViewById(R.id.main_toolbar);
		setSupportActionBar(toolbar);

		setPagerAdapter();

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mPagerAdapter);
	}

	private void setPagerAdapter() {
		mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
			private final Fragment[] mFragments = new Fragment[] {
					new OnSaleFragment(),
					new OnSaleFragment(),
					new OnSaleFragment(),
			};
			private final String[] mFragmentNames = new String[] {
					"Men",
					"Women",
					"Kids",
					"Electronics"
			};
			@Override
			public Fragment getItem(int position) {
				return mFragments[position];
			}
			@Override
			public int getCount() {
				return mFragments.length;
			}
			@Override
			public CharSequence getPageTitle(int position) {
				return mFragmentNames[position];
			}
		};
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		Fragment f = getSupportFragmentManager().findFragmentByTag("CHECKOUT_FRAGMENT");
		Log.i(TAG, "Back pressed and id = " + item.getItemId() + "-" + R.id.home);
		if(item.getItemId() == android.R.id.home){
			onBackPressed();
		}

		if (f != null && f.isVisible()) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}