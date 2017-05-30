package com.kisita.uza.listerners;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kisita.uza.R;
import com.kisita.uza.activities.UzaActivity;
import com.kisita.uza.model.Data;
import com.kisita.uza.ui.CheckoutFragment;
import com.kisita.uza.utils.UzaCardAdapter;

import java.util.ArrayList;
import java.util.StringTokenizer;

import static com.kisita.uza.model.Data.UZA.PRICE;

/**
 * Created by Hugues on 30/04/2017.
 */
public class CommandsChildEventListener implements ChildEventListener {
    final private String TAG = "### CommandsListener";
    private ArrayList<Data> mItemsList;
    private UzaCardAdapter mAdapter;
    private  DatabaseReference mDatabase;
    private Context mContext;
    private double mPrice = 0;
    TextView mPriceView;

    private Data data;
    private String mCurrency = "";


    public CommandsChildEventListener(ArrayList<Data> itemsList, UzaCardAdapter adapter, DatabaseReference reference, Context context) {
        this.mAdapter = adapter;
        this.mItemsList = itemsList;
        this.mDatabase = reference;
        this.mContext = context;
        getCurrency();
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Log.i(TAG, "Command added : " + dataSnapshot.getKey().toString());

        final String commandKey = dataSnapshot.getKey().toString();
        mDatabase.child("items")
                .child(dataSnapshot.child("key").getValue().toString())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists())
                            return;
                        ArrayList<String> articleData = new ArrayList<>();
                        articleData.add(dataSnapshot.getKey());

                        if(dataSnapshot.child("name").getValue() != null){
                            articleData.add(dataSnapshot.child("name").getValue().toString());
                        }else{
                            articleData.add("");
                        }

                        if(dataSnapshot.child("price").getValue() != null){
                            articleData.add(dataSnapshot.child("price").getValue().toString());
                            mPrice +=  Double.valueOf(dataSnapshot.child("price").getValue().toString());
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

                        if(dataSnapshot.child("color").getValue() != null){
                            articleData.add(dataSnapshot.child("color").getValue().toString());
                        }else{
                            articleData.add("");
                        }

                        if(dataSnapshot.child("size").getValue() != null){
                            articleData.add(dataSnapshot.child("size").getValue().toString());
                        }else{
                            articleData.add("");
                        }
                        //TODO Use array instead of list
                        data = new Data(articleData.toArray(new String[articleData.size()]), commandKey);
                        mItemsList.add(data);
                        mPriceView = (TextView) (((UzaActivity)mContext).findViewById(R.id.total));
                        if(mPriceView != null)
                            mPriceView.setText(String.valueOf(mPrice) + " " + mCurrency);
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void getCurrency(){
        SharedPreferences sharedPref = mContext.getSharedPreferences(mContext.getResources().getString(R.string.uza_keys),
                Context.MODE_PRIVATE);
        mCurrency = sharedPref.getString(mContext.getString(R.string.uza_currency),"EUR");
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        Log.i(TAG,"onChildAdded" );
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        Log.i(TAG, "onChildRemoved - " + dataSnapshot.child("key").getValue().toString());
        for (Data d : mItemsList) {
            if (d.getUid().equalsIgnoreCase(dataSnapshot.child("key").getValue().toString())){
                mItemsList.remove(d);
                mAdapter.notifyDataSetChanged();
                mPrice -= Double.valueOf(d.getTexts()[PRICE]);
                mPrice = Math.round(mPrice);
                mPriceView = (TextView) (((UzaActivity)mContext).findViewById(R.id.total));
                if(mPriceView != null)
                    mPriceView.setText(String.valueOf(mPrice) + " " + mCurrency);
                ((UzaActivity) mContext).commandsCount();
                break;
            }
        }
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
