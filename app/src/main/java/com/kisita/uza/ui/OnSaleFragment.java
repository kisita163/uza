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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.kisita.uza.R;
import com.kisita.uza.activities.ChoicesActivity;
import com.kisita.uza.custom.CustomFragment;
import com.kisita.uza.listerners.ItemChildEventListener;
import com.kisita.uza.model.Data;
import com.kisita.uza.provider.UzaContract;
import com.kisita.uza.utils.UzaCardAdapter;

import java.util.ArrayList;

import static com.kisita.uza.model.Data.ITEMS_COLUMNS;
import static com.kisita.uza.model.Data.UZA.TYPE;
import static com.kisita.uza.utils.UzaFunctions.getPicturesUrls;


/**
 * The Class OnSaleFragment is the fragment that shows the products in GridView.
 */
public class OnSaleFragment extends CustomFragment implements  LoaderManager.LoaderCallbacks<Cursor>
{
	final static String TAG = "### OnSaleFragment";
	final static String QUERY = "QUERY";

	private static final int RESULT_CODE = 1;


	private UzaCardAdapter mCardadapter;
	private ArrayList<Data> itemsList;

	private DatabaseReference mDatabase;
	private ItemChildEventListener mChildEventListener;
	private String mQuery;
    private android.support.design.widget.FloatingActionButton choicesButton;

	private String[]  mTypes;


	/* Handle search button */
	private boolean choiceButtonActivated = true;

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

		RecyclerView recList = (RecyclerView) v.findViewById(R.id.cardList);

		if(choiceButtonActivated) {
			choicesButton = (android.support.design.widget.FloatingActionButton) v.findViewById(R.id.fabCart);
			choicesButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent choices = new Intent(getActivity(), ChoicesActivity.class);
					choices.putExtra(getString(R.string.choices), mQuery);
					startActivityForResult(choices, RESULT_CODE);
				}
			});
		}else{
			choicesButton.setVisibility(View.GONE);
		}

		itemsList = new ArrayList<>();
		recList.setHasFixedSize(true);


		StaggeredGridLayoutManager llm = new StaggeredGridLayoutManager(2,
				StaggeredGridLayoutManager.VERTICAL);

		recList.setLayoutManager(llm);
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
		mChildEventListener = null;
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode== RESULT_CODE)
		{
			String choice=data.getStringExtra(getString(R.string.choice));
            ArrayList<Data> tmpList = new ArrayList<>();
            Log.i(TAG, " selected is  : "+choice);
            for (Data d : itemsList) {
                if (d.getData()[TYPE].equalsIgnoreCase(choice) || choice.equalsIgnoreCase(getString(R.string.all))) {
                    tmpList.add(d);
                }
            }
            mCardadapter.setItemsList(tmpList);
            mCardadapter.notifyDataSetChanged();
		}
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
		ArrayList<String> dataArray = new ArrayList<>();
		while (data.moveToNext()) {
			String  []  rowdata  =  {
					data.getString(Data.UZA.UID),
					data.getString(Data.UZA.NAME),
					data.getString(Data.UZA.PRICE),
					data.getString(Data.UZA.CURRENCY),
					data.getString(Data.UZA.BRAND),
					data.getString(Data.UZA.DESCRIPTION),
					data.getString(Data.UZA.SELLER),
					data.getString(Data.UZA.CATEGORY),
					data.getString(Data.UZA.TYPE),
					data.getString(Data.UZA.COLOR),
					data.getString(Data.UZA.SIZE),
					"",
					data.getString(Data.UZA.WEIGHT),
					data.getString(Data.UZA.URL),
					//data.getString(Data.UZA.QUANTITY),
					//data.getString(KEY)
			};

			Data d = new Data(rowdata,
					getPicturesUrls(data.getString(Data.UZA.PICTURES))
			);
			itemsList.add(d);
			mCardadapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {

	}
}
