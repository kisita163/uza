package com.kisita.uza.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.kisita.uza.ui.BlankFragment;
import com.kisita.uza.ui.CheckoutFragment;
import com.kisita.uza.ui.CommandsFragment;
import com.kisita.uza.ui.OnSaleFragment;
import com.kisita.uza.ui.SettingsFragment;
import com.kisita.uza.ui.StartFragment;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;

import static com.kisita.uza.custom.CustomActivity.BikekoMenu.ARTWORKS;
import static com.kisita.uza.custom.CustomActivity.BikekoMenu.BILLING;
import static com.kisita.uza.custom.CustomActivity.BikekoMenu.CART;
import static com.kisita.uza.custom.CustomActivity.BikekoMenu.COMMANDS;
import static com.kisita.uza.custom.CustomActivity.BikekoMenu.FAVOURITES;
import static com.kisita.uza.custom.CustomActivity.BikekoMenu.FILTERS;
import static com.kisita.uza.custom.CustomActivity.BikekoMenu.HOME;
import static com.kisita.uza.custom.CustomActivity.BikekoMenu.LOGOUT;
import static com.kisita.uza.custom.CustomActivity.BikekoMenu.LOGS;
import static com.kisita.uza.ui.SettingsFragment.MAX_PRICE_VALUE;
import static com.kisita.uza.utils.Settings.isAllBillingInformationSet;
import static com.kisita.uza.utils.UzaFunctions.TRANSACTION_OK;
import static com.kisita.uza.utils.UzaFunctions.getPriceDouble;
import static com.kisita.uza.utils.UzaFunctions.setPrice;

public class DrawerActivity extends CustomActivity
        implements NavigationView.OnNavigationItemSelectedListener,CheckoutFragment.OnCheckoutInteractionListener,StartFragment.OnHomeInteractionListener {

    private static final String CURRENT_FRAGMENT_ID = "current_fragment_id";

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

        setGreeting(userName,userPicture);
        setFragment(setCurrentFragment(mNavigationView.getMenu().getItem(mCheckedItem)));
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
        //String name      = Settings.getSharedPreferences(this).getString(getResources().getString(R.string.uza_billing_name),"");
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
        Fragment fragment = BlankFragment.newInstance(CART.ordinal());
        ArrayList<Data> data;
        switch (item.getItemId()) {
            case R.id.nav_artworks:
                mCheckedItem  = ARTWORKS.ordinal();
                data = getFilteredItems();
                if(data.size() > 0)
                    fragment = OnSaleFragment.newInstance(data);
                break;
            /*case R.id.nav_artists:
                mCheckedItem  = ARTISTS;
                fragment = OnSaleFragment.newInstance();
                break;*/
            case R.id.nav_home:
                mCheckedItem  = HOME.ordinal();
                fragment = StartFragment.newInstance();
                break;
            case R.id.nav_manage:
                mCheckedItem  = FILTERS.ordinal();
                fragment = new SettingsFragment();
                break;
            case R.id.nav_checkout:
                mCheckedItem  = CART.ordinal();
                data = getInCartItems();
                if(data.size() > 0)
                    fragment = CheckoutFragment.newInstance(data);
                break;
            case R.id.nav_favourites:
                mCheckedItem = FAVOURITES.ordinal();
                data = getFavouritesItems();
                if(data.size() > 0)
                    fragment = OnSaleFragment.newInstance(data);
                break;
            case R.id.nav_commands:
                mCheckedItem = COMMANDS.ordinal();
                data = getCommandsItems();
                if(data.size() > 0)
                    fragment = CommandsFragment.newInstance(data);
                break;
            case R.id.nav_billing_info:
                mCheckedItem = BILLING.ordinal();
                fragment = BillingFragment.newInstance();
                break;
            case R.id.nav_logout:
                mCheckedItem = LOGOUT.ordinal();
                fragment = null;
                handlingLogout();
                break;
            case R.id.nav_email:
                mCheckedItem = LOGS.ordinal();
                fragment = null;
                new LogReporting(this).collectAndSendLogs();
                break;
            default:
                fragment = OnSaleFragment.newInstance(getFilteredItems());
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
        if(f != null) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                    .addToBackStack(mFragmentsStack.peek().toString())
                    .replace(R.id.frame, f,mFragmentsStack.peek().toString())
                    .commit();
        }
        Log.i(TAG,"Current fragment tag is  : "+ mFragmentsStack.peek().toString());
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
        // Go to LoginActivity
        Intent startLogin = new Intent(DrawerActivity.this,LoginActivity.class);
        startLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(startLogin);
        finish();

    }

    @Override
    public void onHomeInteraction(int button) {
        if(button == ARTWORKS.ordinal()){
            setFragment(setCurrentFragment(mNavigationView.getMenu().getItem(ARTWORKS.ordinal())));
        }

        /*if(button == ARTISTS){
            setFragment(setCurrentFragment(mNavigationView.getMenu().getItem(ARTISTS)));
            return;
        }*/
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENT_FRAGMENT_ID,mCheckedItem);
        super.onSaveInstanceState(outState);
    }


    protected void notifyChanges(){
        Fragment f = getSupportFragmentManager().findFragmentByTag(mFragmentsStack.peek().toString());
        setCartItemNumber();
        if(f != null) {
            Log.i(TAG, f.getTag());
            if(f instanceof OnSaleFragment)
                ((OnSaleFragment) f).notifyChanges(getFilteredItems());
            if(f instanceof CommandsFragment)
                ((CommandsFragment) f).notifyChanges(getCommandsItems());
        }
    }

    /**
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are <em>not</em> resumed.  This means
     * that in some cases the previous state may still be saved, not allowing
     * fragment transactions that modify the state.  To correctly interact
     * with fragments in their proper state, you should instead override
     * {@link #onResumeFragments()}.
     */
    @Override
    protected void onResume() {
        Log.i(TAG,"Resume items here" + itemsList.size());
        notifyChanges();
        super.onResume();
    }

    private ArrayList<Data> getInCartItems(){
        ArrayList<Data> d = new ArrayList<>();

        for(Data data : itemsList){
            if(data.isInCart())
                d.add(data);
        }
        return d;
    }

    private ArrayList<Data> getFavouritesItems(){
        ArrayList<Data> d = new ArrayList<>();

        for(Data data : itemsList){
            if(data.isFavourite())
                d.add(data);
        }
        return d;
    }

    private ArrayList<Data> getCommandsItems(){
        ArrayList<Data> d = new ArrayList<>();

        for(Data data : itemsList){
            if(data.isCommand())
                d.add(data);
        }
        return d;
    }


    private ArrayList<Data> getFilteredItems(){
        ArrayList<Data> d = new ArrayList<>();

        for(Data data : itemsList)
            if(filterPrice(data))
                if(filterType(data)) {
                    d.add(data);
                }

        return d;
    }

    private boolean filterPrice(Data data) {
        SharedPreferences sharedPref = getSharedPreferences(getResources().getString(R.string.uza_keys), Context.MODE_PRIVATE);

        String p = setPrice(data.getCurrency(),data.getPrice(),this); // Getting the price according to the currency

        double price = getPriceDouble(p);

        double minPrice = sharedPref.getLong("priceMinValue",0);
        double maxPrice = sharedPref.getLong("priceMaxValue",MAX_PRICE_VALUE);

        if(price >= 0){
            if(price < maxPrice && price >= minPrice){
                return true;
            }else if(maxPrice == MAX_PRICE_VALUE && price >= maxPrice){
                return true; // +MAX_PRICE_VALUE
            }else{
                Log.i(TAG,"Price not in the selected range");
                return false;
            }
        }
        Log.i(TAG,"Bad formatted price");
        return false;
    }

    private boolean filterType(Data data) {
        SharedPreferences sharedPref =getSharedPreferences(getResources().getString(R.string.uza_keys), Context.MODE_PRIVATE);


        if(data.getType().equalsIgnoreCase(getString(R.string.painting_key)))
            return sharedPref.getBoolean(getString(R.string.painting_key),true);

        if(data.getType().equalsIgnoreCase(getString(R.string.photography_key)))
            return sharedPref.getBoolean(getString(R.string.photography_key),true);

        if(data.getType().equalsIgnoreCase(getString(R.string.drawing_key)))
            return sharedPref.getBoolean(getString(R.string.drawing_key),true);

        if(data.getType().equalsIgnoreCase(getString(R.string.sculpture_key)))
            return sharedPref.getBoolean(getString(R.string.sculpture_key),true);

        if(data.getType().equalsIgnoreCase(getString(R.string.textile_key)))
            return sharedPref.getBoolean(getString(R.string.textile_key),true);

        if(data.getType().equalsIgnoreCase(getString(R.string.literature_key)))
            return sharedPref.getBoolean(getString(R.string.literature_key),true);

        return false;
    }

    public void setCartItemNumber(){
        String num = String.valueOf(getInCartItems().size());
        mNavigationView.getMenu().getItem(CART.ordinal()).setTitle(getString(R.string.cart) + " ( "+num+" )");
    }

    public void setCartItemNumber(int num){
        String s = getString(R.string.cart) + " ( "+num+" )";
        mToolbarTitle.setText(s);
        mNavigationView.getMenu().getItem(CART.ordinal()).setTitle(s);
    }
}
