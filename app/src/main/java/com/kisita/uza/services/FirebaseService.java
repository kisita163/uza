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


public class FirebaseService extends Service {
    private static final String TAG = "### FirebaseService";

    private DatabaseReference  mDatabase = FirebaseDatabase.getInstance().getReference();;

    private ChildEventListener itemsListener;

    private ChildEventListener commandsListener;

    private ChildEventListener favouritesListener;

    private ContentValues itemsValues      = new ContentValues();
    private ContentValues commandsValues   = new ContentValues();
    private ContentValues favouritesValues = new ContentValues();

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
    }

    /* Items Query */
    public Query getItemsQuery() {
        return mDatabase.child("items");
    }

    /*Commands query */
    public Query getCommandsQuery() {
        return mDatabase.child("users-data").child(getUid()).child("commands");
    }

    /*Favourites query*/
    public Query getFavouritesQuery() {
        return mDatabase.child("users-data").child(getUid()).child("likes");
    }

    /* get firebase user uid */
    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }


    private void setFavouritesListener() {
        favouritesListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                insertFavouritesData(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                insertFavouritesData(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                removeEntryFromTable(dataSnapshot,UzaContract.LikesEntry.CONTENT_URI);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG,"Starting firebase service for : " + FirebaseAuth.getInstance().getCurrentUser().getEmail());
        initTables();
        // Items
        setItemsListener(); // This method need to be called prior to add listener to query
        getItemsQuery().addChildEventListener(itemsListener);

        // Commands
        setCommandsListener();
        getCommandsQuery().addChildEventListener(commandsListener);

        // Favourites
        setFavouritesListener();
        getFavouritesQuery().addChildEventListener(favouritesListener);
        return super.onStartCommand(intent, flags, startId);
    }

    private void initTables() {
        removeAllEntriesFromTable(UzaContract.LikesEntry.CONTENT_URI);
        removeAllEntriesFromTable(UzaContract.ItemsEntry.CONTENT_URI);
        removeAllEntriesFromTable(UzaContract.CommandsEntry.CONTENT_URI_COMMANDS);
    }

    private void setCommandsListener() {
        commandsListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                insertCommandData(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                insertCommandData(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                removeEntryFromTable(dataSnapshot,UzaContract.CommandsEntry.CONTENT_URI_COMMANDS);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    void setItemsListener(){
        itemsListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                insertItemsData(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //Log.i(TAG,"onChildChanged");
                insertItemsData(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                //Log.i(TAG,"onChildRemoved");
                removeEntryFromTable(dataSnapshot,UzaContract.ItemsEntry.CONTENT_URI);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                //Log.i(TAG,"onChildMoved");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i(TAG,"onCancelled " + databaseError.getMessage());
            }
        };
    }




    void insertCommandData(DataSnapshot dataSnapshot){
        commandsValues.clear();
        commandsValues.put(UzaContract.CommandsEntry.COLUMN_KEY          ,FirebaseUtils.getItemData(dataSnapshot,  UzaContract.CommandsEntry.COLUMN_KEY));
        commandsValues.put(UzaContract.CommandsEntry.COLUMN_COLOR        ,FirebaseUtils.getItemData(dataSnapshot,  UzaContract.CommandsEntry.COLUMN_COLOR));
        commandsValues.put(UzaContract.CommandsEntry.COLUMN_SIZE         ,FirebaseUtils.getItemData(dataSnapshot,  UzaContract.CommandsEntry.COLUMN_SIZE));
        commandsValues.put(UzaContract.CommandsEntry.COLUMN_COMMENT      ,FirebaseUtils.getItemData(dataSnapshot,  UzaContract.CommandsEntry.COLUMN_COMMENT));
        commandsValues.put(UzaContract.CommandsEntry.COLUMN_QUANTITY     ,FirebaseUtils.getItemData(dataSnapshot,  UzaContract.CommandsEntry.COLUMN_QUANTITY));
        commandsValues.put(UzaContract.CommandsEntry.COLUMN_STATE        ,FirebaseUtils.getItemData(dataSnapshot,  UzaContract.CommandsEntry.COLUMN_STATE));
        commandsValues.put(UzaContract.CommandsEntry._ID                 ,FirebaseUtils.getItemId(dataSnapshot));

        // Finally, insert item's data into the database.
        getApplicationContext().getContentResolver().insert(
                UzaContract.CommandsEntry.CONTENT_URI_COMMANDS,
                commandsValues
        );
        //Log.i(TAG,insertUri.toString());
    }


    void insertItemsData(DataSnapshot dataSnapshot){
        //Log.i(TAG,"onChildAdded " + FirebaseUtils.getItemData(dataSnapshot, UzaContract.ItemsEntry.COLUMN_ID));
        itemsValues.clear();
        itemsValues.put(UzaContract.ItemsEntry.COLUMN_NAME           , FirebaseUtils.getItemData(dataSnapshot,  UzaContract.ItemsEntry.COLUMN_NAME));
        itemsValues.put(UzaContract.ItemsEntry.COLUMN_BRAND          , FirebaseUtils.getItemData(dataSnapshot,  UzaContract.ItemsEntry.COLUMN_BRAND));
        itemsValues.put(UzaContract.ItemsEntry.COLUMN_CATEGORY       , FirebaseUtils.getItemData(dataSnapshot,  UzaContract.ItemsEntry.COLUMN_CATEGORY));
        itemsValues.put(UzaContract.ItemsEntry.COLUMN_TYPE           , FirebaseUtils.getItemData(dataSnapshot,  UzaContract.ItemsEntry.COLUMN_TYPE));
        itemsValues.put(UzaContract.ItemsEntry.COLUMN_CURRENCY       , FirebaseUtils.getItemData(dataSnapshot,  UzaContract.ItemsEntry.COLUMN_CURRENCY));
        itemsValues.put(UzaContract.ItemsEntry.COLUMN_SELLER         , FirebaseUtils.getItemData(dataSnapshot,  UzaContract.ItemsEntry.COLUMN_SELLER));
        itemsValues.put(UzaContract.ItemsEntry.COLUMN_DESCRIPTION    , FirebaseUtils.getItemData(dataSnapshot,  UzaContract.ItemsEntry.COLUMN_DESCRIPTION));
        itemsValues.put(UzaContract.ItemsEntry._ID                   , FirebaseUtils.getItemId(dataSnapshot));//getItemData(dataSnapshot,  UzaContract.ItemsEntry.COLUMN_ID));
        itemsValues.put(UzaContract.ItemsEntry.COLUMN_AUTHOR         , FirebaseUtils.getItemData(dataSnapshot,  UzaContract.ItemsEntry.COLUMN_AUTHOR));//getItemData(dataSnapshot,  UzaContract.ItemsEntry.COLUMN_ID));
        itemsValues.put(UzaContract.ItemsEntry.COLUMN_PICTURES       , FirebaseUtils.getPicures(dataSnapshot));
        itemsValues.put(UzaContract.ItemsEntry.COLUMN_URL            , FirebaseUtils.getItemData(dataSnapshot,  UzaContract.ItemsEntry.COLUMN_URL));
        itemsValues.put(UzaContract.ItemsEntry.COLUMN_SIZE           , FirebaseUtils.getItemData(dataSnapshot,  UzaContract.ItemsEntry.COLUMN_SIZE));
        itemsValues.put(UzaContract.ItemsEntry.COLUMN_WEIGHT         , FirebaseUtils.getItemData(dataSnapshot,  UzaContract.ItemsEntry.COLUMN_WEIGHT));
        itemsValues.put(UzaContract.ItemsEntry.COLUMN_PRICE          , FirebaseUtils.getItemData(dataSnapshot,  UzaContract.ItemsEntry.COLUMN_PRICE));

        // Finally, insert item's data into the database.
        getApplicationContext().getContentResolver().insert(
                UzaContract.ItemsEntry.CONTENT_URI,
                itemsValues
        );
        //Log.i(TAG,insertUri.toString());
    }

    void insertFavouritesData(DataSnapshot dataSnapshot){
        //Log.i(TAG,dataSnapshot.getKey().toString()+" *** " + dataSnapshot.getValue().toString());
        favouritesValues.clear();
        favouritesValues.put(UzaContract.LikesEntry.COLUMN_LIKES        ,dataSnapshot.getValue().toString());
        favouritesValues.put(UzaContract.LikesEntry._ID                 ,dataSnapshot.getKey().toString());

        // Finally, insert item's data into the database.
        getApplicationContext().getContentResolver().insert(
                UzaContract.LikesEntry.CONTENT_URI,
                favouritesValues
        );
        //Log.i(TAG,insertUri.toString());
    }

    void removeEntryFromTable(DataSnapshot dataSnapshot,Uri uri){
        String selection         = "_ID = ?";
        String [] selectionArgs  = {dataSnapshot.getKey()};

        getApplicationContext().getContentResolver().delete(
                uri,
                selection,
                selectionArgs);
    }

    void removeAllEntriesFromTable(Uri uri){
        getApplicationContext().getContentResolver().delete(
                uri,
                null,
                null);
    }
}
