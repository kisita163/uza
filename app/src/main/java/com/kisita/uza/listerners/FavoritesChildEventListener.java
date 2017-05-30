package com.kisita.uza.listerners;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kisita.uza.R;
import com.kisita.uza.activities.UzaActivity;
import com.kisita.uza.model.Data;
import com.kisita.uza.utils.UzaCardAdapter;

import java.util.ArrayList;

/**
 * Created by HuguesKi on 26/05/2017.
 */

public class FavoritesChildEventListener implements ChildEventListener {
    final private String TAG = "### CommandsListener";
    private ArrayList<Data> mItemsList;
    private UzaCardAdapter mAdapter;
    private DatabaseReference mDatabase;
    private String[] str;

    private Data data;

    public FavoritesChildEventListener(ArrayList<Data> itemsList,UzaCardAdapter adapter,DatabaseReference reference) {
        this.mAdapter = adapter;
        this.mItemsList = itemsList;
        this.mDatabase = reference;
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Log.i(TAG, "Favorite added : " + dataSnapshot.getKey());
        final String commandKey = dataSnapshot.getKey();
        mDatabase.child("items")
                .child(dataSnapshot.getValue().toString())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists())
                            return;
                        str = new String[]{
                                dataSnapshot.getKey(),
                                dataSnapshot.child("name").getValue().toString(),
                                dataSnapshot.child("price").getValue().toString(),
                                dataSnapshot.child("currency").getValue().toString(),
                                dataSnapshot.child("brand").getValue().toString(),
                                dataSnapshot.child("description").getValue().toString(),
                                dataSnapshot.child("seller").getValue().toString(),
                                dataSnapshot.child("category").getValue().toString()};
                        data = new Data(str, commandKey);
                        mItemsList.add(data);
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        Log.i(TAG,"onChildAdded" );
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        Log.i(TAG, "onChildRemoved - " + dataSnapshot.getValue().toString());
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
