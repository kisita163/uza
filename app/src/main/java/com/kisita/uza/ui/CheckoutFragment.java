package com.kisita.uza.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.kisita.uza.R;
import com.kisita.uza.custom.CustomFragment;
import com.kisita.uza.listerners.CommandsChildEventListener;
import com.kisita.uza.model.Data;
import com.kisita.uza.utils.CartDrawable;
import com.kisita.uza.utils.UzaCardAdapter;

import java.util.ArrayList;

/**
 * The Class CheckoutFragment is the fragment that shows the list products for fragment_checkout
 * and show the credit card details as well. You need to load and display actual
 * contents.
 */
public class CheckoutFragment extends CustomFragment
{

	/** The product list. */
	private ArrayList<Data> itemsList;
	private DatabaseReference mDatabase;
	private UzaCardAdapter mCardadapter;
	private CommandsChildEventListener mChildEventListener;
	private final static String TAG = "### CheckoutFragment";
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


		StaggeredGridLayoutManager llm = new StaggeredGridLayoutManager(2,
				StaggeredGridLayoutManager.HORIZONTAL);

		llm.setOrientation(LinearLayoutManager.VERTICAL);
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
		mDatabase = FirebaseDatabase.getInstance().getReference();
		mDatabase.keepSynced(true);


		mChildEventListener = new CommandsChildEventListener(itemsList,mCardadapter,mDatabase);
		Query itemsQuery = getQuery(mDatabase);
		itemsQuery.addChildEventListener(mChildEventListener);
	}

	public Query getQuery(DatabaseReference databaseReference) {
		return databaseReference.child("users-data").child(getUid()).child("commands");
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		Log.i(TAG, "onCreateOptionsMenu");
		MenuItem itemCart = menu.findItem(R.id.action_cart);
		mIcon = (LayerDrawable) itemCart.getIcon();
		setBadgeColor(this.getContext(), mIcon, R.color.gray);
		super.onCreateOptionsMenu(menu, inflater);
	}

	private void setBadgeColor(Context context, LayerDrawable icon, int color) {
		CartDrawable badge;

		// Reuse drawable if pos
		// sible
		Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
		if (reuse != null && reuse instanceof CartDrawable) {
			badge = (CartDrawable) reuse;
		} else {
			badge = new CartDrawable(context);
		}
		badge.setColor(color);
		icon.mutate();
		icon.setDrawableByLayerId(R.id.ic_badge, badge);
	}
	//TODO remove articles from cart
}
