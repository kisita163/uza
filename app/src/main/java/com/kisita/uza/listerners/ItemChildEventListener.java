package com.kisita.uza.listerners;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kisita.uza.model.Data;
import com.kisita.uza.utils.UzaCardAdapter;

import java.util.ArrayList;


/**
 * Created by Hugues on 22/04/2017.
 */
public class ItemChildEventListener implements ChildEventListener{

    private final long ONE_MEGABYTE = 1024 * 1024;
    private ArrayList<Data> mItemsList;
    private UzaCardAdapter mAdapter;
    private FirebaseStorage mStorage;
    private StorageReference mStorageRef;
    private Data data;

    public ItemChildEventListener(ArrayList<Data> itemsList, UzaCardAdapter adapter) {
        this.mAdapter = adapter;
        this.mItemsList = itemsList;
        mStorage = FirebaseStorage.getInstance();
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        String[] str = new String[]{
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
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        //TODO Update the changed item in the list (see database app)
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        //TODO Remove the removed item in the list (see database app)

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
