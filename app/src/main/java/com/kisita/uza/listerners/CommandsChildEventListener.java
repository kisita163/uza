package com.kisita.uza.listerners;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kisita.uza.R;
import com.kisita.uza.model.Data;
import com.kisita.uza.utils.UzaCardAdapter;

import java.util.ArrayList;

/**
 * Created by Hugues on 30/04/2017.
 */
public class CommandsChildEventListener implements ChildEventListener {
    final private String TAG = "### CheckEventListener";
    private ArrayList<Data> mItemsList;
    private UzaCardAdapter mAdapter;
    private  DatabaseReference mDatabase;
    private DataSnapshot query;

    public CommandsChildEventListener(ArrayList<Data> itemsList,UzaCardAdapter adapter,DatabaseReference reference) {
        this.mAdapter = adapter;
        this.mItemsList = itemsList;
        this.mDatabase = reference;
    }
    //TODO remove item from commands when it is removed from item list
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Log.i(TAG, "onChildAdded ");
        mDatabase.child("items")
                .child(dataSnapshot.getValue().toString())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.exists())
                            return;
                        Log.i(TAG, dataSnapshot.getValue().toString());
                        mItemsList.add(new Data(new String[]{
                                dataSnapshot.getKey(),
                                dataSnapshot.child("name").getValue().toString(),
                                dataSnapshot.child("price").getValue().toString(),
                                dataSnapshot.child("currency").getValue().toString(),
                                dataSnapshot.child("brand").getValue().toString(),
                                dataSnapshot.child("description").getValue().toString(),
                                dataSnapshot.child("seller").getValue().toString(),
                                dataSnapshot.child("category").getValue().toString()},
                                //TODO                               dataSnapshot.child("pictures").getValue().toString()
                                new int[]{R.drawable.on_sale_item2}));
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
        Log.i(TAG,"onChildAdded" );
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
