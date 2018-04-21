package com.kisita.uza.ui;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.kisita.uza.R;
import com.kisita.uza.custom.CustomFragment;
import com.kisita.uza.model.Command;
import com.kisita.uza.model.Data;
import com.kisita.uza.provider.UzaContract;

import static com.kisita.uza.model.Data.FAVOURITES_COLUMNS;
import static com.kisita.uza.utils.UzaFunctions.getCommandState;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CommandDetails.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CommandDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommandDetails extends CustomFragment{
    // the fragment initialization parameters
    private static final String TAG = "### CommandDetails";

    private static final String ITEM_DATA = "ITEM_DATA";

    protected OnFragmentInteractionListener mListener;

    private Command itemData;


    public CommandDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param data Item's data.
     * @return A new instance of fragment CommandDetails.
     */
    public static CommandDetails newInstance(Data data) {
        CommandDetails fragment = new CommandDetails();
        Bundle args = new Bundle();
        args.putSerializable(ITEM_DATA,data);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            itemData     = (Command) getArguments().getSerializable(ITEM_DATA);
        }
        //printKeyHash();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater,container,savedInstanceState);
        @SuppressLint("InflateParams")
        View v = inflater.inflate(R.layout.command_container, null);
        setHasOptionsMenu(true);
        return setupView(v);
    }

    private void onButtonPressed(int state) {
        if (mListener != null) {
            mListener.onStateChanged(itemData.getUser(),itemData.getCommandId(),state);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnCommandInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Setup the click & other events listeners for the view components of this
     * screen. You can add your logic for Binding the data to TextViews and
     * other views as per your need.
     */
    private View setupView(View v)
    {

        /*TextView  itemId            = v.findViewById(R.id.item_id_value);
        TextView  itemPrice         = v.findViewById(R.id.item_price_value);*/
        //
        TextView  commandId         = v.findViewById(R.id.command_id_value);
        final TextView  commandState      = v.findViewById(R.id.command_state_value);
        TextView  commandQty        = v.findViewById(R.id.command_quantity_value);
        //
        TextView  billingAddress    = v.findViewById(R.id.address);
        TextView  billingPostal     = v.findViewById(R.id.postal_code);
        TextView  billingProvince   = v.findViewById(R.id.province);
        TextView  billingCity       = v.findViewById(R.id.city);
        TextView  billingCountry    = v.findViewById(R.id.country);
        TextView  billingNumber     = v.findViewById(R.id.number);
        TextView  billingFirstName  = v.findViewById(R.id.first_name);
        TextView  billingLastName   = v.findViewById(R.id.last_name);


        commandId.setText(itemData.getCommandId());
        commandState.setText(getCommandState(itemData.getCommandState()));
        final String[] listItems = getResources().getStringArray(R.array.command_state);
        commandState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                mBuilder.setSingleChoiceItems(listItems, itemData.getCommandState() - 1 , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        commandState.setText(listItems[i]);
                        itemData.setCommandState(String.valueOf(i + 1));
                        //update database
                        Log.i(TAG,itemData.getUser());
                        Log.i(TAG,itemData.getCommandId());
                        onButtonPressed(i + 1);
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });


        commandQty.setText(itemData.getQuantity());


        billingAddress.setText(itemData.getAddress());
        billingPostal.setText(itemData.getPostal());
        billingCountry.setText(itemData.getCountry());
        billingFirstName.setText(itemData.getFirstName());
        billingCity.setText(itemData.getCity());
        billingLastName.setText(itemData.getLastName());
        billingNumber.setText(itemData.getNumber());
        billingProvince.setText(itemData.getProvince());

        //initPager(v);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                Toast.makeText(this.getContext(), "Unknown error occured", Toast.LENGTH_LONG).show();
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri PlacesUri = UzaContract.LikesEntry.CONTENT_URI;
        //Log.i(TAG,itemData.getData()[UID]);
        return new CursorLoader(getContext(),
                PlacesUri,
                FAVOURITES_COLUMNS,
                itemData.getItemId(),
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        while (data.moveToNext()) {
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

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
    public interface OnFragmentInteractionListener {
         void onStateChanged(String user,String commandId,int state);
    }
}
