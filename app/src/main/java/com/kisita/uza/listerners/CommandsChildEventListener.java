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

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

import static com.kisita.uza.model.Data.UZA.CURRENCY;
import static com.kisita.uza.model.Data.UZA.PRICE;
import static com.kisita.uza.model.Data.UZA.WEIGHT;
import static com.kisita.uza.utils.UzaCardAdapter.setFormat;
import static com.kisita.uza.utils.UzaCardAdapter.setPrice;

/*
 * Created by Hugues on 30/04/2017.
 */
public class CommandsChildEventListener implements ChildEventListener {
    final private String TAG = "### CommandsListener";
    private ArrayList<Data> mItemsList;
    private UzaCardAdapter mAdapter;
    private  DatabaseReference mDatabase;
    private Context mContext;
    private double mPrice  = 0;
    private double mWeight = 0;
    private double costPerWeight = 0 ;
    private double shippingCost = 0;

    private TextView mPriceView;
    private TextView mShippingView;
    private TextView mTotalView;

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
        //Log.i(TAG, "Command added : " + dataSnapshot.getKey().toString());
        final SharedPreferences sharedPref = mContext.getSharedPreferences(mContext.getResources().getString(R.string.uza_keys), Context.MODE_PRIVATE);
        costPerWeight = Double.valueOf(sharedPref.getString("region1","0.0"));

        final String commandKey = dataSnapshot.getKey();
        final String quantity   = (dataSnapshot.child("quantity").getValue() != null)? dataSnapshot.child("quantity").getValue().toString() : "1";
        final String color      = (dataSnapshot.child("color").getValue() != null)? dataSnapshot.child("color").getValue().toString() : "";
        final String size       = (dataSnapshot.child("size").getValue() != null)? dataSnapshot.child("size").getValue().toString() : "";

        final String[] commandsDetails = new String[]{quantity,color,size};

        mDatabase.child("items")
                .child(dataSnapshot.child("key").getValue().toString())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<String> pictures = new ArrayList<>();
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
                            double time = Double.valueOf(quantity);
                            articleData.add(dataSnapshot.child("price").getValue().toString());
                            String oldPrice = dataSnapshot.child("price").getValue().toString();
                            String newPrice = setPrice(dataSnapshot.child("currency").getValue().toString(),oldPrice,mContext);
                            mPrice +=  time*(Double.valueOf(newPrice.replace(",",".")));
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
                            double time = Double.valueOf(quantity);
                            String weight  = dataSnapshot.child("weight").getValue().toString();
                            articleData.add(weight);
                            mWeight +=  (time*(Double.valueOf(weight.replace(",","."))));
                            //Log.i(TAG,"************ weight  = "+mWeight);
                        }else{
                            articleData.add("");
                        }

                        //TODO Use array instead of list
                        data = new Data(articleData.toArray(new String[articleData.size()]), commandKey , commandsDetails,pictures);
                        mItemsList.add(data);
                        mPriceView    = (TextView) (((UzaActivity)mContext).findViewById(R.id.order_amount_value));
                        mShippingView = (TextView) (((UzaActivity)mContext).findViewById(R.id.shipping_cost_value));
                        mTotalView    = (TextView) (((UzaActivity)mContext).findViewById(R.id.total));

                        if(mPriceView != null) {
                            mPriceView.setText(setFormat(String.valueOf(mPrice)) + " " + mCurrency);
                        }

                        if(mShippingView != null){
                             // shipping cost per kg
                            //Log.i(TAG, "**** current cost is "+ costPerWeight *mWeight);
                            String cost_string = setPrice("EUR",setFormat(String.valueOf(costPerWeight *mWeight)),mContext);
                            //Log.i(TAG, "**** current cost string is "+cost_string);
                            shippingCost = Double.valueOf(cost_string);
                            mShippingView.setText(cost_string + " " + mCurrency);
                        }

                        if(mTotalView != null) {
                            mTotalView.setText(setFormat(String.valueOf(shippingCost + mPrice)) + " " + mCurrency);
                        }
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
        mCurrency = sharedPref.getString(mContext.getString(R.string.uza_currency),"CDF");
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        //Log.i(TAG,"onChildAdded" );
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        //Log.i(TAG, "onChildRemoved - " + dataSnapshot.child("key").getValue().toString());
        for (Data d : mItemsList) {
            if (d.getUid().equalsIgnoreCase(dataSnapshot.child("key").getValue().toString())){
                mItemsList.remove(d);
                mAdapter.notifyDataSetChanged();

                mPriceView = (TextView) (((UzaActivity)mContext).findViewById(R.id.order_amount_value));
                if(mShippingView != null){
                    mShippingView.setText(getNewShippingCost(d) + " " + mCurrency);
                }
                if(mPriceView != null) {
                    mPriceView.setText(getNewPrice(d) + " " + mCurrency);
                }

                if(mTotalView != null) {
                    mTotalView.setText(setFormat(String.valueOf(shippingCost + mPrice)) + " " + mCurrency);
                }
                ((UzaActivity) mContext).commandsCount();
                break;
            }
        }
    }

    private String getNewPrice(Data d) {
        double time = 0;
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        try {
            time = Double.valueOf(d.getCommandDetails()[0]);
        }catch (NumberFormatException e){
            // Do Nothing
        }
        mPrice -= time*Double.valueOf(setPrice(d.getTexts()[CURRENCY],d.getTexts()[PRICE],mContext));

        return  df.format(mPrice);
    }

    private String getNewShippingCost(Data d) {
        double time = 0;
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        try {
            time = Double.valueOf(d.getCommandDetails()[0]);
            mWeight -= time*Double.valueOf(d.getTexts()[WEIGHT]);
        }catch (NumberFormatException e){
            // Do Nothing
        }

        shippingCost = Double.valueOf(setPrice("EUR",String.valueOf(costPerWeight *mWeight),mContext));

        return  df.format(shippingCost);
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
