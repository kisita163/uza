package com.kisita.uza.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kisita.uza.R;
import com.kisita.uza.activities.UzaActivity;
import com.kisita.uza.model.Data;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

import static com.kisita.uza.model.Data.UZA.*;

/**
 * Created by Hugues on 23/04/2017.
 */
public class UzaCardAdapter extends
        RecyclerView.Adapter<UzaCardAdapter.CardViewHolder> implements OnFailureListener
{
    private static final String TAG = "### UzaCardAdapter";
    private ArrayList<Data> itemsList;
    private Context mContext;
    private AdapterView.OnItemClickListener mOnItemClickListener;
    private FirebaseStorage mStorage;
    private StorageReference mStorageRef;
    private DatabaseReference mCommands;
    private Boolean hasRemove = false;

    public UzaCardAdapter(Context context,ArrayList<Data> items) {
        this.mContext = context;
        this.itemsList = items;
        mStorage = FirebaseStorage.getInstance();
    }

    public UzaCardAdapter(Context context, ArrayList<Data> items, Boolean remove) {
        this.mContext = context;
        this.itemsList = items;
        mStorage = FirebaseStorage.getInstance();
        hasRemove = remove;
    }

    public void setItemsList(ArrayList<Data> itemsList) {
        this.itemsList = itemsList;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.grid_item, viewGroup, false);
        return new CardViewHolder(itemView,this);
    }

    @Override
    public void onBindViewHolder(UzaCardAdapter.CardViewHolder holder, int position) {
        //Log.i(TAG, "Position is  : " + position);
        final Data d = itemsList.get(position);

        mStorageRef = mStorage.getReferenceFromUrl("gs://glam-afc14.appspot.com/" + d.getUid() + "/android.png");

        holder.lbl1.setText(d.getTexts()[NAME]); // Name
        String price = setPrice(d.getTexts()[CURRENCY], d.getTexts()[PRICE],mContext);
        holder.lbl2.setText(price + " "+getCurrency(mContext));
        holder.lbl3.setText(d.getTexts()[BRAND]);
        if (hasRemove) {
            holder.lbl0.setText(setCommandString(d.getCommandDetails()));
            //Log.i(TAG,"details length is : "+d.getCommandDetails().length);
            if(!d.getCommandDetails()[1].equalsIgnoreCase(""))
                holder.lbl4.setBackgroundColor(Color.parseColor(d.getCommandDetails()[1].trim()));
            else
                holder.lbl4.setVisibility(View.GONE);
            holder.mRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.i(TAG, "Remove pressed - " + d.getUid());
                    mCommands = FirebaseDatabase.getInstance()
                            .getReference()
                            .child("users-data")
                            .child(getUid())
                            .child("commands");
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setMessage(R.string.RemoveCommand)
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mCommands.child(d.getKey()).removeValue();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        }
        // Load the image using Glide
        Glide.with(mContext)
                .using(new FirebaseImageLoader())
                .load(mStorageRef)
                .fitCenter()
                .dontTransform()
                .error(R.drawable.on_sale_item6)
                .into(holder.img);


        mOnItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(mContext, UzaActivity.class);
                intent.putExtra("fragment",3);
                intent.putExtra("details", itemsList.get(i).getTexts());
                mContext.startActivity(intent);
            }
        };
    }

    public static String setFormat(String str){
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        return df.format(Double.valueOf(str));
    }

    public static String setPrice(String currency,String price,Context context) {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        //TODO Get ratio from firebase
        // euros to cdf
        double eur_cdf = 1623.58;// 1 euros = 1623.58 fc;
        // usd to cdf
        double usd_cdf = 1443.86;// 1 usd   = 1443.86 fc;

        double usd_eur = 0.889098;

        double p = Double.valueOf(price);

        String mCurrency = getCurrency(context);


        if(mCurrency.equalsIgnoreCase(currency)) {
            return price;
        }else if(currency.equalsIgnoreCase("CDF") && mCurrency.equalsIgnoreCase("EUR")){
            p = p/eur_cdf;
        }else if(currency.equalsIgnoreCase("EUR") && mCurrency.equalsIgnoreCase("CDF")){
            p = Math.ceil(p*eur_cdf);
        }else if(currency.equalsIgnoreCase("USD") && mCurrency.equalsIgnoreCase("EUR")) {
            p = p*usd_eur;
        }else if(currency.equalsIgnoreCase("EUR") && mCurrency.equalsIgnoreCase("USD")) {
            p = p/usd_eur;
        }else if(currency.equalsIgnoreCase("USD") && mCurrency.equalsIgnoreCase("CDF")) {
            p = Math.ceil(p*usd_cdf);
        }else if(currency.equalsIgnoreCase("CDF") && mCurrency.equalsIgnoreCase("USD")) {
            p = p/usd_cdf;

        }else{
            return price;
        }
        return df.format(p);
    }

    @Override
    public int getItemCount() {
        //Log.i(TAG, "count = " + itemsList.size());
        return itemsList.size();
    }

    private void onItemHolderClick(CardViewHolder itemHolder) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(null, itemHolder.itemView,
                    itemHolder.getAdapterPosition(), itemHolder.getItemId());
        }
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        String errorMessage = e.getMessage();

        //Log.i(TAG, "Failure occurred. Error code is  : " + errorMessage + "-" + e.getCause().toString());
    }

    private String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    /**
     * The Class CardViewHolder is the View Holder class for Adapter views.
     */
    public class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        /** The lbl3. */
        private TextView lbl0,lbl1, lbl2, lbl3;
        /** The img. */
        private ImageView img,lbl4;
        private ImageView mRemove;
        UzaCardAdapter mAdapter;

        /**
         * Instantiates a new card view holder.
         *
         * @param v
         *            the v
         */
        private CardViewHolder(View v,UzaCardAdapter adapter)
        {
            super(v);
            this.mAdapter = adapter;
            lbl0 = (TextView) v.findViewById(R.id.lbl0);
            lbl1 = (TextView) v.findViewById(R.id.lbl1);
            lbl2 = (TextView) v.findViewById(R.id.lbl2);
            lbl3 = (TextView) v.findViewById(R.id.lbl3);
            lbl4 = (ImageView) v.findViewById(R.id.lbl4);
            img = (ImageView) v.findViewById(R.id.img);
            mRemove = (ImageView) v.findViewById(R.id.remove);
            if (!hasRemove){
                mRemove.setVisibility(View.GONE);
                lbl0.setVisibility(View.GONE);
                lbl4.setVisibility(View.GONE);
            }



            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mAdapter.onItemHolderClick(this);
        }
    }

    private static String getCurrency(Context context){
        String currency;
        SharedPreferences sharedPref = context.getSharedPreferences(context.getResources().getString(R.string.uza_keys),
                Context.MODE_PRIVATE);
        currency = sharedPref.getString(context.getString(R.string.uza_currency),"CDF");
        return currency;
    }

    private String setCommandString(String [] commandDetails){
        String s = "Qty: "+ commandDetails[0];
        //Log.i(TAG,""+commandDetails[0]+"-"+commandDetails[1]+"-"+commandDetails[2]);
        if(!commandDetails[2].equalsIgnoreCase("")) {
            if(!commandDetails[2].equalsIgnoreCase("size"))
                s = s + " | size: " + commandDetails[2];
        }
        if(!commandDetails[1].equalsIgnoreCase(""))
            s = s + " | color :";

        return s;
    }
}
