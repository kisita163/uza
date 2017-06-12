package com.kisita.uza.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.kisita.uza.R;
import com.kisita.uza.custom.CustomActivity;
import com.kisita.uza.model.Data;
import com.kisita.uza.ui.LeftNavAdapter;
import com.kisita.uza.ui.OnSaleFragment;
import com.kisita.uza.ui.StoresFragment;

import java.util.ArrayList;


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
    /**
     * Drawer ListView
     **/
    private ListView drawerLeft;
    /**
     * Drawer layout
     **/
    private DrawerLayout mDrawerLayout;

    private ActionBarDrawerToggle mDrawerToggle;

    private Context mContext;
    private FirebaseAuth mAuth;

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

        mContext = this;

		mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mPagerAdapter);
        commandsCount();
    }

	private void setPagerAdapter() {
		mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
			private final Fragment[] mFragments = new Fragment[] {
                    OnSaleFragment.newInstance("Men"),
                    OnSaleFragment.newInstance("Women"),
                    OnSaleFragment.newInstance("Kids"),
                    OnSaleFragment.newInstance("Electronic"),
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
}