package com.kisita.uza.ui;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.kisita.uza.R;
import com.kisita.uza.custom.CustomFragment;
import com.kisita.uza.listerners.CommandsChildEventListener;
import com.kisita.uza.listerners.FavoritesChildEventListener;
import com.kisita.uza.model.Data;
import com.kisita.uza.utils.UzaCardAdapter;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class FavoritesFragment extends CustomFragment {
    final static String TAG = "### FavoritesFragment";
    /*The card adapter*/
    private UzaCardAdapter mCardadapter;
    /*The list of items*/
    private ArrayList<Data> itemsList;
    /*Firebase database*/
    private DatabaseReference mDatabase;
    /*likes event listener*/
    private FavoritesChildEventListener mChildEventListener;

    public FavoritesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_card, container, false);
        setupView(v);
        return v;
    }

    private void setupView(View v)
    {

        RecyclerView recList = (RecyclerView) v.findViewById(R.id.cardList);
        itemsList = new ArrayList<>();
        recList.setHasFixedSize(true);


        StaggeredGridLayoutManager llm = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        setHasOptionsMenu(true);
        recList.setLayoutManager(llm);
        mCardadapter = new UzaCardAdapter(this.getContext(),itemsList);
        recList.setAdapter(mCardadapter);
        loadData();
    }

    /**
     * Load  product data for displaying on the RecyclerView.
     */
    private void  loadData()
    {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);


        mChildEventListener = new FavoritesChildEventListener(itemsList,mCardadapter,mDatabase);
        Query itemsQuery = getQuery(mDatabase);
        itemsQuery.addChildEventListener(mChildEventListener);
    }

    public Query getQuery(DatabaseReference databaseReference) {
        return databaseReference.child("users-data").child(getUid()).child("likes");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mChildEventListener = null;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.findItem(R.id.favourite);
        item.setVisible(false);
    }
}
