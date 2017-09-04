package com.kisita.uza.services;

import com.google.firebase.database.DataSnapshot;

/**
 * Created by HuguesKi on 03-09-17.
 */

public class FirebaseUtils {
    public static String getItemData(DataSnapshot dataSnapshot,String name){
        if(dataSnapshot.child(name).getValue() != null){
            return (dataSnapshot.child(name).getValue().toString());
        }else{
            return ("");
        }
    }
}
