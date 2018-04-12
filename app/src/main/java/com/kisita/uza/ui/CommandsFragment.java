package com.kisita.uza.ui;

import android.content.Context;
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
import com.kisita.uza.utils.UzaCommandAdapter;

import java.util.ArrayList;

import static com.kisita.uza.model.Data.COMMAND_DATA;
import static com.kisita.uza.model.Data.FAVOURITE_DATA;
import static com.kisita.uza.model.Data.ITEMS_COLUMNS;
import static com.kisita.uza.model.Data.ITEMS_COMMANDS_COLUMNS;


/*
 * A placeholder fragment containing a simple view.
 */
public class CommandsFragment extends CustomFragment implements  LoaderManager.LoaderCallbacks<Cursor> {
    final static String TAG = "### CommandsFragment";
    /*The card adapter*/
    private UzaCommandAdapter mCardadapter;
    /*The list of items*/
    private ArrayList<Data> itemsList;

    public CommandsFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DetailFragment.
     */
    public static CommandsFragment newInstance() {
        return new CommandsFragment();
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
        mCardadapter = new UzaCommandAdapter(this.getContext(),itemsList);
        recList.setAdapter(mCardadapter);
    }

    @Override
    public void onDetach() {
        Log.i(TAG,"Fragment detached. array size is " + itemsList.size());
        super.onDetach();
    }

    @Override
    public void onAttach(Context context) {
        Log.i(TAG,"Fragment attached. array size is ");
        super.onAttach(context);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.i(TAG,"Loader created");
        Uri PlacesUri = UzaContract.CommandsEntry.CONTENT_URI_COMMANDS;

        return new CursorLoader(getContext(),
                PlacesUri,
                ITEMS_COMMANDS_COLUMNS,
                "",
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        while (data.moveToNext()) {

            Data d = new Data(data,COMMAND_DATA);
            itemsList.add(d);
            mCardadapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
