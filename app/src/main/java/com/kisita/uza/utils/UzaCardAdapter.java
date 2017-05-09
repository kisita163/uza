package com.kisita.uza.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import com.kisita.uza.R;
import com.kisita.uza.activities.UzaActivity;
import com.kisita.uza.model.Data;

import java.util.ArrayList;

/**
 * Created by Hugues on 23/04/2017.
 */
public class UzaCardAdapter extends
        RecyclerView.Adapter<UzaCardAdapter.CardViewHolder>
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
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.grid_item, viewGroup, false);
        return new CardViewHolder(itemView,this);
    }

    @Override
    public void onBindViewHolder(UzaCardAdapter.CardViewHolder holder, int position) {
        Data d = itemsList.get(position);
        holder.lbl1.setText(d.getTexts()[Data.UzaData.NAME.ordinal()]); // Name
        holder.lbl2.setText(d.getTexts()[Data.UzaData.SELLER.ordinal()]);
        holder.lbl3.setText(d.getTexts()[Data.UzaData.PRICE.ordinal()]);
        holder.img.setImageResource(d.getResources()[0]);

        mOnItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(mContext, UzaActivity.class);
                intent.putExtra("fragment",3);
                intent.putExtra("Details",itemsList.get(i).getTexts());
                mContext.startActivity(intent);
            }
        };
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    private void onItemHolderClick(CardViewHolder itemHolder) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(null, itemHolder.itemView,
                    itemHolder.getAdapterPosition(), itemHolder.getItemId());
        }
    }

    /**
     * The Class CardViewHolder is the View Holder class for Adapter views.
     */
    public class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        /** The lbl3. */
        protected TextView lbl1, lbl2, lbl3;
        /** The img. */
        protected ImageView img;
        UzaCardAdapter mAdapter;

        /**
         * Instantiates a new card view holder.
         *
         * @param v
         *            the v
         */
        public CardViewHolder(View v,UzaCardAdapter adapter)
        {
            super(v);
            this.mAdapter = adapter;
            lbl1 = (TextView) v.findViewById(R.id.lbl1);
            lbl2 = (TextView) v.findViewById(R.id.lbl2);
            lbl3 = (TextView) v.findViewById(R.id.lbl3);
            img = (ImageView) v.findViewById(R.id.img);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mAdapter.onItemHolderClick(this);
        }
    }
}
