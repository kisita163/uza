package com.kisita.uza.activities;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.kisita.uza.R;
import com.kisita.uza.custom.CustomActivity;
import com.kisita.uza.services.Token;
import com.kisita.uza.ui.NewArticleFragment;
import com.kisita.uza.ui.OnSaleFragment;
import com.kisita.uza.ui.StoresFragment;
import com.kisita.uza.utils.MainPagerAdapter;

import java.util.ArrayList;


/**
 * The Activity MainActivity will launched after the LoginActivity and it is the
 * Home/Base activity of the app which holds all the Fragments and also show the
 * Sliding Navigation drawer. You can write your code for displaying actual
 * items on Drawer layout.
 */
@SuppressLint("InlinedApi")
public class MainActivity extends CustomActivity implements  StoresFragment.OnFragmentInteractionListener
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

		SharedPreferences sharedPref = getSharedPreferences(getResources().getString(R.string.uza_keys),
				Context.MODE_PRIVATE);
		String selectedFragments = sharedPref.getString(getString(R.string.uza_main_fragments),null);
		String title = sharedPref.getString(getString(R.string.uza_store),"UZA");

		//mTitle.setText(title);

		Log.i(TAG,"Selected fragments are :"+selectedFragments);
		//Customer fragments
		menFragment         = OnSaleFragment.newInstance(getString(R.string.men));
		womenFragment       = OnSaleFragment.newInstance(getString(R.string.women));
		kidsFragment        = OnSaleFragment.newInstance(getString(R.string.kids));
		electronicsFragment = OnSaleFragment.newInstance(getString(R.string.electronic));
		//homeFragment        = OnSaleFragment.newInstance("Home");
		booksFragment        = OnSaleFragment.newInstance(getString(R.string.books));

		//Merchant fragments
		//commandsFragment    = OnSaleFragment.newInstance("Commands");
		//newArticleFragment  = NewArticleFragment.newInstance();



		mPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(),fragments,fragmentNames);
		setPagerAdapter(selectedFragments);
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

	@Override
	public void onStoreSelectedListener(String store,String selectedFragments) {
		//Log.i(TAG,"###### Selected store is : "+store+ " and selected fragments are  : "+selectedFragments);
		//mTitle.setText(store);
		setPagerAdapter(selectedFragments);

		SharedPreferences sharedPref = getSharedPreferences(getResources().getString(R.string.uza_keys),Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(getString(R.string.uza_store),store);
		editor.putString(getString(R.string.uza_main_fragments),selectedFragments);
		editor.apply();

		showProgressDialog();
		mPagerAdapter.notifyDataSetChanged();
		mViewPager.setCurrentItem(1);
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
						}
					});
				}
			}
		}).start();
	}

	private void setPagerAdapter(String selectedFragments) {
		mPagerAdapter.clean();
		//mPagerAdapter.add(storesFragment, customerFragmentNames[STORE]);
		//if(selectedFragments == null){
			mPagerAdapter.add(menFragment,getString(R.string.men));
			mPagerAdapter.add(womenFragment,getString(R.string.women));
			mPagerAdapter.add(kidsFragment, getString(R.string.kids));
			mPagerAdapter.add(electronicsFragment,getString(R.string.electronic));
			//mPagerAdapter.add(homeFragment, customerFragmentNames[HOME]);
			mPagerAdapter.add(booksFragment,getString(R.string.books));
		/*}else {

			if (selectedFragments.contains("Men=1"))
				mPagerAdapter.add(menFragment, customerFragmentNames[MEN]);

			if (selectedFragments.contains("Women=1"))
				mPagerAdapter.add(womenFragment, customerFragmentNames[WOMEN]);

			if (selectedFragments.contains("Kids=1"))
				mPagerAdapter.add(kidsFragment, customerFragmentNames[KIDS]);

			if (selectedFragments.contains("Electronics=1"))
				mPagerAdapter.add(electronicsFragment, customerFragmentNames[ELECTRONICS]);

			if (selectedFragments.contains("Home=1"))
				mPagerAdapter.add(homeFragment, customerFragmentNames[HOME]);

			if (selectedFragments.contains("Food=1"))
				mPagerAdapter.add(booksFragment, customerFragmentNames[BOOKS]);
		}*/
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