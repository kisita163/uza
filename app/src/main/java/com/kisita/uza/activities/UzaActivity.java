package com.kisita.uza.activities;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import com.facebook.CallbackManager;
import com.kisita.uza.R;
import com.kisita.uza.custom.CustomActivity;
import com.kisita.uza.model.Data;
import com.kisita.uza.ui.BillingFragment;
import com.kisita.uza.ui.CheckoutFragment;
import com.kisita.uza.ui.DetailFragment;
import com.kisita.uza.ui.FavoritesFragment;
import com.kisita.uza.ui.ItemsFragment;
import com.kisita.uza.ui.MapsFragment;
import com.kisita.uza.ui.SettingsFragment;

import java.util.ArrayList;



public class UzaActivity extends CustomActivity implements CheckoutFragment.OnCheckoutInteractionListener,
       ItemsFragment.OnItemFragmentInteractionListener, DetailFragment.OnFragmentInteractionListener {
    /**
     * The toolbar.
     */
    public Toolbar toolbar;

    private CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        toolbar = findViewById(R.id.checkout_toolbar);

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
                f = new FavoritesFragment();
                break;
            case (1):
                f = new CheckoutFragment();
                break;
            case (2):
                f = new SettingsFragment();
                break;
            case (3):
                f = DetailFragment.newInstance((Data) getIntent().getSerializableExtra("details"));
                break;
            case(4):
                title=getString(R.string.billing_information);
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "received result is  : " + resultCode + " request code : "+requestCode);
        handleFacebookResult(requestCode,resultCode, data);
    }

    private void handleFacebookResult(int requestCode,int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        hideProgressDialog();
    }

    @Override
    public void onItemFragmentInteraction(String title) {
        //Log.i(TAG,"Reload fragment with tag : "+title);
        // Reload current fragment
		Fragment frg;
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

    @Override
    public void onItemAddedInCart(String[] details, boolean update) {
        if(mBound){
            mService.addNewItemInCart(details);
        }
    }
}
