package com.kisita.uza.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.kisita.uza.R;
import com.kisita.uza.listerners.CommandsChildEventListener;
import com.kisita.uza.listerners.ItemChildEventListener;

/**
 * Use the {@link CommandsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommandsFragment extends ItemsFragment {

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
        Log.i(TAG,"Loading data in here ......");

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);

        final Query itemsQuery = getQuery(mDatabase);

        CommandsChildEventListener    mChildEventListener = new CommandsChildEventListener(getItemsList(), getCardAdapter(), mDatabase, this.getActivity(),true);
        itemsQuery.addChildEventListener(mChildEventListener);
    }

    @Override
    Query getQuery(DatabaseReference databaseReference) {
        return databaseReference.child("users-data").child(getUid()).child("commands");
    }
}
