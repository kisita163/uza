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

import java.util.ArrayList;


/**
 * The Activity MainActivity will launched after the LoginActivity and it is the
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
        //setupLeftNavDrawer();
        //setDrawerOpenCloseEvent();
        //TODO Database to store pictures byte²
    }

	private void setPagerAdapter() {
		mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
			private final Fragment[] mFragments = new Fragment[] {
                    OnSaleFragment.newInstance("Men"),
                    OnSaleFragment.newInstance("Women"),
                    OnSaleFragment.newInstance("Kid"),
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

    private void setDrawerOpenCloseEvent() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.string.drawer_open,
                R.string.drawer_close) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void setupLeftNavDrawer() {
        drawerLeft = (ListView) findViewById(R.id.left_drawer);

        View header = getLayoutInflater().inflate(R.layout.left_nav_header,
                null);

        drawerLeft.addHeaderView(header);

        final ArrayList<Data> al = new ArrayList<Data>();
        al.add(new Data(new String[]{"Explore"}, new int[]{
                R.drawable.ic_nav1, R.drawable.ic_nav1_sel}));
        al.add(new Data(new String[]{"Favourites"}, new int[]{
                R.drawable.ic_nav2, R.drawable.ic_nav2_sel}));
        al.add(new Data(new String[]{"Cart"}, new int[]{
                R.drawable.ic_nav3, R.drawable.ic_nav3_sel}));
        al.add(new Data(new String[]{"Settings"}, new int[]{
                R.drawable.ic_nav4, R.drawable.ic_nav4_sel}));
        al.add(new Data(new String[]{"Logout"}, new int[]{
                R.drawable.ic_nav5, R.drawable.ic_nav5_sel}));

        final LeftNavAdapter adp = new LeftNavAdapter(this, al);
        drawerLeft.setAdapter(adp);
        drawerLeft.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "position : " + position);
                Intent intent;
                switch (position) {
                    case (1):
                        break;
                    case (2):
                        intent = new Intent(mContext, UzaActivity.class);
                        intent.putExtra("fragment",0);
                        mContext.startActivity(intent);
                        break;
                    case (3):
                        intent = new Intent(mContext, UzaActivity.class);
                        intent.putExtra("fragment",1);
                        mContext.startActivity(intent);
                        break;
                    case (4):
                        intent = new Intent(mContext, UzaActivity.class);
                        intent.putExtra("fragment",2);
                        mContext.startActivity(intent);
                        break;
                    case (5):
                        mAuth = FirebaseAuth.getInstance();
                        mAuth.signOut();
                        LoginManager.getInstance().logOut();
                        intent = new Intent(mContext, LoginActivity.class);
                        ((Activity) mContext).finish();
                        mContext.startActivity(intent);
                        break;
                }
                mDrawerLayout.closeDrawer(drawerLeft);
            }
        });
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
}