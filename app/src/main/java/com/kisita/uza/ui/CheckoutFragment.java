package com.kisita.uza.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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

import com.kisita.uza.R;
import com.kisita.uza.model.Data;
import com.kisita.uza.provider.UzaContract;
import com.kisita.uza.utils.UzaCardAdapter;

import java.util.ArrayList;

import static com.kisita.uza.model.Data.ITEMS_COMMANDS_COLUMNS;
import static com.kisita.uza.model.Data.UZA.KEY;
import static com.kisita.uza.utils.UzaFunctions.addDoubles;
import static com.kisita.uza.utils.UzaFunctions.getCost;
import static com.kisita.uza.utils.UzaFunctions.getCurrency;
import static com.kisita.uza.utils.UzaFunctions.getPicturesUrls;
import static com.kisita.uza.utils.UzaFunctions.getShippingCost;
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
	private UzaCardAdapter mCardadapter;

	private TextView orderAmountField;
	private TextView shippingCostField;
	private TextView totalAmountField;

	private Button checkoutButton;

	/* Order amount*/
	private double  mOrder        = 0;
	private double  mShippingCost = 0;
	private double  mTotalAmount  = 0;

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
	protected void setupView(View v)
	{

		RecyclerView recList =  v.findViewById(R.id.cardList);
		checkoutButton       = v.findViewById(R.id.btnDone);

		checkoutButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO format amount here ???
				SharedPreferences sharedPref = getContext().getSharedPreferences(getResources().getString(R.string.uza_keys), Context.MODE_PRIVATE);
				onCheckoutPressed(sharedPref.getString("total_amount","0.0"));
			}
		});

		checkoutButton.setClickable(false);
		checkoutButton.setBackgroundColor(getResources().getColor(R.color.main_grey));

		orderAmountField  = v.findViewById(R.id.order_amount_value);
		shippingCostField = v.findViewById(R.id.shipping_cost_value);
		totalAmountField  = v.findViewById(R.id.total);

		itemsList = new ArrayList<>();

		recList.setHasFixedSize(true);
		setHasOptionsMenu(true);

		StaggeredGridLayoutManager llm = new StaggeredGridLayoutManager(1,
				StaggeredGridLayoutManager.VERTICAL);

		DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recList.getContext(),
				DividerItemDecoration.VERTICAL);
		recList.addItemDecoration(dividerItemDecoration);

		recList.setLayoutManager(llm);
		mCardadapter = new UzaCardAdapter(this.getContext(), itemsList, this , true);
		recList.setAdapter(mCardadapter);
		loadData();
	}

	private void handleCost(String newCost, String newQantity) {
		// Initializing fields
		/*
		shippingCostField.setText("0.0");
		totalAmount.setText("0.0");*/
		mOrder = addDoubles(mOrder,Double.valueOf(getCost(newCost,newQantity)));
		Log.i(TAG,"Total order is : " + mOrder + " " + newCost +  " " + Double.valueOf(getCost(newCost,newQantity)));
		orderAmountField.setText((String.valueOf(mOrder)) + " " + getCurrency(getContext()));
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
					data.getString(Data.UZA.QUANTITY),
					data.getString(KEY)
			};

			/*Log.i(TAG,data.getString(Data.UZA.UID)         + " " +
					  data.getString(Data.UZA.NAME)        + " " +
					  data.getString(Data.UZA.PRICE)       + " " +
					  data.getString(Data.UZA.CURRENCY)    + " " +
					  data.getString(Data.UZA.BRAND)       + " " +
					  data.getString(Data.UZA.DESCRIPTION) + " " +
					  data.getString(Data.UZA.SELLER)      + " " +
					  data.getString(Data.UZA.CATEGORY)    + " " +
					  data.getString(Data.UZA.TYPE)        + " " +
					  data.getString(Data.UZA.COLOR)       + " " +
					  data.getString(Data.UZA.QUANTITY)    + " " +
					  data.getString(Data.UZA.KEY)         + " " +
					  data.getString(Data.UZA.NAME));*/
			Log.i(TAG,setPrice(data.getString(Data.UZA.CURRENCY),data.getString(Data.UZA.PRICE),getContext()));
			handleCost(setPrice(data.getString(Data.UZA.CURRENCY),data.getString(Data.UZA.PRICE),getContext()), // The item price in the currency used by the app
					      data.getString(Data.UZA.QUANTITY)); // item quantity

			handleShippingCost(data.getString(Data.UZA.WEIGHT),data.getString(Data.UZA.QUANTITY));

			handleTotalAmount();
			Data d = new Data(rowdata,
					getPicturesUrls(data.getString(Data.UZA.PICTURES))
			);
			itemsList.add(d);
			mCardadapter.notifyDataSetChanged();
		}
	}

	private void handleTotalAmount() {

		SharedPreferences sharedPref = getContext().getSharedPreferences(getContext().getResources().getString(R.string.uza_keys), Context.MODE_PRIVATE);
		mTotalAmount = addDoubles(mShippingCost,mOrder);

		String amount  = setFormat(String.valueOf(mTotalAmount));

		totalAmountField.setText(amount + " " + getCurrency(getContext()));
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString("total_amount",amount);
		editor.apply();

		if(mTotalAmount > 0){
			checkoutButton.setClickable(true);
			checkoutButton.setBackgroundColor(getResources().getColor(R.color.main_color));
		}
	}

	private void handleShippingCost(String weight, String quantity) {

		if(weight.equalsIgnoreCase("")){
			weight = "0";
		}
		mShippingCost = addDoubles(mShippingCost,Double.valueOf(getShippingCost(weight,quantity)));
		shippingCostField.setText(String.valueOf(mShippingCost) + " " + getCurrency(getContext()));
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
			mListener        = (OnCheckoutInteractionListener) context;
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

	public void onRemovePressedListener(Data d){
		itemsList.remove(d);
		mCardadapter.notifyDataSetChanged();

		String where   = "_ID = ?";
		String [] args =  {d.getData()[KEY]};
		int rowDeleted = getContext().getContentResolver().delete(
				UzaContract.CommandsEntry.CONTENT_URI_COMMANDS,
				where,
				args
		);
		Log.i(TAG,"**** "+rowDeleted);
		//Log.i(TAG, "onRemovePressedListener " + getTag());
		onReloadRequest(getTag());
	}
}
