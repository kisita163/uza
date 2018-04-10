package com.kisita.uza.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.MenuItem;
import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.exceptions.InvalidArgumentException;
import com.kisita.uza.R;
import com.kisita.uza.custom.CustomActivity;
import com.kisita.uza.model.Data;
import com.kisita.uza.ui.CheckoutFragment;
import com.kisita.uza.ui.ChoicesFragment;
import com.kisita.uza.ui.FavoritesFragment;
import com.kisita.uza.ui.OnSaleFragment;
import com.kisita.uza.ui.SettingsFragment;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;

import static com.kisita.uza.utils.UzaFunctions.TRANSACTION_OK;

public class MainActivity extends CustomActivity implements CheckoutFragment.OnCheckoutInteractionListener
{

    private static final String CURRENT_FRAGMENT_ID = "current_fragment_id";

    private Fragment fragment = OnSaleFragment.newInstance();

    private int mCurrentFragmentId = R.id.navigation_home;

    private Stack<Integer> mFragmentsStack = new Stack<>();

    private BottomNavigationView mBottomNavigationView;

    private boolean mEmptyingStack = false;

    private static final int REQUEST_CODE = 100;

    private String mAuthorization;

    private ArrayList<Data> mCommands;

    private String mAmount = "0";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if(item.getItemId() != mCurrentFragmentId)
                setFragment(setCurrentFragment(item.getItemId()));
            return true;
        }
    };

    private Fragment setCurrentFragment(int fragmentId){
        Fragment fragment;
        switch (fragmentId) {
            case R.id.navigation_home:
                fragment = OnSaleFragment.newInstance();
                mCurrentFragmentId = fragmentId;
                break;
            case R.id.navigation_tune:
                fragment = new SettingsFragment();
                mCurrentFragmentId = fragmentId;
                break;
            case R.id.navigation_user:
                fragment = ChoicesFragment.newInstance(getString(R.string.settings));
                mCurrentFragmentId = fragmentId;
                break;
            case R.id.navigation_cart:
                fragment = new CheckoutFragment();
                mCurrentFragmentId = fragmentId;
                break;
            case R.id.navigation_favourite:
                fragment = new FavoritesFragment();
                mCurrentFragmentId = fragmentId;
                break;
            default:
                fragment = OnSaleFragment.newInstance();
                mCurrentFragmentId = R.id.navigation_home;
                break;
        }

        if(!mEmptyingStack) {
            mFragmentsStack.push(mCurrentFragmentId);
            Log.i(TAG,"Stack : "+ mFragmentsStack);
        }else{
            mEmptyingStack = false;
        }

        return fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomNavigationView = findViewById(R.id.navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if(savedInstanceState != null){
            mCurrentFragmentId = savedInstanceState.getInt(CURRENT_FRAGMENT_ID);
        }
        fetchAuthorization();
        setFragment(setCurrentFragment(mCurrentFragmentId));
    }

     @Override
     protected void onAuthorizationFetched(String token) {

        if(token != null) {
            Log.i(TAG,"Authorization received");
            mAuthorization = token;
            setUpBrainTreeFragment();
        }else{
            Log.i(TAG,"Authorization not received. We will try to get it again");
            fetchAuthorization(); // Try again to get a token
        }
     }

     private void setUpBrainTreeFragment() {
        try {
           BraintreeFragment.newInstance(this,mAuthorization);
        } catch (InvalidArgumentException e) {
            Log.i(TAG,"There was an issue with authorization string");
        }
    }

    public void setFragment(Fragment f){
        /* Note : You should not add transactions to the back stack when the transaction is for horizontal navigation
        (such as when switching tabs) or when modifying the content appearance (such as when adjusting filters).
         */
        Log.i(TAG,"setFragment()  "+fragment.toString());
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right,R.anim.slide_out_left,R.anim.slide_in_left,R.anim.slide_out_right)
                .addToBackStack(null)
                .replace(R.id.frame,f)
                .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        if(mCurrentFragmentId != 0){
            outState.putInt(CURRENT_FRAGMENT_ID,mCurrentFragmentId);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        try {
            mEmptyingStack = true;
            mFragmentsStack.pop();
            mBottomNavigationView.setSelectedItemId((mFragmentsStack.peek()));
            Log.i(TAG,"Stack(after pop) : "+ mFragmentsStack);
        }catch(EmptyStackException e){
            finish();
        }
    }

    void paymentRequest() {
        Log.i(TAG, "Launch Payment activity");

        DropInRequest dropInRequest = new DropInRequest()
                .maskCardNumber(true)
                .maskSecurityCode(true)
                .clientToken(mAuthorization);
        startActivityForResult(dropInRequest.getIntent(this), REQUEST_CODE);
    }

    @Override
    public void onCheckoutInteraction(String amount, ArrayList<Data> commands) {
        mAmount   = String.valueOf(amount);
        mCommands = commands;
        paymentRequest();
        Log.i(TAG,"Start transaction with amount = "+mAmount);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        showProgressDialog("Processing ...");
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                // use the result to update your UI and send the payment method nonce to your server
                sendPaymentNonce(result.getPaymentMethodNonce().getNonce(),mAmount);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                //TODO
                // the user canceled
                hideProgressDialog();
            } else {
                //TODO
                // handle errors here, an exception may be available in
                hideProgressDialog();
            }
        }
    }

    @Override
    protected void onTransactionDone(String responseString) {
        if(responseString.equalsIgnoreCase(TRANSACTION_OK)){
            setFragment(setCurrentFragment(R.id.navigation_home));
            // Update commands
            if(mBound) {
                mService.setCommandsState(mCommands);
            }else{
                //TODO Something wrong happened here
            }
        }
        hideProgressDialog();
    }
}
