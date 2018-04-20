package com.kisita.uza.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.kisita.uza.R;
import com.kisita.uza.activities.DrawerActivity;
import com.kisita.uza.model.Data;
import com.kisita.uza.provider.UzaContract;
import com.kisita.uza.utils.UzaCheckoutPageAdapter;
import java.util.ArrayList;
import static com.kisita.uza.model.Data.CHECKOUT_DATA;
import static com.kisita.uza.model.Data.ITEMS_COMMANDS_COLUMNS;
import static com.kisita.uza.utils.UzaFunctions.addDoubles;
import static com.kisita.uza.utils.UzaFunctions.getCost;
import static com.kisita.uza.utils.UzaFunctions.getCurrency;
import static com.kisita.uza.utils.UzaFunctions.getShippingCost;
import static com.kisita.uza.utils.UzaFunctions.infoAlertDialog;
import static com.kisita.uza.utils.UzaFunctions.questionAlertDialog;
import static com.kisita.uza.utils.UzaFunctions.setFormat;
import static com.kisita.uza.utils.UzaFunctions.setPrice;

/**
 * The Class CheckoutFragment is the fragment that shows the list products for fragment_checkout
 * and show the credit card details as well. You need to load and display actual
 * contents.
 */
public class CheckoutFragment extends ItemsFragment implements LoaderManager.LoaderCallbacks<Cursor>
{
	/** The product list. */
	private ArrayList<Data> itemsList;
	private UzaCheckoutPageAdapter mCheckoutItemsAdapter;

	private TextView orderAmountField;
	private TextView shippingCostField;
	private TextView totalAmountField;

	/* Order amount*/
	private double  mOrder        = 0;
	private double  mShippingCost = 0;
	private double  mTotalAmount  = 0;

	private LinearLayout mCheckoutLayout;

	private OnCheckoutInteractionListener mListener;

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@SuppressLint({ "InflateParams", "InlinedApi" })
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.fragment_checkout, null);
		setTouchNClick(v.findViewById(R.id.checkout));
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
	protected void setupView(View v)
	{

		//RecyclerView recList =  v.findViewById(R.id.cardList);
		Button checkoutButton = v.findViewById(R.id.checkout);

		checkoutButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onCheckoutPressed(String.valueOf(mTotalAmount));
			}
		});

		//checkoutButton.setClickable(false);
		//checkoutButton.setBackgroundColor(getResources().getColor(R.color.main_grey));

		orderAmountField  = v.findViewById(R.id.order_amount_value);
		shippingCostField = v.findViewById(R.id.shipping_cost_value);
		totalAmountField  = v.findViewById(R.id.total);
		mCheckoutLayout   = v.findViewById(R.id.cost_layout);

		mCheckoutLayout.setVisibility(View.INVISIBLE);

		itemsList = new ArrayList<>();

		setHasOptionsMenu(true);

		initPager(v);
		loadData();
	}

	private void handleCost(String newCost, String newQantity) {
		// Initializing fields
		/*
		shippingCostField.setText("0.0");
		totalAmount.setText("0.0");*/
		mOrder = addDoubles(mOrder,Double.valueOf(getCost(newCost,newQantity)));
		Log.i(TAG,"Total order is : " + mOrder + " " + newCost +  " " + Double.valueOf(getCost(newCost,newQantity)) + "quantity = "+newQantity);
		orderAmountField.setText(setFormat(String.valueOf(mOrder)) + " " + getCurrency(getContext()));
	}

	/**
	 * Load  product data for displaying on the RecyclerView.
	 */
	void  loadData()
	{
		if (getLoaderManager().getLoader(0) == null){
			getLoaderManager().initLoader(0, null, this);
		}else{
			getLoaderManager().restartLoader(0,null,this);
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		Uri PlacesUri = UzaContract.CommandsEntry.CONTENT_URI_CHECKOUT;

		return new CursorLoader(getContext(),
				PlacesUri,
				ITEMS_COMMANDS_COLUMNS,
				null,
				null,
				null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		//Log.i(TAG,"Load finished : itemList size = "+itemsList.size());
		mOrder        = 0;
		mTotalAmount  = 0;
		mShippingCost = 0;

		while (data.moveToNext()) {
			Data d = new Data(data, CHECKOUT_DATA);

			handleCost(setPrice(d.getCurrency(),d.getPrice(),getContext()), // The item price in the currency used by the app
					      d.getQuantity()); // item quantity

			handleShippingCost(d.getWeight(),d.getQuantity());

			handleTotalAmount();

			itemsList.add(d);
			mCheckoutItemsAdapter.notifyDataSetChanged();
		}

		if(mTotalAmount > 0){
			mCheckoutLayout.setVisibility(View.VISIBLE);
		}
	}

	private void handleTotalAmount() {

		SharedPreferences sharedPref = getContext().getSharedPreferences(getContext().getResources().getString(R.string.uza_keys), Context.MODE_PRIVATE);
		mTotalAmount = addDoubles(mShippingCost,mOrder);

		String amount  = String.valueOf(mTotalAmount);

		totalAmountField.setText(setFormat(amount) + " " + getCurrency(getContext()));
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString("total_amount",amount);
		editor.apply();

		/*if(mTotalAmount > 0){
			checkoutButton.setClickable(true);
			checkoutButton.setBackgroundColor(getResources().getColor(R.color.main_color));
		}*/
	}

	private void handleShippingCost(String weight, String quantity) {

		if(weight.equalsIgnoreCase("")){
			weight = "0";
		}
		mShippingCost = addDoubles(mShippingCost,Double.valueOf(getShippingCost(weight,quantity)));
		shippingCostField.setText(setFormat(String.valueOf(mShippingCost)) + " " + getCurrency(getContext()));
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {

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
		void onCheckoutInteraction(String amount,ArrayList<Data> commands);
	}

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
			mListener        = (OnCheckoutInteractionListener) context;
		} else {
			throw new RuntimeException(context.toString()
					+ " must implement OnCheckoutInteractionListener");
		}
	}

	public void onRemovePressedListener(final Data d){

		//Log.i(TAG,"**** "+rowDeleted);
		//Log.i(TAG, "onRemovePressedListener " + getTag());
		questionAlertDialog(getContext(),getString(R.string.remove_item), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				switch (i){
					case DialogInterface.BUTTON_POSITIVE:
						removeItemFromCart(d);
						// Update activity checkout fragment
						((DrawerActivity)getActivity()).setFragment(new CheckoutFragment());
						break;
					case DialogInterface.BUTTON_NEGATIVE:
						//No button clicked
						break;
				}
			}
		});
	}

	private void removeItemFromCart(Data d) {
		itemsList.remove(d);
		mCheckoutItemsAdapter.notifyDataSetChanged();

		String where   = "_ID = ?";
		String [] args =  {d.getCheckoutId()};
		getContext().getContentResolver().delete(
				UzaContract.CommandsEntry.CONTENT_URI_COMMANDS,
				where,
				args
		);

		// Delete command in firebase
		DatabaseReference commands = getDb().child("users-data").child(getUid()).child("commands");
		commands.child(d.getCheckoutId()).removeValue();
	}

	/*
	 *  pager view initialization.
	 */

	private void initPager(View v)
	{
		/* The pager. */
		ViewPager pager = v.findViewById(R.id.pager);
		pager.setPageMargin(10);
		/* The view that hold dots. */
		LinearLayout vDots = v.findViewById(R.id.vDots);

		mCheckoutItemsAdapter = new UzaCheckoutPageAdapter(getContext(),itemsList, vDots, this);

		pager.setOnPageChangeListener(mCheckoutItemsAdapter);

		pager.setAdapter(mCheckoutItemsAdapter);
	}
}
