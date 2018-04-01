package com.kisita.uza.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;
import com.braintreepayments.api.BraintreePaymentActivity;
import com.braintreepayments.api.PaymentRequest;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.facebook.CallbackManager;
import com.kisita.uza.R;
import com.kisita.uza.custom.CustomActivity;
import com.kisita.uza.model.Data;
import com.kisita.uza.services.Token;
import com.kisita.uza.ui.BillingFragment;
import com.kisita.uza.ui.CheckoutFragment;
import com.kisita.uza.ui.DetailFragment;
import com.kisita.uza.ui.FavoritesFragment;
import com.kisita.uza.ui.ItemsFragment;
import com.kisita.uza.ui.MapsFragment;
import com.kisita.uza.ui.SettingsFragment;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import cz.msebera.android.httpclient.Header;


public class UzaActivity extends CustomActivity implements CheckoutFragment.OnCheckoutInteractionListener,
       ItemsFragment.OnItemFragmentInteractionListener {
    /**
     * The toolbar.
     */
    public Toolbar toolbar;

    private static final int REQUEST_CODE = Menu.FIRST;

    private String mAmount = "0.0";

    private CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        toolbar = (Toolbar) findViewById(R.id.checkout_toolbar);

        setFragment(getIntent().getIntExtra("fragment", -1));

        setSupportActionBar(toolbar);

        Drawable upArrow = getResources().getDrawable(R.drawable.ic_action_close);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        callbackManager = CallbackManager.Factory.create();
    }


    void setFragment(int fid) {
        String title = "";
        Fragment f = null;
        switch (fid) {
            case (0):
                title = getString(R.string.favourites);
                f = new FavoritesFragment();
                break;
            case (1):
                title = getString(R.string.checkout);
                f = new CheckoutFragment();
                break;
            case (2):
                title = getString(R.string.settings);
                f = new SettingsFragment();
                break;
            case (3):
                //title = getIntent().getStringArrayExtra("details")[NAME];
                f = DetailFragment.newInstance((Data) getIntent().getSerializableExtra("details"));
                break;
            case(4):
                toolbar.setTitle(R.string.commands);
                f = BillingFragment.newInstance();
                break;
            default:
                break;
        }
        toolbar.setTitle(title);
        if (f != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, f , title)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getFragmentManager().popBackStackImmediate();
    }

    @Override
    public void onCheckoutInteraction(String amount, ArrayList<Data> commands) {

    }



    void paymentRequest() {
        Log.i(TAG, "Launch braintree activity");
        SharedPreferences sharedPref = getSharedPreferences(getResources().getString(R.string.uza_keys),
                Context.MODE_PRIVATE);
        String token = sharedPref.getString("braintree_token", null);
        if (token == null) {
            Toast.makeText(getApplicationContext(), getString(R.string.failed_to_get_token), Toast.LENGTH_LONG).show();
            //TODO Retry here ?
            return;
        }

        PaymentRequest paymentRequest = new PaymentRequest()
                .clientToken(token)
                .amount(mAmount)
                .androidPayPhoneNumberRequired(true)
                .primaryDescription(getString(R.string.secure_payement))
                .secondaryDescription(getString(R.string.app_name))
                .actionBarTitle(getString(R.string.payement))
                .androidPayShippingAddressRequired(true)

                .submitButtonText(getString(R.string.pay));
        startActivityForResult(paymentRequest.getIntent(this), REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "received result is  : " + resultCode + " request code : "+requestCode);
        //showProgressDialog(getString(R.string.please_wait));
        switch(requestCode){
            case REQUEST_CODE:
                handlePaymentResult(resultCode, data);
                break;
            default:
                handleFacebookResult(requestCode,resultCode, data);
        }

    }

    private void handleFacebookResult(int requestCode,int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        hideProgressDialog();
    }

    private void handlePaymentResult(int resultCode, Intent data) {
        if (resultCode == BraintreePaymentActivity.RESULT_OK) {
            PaymentMethodNonce paymentMethodNonce = data.getParcelableExtra(BraintreePaymentActivity.EXTRA_PAYMENT_METHOD_NONCE);
            AsyncHttpClient client = new AsyncHttpClient();
            Log.i("Checkout result", paymentMethodNonce.getNonce());

            RequestParams requestParams = new RequestParams();
            requestParams.put("payment_method_nonce", paymentMethodNonce.getNonce());
            requestParams.put("amount", mAmount);

            client.post(Token.BRAINTREE_SERVER, requestParams, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Toast.makeText(getBaseContext(), R.string.fail_to_contact_payment_server, Toast.LENGTH_LONG).show();
                    hideProgressDialog();
                    onBackPressed();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    Toast.makeText(getBaseContext(), responseString, Toast.LENGTH_LONG).show();
                    Log.i(TAG, responseString);
                    if(responseString.equalsIgnoreCase("OK")){
                        // Change command state
                        // Go to the fragment that shows commands in processing
                        /*getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_frame, CommandsFragment.newInstance("",false),"COMMANDS")
                                .commit();*/
                    }else{
                        Toast.makeText(getBaseContext(), R.string.transaction_rejected, Toast.LENGTH_LONG).show();
                    }
                    hideProgressDialog();
                }
            });
        }else if(resultCode == RESULT_CANCELED){
            hideProgressDialog();
        }else{
            Toast.makeText(getBaseContext(), R.string.fail_to_contact_payment_server, Toast.LENGTH_LONG).show();
            hideProgressDialog();
        }
    }

    @Override
    public void onItemFragmentInteraction(String title) {
        //Log.i(TAG,"Reload fragment with tag : "+title);
        // Reload current fragment
		Fragment frg = null;
		frg = getSupportFragmentManager().findFragmentByTag(title);
		final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.detach(frg);
		ft.attach(frg);
		ft.commit();
    }

    @Override
    public void onCommandSelectedInteraction(String key) {
        //Log.i(TAG,"key received  : "+key);
        Fragment f = MapsFragment.newInstance(key);
        toolbar.setTitle(R.string.command_details);
        if (f != null) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                    .addToBackStack(null)
                    .replace(R.id.content_frame, f , "Command details")
                    .commit();
        }
    }
}
