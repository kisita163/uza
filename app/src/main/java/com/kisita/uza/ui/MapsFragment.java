package com.kisita.uza.ui;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kisita.uza.R;
import com.kisita.uza.provider.UzaContract;

import static com.kisita.uza.model.Data.COMMANDS_COLUMNS;

/*
 * Use the {@link MapsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapsFragment extends ItemsFragment implements OnMapReadyCallback{
    // the fragment initialization parameters
    private static final String COMMAND_KEY = "key";

    private GoogleMap mMap;
    private MapView   mapView;
    private String    mKey;

    private TextView  mCommandId;
    private TextView  mCommandState;
    private TextView  mCommandTransporter;
    private TextView  mCommandContact;
    private TextView  mCommandQuantity;
    private TextView  mCommandColor;
    private TextView  mCommandSize;
    private TextView  mCommandComment;


    public MapsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param key Command key.
     * @return A new instance of fragment MapsFragment.
     */
    public static MapsFragment newInstance(String key) {
        MapsFragment fragment = new MapsFragment();
        Bundle args = new Bundle();
        args.putString(COMMAND_KEY, key);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
        if (getArguments() != null) {
            mKey = getArguments().getString(COMMAND_KEY);
        }
    }

    @Override
    void loadData() {
        if (getLoaderManager().getLoader(0) == null){
            getLoaderManager().initLoader(0, null, this);
        }else{
            getLoaderManager().restartLoader(0,null,this);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_maps, container, false);
        mapView = v.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        mCommandId          = v.findViewById(R.id.command_id);
        mCommandState       = v.findViewById(R.id.command_state);
        mCommandTransporter = v.findViewById(R.id.command_transporter);
        mCommandContact     = v.findViewById(R.id.command_contact);
        mCommandQuantity    = v.findViewById(R.id.command_quantity);
        mCommandColor       = v.findViewById(R.id.command_color);
        mCommandSize        = v.findViewById(R.id.command_size);
        mCommandComment     = v.findViewById(R.id.command_comment);

        return  v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.i(TAG,"Loader created");
        Uri PlacesUri = UzaContract.CommandsEntry.CONTENT_URI_COMMANDS;

        return new CursorLoader(getContext(),
                PlacesUri,
                COMMANDS_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //Log.i(TAG,data.getCount() + "");
        while (data.moveToNext()) {
            if(data.getString(0).equalsIgnoreCase(mKey)){
                mCommandId.setText(mKey);
                mCommandQuantity.setText(data.getString(5));
                mCommandState.setText(commandSate(data.getString(6)));

                if(data.getString(2).equalsIgnoreCase("")){ //size
                    mCommandSize.setText(R.string.none);
                }else{
                    mCommandSize.setText(data.getString(2));
                }

                if(data.getString(3).equalsIgnoreCase("")){ //color
                    mCommandColor.setText(R.string.none);
                }else{
                    mCommandColor.setText(data.getString(3));
                }

                if(data.getString(4).equalsIgnoreCase("")){ //comment
                    mCommandComment.setText(R.string.none);
                }else{
                    mCommandComment.setText(data.getString(4));
                }
            }
            //Log.i(TAG,data.getString(0) + " " + mKey);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i(TAG,"onMapReady");
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private String commandSate(String state){
        String s;

        switch (state){
            case "1" :
                s = "Processing";
                break;
            case "2":
                s = "Sent";
                break;
            case "3":
                s = "Delivered";
                break;
            default:
                s = "Unknown state";
                break;
        }

        return s;
    }
}
