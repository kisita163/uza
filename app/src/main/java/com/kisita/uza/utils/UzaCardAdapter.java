package com.kisita.uza.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
import com.kisita.uza.ui.CheckoutFragment;
import com.kisita.uza.ui.CommandsFragment;

import java.util.ArrayList;

import static com.kisita.uza.model.Data.UZA.*;
import static com.kisita.uza.utils.UzaFunctions.getCost;
import static com.kisita.uza.utils.UzaFunctions.getCurrency;
import static com.kisita.uza.utils.UzaFunctions.setFormat;
import static com.kisita.uza.utils.UzaFunctions.setPrice;

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

    private Fragment mAdapterListener;

    private FirebaseStorage mStorage;
    private StorageReference  mStorageRef;
    private DatabaseReference mCommands;
    private DatabaseReference mCommands1;
    private Boolean isCheckout = false;

    public UzaCardAdapter(Context context,ArrayList<Data> items) {
        this.mContext = context;
        this.itemsList = items;
        mStorage = FirebaseStorage.getInstance();
    }

    public UzaCardAdapter(Context context, ArrayList<Data> items, CheckoutFragment fragment, Boolean remove) {
        this.mContext    = context;
        this.itemsList   = items;
        mStorage         = FirebaseStorage.getInstance();
        isCheckout       = remove;
        mAdapterListener = fragment;
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
            setCommandString(holder,d.getData());
            //Log.i(TAG,"details length is : "+d.getCommandDetails().length);
            if (!d.getData()[COLOR].equalsIgnoreCase("")) {
                holder.color.setBackgroundColor(Color.parseColor(d.getData()[COLOR].trim()));
            } else
                holder.color.setVisibility(View.GONE);
            holder.name.setText(d.getData()[NAME]);
            String price = setPrice(d.getData()[CURRENCY], d.getData()[PRICE],mContext);
            price = getCost(price,d.getData()[QUANTITY]);
            holder.price.setText(setFormat(price) + " "+getCurrency(mContext));
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
                            .child(d.getData()[KEY]);

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
                                    mCommands.child(d.getData()[KEY]).removeValue();
                                    mCommands1.removeValue();
                                    ((CheckoutFragment) mAdapterListener).onRemovePressedListener(d);
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        }else{
            holder.lbl1.setText(d.getData()[NAME]); // Name
            String price = setPrice(d.getData()[CURRENCY], d.getData()[PRICE],mContext);
            holder.lbl2.setText(setFormat(price) + " "+getCurrency(mContext));
            holder.lbl3.setText(d.getData()[BRAND]);
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

                if(mAdapterListener instanceof CommandsFragment){
                        ((CommandsFragment) mAdapterListener).onCommandSelected(itemsList.get(i).getData()[KEY]);
                }else {

                    Intent intent = new Intent(mContext, UzaActivity.class);
                    intent.putExtra("fragment", 3);
                    intent.putExtra("details", itemsList.get(i));
                    mContext.startActivity(intent);
                }
            }
        };
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
        e.printStackTrace();
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
                img  =  v.findViewById(R.id.img);
                lbl1 =  v.findViewById(R.id.lbl1);
                lbl2 =  v.findViewById(R.id.lbl2);
                lbl3 =  v.findViewById(R.id.lbl3);
            }else{      // Ticket style presentation

                name     =  v.findViewById(R.id.item_name);
                size     =  v.findViewById(R.id.item_size);
                quantity =  v.findViewById(R.id.item_quantity);
                price    =  v.findViewById(R.id.item_price);
                color    =  v.findViewById(R.id.item_color);
                remove   =  v.findViewById(R.id.remove);

                colorTag =  v.findViewById(R.id.color_tag);
                sizeTag  =  v.findViewById(R.id.size_tag);
            }
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mAdapter.onItemHolderClick(this);
        }
    }

    private void setCommandString(UzaCardAdapter.CardViewHolder holder,String [] data){
        holder.quantity.setText(data[QUANTITY]);
        holder.size.setVisibility(View.GONE);
        holder.color.setVisibility(View.GONE);
        holder.colorTag.setVisibility(View.GONE);
        holder.sizeTag.setVisibility(View.GONE);

        //Log.i(TAG,""+data[0]+"-"+data[1]+"-"+data[2]);
        if(!data[SIZE].equalsIgnoreCase("")) { //There is a size
            if(!data[2].equalsIgnoreCase("size")){
                holder.size.setVisibility(View.VISIBLE);
                holder.sizeTag.setVisibility(View.VISIBLE);
                holder.size.setText(data[SIZE]);
            }
        }
        if(!data[COLOR].equalsIgnoreCase("")){ //There is a color
            holder.colorTag.setVisibility(View.VISIBLE);
            holder.color.setVisibility(View.VISIBLE);
        }
    }

    public void setAdapterListener(Fragment mAdapterListener) {
        this.mAdapterListener = mAdapterListener;
    }
}
