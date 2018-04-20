package com.kisita.uza.ui;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.kisita.uza.R;
import com.kisita.uza.custom.CustomFragment;
import com.kisita.uza.model.Command;
import com.kisita.uza.model.Data;
import com.kisita.uza.provider.UzaContract;
import com.kisita.uza.utils.UzaPageAdapter;
import static com.kisita.uza.model.Data.FAVOURITES_COLUMNS;
import static com.kisita.uza.utils.UzaFunctions.getCommandState;
import static com.kisita.uza.utils.UzaFunctions.getCurrency;
import static com.kisita.uza.utils.UzaFunctions.setFormat;
import static com.kisita.uza.utils.UzaFunctions.setPrice;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends CustomFragment{
    // the fragment initialization parameters
    private static final String TAG = "### DetailFragment";

    private static final String ITEM_DATA = "ITEM_DATA";

    protected OnFragmentInteractionListener mListener;

    private String mCurrency;

    private Command itemData;


    public DetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param data Item's data.
     * @return A new instance of fragment DetailFragment.
     */
    public static DetailFragment newInstance(Data data) {
        DetailFragment fragment = new DetailFragment();
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
            mCurrency = getCurrency(getContext());
        }
        //printKeyHash();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater,container,savedInstanceState);
        @SuppressLint("InflateParams")
        View v = inflater.inflate(R.layout.fragment_detail, null);
        setHasOptionsMenu(true);
        return setupView(v);
    }

    private void onButtonPressed(String[] details, boolean update) {
        if (mListener != null) {
            mListener.onCommandChanged(details,update);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnNewArticleInteractionListener");
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

        TextView  itemId            = v.findViewById(R.id.item_id_value);
        TextView  itemPrice         = v.findViewById(R.id.item_price_value);
        //
        TextView  commandId         = v.findViewById(R.id.command_id_value);
        TextView  commandState      = v.findViewById(R.id.command_state_value);
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


        String price = setPrice(itemData.getCurrency(),itemData.getPrice(),getContext());
        String s = setFormat(price) + " "+mCurrency;
        itemPrice.setText(s);
        itemId.setText(itemData.getItemId());

        commandId.setText(itemData.getCommandId());
        commandState.setText(getCommandState(itemData.getCommandState()));
        commandQty.setText(itemData.getQuantity());


        billingAddress.setText(itemData.getAddress());
        billingPostal.setText(itemData.getPostal());
        billingCountry.setText(itemData.getCountry());
        billingFirstName.setText(itemData.getFirstName());
        billingCity.setText(itemData.getCity());
        billingLastName.setText(itemData.getLastName());
        billingNumber.setText(itemData.getNumber());
        billingProvince.setText(itemData.getProvince());

        initPager(v);
        return v;
    }

    /**
     * Inits the pager view.
     */
    private void initPager(View v)
    {
        /* The pager. */
        ViewPager pager = v.findViewById(R.id.pager);
        pager.setPageMargin(10);
        /* The view that hold dots. */
        LinearLayout vDots = v.findViewById(R.id.vDots);

        UzaPageAdapter adapter = new UzaPageAdapter(getContext(), itemData, vDots, null);

        pager.setOnPageChangeListener(adapter);

        pager.setAdapter(adapter);
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
         void onCommandChanged(String[] details, boolean update);
    }
}
