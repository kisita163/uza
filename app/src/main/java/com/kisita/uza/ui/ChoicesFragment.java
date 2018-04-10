package com.kisita.uza.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.Loader;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.kisita.uza.R;
import com.kisita.uza.activities.UzaActivity;
import com.kisita.uza.custom.CustomFragment;
import com.kisita.uza.internal.LogReporting;
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

    private UzaListAdapter mListAdapter;

    private SharedPreferences sharedPref;

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
        sharedPref = getActivity().getSharedPreferences(getResources().getString(R.string.uza_keys), Context.MODE_PRIVATE);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_choices, container, false);

        setHasOptionsMenu(true);
        // Set the adapter
        Context context = view.getContext();
        List<UzaListItem> list;
        RecyclerView recyclerView = view.findViewById(R.id.storeList);

        recyclerView.setLayoutManager(new GridLayoutManager(context,1));

        DividerItemDecoration dividerVertical = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);

        recyclerView.addItemDecoration(dividerVertical);
        //Log.i("ChoiceActiviyFragment","listener passed to adapter");

        list = SettingsContent.ITEMS;

        mListAdapter = new UzaListAdapter(context, list,this);

        if(list != null)
            recyclerView.setAdapter(mListAdapter);

        TextView greeting        = view.findViewById(R.id.greeting);
        ImageView userPicture    = view.findViewById(R.id.user_picture);

        setGreeting(greeting,userPicture);

        return view;
    }

    private void setGreeting(TextView greeting, final ImageView userPicture) {
        //Text
        String name      = sharedPref.getString(getResources().getString(R.string.uza_billing_name),"");
        String message  = getString(R.string.hello) + " " + name + "!";
        greeting.setText(message);
        //Image
        if((LoginManager.getInstance() != null) && (Profile.getCurrentProfile() != null)){
            Uri pic = Profile.getCurrentProfile().getProfilePictureUri(400,400);

            Glide.with(getContext()).load(pic.toString()).asBitmap().centerCrop().into(new BitmapImageViewTarget(userPicture) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getContext().getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    userPicture.setImageDrawable(circularBitmapDrawable);
                }
            });
            Log.i(TAG,"Pic uri is  : "+ pic.getAuthority() + " " + pic.getPath()+ " "+ pic.toString());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }


    public void onChoiceMadeListener(String name){

        if(name.equalsIgnoreCase(getString(R.string.currency))){
            handleCurrency();
        }
        if(name.equalsIgnoreCase(getString(R.string.billing_information))){
            handleBillingInfo();
        }

        if(name.equalsIgnoreCase(getString(R.string.commands))){
            handleCommands();
        }
        if(name.equalsIgnoreCase(getString(R.string.about_us))){
            handleAboutUs();
        }

        if(name.equalsIgnoreCase(getString(R.string.action_logout))){
            handleLogout();
        }

        if(name.equalsIgnoreCase(getString(R.string.send_logs))){
            new LogReporting(getContext()).collectAndSendLogs();
        }
    }

    private void handleCommands() {
        Log.i(TAG,"Handling billing info");
        Intent intent = new Intent(getContext(), UzaActivity.class);
        intent.putExtra("fragment", 0);
        getContext().startActivity(intent);
    }

    private void handleLogout() {
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

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.UzaAlertDialogTheme);
        builder.setMessage(getString(R.string.logout) + " " + getString(R.string.app_name) + " ?")
                .setPositiveButton(R.string.yes, dialogClickListener)
                .setNegativeButton(R.string.no, dialogClickListener).show();
    }

    private void handleAboutUs() {
        Log.i(TAG,"Handling about us");
    }

    private void handleBillingInfo() {
        Log.i(TAG,"Handling billing info");
        Intent intent = new Intent(getContext(), UzaActivity.class);
        intent.putExtra("fragment", 4);
        getContext().startActivity(intent);
    }

    private void handlePaymentMethod() {
        Log.i(TAG,"Handling payment method");
    }

    private void handleCurrency() {
        Log.i(TAG,"Handling currency");

        final SharedPreferences.Editor editor = sharedPref.edit();
        int pos     = sharedPref.getInt(getResources().getString(R.string.uza_currency_position), 0);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.UzaAlertDialogTheme);
        // Set the dialog title
        builder
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setSingleChoiceItems(R.array.currency,pos,null)
                // Set the action buttons
                .setPositiveButton(R.string.select, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();

                        Log.i(TAG,"Selected currency is : " + selectedPosition);

                        editor.putString(getString(R.string.uza_currency), getContext().getResources().getStringArray(R.array.currency)[selectedPosition]);
                        editor.putInt(getString(R.string.uza_currency_position),selectedPosition);
                        editor.apply();

                        mListAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        builder.show();
    }

    private void handleNotifications() {
        Log.i(TAG,"Handling notifications");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    void logout(){
        // Sign out Firebase
        FirebaseAuth.getInstance().signOut();
        // Sign out facebook if needed
        if(LoginManager.getInstance() != null)
            LoginManager.getInstance().logOut();

        getActivity().finish();
    }
}
