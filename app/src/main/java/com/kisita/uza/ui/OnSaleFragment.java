package com.kisita.uza.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.kisita.uza.R;
import com.kisita.uza.custom.CustomFragment;
import com.kisita.uza.model.Data;
import com.kisita.uza.provider.UzaContract;
import com.kisita.uza.utils.UzaCardAdapter;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static com.kisita.uza.model.Data.ITEMS_COLUMNS;
import static com.kisita.uza.model.Data.ITEM_DATA;
import static com.kisita.uza.ui.SettingsFragment.MAX_PRICE_VALUE;
import static com.kisita.uza.utils.UzaFunctions.getPriceDouble;
import static com.kisita.uza.utils.UzaFunctions.setPrice;


/*
 * The Class OnSaleFragment is the fragment that shows the products in GridView.
 */
public class OnSaleFragment extends CustomFragment implements  LoaderManager.LoaderCallbacks<Cursor>
{
	final static String TAG = "### OnSaleFragment";
	final static String QUERY = "QUERY";

	private UzaCardAdapter mCardAdapter;

	private ArrayList<Data> itemsList;

	private String mQuery;

	private boolean mListFilled = false;

	//we are going to use a handler to be able to run in our TimerTask
	final Handler handler = new Handler();

	Timer scheduledLoad;

	// this will run when timer elapses
	TimerTask mTimerTask = new TimerTask() {

		@Override
		public void run() {
			handler.post(new Runnable() {
				@Override
				public void run() {
					if(itemsList.size() == 0 && !mListFilled) { // items list is empty. Try to load again
						Log.i(TAG,"Items list is empty. Try to load again");
						loadData();
					}else{
                        stopScheduledTask();
                    }
				}
			});
		}

	};

    private void stopScheduledTask() {
        // Stopping timer task
        if(scheduledLoad != null) {
            scheduledLoad.cancel();
            scheduledLoad = null;
        }
    }

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */

	public OnSaleFragment() {
		// Required empty public constructor
	}

	public static OnSaleFragment newInstance() {
		OnSaleFragment fragment = new OnSaleFragment();
		Bundle args = new Bundle();
		args.putString(QUERY, "Arts");
		fragment.setArguments(args);
		return fragment;
	}

	@SuppressLint({ "InflateParams", "InlinedApi" })
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.fragment_onsale, null);
		setHasOptionsMenu(true);
		setupView(v);
		return v;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mQuery = getArguments().getString(QUERY);
		}
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

		RecyclerView recList = v.findViewById(R.id.cardList);

		itemsList = new ArrayList<>();
		recList.setHasFixedSize(true);


		StaggeredGridLayoutManager llm = new StaggeredGridLayoutManager(1,
				StaggeredGridLayoutManager.VERTICAL);

		recList.setLayoutManager(llm);

		mCardAdapter = new UzaCardAdapter(this.getContext(),itemsList);
		recList.setAdapter(mCardAdapter);
		loadData();
	}


    @Override
    public void onStart() {
	    if(scheduledLoad == null) {
            scheduledLoad = new Timer("load data");
            scheduledLoad.schedule(mTimerTask, 500, 1500); // Verify itemData after 500 ms
        }
        super.onStart();
    }

    @Override
    public void onStop() {
	    stopScheduledTask();
        super.onStop();
    }

    /**
	 * Load  product data for displaying on the RecyclerView.
	 */
	private void  loadData()
	{
		if (getLoaderManager().getLoader(0) == null){
			getLoaderManager().initLoader(0, null, this);
		}else{
			getLoaderManager().restartLoader(0,null,this);
		}
	}


	@Override
	public void onDetach() {
		super.onDetach();
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		//Log.i(TAG,"Loader created");
		Uri PlacesUri = UzaContract.ItemsEntry.CATEGORY_URI;

		return new CursorLoader(getContext(),
				PlacesUri,
				ITEMS_COLUMNS,
				mQuery,
				null,
				null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		Log.i(TAG,"Load finished " + data.getCount());

		if(data.getCount() > 0)
		    mListFilled = true;

		while (data.moveToNext()) {

			/*for(int i = 0 ; i < data.getColumnCount() ; i ++ ){
				if(data.getString(i) == null){
					Log.i(TAG, "null");
				}
				else {
					Log.i(TAG, data.getString(i));
				}
			}*/

			Data d = new Data(data,ITEM_DATA);
			// add new item into the list of items
			if(filterType(d)) // Filter data type before adding it into the data list
				if(filterPrice(d))
					itemsList.add(d);
			mCardAdapter.notifyDataSetChanged();
		}
	}

	private boolean filterPrice(Data data) {
		SharedPreferences sharedPref = getActivity().getSharedPreferences(getResources().getString(R.string.uza_keys), Context.MODE_PRIVATE);

		String p = setPrice(data.getCurrency(),data.getPrice(),getContext()); // Getting the price according to the currency

		double price = getPriceDouble(p);

		double minPrice = sharedPref.getLong("priceMinValue",0);
		double maxPrice = sharedPref.getLong("priceMaxValue",MAX_PRICE_VALUE);

		if(price >= 0){
			if(price < maxPrice && price >= minPrice){
				return true;
			}else if(maxPrice == MAX_PRICE_VALUE && price >= maxPrice){
				return true; // +MAX_PRICE_VALUE
			}else{
				Log.i(TAG,"Price not in the selected range");
				return false;
			}
		}
		Log.i(TAG,"Bad formatted price");
		return false;
	}

	private boolean filterType(Data data) {
		SharedPreferences sharedPref = getActivity().getSharedPreferences(getResources().getString(R.string.uza_keys), Context.MODE_PRIVATE);

		Log.i(TAG,"Data type is  : "+data.getType());

		if(data.getType().equalsIgnoreCase(getString(R.string.painting_key)))
			return sharedPref.getBoolean(getString(R.string.painting_key),true);

		if(data.getType().equalsIgnoreCase(getString(R.string.photography_key)))
			return sharedPref.getBoolean(getString(R.string.photography_key),true);

		if(data.getType().equalsIgnoreCase(getString(R.string.drawing_key)))
			return sharedPref.getBoolean(getString(R.string.drawing_key),true);

		if(data.getType().equalsIgnoreCase(getString(R.string.sculpture_key)))
			return sharedPref.getBoolean(getString(R.string.sculpture_key),true);

		if(data.getType().equalsIgnoreCase(getString(R.string.textile_key)))
			return sharedPref.getBoolean(getString(R.string.textile_key),true);

		if(data.getType().equalsIgnoreCase(getString(R.string.literature_key)))
			return sharedPref.getBoolean(getString(R.string.literature_key),true);

		return false;
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {

	}
}
