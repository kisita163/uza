package com.kisita.uza.ui;

import android.annotation.SuppressLint;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.kisita.uza.R;
import com.kisita.uza.custom.CustomFragment;
import com.kisita.uza.listerners.CommandsChildEventListener;
import com.kisita.uza.model.Data;
import com.kisita.uza.utils.UzaCardAdapter;

import java.util.ArrayList;

/**
 * The Class CheckoutFragment is the fragment that shows the list products for fragment_checkout
 * and show the credit card details as well. You need to load and display actual
 * contents.
 */
public class CheckoutFragment extends CustomFragment
{

	private final static String TAG = "### CheckoutFragment";
	/** The product list. */
	private ArrayList<Data> itemsList;
	private DatabaseReference mDatabase;
	private UzaCardAdapter mCardadapter;
	private CommandsChildEventListener mChildEventListener;
	private DatabaseReference commands;
	/* cart icon*/
	private LayerDrawable mIcon;

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@SuppressLint({ "InflateParams", "InlinedApi" })
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.fragment_checkout, null);

		setTouchNClick(v.findViewById(R.id.btnDone));
		setupView(v);
		return v;
	}

	/* (non-Javadoc)
	 * @see com.whatshere.custom.CustomFragment#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v)
	{
		super.onClick(v);
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
		itemsList = new ArrayList<>();
		recList.setHasFixedSize(true);
		setHasOptionsMenu(true);

		StaggeredGridLayoutManager llm = new StaggeredGridLayoutManager(1,
				StaggeredGridLayoutManager.HORIZONTAL);

		llm.setOrientation(LinearLayoutManager.VERTICAL);
		recList.setLayoutManager(llm);
		mCardadapter = new UzaCardAdapter(this.getContext(), itemsList, true);
		recList.setAdapter(mCardadapter);
		loadData();
	}

	/**
	 * Load  product data for displaying on the RecyclerView.
	 */
	private void  loadData()
	{
		mDatabase = FirebaseDatabase.getInstance().getReference();
		mDatabase.keepSynced(true);


		mChildEventListener = new CommandsChildEventListener(itemsList, mCardadapter, mDatabase, this.getActivity());
		Query itemsQuery = getQuery(mDatabase);
		itemsQuery.addChildEventListener(mChildEventListener);
	}

	public Query getQuery(DatabaseReference databaseReference) {
		return databaseReference.child("users-data").child(getUid()).child("commands");
	}
	//TODO remove articles from cart
}
