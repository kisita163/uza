package com.kisita.uza.ui;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.kisita.uza.R;
import com.kisita.uza.custom.CustomFragment;
import com.kisita.uza.model.Data;
import com.kisita.uza.provider.UzaContract;
import com.kisita.uza.utils.UzaCardAdapter;
import java.util.ArrayList;
import java.util.Arrays;
import static com.kisita.uza.model.Data.FAVOURITE_DATA;
import static com.kisita.uza.model.Data.ITEMS_COLUMNS;



/*
 * A placeholder fragment containing a simple view.
 */
public class FavoritesFragment extends CustomFragment {
    final static String TAG = "### FavoritesFragment";
    /*The list of items*/
    private ArrayList<Data> itemsList;

    public FavoritesFragment() {
    }

    public static FavoritesFragment newInstance(ArrayList<Data> itemsList) {
        FavoritesFragment fragment = new FavoritesFragment();
        Bundle args = new Bundle();
        args.putSerializable(ITEMS,itemsList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            itemsList = (ArrayList<Data>) getArguments().getSerializable(ITEMS);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_card, container, false);
        setupView(v);
        return v;
    }

    @Override
    protected void notifyChanges(ArrayList<Data> data) {
        if(mCardAdapter != null) {
            mCardAdapter.resetItemsList(data);
            mCardAdapter.notifyDataSetChanged();
        }
    }

    private void setupView(View v)
    {

        RecyclerView recList = v.findViewById(R.id.cardList);
        recList.setHasFixedSize(true);


        StaggeredGridLayoutManager llm = new StaggeredGridLayoutManager(1,
                StaggeredGridLayoutManager.VERTICAL);
        setHasOptionsMenu(true);
        recList.setLayoutManager(llm);
        mCardAdapter = new UzaCardAdapter(this.getContext(),itemsList);
        recList.setAdapter(mCardAdapter);
    }
}
