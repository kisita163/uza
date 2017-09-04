package com.kisita.uza.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.kisita.uza.R;
import com.kisita.uza.custom.CustomFragment;
import com.kisita.uza.listerners.CommandsChildEventListener;
import com.kisita.uza.model.Data;
import com.kisita.uza.utils.UzaCardAdapter;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.lang.reflect.Array;
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
	private UzaCardAdapter mCardadapter;
	CommandsChildEventListener mChildEventListener;

	private TextView orderAmount;
	private TextView shippingCost;
	private TextView totalAmount;

	private OnCheckoutInteractionListener mListener;

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
		Button checkout = (Button)v.findViewById(R.id.btnDone);

		checkout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO format amount here ???
				SharedPreferences sharedPref = getContext().getSharedPreferences(getResources().getString(R.string.uza_keys), Context.MODE_PRIVATE);
				onCheckoutPressed(sharedPref.getString("total_amount","0.0"));
			}
		});

		orderAmount  = (TextView)v.findViewById(R.id.order_amount_value);
		shippingCost = (TextView)v.findViewById(R.id.shipping_cost_value);
		totalAmount  = (TextView)v.findViewById(R.id.total);

		itemsList = new ArrayList<>();
		// Initializing fields
		orderAmount.setText("0.0");
		shippingCost.setText("0.0");
		totalAmount.setText("0.0");

		recList.setHasFixedSize(true);
		setHasOptionsMenu(true);

		StaggeredGridLayoutManager llm = new StaggeredGridLayoutManager(1,
				StaggeredGridLayoutManager.VERTICAL);

		DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recList.getContext(),
				DividerItemDecoration.VERTICAL);
		recList.addItemDecoration(dividerItemDecoration);

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
		DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
		mDatabase.keepSynced(true);


		mChildEventListener = new CommandsChildEventListener(itemsList, mCardadapter, mDatabase, this.getActivity());
		Query itemsQuery = getQuery(mDatabase);
		itemsQuery.addChildEventListener(mChildEventListener);
	}

	public Query getQuery(DatabaseReference databaseReference) {
		return databaseReference.child("users-data").child(getUid()).child("commands");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mChildEventListener = null;
		mListener = null;
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated
	 * to the activity and potentially other fragments contained in that
	 * activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnCheckoutInteractionListener {
		// TODO: Update argument type and name
		void onCheckoutInteraction(String amount,ArrayList<Data> commands);
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onCheckoutPressed(String amount) {
		//Log.i("***** form checkout",ItemsFragment.printItems(itemsList));
		if (mListener != null) {
			mListener.onCheckoutInteraction(amount,itemsList);
		}
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof OnCheckoutInteractionListener) {
			mListener = (OnCheckoutInteractionListener) context;
		} else {
			throw new RuntimeException(context.toString()
					+ " must implement OnNewArticleInteractionListener");
		}
	}

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.findItem(R.id.action_cart);
        item.setVisible(false);
    }
}
