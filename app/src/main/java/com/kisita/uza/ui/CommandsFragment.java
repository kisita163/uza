package com.kisita.uza.ui;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.kisita.uza.R;
import com.kisita.uza.model.Data;
import com.kisita.uza.provider.UzaContract;

import java.util.ArrayList;
import java.util.Arrays;

import static com.kisita.uza.model.Data.ITEMS_COLUMNS;
import static com.kisita.uza.model.Data.ITEMS_COMMANDS_COLUMNS;
import static com.kisita.uza.utils.UzaFunctions.getPicturesUrls;


/**
 * Use the {@link CommandsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommandsFragment extends ItemsFragment implements  LoaderManager.LoaderCallbacks<Cursor> {

    public static CommandsFragment newInstance(String query , boolean choiceButton) {
        Log.i(TAG,"Creating fragment here ......");
        CommandsFragment fragment = new CommandsFragment();
        Bundle args = new Bundle();
        args.putString(QUERY, query);
        args.putBoolean(CHOICE_BUTTON, choiceButton);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    void loadData() {
        //Log.i(TAG,"Loading data in here ......");
        //getCardAdapter().setAdapterListener(this);
        if (getLoaderManager().getLoader(0) == null){
            getLoaderManager().initLoader(0, null, this);
        }else{
            getLoaderManager().restartLoader(0,null,this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.i(TAG,"Loader created");
        Uri PlacesUri = UzaContract.CommandsEntry.CONTENT_URI_COMMANDS;

        return new CursorLoader(getContext(),
                PlacesUri,
                ITEMS_COMMANDS_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.i(TAG,"Load finished");
        while (data.moveToNext()) {
            String  []  rowData  =  {
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
                              "",
                              data.getString(Data.UZA.KEY),

            };
            Data d = new Data(rowData,
                              getPicturesUrls(data.getString(Data.UZA.PICTURES))
            );
            getItemsList().add(d);
            getCardAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.findItem(R.id.action_commands);
        item.setVisible(false);
    }

    public void onCommandSelected(String key)
    {
        //Log.i(TAG,"****** command selected " + key);
        showCommandDetails(key);
    }
}
