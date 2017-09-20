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

/*
 * Created by HuguesKi on 26/05/2017.
 */

public class FavoritesChildEventListener implements ChildEventListener {
    final private String TAG = "### CommandsListener";
    private ArrayList<Data> mItemsList;
    private UzaCardAdapter mAdapter;
    private DatabaseReference mDatabase;

    private Data data;

    public FavoritesChildEventListener(ArrayList<Data> itemsList,UzaCardAdapter adapter,DatabaseReference reference) {
        this.mAdapter = adapter;
        this.mItemsList = itemsList;
        this.mDatabase = reference;
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        //Log.i(TAG, "Favorite added : " + dataSnapshot.getKey());
        final String commandKey = dataSnapshot.getKey();
        mDatabase.child("items")
                .child(dataSnapshot.getValue().toString())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists())
                            return;
                        ArrayList<String> articleData = new ArrayList<>();
                        ArrayList<String> pictures = new ArrayList<>();
                        articleData.add(dataSnapshot.getKey());

                        if(dataSnapshot.child("name").getValue() != null){
                            articleData.add(dataSnapshot.child("name").getValue().toString());
                        }else{
                            articleData.add("");
                        }

                        if(dataSnapshot.child("price").getValue() != null){
                            articleData.add(dataSnapshot.child("price").getValue().toString());
                        }else{
                            articleData.add("");
                        }

                        if(dataSnapshot.child("currency").getValue() != null){
                            articleData.add(dataSnapshot.child("currency").getValue().toString());
                        }else{
                            articleData.add("");
                        }

                        if(dataSnapshot.child("brand").getValue() != null){
                            articleData.add(dataSnapshot.child("brand").getValue().toString());
                        }else{
                            articleData.add("");
                        }

                        if(dataSnapshot.child("description").getValue() != null){
                            articleData.add(dataSnapshot.child("description").getValue().toString());
                        }else{
                            articleData.add("");
                        }

                        if(dataSnapshot.child("seller").getValue() != null){
                            articleData.add(dataSnapshot.child("seller").getValue().toString());
                        }else{
                            articleData.add("");
                        }

                        if(dataSnapshot.child("category").getValue() != null){
                            articleData.add(dataSnapshot.child("category").getValue().toString());
                        }

                        if(dataSnapshot.child("type").getValue() != null){
                            articleData.add(dataSnapshot.child("type").getValue().toString());
                        }else{
                            articleData.add("");
                        }

                        if(dataSnapshot.child("colors").getValue() != null){
                            //Log.i(TAG,dataSnapshot.child("colors").getValue().toString());
                            articleData.add(dataSnapshot.child("colors").getValue().toString());
                        }else{
                            //Log.i(TAG, "No color");
                            articleData.add("");
                        }

                        if(dataSnapshot.child("sizes").getValue() != null){
                            articleData.add(dataSnapshot.child("sizes").getValue().toString());
                        }else{
                            articleData.add("");
                        }

                        if(dataSnapshot.child("pictures").getValue() != null){
                            for(DataSnapshot d  : dataSnapshot.child("pictures").getChildren()){
                                //Log.i(TAG,d.getValue().toString());
                                pictures.add(d.getValue().toString());
                            }
                            articleData.add(dataSnapshot.child("pictures").getValue().toString());
                            //Log.i(TAG,dataSnapshot.child("pictures").getValue().toString());
                        }else{
                            articleData.add("");
                        }

                        if(dataSnapshot.child("weight").getValue() != null){
                            articleData.add(dataSnapshot.child("weight").getValue().toString());
                        }else{
                            articleData.add("");
                        }

                        data = new Data(articleData.toArray(new String[articleData.size()]), commandKey, pictures);
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
        //Log.i(TAG,"onChildAdded" );
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        //Log.i(TAG, "onChildRemoved - " + dataSnapshot.getValue().toString());
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
