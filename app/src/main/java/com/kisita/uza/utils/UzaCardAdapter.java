package com.kisita.uza.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.kisita.uza.R;
import com.kisita.uza.activities.UzaActivity;
import com.kisita.uza.model.Data;

import java.util.ArrayList;

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
    private String mCurrency;

    public UzaCardAdapter(Context context,ArrayList<Data> items) {
        this.mContext = context;
        this.itemsList = items;
        mStorage = FirebaseStorage.getInstance();
        getCurrency();
    }

    public UzaCardAdapter(Context context, ArrayList<Data> items, Boolean remove) {
        this.mContext = context;
        this.itemsList = items;
        mStorage = FirebaseStorage.getInstance();
        hasRemove = remove;
        getCurrency();
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
        Log.i(TAG, "Position is  : " + position);
        final Data d = itemsList.get(position);

        mStorageRef = mStorage.getReferenceFromUrl("gs://glam-afc14.appspot.com/" + d.getUid() + "/android.png");

        holder.lbl1.setText(d.getTexts()[Data.UzaData.NAME.ordinal()]); // Name
        holder.lbl2.setText(d.getTexts()[Data.UzaData.PRICE.ordinal()] + " "+mCurrency);
        holder.lbl3.setText(d.getTexts()[Data.UzaData.BRAND.ordinal()]);
        if (hasRemove) {
            holder.mRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "Remove pressed - " + d.getUid());
                    mCommands = FirebaseDatabase.getInstance()
                            .getReference()
                            .child("users-data")
                            .child(getUid())
                            .child("commands");
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setMessage("Would you really remove this article from the cart?")
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
                intent.putExtra("picture", itemsList.get(i).getmPicBytes());
                mContext.startActivity(intent);
            }
        };
    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "count = " + itemsList.size());
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

        Log.i(TAG, "Failure occurred. Error code is  : " + errorMessage + "-" + e.getCause().toString());
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
        private TextView lbl1, lbl2, lbl3;
        /** The img. */
        private ImageView img;
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
            lbl1 = (TextView) v.findViewById(R.id.lbl1);
            lbl2 = (TextView) v.findViewById(R.id.lbl2);
            lbl3 = (TextView) v.findViewById(R.id.lbl3);
            img = (ImageView) v.findViewById(R.id.img);
            mRemove = (ImageView) v.findViewById(R.id.remove);
            if (!hasRemove)
                mRemove.setVisibility(View.GONE);


            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mAdapter.onItemHolderClick(this);
        }
    }

    private void getCurrency(){
        SharedPreferences sharedPref = mContext.getSharedPreferences(mContext.getResources().getString(R.string.uza_keys),
                Context.MODE_PRIVATE);
        mCurrency = sharedPref.getString(mContext.getString(R.string.uza_currency),"EUR");
    }
}
