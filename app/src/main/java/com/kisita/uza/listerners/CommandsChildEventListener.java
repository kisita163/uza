package com.kisita.uza.listerners;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kisita.uza.model.Data;
import com.kisita.uza.utils.UzaCardAdapter;

import java.util.ArrayList;

/**
 * Created by Hugues on 30/04/2017.
 */
public class CommandsChildEventListener implements ChildEventListener {
    final private String TAG = "### CommandsListener";
    private final long ONE_MEGABYTE = 1024 * 1024;
    private ArrayList<Data> mItemsList;
    private UzaCardAdapter mAdapter;
    private  DatabaseReference mDatabase;
    private String[] str;
    private FirebaseStorage mStorage;
    private StorageReference mStorageRef;
    private Data data;

    public CommandsChildEventListener(ArrayList<Data> itemsList,UzaCardAdapter adapter,DatabaseReference reference) {
        this.mAdapter = adapter;
        this.mItemsList = itemsList;
        this.mDatabase = reference;
        mStorage = FirebaseStorage.getInstance();
    }
    //TODO remove item from commands when it is removed from item list
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Log.i(TAG, "Command added : " + dataSnapshot.getKey());
        mDatabase.child("items")
                .child(dataSnapshot.getValue().toString())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.exists())
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
                        data = new Data(str);
                        mStorageRef = mStorage.getReferenceFromUrl("gs://glam-afc14.appspot.com/" + dataSnapshot.getKey() + "/android.png");
                        mStorageRef.child(dataSnapshot.getKey() + "/android.png");
                        mStorageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new PictureEventListener(data, mAdapter, dataSnapshot.getKey()));
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
        Log.i(TAG,"onChildAdded" );
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
