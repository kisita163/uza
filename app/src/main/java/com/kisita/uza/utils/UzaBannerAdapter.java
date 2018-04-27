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

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.kisita.uza.R;
import com.kisita.uza.activities.UzaActivity;
import com.kisita.uza.model.Data;
import com.kisita.uza.ui.DetailFragment;
import com.kisita.uza.utils.UzaPageAdapter;

import java.util.ArrayList;

import static com.kisita.uza.utils.UzaFunctions.getCurrency;
import static com.kisita.uza.utils.UzaFunctions.setFormat;
import static com.kisita.uza.utils.UzaFunctions.setPrice;

/*
 * Created by Hugues on 23/04/2017.
 */
public class UzaBannerAdapter extends
        RecyclerView.Adapter<com.kisita.uza.utils.UzaBannerAdapter.CardViewHolder> implements OnFailureListener
{
    private static final String TAG = "### UzaBannerAdapter";
    private ArrayList<Data> itemsList;
    private Context mContext;

    private AdapterView.OnItemClickListener mOnItemClickListener;

    public UzaBannerAdapter(Context context,ArrayList<Data> items) {
        this.mContext = context;
        this.itemsList = items;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        int rId = R.layout.banner_img;  //

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(rId, viewGroup, false);
        return new CardViewHolder(itemView,this);
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        final Data d = itemsList.get(position);
        Log.i(TAG,"---> item added here : " +d.getItemId() );
        Glide.with(mContext)
                .load(d.getPictures().get(0))
                .fitCenter()
                .error(R.drawable.anonymous_user)
                .into(holder.sameAuthor);
        // Item listeners
        mOnItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                openDetailFragment(i);
            }
        };
    }

    private void openDetailFragment(int position) {
        if(mContext instanceof UzaActivity)
            ((UzaActivity) mContext).updateForegroundFragment(itemsList.get(position).getAuthor(), DetailFragment.newInstance( itemsList.get(position)));
        else{
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

    /**
     * The Class CardViewHolder is the View Holder class for Adapter views.
     */
    public class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        com.kisita.uza.utils.UzaBannerAdapter mAdapter;

        ImageView sameAuthor;

        /**
         * Instantiates a new card view holder.
         *
         * @param v
         *            the v
         */
        private CardViewHolder(View v, com.kisita.uza.utils.UzaBannerAdapter adapter)
        {
            super(v);
            this.mAdapter = adapter;
            sameAuthor =  v.findViewById(R.id.img);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mAdapter.onItemHolderClick(this);
        }
    }
}
