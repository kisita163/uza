package com.kisita.uza.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.kisita.uza.R;
import com.kisita.uza.custom.CustomActivity;
import com.kisita.uza.ui.OnSaleFragment;
import com.kisita.uza.ui.StoresFragment;


/**
 * The Activity MainActivity will launched after the LoginActivity and it is the
 * Home/Base activity of the app which holds all the Fragments and also show the
 * Sliding Navigation drawer. You can write your code for displaying actual
 * items on Drawer layout.
 */
@SuppressLint("InlinedApi")
public class MainActivity extends CustomActivity implements OnSaleFragment.OnFragmentInteractionListener, StoresFragment.OnFragmentInteractionListener
{
	/** The toolbar. */
	public Toolbar toolbar;

	private FragmentPagerAdapter mPagerAdapter;
    /**
     * View pager
     **/
    private ViewPager mViewPager;
	/*
	 */
	private StoresFragment storesFragment;
	private OnSaleFragment menFragment;
	private OnSaleFragment womenFragment;
	private OnSaleFragment kidsFragment;
	private OnSaleFragment electronicsFragment;

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

		getSupportActionBar().setDisplayShowHomeEnabled(true);
		//getSupportActionBar().setIcon(R.drawable.);
		getSupportActionBar().setTitle("");

        setPagerAdapter();

		mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setCurrentItem(1);
        commandsCount();
    }

	private void setPagerAdapter() {
		storesFragment      = StoresFragment.newInstance();
		menFragment         = OnSaleFragment.newInstance("Men");
		womenFragment       = OnSaleFragment.newInstance("Women");
		kidsFragment        = OnSaleFragment.newInstance("Kids");
		electronicsFragment = OnSaleFragment.newInstance("Electronic");

		mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
			private final Fragment[] mFragments = new Fragment[] {
					storesFragment,
                    menFragment,
                    womenFragment,
                    kidsFragment,
                    electronicsFragment
            };
			private final String[] mFragmentNames = new String[] {
					"Stores",
					"Men",
					"Women",
					"Kids",
					"Electronics"
			};

			@Override
			public int getItemPosition(Object object) {
				return POSITION_NONE;
			}

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
	public boolean onOptionsItemSelected(MenuItem item) {
       /* if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
		}*/
		return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        //mDrawerToggle.syncState();
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        //boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        commandsCount();
        super.onResume();
    }

    @Override
    public void onFragmentInteraction() {

    }

	@Override
	public void onFragmentInteraction(String store) {
		Log.i(TAG,"###### Selected store is : "+store);

		SharedPreferences sharedPref = getSharedPreferences(getResources().getString(R.string.uza_keys),Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(getString(R.string.uza_store),store);
		editor.apply();

		showProgressDialog();
		new Thread(new Runnable() {
			@Override
			public void run()
			{

				try
				{

					Thread.sleep(2000);

				} catch (Exception e)
				{
					e.printStackTrace();
				} finally
				{
					runOnUiThread(new Runnable() {
						@Override
						public void run()
						{
							hideProgressDialog();
							mViewPager.setCurrentItem(1);
							mPagerAdapter.notifyDataSetChanged();
						}
					});
				}
			}
		}).start();
	}
}