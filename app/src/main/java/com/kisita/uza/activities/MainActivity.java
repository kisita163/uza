package com.kisita.uza.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


import com.kisita.uza.R;
import com.kisita.uza.custom.CustomActivity;
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
public class MainActivity extends CustomActivity implements OnSaleFragment.OnFragmentInteractionListener, StoresFragment.OnFragmentInteractionListener, NewArticleFragment.OnNewArticleInteractionListener
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
	private TextView mTitle;

	// Customer fragment
	private StoresFragment storesFragment;
	private OnSaleFragment menFragment;
	private OnSaleFragment womenFragment;
	private OnSaleFragment kidsFragment;
	private OnSaleFragment electronicsFragment;
	private OnSaleFragment booksFragment;
	private OnSaleFragment homeFragment;
	// Merchant fragment
	private OnSaleFragment commandsFragment;
	private NewArticleFragment newArticleFragment;

	public final static int STORE = 0;
	public final static int MEN = 1;
	public final static int WOMEN = 2;
	public final static int KIDS = 3;
	public final static int ELECTRONICS = 4;
	public final static int HOME = 5;
	public final static int FOOD = 6;
	public final static int BOOKS = 7;

	public final String[] customerFragmentNames = new String[] {
			//"Stores",
			"Men",
			"Women",
			"Kids",
			"Electronics",
			//"Home",
			"Books"
	};

	/*public final String[] merchantFragmentNames = new String[] {
			"Commands",
			"New Article"
	};*/


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

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mTitle     = (TextView) findViewById(R.id.title_view);

		setPagerAdapter();

        mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setCurrentItem(0);
        commandsCount();
    }

	private void setPagerAdapter() {
		ArrayList<Fragment> fragments = new ArrayList<>();
		ArrayList<String> fragmentNames = new ArrayList<>();

		SharedPreferences sharedPref = getSharedPreferences(getResources().getString(R.string.uza_keys),
				Context.MODE_PRIVATE);
		String selectedFragments = sharedPref.getString(getString(R.string.uza_main_fragments),null);
		String title = sharedPref.getString(getString(R.string.uza_store),"UZA");

		mTitle.setText(title);

		Log.i(TAG,"Selected fragments are :"+selectedFragments);
		//Customer fragments
		storesFragment      = StoresFragment.newInstance();
		menFragment         = OnSaleFragment.newInstance("Men");
		womenFragment       = OnSaleFragment.newInstance("Women");
		kidsFragment        = OnSaleFragment.newInstance("Kids");
		electronicsFragment = OnSaleFragment.newInstance("Electronic");
		//homeFragment        = OnSaleFragment.newInstance("Home");
		booksFragment        = OnSaleFragment.newInstance("Food");

		//Merchant fragments
		//commandsFragment    = OnSaleFragment.newInstance("Commands");
		//newArticleFragment  = NewArticleFragment.newInstance();



		mPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(),fragments,fragmentNames);
		setPagerAdapter(selectedFragments);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.action_store){
			Log.i(TAG,"Store pressed ... ");
			mPagerAdapter.clean();
			//mPagerAdapter.add(commandsFragment, merchantFragmentNames[0]);
			//mPagerAdapter.add(newArticleFragment, merchantFragmentNames[1]);
			mPagerAdapter.notifyDataSetChanged();
		}
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
	public void onStoreSelectedListener(String store,String selectedFragments) {
		//Log.i(TAG,"###### Selected store is : "+store+ " and selected fragments are  : "+selectedFragments);
		mTitle.setText(store);
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
			mPagerAdapter.add(menFragment, customerFragmentNames[0]);
			mPagerAdapter.add(womenFragment, customerFragmentNames[1]);
			mPagerAdapter.add(kidsFragment, customerFragmentNames[2]);
			mPagerAdapter.add(electronicsFragment, customerFragmentNames[3]);
			//mPagerAdapter.add(homeFragment, customerFragmentNames[HOME]);
			mPagerAdapter.add(booksFragment, customerFragmentNames[4]);
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

	@Override
	public void onNewArticleInteraction(Uri uri) {

	}
}