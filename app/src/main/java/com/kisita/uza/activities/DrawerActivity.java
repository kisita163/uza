package com.kisita.uza.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.exceptions.InvalidArgumentException;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.kisita.uza.R;
import com.kisita.uza.custom.CustomActivity;
import com.kisita.uza.internal.LogReporting;
import com.kisita.uza.model.Data;
import com.kisita.uza.ui.BillingFragment;
import com.kisita.uza.ui.CheckoutFragment;
import com.kisita.uza.ui.CommandsFragment;
import com.kisita.uza.ui.FavoritesFragment;
import com.kisita.uza.ui.OnSaleFragment;
import com.kisita.uza.ui.SettingsFragment;
import com.kisita.uza.ui.StartFragment;
import com.kisita.uza.utils.Settings;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;

import static com.kisita.uza.utils.Settings.isAllBillingInformationSet;
import static com.kisita.uza.utils.UzaFunctions.TRANSACTION_OK;

public class DrawerActivity extends CustomActivity
        implements NavigationView.OnNavigationItemSelectedListener,CheckoutFragment.OnCheckoutInteractionListener,StartFragment.OnHomeInteractionListener {

    private Fragment fragment = OnSaleFragment.newInstance();

    private Stack<MenuItem> mFragmentsStack = new Stack<>();
    private Stack<Integer> mNavItemsStack  = new Stack<>();

    private boolean mEmptyingStack = false;

    private TextView mToolbarTitle;

    private static final int REQUEST_CODE = 100;

    private String mAuthorization;

    private ArrayList<Data> mCommands;

    private String mAmount = "0";

    Toolbar mToolbar;

    NavigationView mNavigationView;

    private int mCheckedItem = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_title);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        View navigationHeader = mNavigationView.getHeaderView(0);
        ImageView userPicture = navigationHeader.findViewById(R.id.imageView);
        TextView  userName    = navigationHeader.findViewById(R.id.textView);

        fetchAuthorization();
        setFragment(setCurrentFragment(mNavigationView.getMenu().getItem(0)));

        setGreeting(userName,userPicture);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            try {
                mEmptyingStack = true;
                mFragmentsStack.pop();
                mNavItemsStack.pop();

                setFragment(setCurrentFragment(mFragmentsStack.peek()));
                mNavigationView.getMenu().getItem(mNavItemsStack.peek()).setChecked(true);

            }catch(EmptyStackException e){
                finish();
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        setFragment(setCurrentFragment(item));
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private void setGreeting(TextView greeting, final ImageView userPicture) {
        //Text
        String name      = Settings.getSharedPreferences(this).getString(getResources().getString(R.string.uza_billing_name),"");
        String message   = "";//getString(R.string.hello) + " " + name + " !"; //TODO
        greeting.setText(message);
        //Image
        if((LoginManager.getInstance() != null) && (Profile.getCurrentProfile() != null)){
            Uri pic = Profile.getCurrentProfile().getProfilePictureUri(400,400);

            Glide.with(this).load(pic.toString()).asBitmap().centerCrop().into(new BitmapImageViewTarget(userPicture) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    userPicture.setImageDrawable(circularBitmapDrawable);
                }
            });
            Log.i(TAG,"Pic uri is  : "+ pic.getAuthority() + " " + pic.getPath()+ " "+ pic.toString());
        }
    }

    private Fragment setCurrentFragment(MenuItem item){
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.nav_home:
                mCheckedItem  = HOME;
                fragment = StartFragment.newInstance();
                break;
            case R.id.nav_manage:
                mCheckedItem  = FILTERS;
                fragment = new SettingsFragment();
                break;
            case R.id.nav_new_item:
                mCheckedItem  = CART;
                fragment = new CheckoutFragment();
                break;
            case R.id.nav_commands:
                mCheckedItem = COMMANDS;
                fragment = CommandsFragment.newInstance();
                break;
            case R.id.nav_logout:
                mCheckedItem = LOGOUT;
                handlingLogout();
                break;
            case R.id.nav_email:
                mCheckedItem = LOGS;
                new LogReporting(this).collectAndSendLogs();
                break;
            default:
                fragment = OnSaleFragment.newInstance();
                break;
        }


        if((item.getItemId() != R.id.nav_logout ) && (item.getItemId() != R.id.nav_email)) {
            if(item.getItemId() == R.id.nav_home ) {
                mToolbarTitle.setText(getString(R.string.app_name));
            }else {
                mToolbarTitle.setText(item.getTitle());
            }
        }


        if(!mEmptyingStack) {
            mFragmentsStack.push(item);
            mNavItemsStack.push(mCheckedItem);
        }else{
            mEmptyingStack = false;
        }

        return fragment;
    }

    private void handlingLogout() {

        Log.i(TAG,"Handling logout");

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        logout();
                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.UzaAlertDialogTheme);
        builder.setMessage(getString(R.string.logout) + " " + getString(R.string.app_name) + " ?")
                .setPositiveButton(R.string.yes, dialogClickListener)
                .setNegativeButton(R.string.no, dialogClickListener).show();
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
        if(f != null) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                    .addToBackStack(null)
                    .replace(R.id.frame, f)
                    .commit();
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
        if(isAllBillingInformationSet(this)) {
            paymentRequest();
            Log.i(TAG, "Start transaction with amount = " + mAmount);
        }else{
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.UzaAlertDialogTheme);
            builder.setMessage(getString(R.string.Billing_info_missing))
                    .setPositiveButton(R.string.ok, dialogClickListener).show();
        }
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
            setFragment(setCurrentFragment(mNavigationView.getMenu().getItem(0)));
            // Update commands
            if(mBound) {
                mService.setCommandsState(mCommands);
            }else{
                //TODO Something wrong happened here
            }
        }
        hideProgressDialog();
    }

    void logout(){
        // Sign out Firebase
        FirebaseAuth.getInstance().signOut();
        // Sign out facebook if needed
        if(LoginManager.getInstance() != null)
            LoginManager.getInstance().logOut();

        finish();
    }

    @Override
    public void onHomeInteraction(int button) {
        if(button == ARTWORKS){
            setFragment(setCurrentFragment(mNavigationView.getMenu().getItem(ARTWORKS)));
            return;
        }

        if(button == ARTISTS){
            setFragment(setCurrentFragment(mNavigationView.getMenu().getItem(ARTISTS)));
            return;
        }
    }
}
