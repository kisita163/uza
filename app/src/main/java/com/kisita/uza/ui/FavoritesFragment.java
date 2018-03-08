package com.kisita.uza.ui;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import static com.kisita.uza.model.Data.FAVOURITES_COLUMNS;
import static com.kisita.uza.model.Data.ITEMS_COLUMNS;
import static com.kisita.uza.model.Data.UZA.KEY;
import static com.kisita.uza.utils.UzaFunctions.getPicturesUrls;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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
    public void onDetach() {
        super.onDetach();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.findItem(R.id.favourite);
        item.setVisible(false);
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
        Log.i(TAG,"result "+ data.getCount());
        while (data.moveToNext()) {
            String  []  rowdata  =  {
                    data.getString(Data.UZA.UID),
                    data.getString(Data.UZA.NAME),
                    data.getString(Data.UZA.PRICE),
                    data.getString(Data.UZA.CURRENCY),
                    data.getString(Data.UZA.BRAND),
                    data.getString(Data.UZA.DESCRIPTION),
                    data.getString(Data.UZA.SELLER),
                    data.getString(Data.UZA.CATEGORY),
                    data.getString(Data.UZA.TYPE),
                    data.getString(Data.UZA.COLOR),
                    data.getString(Data.UZA.SIZE),
                    "",
                    data.getString(Data.UZA.WEIGHT),
                    data.getString(Data.UZA.URL),
                    //data.getString(Data.UZA.QUANTITY),
                    //data.getString(KEY)
            };
            Data d = new Data(rowdata,
                    getPicturesUrls(data.getString(Data.UZA.PICTURES))
            );
            itemsList.add(d);
            mCardadapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
