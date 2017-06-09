package com.kisita.uza.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.TextView;

import com.braintreepayments.api.BraintreePaymentActivity;
import com.braintreepayments.api.PaymentRequest;
import com.braintreepayments.api.models.PaymentMethodNonce;
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
	/** The product list. */
	private ArrayList<Data> itemsList;
	private UzaCardAdapter mCardadapter;
	CommandsChildEventListener mChildEventListener;
	private TextView total;
    private String clientToken;
    private static final int REQUEST_CODE = Menu.FIRST;

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@SuppressLint({ "InflateParams", "InlinedApi" })
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.fragment_checkout, null);

        getToken();
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
                PaymentRequest paymentRequest = new PaymentRequest()
                        .clientToken(clientToken) // TODO Firebase token doesn't work here
                        .amount(total.getText().toString())
                        .primaryDescription("Secure payment")
                        .secondaryDescription("Uza")
                        .actionBarTitle("Payment")
                        .submitButtonText("Pay");
                startActivityForResult(paymentRequest.getIntent(getActivity()), REQUEST_CODE);
			}
		});

		total = (TextView)v.findViewById(R.id.total);
		itemsList = new ArrayList<>();
		total.setText("0.0");
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
		if(mChildEventListener != null){
			mChildEventListener = null;
		}
	}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == BraintreePaymentActivity.RESULT_OK) {
            PaymentMethodNonce paymentMethodNonce = data.getParcelableExtra(BraintreePaymentActivity.EXTRA_PAYMENT_METHOD_NONCE);

            Log.i("Checkout result",paymentMethodNonce.getNonce());

            /*RequestParams requestParams = new RequestParams();
            requestParams.put("payment_method_nonce", paymentMethodNonce.getNonce());
            requestParams.put("amount", "10.00");*/

           /* client.post(SERVER_BASE + "/payment", requestParams, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Toast.makeText(SDKActivity.this, responseString, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    Toast.makeText(SDKActivity.this, responseString, Toast.LENGTH_LONG).show();
                }
            });*/
        }
    }


    private void getToken() {
        clientToken = "eyJ2ZXJzaW9uIjoyLCJhdXRob3JpemF0aW9uRmluZ2VycHJpbnQiOiIyN2E1MDczYWM1ZTIxZjVmZDI5YWIzMTFmNDViZDBlODE0ODMyY2Y4ZmE2MTBlOTM2YjQzYjViNTdhMjQ4N2FjfGNyZWF0ZWRfYXQ9MjAxNy0wNi0wOFQxNzo0Njo1Ni42ODA0MDc4NjErMDAwMFx1MDAyNm1lcmNoYW50X2lkPTM0OHBrOWNnZjNiZ3l3MmJcdTAwMjZwdWJsaWNfa2V5PTJuMjQ3ZHY4OWJxOXZtcHIiLCJjb25maWdVcmwiOiJodHRwczovL2FwaS5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tOjQ0My9tZXJjaGFudHMvMzQ4cGs5Y2dmM2JneXcyYi9jbGllbnRfYXBpL3YxL2NvbmZpZ3VyYXRpb24iLCJjaGFsbGVuZ2VzIjpbXSwiZW52aXJvbm1lbnQiOiJzYW5kYm94IiwiY2xpZW50QXBpVXJsIjoiaHR0cHM6Ly9hcGkuc2FuZGJveC5icmFpbnRyZWVnYXRld2F5LmNvbTo0NDMvbWVyY2hhbnRzLzM0OHBrOWNnZjNiZ3l3MmIvY2xpZW50X2FwaSIsImFzc2V0c1VybCI6Imh0dHBzOi8vYXNzZXRzLmJyYWludHJlZWdhdGV3YXkuY29tIiwiYXV0aFVybCI6Imh0dHBzOi8vYXV0aC52ZW5tby5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tIiwiYW5hbHl0aWNzIjp7InVybCI6Imh0dHBzOi8vY2xpZW50LWFuYWx5dGljcy5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tLzM0OHBrOWNnZjNiZ3l3MmIifSwidGhyZWVEU2VjdXJlRW5hYmxlZCI6dHJ1ZSwicGF5cGFsRW5hYmxlZCI6dHJ1ZSwicGF5cGFsIjp7ImRpc3BsYXlOYW1lIjoiQWNtZSBXaWRnZXRzLCBMdGQuIChTYW5kYm94KSIsImNsaWVudElkIjpudWxsLCJwcml2YWN5VXJsIjoiaHR0cDovL2V4YW1wbGUuY29tL3BwIiwidXNlckFncmVlbWVudFVybCI6Imh0dHA6Ly9leGFtcGxlLmNvbS90b3MiLCJiYXNlVXJsIjoiaHR0cHM6Ly9hc3NldHMuYnJhaW50cmVlZ2F0ZXdheS5jb20iLCJhc3NldHNVcmwiOiJodHRwczovL2NoZWNrb3V0LnBheXBhbC5jb20iLCJkaXJlY3RCYXNlVXJsIjpudWxsLCJhbGxvd0h0dHAiOnRydWUsImVudmlyb25tZW50Tm9OZXR3b3JrIjp0cnVlLCJlbnZpcm9ubWVudCI6Im9mZmxpbmUiLCJ1bnZldHRlZE1lcmNoYW50IjpmYWxzZSwiYnJhaW50cmVlQ2xpZW50SWQiOiJtYXN0ZXJjbGllbnQzIiwiYmlsbGluZ0FncmVlbWVudHNFbmFibGVkIjp0cnVlLCJtZXJjaGFudEFjY291bnRJZCI6ImFjbWV3aWRnZXRzbHRkc2FuZGJveCIsImN1cnJlbmN5SXNvQ29kZSI6IlVTRCJ9LCJjb2luYmFzZUVuYWJsZWQiOmZhbHNlLCJtZXJjaGFudElkIjoiMzQ4cGs5Y2dmM2JneXcyYiIsInZlbm1vIjoib2ZmIn0=";
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.findItem(R.id.action_cart);
        item.setVisible(false);
    }
}
