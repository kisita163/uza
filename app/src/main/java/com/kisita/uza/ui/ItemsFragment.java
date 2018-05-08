package com.kisita.uza.ui;

import android.annotation.SuppressLint;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kisita.uza.R;
import com.kisita.uza.custom.CustomFragment;
import com.kisita.uza.model.Data;
import com.kisita.uza.utils.UzaCardAdapter;

import java.util.ArrayList;


/**
 * The Class OnSaleFragment is the fragment that shows the products in GridView.
 */
public abstract class ItemsFragment extends CustomFragment
{
    final static String TAG = "### ItemsFragment";
    final static String QUERY         = "QUERY";
    private String mQuery;
    private android.support.design.widget.FloatingActionButton choicesButton;


    private OnItemFragmentInteractionListener mListener;

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */

    public ItemsFragment() {
        // Required empty public constructor
    }

    @SuppressLint({ "InflateParams", "InlinedApi" })
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_onsale, null);
        setHasOptionsMenu(true);
        setupView(v);
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mQuery                = getArguments().getString(QUERY);
        }
    }
    /**
     * Setup the view components for this fragment. You write your code for
     * initializing the views, setting the adapters, touch and click listeners
     * etc.
     *
     * @param v
     *            the base view of fragment
     */
    protected void setupView(View v)
    {

        RecyclerView recList = v.findViewById(R.id.cardList);

        recList.setHasFixedSize(true);


        StaggeredGridLayoutManager llm = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);

        recList.setLayoutManager(llm);
        mCardAdapter = new UzaCardAdapter(this.getContext(),itemsList);
        recList.setAdapter(mCardAdapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;

    }

    public void onReloadRequest(String title) {
        Log.i(TAG, "onReloadRequest");
        if (mListener != null) {
            mListener.onItemFragmentInteraction(title);
        }
    }

    public void showCommandDetails(String key){
        if (mListener != null) {
            mListener.onCommandSelectedInteraction(key);
        }
    }

    public ArrayList<Data> getItemsList() {
        return itemsList;
    }

    public String getQuery() {
        return mQuery;
    }

    public UzaCardAdapter getCardAdapter() {
        return mCardAdapter;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnItemFragmentInteractionListener) {

            mListener = (OnItemFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnItemFragmentInteractionListener");
        }
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
    public interface OnItemFragmentInteractionListener {
        void onItemFragmentInteraction(String title);
        void onCommandSelectedInteraction(String key);
    }

    @Override
    public void notifyChanges(ArrayList<Data> data) {
        if(mCardAdapter != null) {
            mCardAdapter.resetItemsList(data);
            mCardAdapter.notifyDataSetChanged();
        }
    }
}
