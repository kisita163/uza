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
 * {@link ItemDetails.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ItemDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ItemDetails extends CustomFragment{
    // the fragment initialization parameters
    private static final String TAG = "### CommandDetails";

    private static final String ITEM_DATA = "ITEM_DATA";

    protected OnFragmentInteractionListener mListener;

    private Command itemData;


    public ItemDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param data Item's data.
     * @return A new instance of fragment CommandDetails.
     */
    public static ItemDetails newInstance(Data data) {
        ItemDetails fragment = new ItemDetails();
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
        View v = inflater.inflate(R.layout.item_container, null);
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
        TextView  itemAvailability  = v.findViewById(R.id.item_availability);
        TextView  itemDescription   = v.findViewById(R.id.item_description);
        //
        String price = setPrice(itemData.getCurrency(),itemData.getPrice(),getContext());
        String s = setFormat(price) + " "+ getCurrency(getContext());
        itemPrice.setText(s);
        itemId.setText(itemData.getItemId());
        itemDescription.setText(itemData.getDescription());

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
        //pager.setPageMargin(10);
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
