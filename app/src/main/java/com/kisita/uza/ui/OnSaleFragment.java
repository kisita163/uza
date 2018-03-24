package com.kisita.uza.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.kisita.uza.R;
import com.kisita.uza.custom.CustomFragment;
import com.kisita.uza.model.Data;
import com.kisita.uza.provider.UzaContract;
import com.kisita.uza.utils.UzaCardAdapter;
import java.util.ArrayList;
import static com.kisita.uza.model.Data.ITEMS_COLUMNS;
import static com.kisita.uza.model.Data.ITEM_DATA;


/*
 * The Class OnSaleFragment is the fragment that shows the products in GridView.
 */
public class OnSaleFragment extends CustomFragment implements  LoaderManager.LoaderCallbacks<Cursor>
{
	final static String TAG = "### OnSaleFragment";
	final static String QUERY = "QUERY";

	private static final int RESULT_CODE = 1;


	private UzaCardAdapter mCardadapter;
	private ArrayList<Data> itemsList;

	private String mQuery;

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */

	public OnSaleFragment() {
		// Required empty public constructor
	}

	public static OnSaleFragment newInstance(String query) {
		OnSaleFragment fragment = new OnSaleFragment();
		Bundle args = new Bundle();
		args.putString(QUERY, query);
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

        /*DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recList.getContext(),
                DividerItemDecoration.VERTICAL);
        recList.addItemDecoration(dividerItemDecoration);*/

		mCardadapter = new UzaCardAdapter(this.getContext(),itemsList);
		recList.setAdapter(mCardadapter);
		loadData();
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
		//Log.i(TAG,"Load finished");
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
			itemsList.add(d);
			mCardadapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {

	}
}
