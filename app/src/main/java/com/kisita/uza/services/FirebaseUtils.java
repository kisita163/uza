package com.kisita.uza.services;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

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

    public static String getPicures(DataSnapshot dataSnapshot){
       String pic  =  "";
        if(dataSnapshot.child("pictures").getValue() != null){
            for(DataSnapshot s : dataSnapshot.child("pictures").getChildren()){
                pic += s.getValue();
                pic += ",";
            }
        }
        return pic;
    }

    public static String columnsArray2string(String [] projection){
        String columns = "";

        for (int i = 0; i < projection.length; i++) {
            columns += projection[i];
            if (i < projection.length - 1)
                columns += ",";
        }

        return columns;
    }

    public static String getItemId(DataSnapshot dataSnapshot){
        return dataSnapshot.getKey();
    }


    public static String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public static String objectToSerialized(Object object){
        String serializedObject = "";
        // serialize the object
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream so = new ObjectOutputStream(bo);
            so.writeObject(object);
            so.flush();
            serializedObject = bo.toString();
        } catch (Exception e) {
            System.out.println(e);
        }

        return serializedObject;
    }

    public static ArrayList<String> serializedToArray(String object){
        // deserialize the object
        ArrayList<String> obj = new ArrayList<>();
        try {
            byte b[] = object.getBytes();
            ByteArrayInputStream bi = new ByteArrayInputStream(b);
            ObjectInputStream si = new ObjectInputStream(bi);
            obj =  (ArrayList<String>) si.readObject();
        } catch (Exception e) {
            System.out.println( e);
        }
        return obj;
    }
}
