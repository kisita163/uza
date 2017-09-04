package com.kisita.uza.services;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.kisita.uza.provider.UzaContract;

import static java.security.AccessController.getContext;

public class FirebaseService extends Service {
    private static final String TAG = "### FirebaseService";

    private DatabaseReference  mDatabase = FirebaseDatabase.getInstance().getReference();;

    private ChildEventListener itemsListener;

    private ChildEventListener commandsListener;

    private ChildEventListener favouritesListener;

    private ContentValues values = new ContentValues();

    public FirebaseService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        setItemsListener(); // This method need to be called prior to add listener to query
        getItemsQuery().addChildEventListener(itemsListener);
    }
    /* Items Query */
    public Query getItemsQuery() {
        return mDatabase.child("items");
    }

    /*Commands query */
    public Query getCommandsQuery() {
        return mDatabase.child("items");
    }

    /*Favourites query*/
    public Query getFavouritesQuery() {
        return mDatabase.child("users-data").child(getUid()).child("likes");
    }

    /* get firebase user uid */
    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    void setItemsListener(){
        itemsListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //Log.i(TAG,"onChildAdded " + FirebaseUtils.getItemData(dataSnapshot, UzaContract.ItemsEntry.COLUMN_ID));
                values.clear();
                values.put(UzaContract.ItemsEntry.COLUMN_NAME           , FirebaseUtils.getItemData(dataSnapshot,  UzaContract.ItemsEntry.COLUMN_NAME));
                values.put(UzaContract.ItemsEntry.COLUMN_BRAND          , FirebaseUtils.getItemData(dataSnapshot,  UzaContract.ItemsEntry.COLUMN_BRAND));
                values.put(UzaContract.ItemsEntry.COLUMN_TYPE           , FirebaseUtils.getItemData(dataSnapshot,  UzaContract.ItemsEntry.COLUMN_TYPE));
                values.put(UzaContract.ItemsEntry.COLUMN_CATEGORY       , FirebaseUtils.getItemData(dataSnapshot,  UzaContract.ItemsEntry.COLUMN_CATEGORY));
                values.put(UzaContract.ItemsEntry.COLUMN_CURRENCY       , FirebaseUtils.getItemData(dataSnapshot,  UzaContract.ItemsEntry.COLUMN_CURRENCY));
                values.put(UzaContract.ItemsEntry.COLUMN_SELLER         , FirebaseUtils.getItemData(dataSnapshot,  UzaContract.ItemsEntry.COLUMN_SELLER));
                values.put(UzaContract.ItemsEntry.COLUMN_DESCRIPTION    , FirebaseUtils.getItemData(dataSnapshot,  UzaContract.ItemsEntry.COLUMN_DESCRIPTION));
                values.put(UzaContract.ItemsEntry.COLUMN_ID             , FirebaseUtils.getItemData(dataSnapshot,  UzaContract.ItemsEntry.COLUMN_ID));
                values.put(UzaContract.ItemsEntry.COLUMN_PICTURES       , FirebaseUtils.getItemData(dataSnapshot,  UzaContract.ItemsEntry.COLUMN_PICTURES));
                values.put(UzaContract.ItemsEntry.COLUMN_URL            , FirebaseUtils.getItemData(dataSnapshot,  UzaContract.ItemsEntry.COLUMN_URL));
                values.put(UzaContract.ItemsEntry.COLUMN_COLOR          , FirebaseUtils.getItemData(dataSnapshot,  UzaContract.ItemsEntry.COLUMN_URL));
                values.put(UzaContract.ItemsEntry.COLUMN_SIZE           , FirebaseUtils.getItemData(dataSnapshot,  UzaContract.ItemsEntry.COLUMN_URL));
                values.put(UzaContract.ItemsEntry.COLUMN_WEIGHT         , FirebaseUtils.getItemData(dataSnapshot,  UzaContract.ItemsEntry.COLUMN_URL));
                values.put(UzaContract.ItemsEntry.COLUMN_PRICE          , FirebaseUtils.getItemData(dataSnapshot,  UzaContract.ItemsEntry.COLUMN_PRICE));

                // Finally, insert item's data into the database.
                Uri insertUri = getApplicationContext().getContentResolver().insert(
                        UzaContract.ItemsEntry.CONTENT_URI,
                        values
                );
                Log.i(TAG,insertUri.toString());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.i(TAG,"onChildChanged");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.i(TAG,"onChildRemoved");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.i(TAG,"onChildMoved");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i(TAG,"onCancelled " + databaseError.getMessage());
            }
        };
    }
}
