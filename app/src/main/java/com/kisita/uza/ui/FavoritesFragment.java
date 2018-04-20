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
public class FavoritesFragment extends CustomFragment implements  LoaderManager.LoaderCallbacks<Cursor> {
    final static String TAG = "### FavoritesFragment";
    /*The card adapter*/
    private UzaCardAdapter mCardadapter;
    /*The list of items*/
    private ArrayList<Data> itemsList;

    public FavoritesFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_card, container, false);
        setupView(v);
        return v;
    }

    private void setupView(View v)
    {

        RecyclerView recList = v.findViewById(R.id.cardList);
        itemsList = new ArrayList<>();
        recList.setHasFixedSize(true);


        StaggeredGridLayoutManager llm = new StaggeredGridLayoutManager(1,
                StaggeredGridLayoutManager.VERTICAL);
        setHasOptionsMenu(true);
        recList.setLayoutManager(llm);
        mCardadapter = new UzaCardAdapter(this.getContext(),itemsList);
        recList.setAdapter(mCardadapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.i(TAG,"Loader created");
        Uri PlacesUri = UzaContract.LikesEntry.CONTENT_URI;

        return new CursorLoader(getContext(),
                PlacesUri,
                ITEMS_COLUMNS,
                "",
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        while (data.moveToNext()) {

            Data d = new Data(data);

            d.setFavourite(true);

            if(Arrays.binarySearch(itemsList.toArray(), d) < 0) // Is the new data already in my list?
                itemsList.add(d);
            mCardadapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
