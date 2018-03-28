package com.kisita.uza.ui;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.kisita.uza.R;
import com.kisita.uza.custom.CustomFragment;
import com.kisita.uza.model.UzaListItem;
import com.kisita.uza.ui.FixedContents.SettingsContent;
import com.kisita.uza.utils.UzaListAdapter;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ChoicesFragment extends CustomFragment {

    final static String QUERY = "QUERY";

    final static  String TAG  = "### ChoicesFragment";

    public ChoicesFragment() {
    }


    public static ChoicesFragment newInstance(String query) {
        ChoicesFragment fragment = new ChoicesFragment();
        Bundle args = new Bundle();
        args.putString(QUERY, query);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_choices, container, false);

        setHasOptionsMenu(true);
        // Set the adapter
        Context context = view.getContext();
        List<UzaListItem> list;
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.storeList);
        recyclerView.setLayoutManager(new GridLayoutManager(context,1));

        DividerItemDecoration dividerVertical = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);

        recyclerView.addItemDecoration(dividerVertical);
        //Log.i("ChoiceActiviyFragment","listener passed to adapter");

        list = SettingsContent.ITEMS;

        if(list != null)
            recyclerView.setAdapter(new UzaListAdapter(context, list,this));

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    public void onChoiceMadeListener(String name){

        if(name.equalsIgnoreCase(getString(R.string.title_notifications))){
            handleNotifications();
        }

        if(name.equalsIgnoreCase(getString(R.string.currency))){
            handleCurrency();
        }
        if(name.equalsIgnoreCase(getString(R.string.payment_mthod))){
            handlePaymentMethod();
        }
        if(name.equalsIgnoreCase(getString(R.string.billing_information))){
            handleBillingInfo();
        }
        if(name.equalsIgnoreCase(getString(R.string.about_us))){
            handleAboutUs();
        }

        if(name.equalsIgnoreCase(getString(R.string.action_logout))){
            handleLogout();
        }
    }

    private void handleLogout() {
        Log.i(TAG,"Handling logout");
        // Sign out Firebase
        FirebaseAuth.getInstance().signOut();
        // Sign out facebook if needed
        if(LoginManager.getInstance() != null)
            LoginManager.getInstance().logOut();

        getActivity().finish();
    }

    private void handleAboutUs() {
        Log.i(TAG,"Handling about us");
    }

    private void handleBillingInfo() {
        Log.i(TAG,"Handling billing info");
    }

    private void handlePaymentMethod() {
        Log.i(TAG,"Handling payment method");
    }

    private void handleCurrency() {
        Log.i(TAG,"Handling currency");
    }

    private void handleNotifications() {
        Log.i(TAG,"Handling notifications");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
}
