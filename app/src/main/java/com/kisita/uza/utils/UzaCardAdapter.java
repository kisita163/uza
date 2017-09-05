package com.kisita.uza.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
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

import java.io.Serializable;
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
    private DatabaseReference mCommands1;
    private Boolean isCheckout = false;

    public UzaCardAdapter(Context context,ArrayList<Data> items) {
        this.mContext = context;
        this.itemsList = items;
        mStorage = FirebaseStorage.getInstance();
    }

    public UzaCardAdapter(Context context, ArrayList<Data> items, Boolean remove) {
        this.mContext = context;
        this.itemsList = items;
        mStorage = FirebaseStorage.getInstance();
        isCheckout = remove;
    }

    public void setItemsList(ArrayList<Data> itemsList) {
        this.itemsList = itemsList;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        int rId;
        if(isCheckout)
            rId = R.layout.grid_item1; // layout for checkout fragment
        else
            rId = R.layout.grid_item;  //

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(rId, viewGroup, false);
        return new CardViewHolder(itemView,this);
    }

    @Override
    public void onBindViewHolder(UzaCardAdapter.CardViewHolder holder, int position) {
        //Log.i(TAG, "Position is  : " + position);
        final Data d = itemsList.get(position);

        mStorageRef = mStorage.getReferenceFromUrl("gs://glam-afc14.appspot.com/" + d.getUid() + "/android.png");

        if (isCheckout) {
            setCommandString(holder,d.getCommandDetails());
            //Log.i(TAG,"details length is : "+d.getCommandDetails().length);
            if (d.getCommandDetails().length > 0){
                if (!d.getCommandDetails()[1].equalsIgnoreCase("")) {
                    holder.color.setBackgroundColor(Color.parseColor(d.getCommandDetails()[1].trim()));
                } else
                    holder.color.setVisibility(View.GONE);
            }
            holder.name.setText(d.getTexts()[NAME]);
            String price = setPrice(d.getTexts()[CURRENCY], d.getTexts()[PRICE],mContext);
            price = getPriceTimeQuantity(price,d.getCommandDetails()[0]);
            holder.price.setText(price + " "+getCurrency(mContext));
            holder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.i(TAG, "Remove pressed - " + d.getUid());
                    mCommands = FirebaseDatabase.getInstance()
                            .getReference()
                            .child("users-data")
                            .child(getUid())
                            .child("commands");

                    mCommands1 = FirebaseDatabase.getInstance()
                            .getReference()
                            .child("commands")
                            .child(d.getKey());

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
                                    mCommands1.removeValue();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
            //holder.img.setVisibility(View.GONE);
           /* Glide.with(mContext)
                    .using(new FirebaseImageLoader())
                    .load(mStorageRef)
                    .fitCenter()
                    .dontTransform()
                    .error(R.drawable.on_sale_item6)
                    .into(holder.img);*/
        }else{
            holder.lbl1.setText(d.getTexts()[NAME]); // Name
            String price = setPrice(d.getTexts()[CURRENCY], d.getTexts()[PRICE],mContext);
            holder.lbl2.setText(price + " "+getCurrency(mContext));
            holder.lbl3.setText(d.getTexts()[BRAND]);
            // Load the image using Glide
            if(d.getPictures().size() > 0){
                Glide.with(mContext)
                        .load(d.getPictures().get(d.getPictures().size()-1))
                        .fitCenter()
                        .dontTransform()
                        .error(R.drawable.on_sale_item6)
                        .into(holder.img);
            } else {
                Glide.with(mContext)
                        .using(new FirebaseImageLoader())
                        .load(mStorageRef)
                        .fitCenter()
                        .dontTransform()
                        .error(R.drawable.on_sale_item6)
                        .into(holder.img);
            }
        }

        mOnItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(mContext, UzaActivity.class);
                intent.putExtra("fragment",3);
                intent.putExtra("details", itemsList.get(i));
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
        SharedPreferences sharedPref = context.getSharedPreferences(context.getResources().getString(R.string.uza_keys), Context.MODE_PRIVATE);

        Log.i(TAG,sharedPref.getString("eur-cdf","") + "**** done");
        Log.i(TAG,sharedPref.getString("usd-cdf","") + "**** done");
        Log.i(TAG,sharedPref.getString("usf-eur","") + "**** done");

        df.setRoundingMode(RoundingMode.CEILING);
        // euros to cdf
        double eur_cdf = Double.valueOf(sharedPref.getString("eur-cdf","1623.58"));//1623.58;// 1 euros = 1623.58 fc;
        // usd to cdf
        double usd_cdf = Double.valueOf(sharedPref.getString("usd-cdf","1443.86"));//1443.86;// 1 usd   = 1443.86 fc;

        double usd_eur = Double.valueOf(sharedPref.getString("usd-eur","0.889098"));//0.889098;

        double p = Double.valueOf(price.replace(",","."));

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
        private TextView lbl1, lbl2, lbl3; //TODO rename these fields
        /** The img. */
        private ImageView img;
        UzaCardAdapter mAdapter;

        // Checkout stuff
        private TextView name, quantity , size, sizeTag, colorTag, price;
        private ImageView color, remove;

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
            if(!isCheckout) { // Common presentation
                img = (ImageView) v.findViewById(R.id.img);
                lbl1 = (TextView) v.findViewById(R.id.lbl1);
                lbl2 = (TextView) v.findViewById(R.id.lbl2);
                lbl3 = (TextView) v.findViewById(R.id.lbl3);
            }else{      // Ticket style presentation

                name = (TextView) v.findViewById(R.id.item_name);
                size = (TextView) v.findViewById(R.id.item_size);
                quantity = (TextView) v.findViewById(R.id.item_quantity);
                price = (TextView) v.findViewById(R.id.item_price);
                color = (ImageView) v.findViewById(R.id.item_color);
                remove = (ImageView) v.findViewById(R.id.remove);

                colorTag = (TextView) v.findViewById(R.id.color_tag);
                sizeTag = (TextView) v.findViewById(R.id.size_tag);
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

    private void setCommandString(UzaCardAdapter.CardViewHolder holder,String [] commandDetails){
        holder.quantity.setText(commandDetails[0]);
        holder.size.setVisibility(View.GONE);
        holder.color.setVisibility(View.GONE);
        holder.colorTag.setVisibility(View.GONE);
        holder.sizeTag.setVisibility(View.GONE);

        //Log.i(TAG,""+commandDetails[0]+"-"+commandDetails[1]+"-"+commandDetails[2]);
        if(!commandDetails[2].equalsIgnoreCase("")) { //There is a size
            if(!commandDetails[2].equalsIgnoreCase("size")){
                holder.size.setVisibility(View.VISIBLE);
                holder.sizeTag.setVisibility(View.VISIBLE);
                holder.size.setText(commandDetails[2]);
            }
        }
        if(!commandDetails[1].equalsIgnoreCase("")){ //There is a color
            holder.colorTag.setVisibility(View.VISIBLE);
            holder.color.setVisibility(View.VISIBLE);
        }
    }

    private String getPriceTimeQuantity(String price, String quantity) {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        double p = Double.valueOf(price.replace(",","."));
        double q = Double.valueOf(quantity.replace(",","."));

        double res = q*p;

        return df.format(res);
    }
}
