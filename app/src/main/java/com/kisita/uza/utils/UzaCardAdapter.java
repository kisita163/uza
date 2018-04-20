package com.kisita.uza.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.kisita.uza.R;
import com.kisita.uza.activities.UzaActivity;
import com.kisita.uza.model.Data;
import com.kisita.uza.provider.UzaContract;
import com.kisita.uza.ui.DetailFragment;

import java.util.ArrayList;

import static com.kisita.uza.utils.UzaFunctions.getCurrency;
import static com.kisita.uza.utils.UzaFunctions.setFormat;
import static com.kisita.uza.utils.UzaFunctions.setPrice;

/*
 * Created by Hugues on 23/04/2017.
 */
public class UzaCardAdapter extends
        RecyclerView.Adapter<UzaCardAdapter.CardViewHolder> implements OnFailureListener
{
    private static final String TAG = "### UzaCardAdapter";
    private ArrayList<Data> itemsList;
    private Context mContext;

    private AdapterView.OnItemClickListener mOnItemClickListener;

    public UzaCardAdapter(Context context,ArrayList<Data> items) {
        this.mContext = context;
        this.itemsList = items;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        int rId = R.layout.grid_item;  //

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(rId, viewGroup, false);
        return new CardViewHolder(itemView,this);
    }

    @Override
    public void onBindViewHolder(UzaCardAdapter.CardViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        //Log.i(TAG, "Position is  : " + position);
        final Data d = itemsList.get(position);

        holder.itemId.setText(d.getItemId()); // Item Type
        String price = setPrice(d.getCurrency(), d.getPrice(),mContext);
        holder.price.setText(setFormat(price) + " "+getCurrency(mContext));

        // Item listeners
        mOnItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                openDetailFragment(i);
            }
        };

        initPager(holder, d, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDetailFragment(position);
            }
        });

        holder.favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(d.isFavourite()) { // Favourite button is pressed
                    ((ImageView) view).setImageResource(R.drawable.ic_action_favorite);
                    d.updateFavourite(false);
                }else{
                    ((ImageView) view).setImageResource(R.drawable.ic_action_favorite_black);
                    d.updateFavourite(true);
                }
            }
        });

        // Set favourite button
        if(d.isFavourite()) {
            holder.favButton.setImageResource(R.drawable.ic_action_favorite_black);
        }

        // Set Command State
        holder.favButton.setVisibility(View.GONE);

    }

    private void openDetailFragment(int position) {

        if(mContext instanceof UzaActivity){
            ((UzaActivity) mContext).updateForegroundFragment(itemsList.get(position).getAuthor(), DetailFragment.newInstance( itemsList.get(position)));
        }else {
            Intent intent = new Intent(mContext, UzaActivity.class);
            intent.putExtra("fragment", 3);
            intent.putExtra("details", itemsList.get(position));
            mContext.startActivity(intent);
        }
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
        UzaCardAdapter mAdapter;

        private TextView itemId, price;

        private ViewPager pager;

        private LinearLayout vDots;

        ImageView favButton;

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

            itemId    =  v.findViewById(R.id.item_id_value);
            price     =  v.findViewById(R.id.item_price_value);
            /* The pager. */
            pager     =  v.findViewById(R.id.pager);
            pager.setPageMargin(10);
            /* The view that hold dots. */
            vDots     = v.findViewById(R.id.vDots);
            /* Favourite button */
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mAdapter.onItemHolderClick(this);
        }
    }


    /**
     * Inits the pager view.
     */
    private void initPager(UzaCardAdapter.CardViewHolder holder, Data data, View.OnClickListener listener)
    {
        UzaPageAdapter adapter = new UzaPageAdapter(mContext, data, holder.vDots, listener);

        holder.pager.setOnPageChangeListener(adapter);
        holder.pager.setAdapter(adapter);
    }
}
