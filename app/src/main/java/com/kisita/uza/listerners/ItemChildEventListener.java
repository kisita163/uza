package com.kisita.uza.listerners;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kisita.uza.R;
import com.kisita.uza.activities.UzaActivity;
import com.kisita.uza.model.Data;
import com.kisita.uza.utils.UzaCardAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import static com.kisita.uza.model.Data.UZA.PRICE;


/**
 * Created by Hugues on 22/04/2017.
 */
public class ItemChildEventListener implements ChildEventListener{

    private static final String TAG = "## ItemChildListener";
    private ArrayList<Data> mItemsList;
    private UzaCardAdapter mAdapter;

    public ItemChildEventListener(ArrayList<Data> itemsList, UzaCardAdapter adapter,String store) {
        this.mAdapter = adapter;
        this.mItemsList = itemsList;
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        //Log.i(TAG, "onChildRemoved - " + dataSnapshot.getKey().toString());
        for (Data d : mItemsList) {
            if (d.getUid().equalsIgnoreCase(dataSnapshot.getKey().toString())){
                mItemsList.remove(d);
                mAdapter.notifyDataSetChanged();
                break;
            }
        }
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Log.i(TAG,"");
    }

    private static void handleReceivedData(DataSnapshot dataSnapshot,ArrayList<Data> list,String store) {

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
            String seller = dataSnapshot.child("seller").getValue().toString();
            if(!store.equalsIgnoreCase("All")) {
                if (!store.equalsIgnoreCase(seller))
                    return;
            }
            articleData.add(seller);
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
            //Log.i(TAG,dataSnapshot.child("sizes").getValue().toString());
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

        //TODO Give list array to data object instead of string array
        list.add(new Data(articleData.toArray(new String[articleData.size()]),pictures));
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        //TODO Update the changed item in the list (see database app)
    }


    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    public static void initItemlist(DataSnapshot dataSnapshot,ArrayList<Data> itemList,String store){
        for(DataSnapshot d :  dataSnapshot.getChildren()){
            handleReceivedData(d,itemList,store);
        }
        Collections.reverse(itemList);
    }

    public static void initCommandlist(DataSnapshot dataSnapshot,ArrayList<Data> itemList,String store){

        handleReceivedData(dataSnapshot,itemList,store);

        Collections.reverse(itemList);
    }
}
