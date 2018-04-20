package com.kisita.uza.activities;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.facebook.CallbackManager;
import com.kisita.uza.R;
import com.kisita.uza.custom.CustomActivity;
import com.kisita.uza.model.Data;
import com.kisita.uza.ui.CheckoutFragment;
import com.kisita.uza.ui.CommandDetails;
import com.kisita.uza.ui.ItemDetails;
import com.kisita.uza.ui.ItemsFragment;
import com.kisita.uza.ui.MapsFragment;

import java.util.ArrayList;


public class UzaActivity extends CustomActivity implements CheckoutFragment.OnCheckoutInteractionListener,
       ItemsFragment.OnItemFragmentInteractionListener, CommandDetails.OnFragmentInteractionListener, ItemDetails.OnFragmentInteractionListener {
    /**
     * The toolbar.
     */
    public Toolbar toolbar;

    private CallbackManager callbackManager;

    private static final String FRAGMENTS_CONFIG = "fragment_config";

    private Fragment mLeftFragment;

    private Fragment mRightFragment;

    private String   mTitle;

    private int mCurrentFragmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        toolbar = findViewById(R.id.checkout_toolbar);

        int fId = getIntent().getIntExtra("fragment", -1);

        if((fId == -1) && (savedInstanceState != null)){
            fId = savedInstanceState.getInt(FRAGMENTS_CONFIG);
        }
        setFragment(fId);
        setSupportActionBar(toolbar);

        Drawable upArrow = getResources().getDrawable(R.drawable.ic_action_close);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        callbackManager = CallbackManager.Factory.create();
    }

    void setFragment(int fid) {

        switch (fid) {
            case (0):
                Data itemData = (Data) getIntent().getSerializableExtra("details");
                mTitle = itemData.getCommandId();
                mRightFragment = CommandDetails.newInstance(itemData);
                mLeftFragment  = ItemDetails.newInstance(itemData);
                break;
            default:
                break;
        }

        mCurrentFragmentId = fid;
        updateForegroundFragment();
    }

    public void updateForegroundFragment() {
        toolbar.setTitle(mTitle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_right_frame, mRightFragment)
                .addToBackStack(null)
                .commit();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_left_frame, mLeftFragment)
                .addToBackStack(null)
                .commit();
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
                    //.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                    .addToBackStack(null)
                    .replace(R.id.content_left_frame, f , "Command details")
                    .commit();
        }
    }

    @Override
    public void onCommandChanged(String[] details, boolean update) {
        if(mBound){
            mService.addNewItemInCart(details);
        }
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG,"Fragment count is  : " + getSupportFragmentManager().getBackStackEntryCount());
        if(getSupportFragmentManager().getBackStackEntryCount() == 1)
            finish();
        else {
            toolbar.setTitle(R.string.commands);
            Drawable upArrow = getResources().getDrawable(R.drawable.ic_action_close);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if(mCurrentFragmentId != 0){
            outState.putInt(FRAGMENTS_CONFIG,mCurrentFragmentId);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return true;
    }
}
