package com.kisita.uza.activities;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;


import com.kisita.uza.R;
import com.kisita.uza.custom.CustomActivity;
import com.kisita.uza.services.Token;
import com.kisita.uza.ui.OnSaleFragment;
import com.kisita.uza.utils.MainPagerAdapter;

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

	private MainPagerAdapter mPagerAdapter;
    /**
     * View pager
     **/
    private ViewPager mViewPager;
	/*
	 */
	/* Title view */
	private ImageView explore;

	// Customer fragment
	private OnSaleFragment menFragment;
	private OnSaleFragment womenFragment;
	private OnSaleFragment kidsFragment;
	private OnSaleFragment electronicsFragment;
	private OnSaleFragment booksFragment;

	// Alarm manager

	public static final long ALARM_TRIGGER_AT_TIME = SystemClock.elapsedRealtime(); // Start now
	public static final long ALARM_INTERVAL = AlarmManager.INTERVAL_HALF_DAY ;
	private AlarmManager alarmManager;
	private PendingIntent pendingIntent;

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
		//getSupportActionBar().setIcon(R.drawable.ic_apps_black_24dp);
		getSupportActionBar().setTitle("");

		mViewPager = (ViewPager) findViewById(R.id.pager);
		explore     = (ImageView) findViewById(R.id.explore);

       explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

		setPagerAdapter();

        mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setCurrentItem(0);
        commandsCount();

		setAlarmManager();
    }

	private void setPagerAdapter() {
		ArrayList<Fragment> fragments = new ArrayList<>();
		ArrayList<String> fragmentNames = new ArrayList<>();

		//Customer fragments
		menFragment         = OnSaleFragment.newInstance(getString(R.string.men));
		womenFragment       = OnSaleFragment.newInstance(getString(R.string.women));
		kidsFragment        = OnSaleFragment.newInstance(getString(R.string.kids));
		electronicsFragment = OnSaleFragment.newInstance(getString(R.string.electronic));
		booksFragment        = OnSaleFragment.newInstance(getString(R.string.books));
		mPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(),fragments,fragmentNames);
		setFragments();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
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


	private void setFragments() {
		mPagerAdapter.clean();
		mPagerAdapter.add(menFragment,getString(R.string.men));
		mPagerAdapter.add(womenFragment,getString(R.string.women));
		mPagerAdapter.add(kidsFragment, getString(R.string.kids));
		mPagerAdapter.add(electronicsFragment,getString(R.string.electronic));
		mPagerAdapter.add(booksFragment,getString(R.string.books));
	}

	private void setAlarmManager(){
		alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
		Intent tokenIntent = new Intent(this, Token.class);
		pendingIntent = PendingIntent.getService(this, 0, tokenIntent, 0);
		alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
				                     ALARM_TRIGGER_AT_TIME,ALARM_INTERVAL,pendingIntent);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		alarmManager.cancel(pendingIntent);
	}
}