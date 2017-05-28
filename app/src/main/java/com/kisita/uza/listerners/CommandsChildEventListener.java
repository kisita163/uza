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

/**
 * Created by Hugues on 30/04/2017.
 */
public class CommandsChildEventListener implements ChildEventListener {
    final private String TAG = "### CommandsListener";
    private ArrayList<Data> mItemsList;
    private UzaCardAdapter mAdapter;
    private  DatabaseReference mDatabase;
    private String[] str;
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
        Log.i(TAG, "Command added : " + dataSnapshot.getValue().toString());

        final String commandKey = dataSnapshot.child("key").getValue().toString();
        mDatabase.child("items")
                .child(commandKey)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists())
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
                        data = new Data(str, commandKey);
                        mItemsList.add(data);
                        mPrice +=  Double.valueOf(dataSnapshot.child("price").getValue().toString());
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
        Log.i(TAG, "onChildRemoved - " + dataSnapshot.getValue().toString());
        for (Data d : mItemsList) {
            if (d.getUid().equalsIgnoreCase(dataSnapshot.getValue().toString())) {
                mItemsList.remove(d);
                mAdapter.notifyDataSetChanged();
                mPrice -= Double.valueOf(d.getTexts()[Data.UzaData.PRICE.ordinal()]);
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
