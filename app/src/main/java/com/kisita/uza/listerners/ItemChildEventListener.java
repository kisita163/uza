package com.kisita.uza.listerners;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.kisita.uza.R;
import com.kisita.uza.utils.UzaCardAdapter;
import com.kisita.uza.model.Data;

import java.util.ArrayList;


/**
 * Created by Hugues on 22/04/2017.
 */
public class ItemChildEventListener implements ChildEventListener{

    private ArrayList<Data> mItemsList;
    private UzaCardAdapter mAdapter;

    public ItemChildEventListener(ArrayList<Data> itemsList, UzaCardAdapter adapter) {
        this.mAdapter = adapter;
        this.mItemsList = itemsList;
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        mItemsList.add(new Data(new String[]{dataSnapshot.child("name").getValue().toString(),
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
