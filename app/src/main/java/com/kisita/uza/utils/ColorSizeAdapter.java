package com.kisita.uza.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kisita.uza.R;
import com.kisita.uza.ui.DetailFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HuguesKi on 10/06/2017.
 */

public class ColorSizeAdapter extends RecyclerView.Adapter<ColorSizeAdapter.CardViewHolder> {

    private final Context context;
    private List<String> itemList;
    private int item = 0;

    public final static int COLOR = 0;
    public final static int SIZE = 1;

    ColorSizeAdapter adapter;

    private List<DetailFragment.ColorSize> objects;

    private DetailFragment fragment;

    private String resource = "";

    private int itemSelectedPosition = -1;

    private OnFieldChangedListener mListener;

    public ColorSizeAdapter(DetailFragment fragment , Context context, List<String> colorList, int item,String resource) {
        this.itemList = colorList;
        this.item = item;
        this.context = context;
        this.adapter = this;
        this.resource = resource;
        this.fragment = fragment;

        objects = new ArrayList<>();

        if(colorList != null) {
            for (String s : colorList) {
                objects.add(new DetailFragment.ColorSize(s, false));
            }
        }

        if(fragment != null ) {
            try {
                mListener = (OnFieldChangedListener) fragment;
            } catch (ClassCastException e) {
                throw new ClassCastException(fragment.toString()
                        + " must implement onFieldChangedListener");
            }
        }
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // get inflater and get view by resource id itemLayout
        View v = null;
        if(item == COLOR)
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.color_layout, parent, false);

        if(item == SIZE)
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.size_layout, parent, false);
        // return ViewHolder with View
        return new CardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ColorSizeAdapter.CardViewHolder holder, final int position) {
        // save information in holder, we have one type in this adapter
        if(holder.mColor != null) {
            try {
                int color = Color.parseColor(itemList.get(position).trim());
                Log.i("ColorSizeAdapter","New data. color is  : "+color);
                holder.mColor.setBackgroundColor(color);
                if(objects.get(position).getName().equalsIgnoreCase(resource)){
                    if(color < -8388607 ) // couleurs sombres
                        holder.mColor.setImageResource(R.drawable.ic_done_white_24dp);
                    else
                        holder.mColor.setImageResource(R.drawable.ic_done_black_24dp);
                }else{
                    holder.mColor.setImageResource(0);
                }
                holder.mColor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onFieldChanged();
                        Log.i("ColorSizeAdapter",""+objects.get(position).isSelected());
                        if(objects.get(position).isSelected()) {
                            Log.i("ColorSizeAdapter", "remove the border");
                            objects.get(position).setSelected(false);
                            itemSelectedPosition = -1;
                            resource="";
                        }
                        else{
                            Log.i("ColorSizeAdapter", "Add the border. Value = "+objects.get(position).getName());
                            objects.get(position).setSelected(true);
                            if(itemSelectedPosition != -1)
                                objects.get(itemSelectedPosition).setSelected(false);
                            itemSelectedPosition = position;
                            resource = objects.get(position).getName();
                        }
                        notifyDataSetChanged();
                    }
                });
            } catch (IllegalArgumentException e) {
                Log.i("ColorSizeAdapter","the received color is "+itemList.get(position));
                holder.mColor.setBackgroundColor(Color.BLACK);
            }
        }
        if(holder.mSize != null){
            if(objects.get(position).getName().equalsIgnoreCase(resource)){
                holder.mSize.setBackground(context.getResources().getDrawable(R.drawable.border));
            }else{
                holder.mSize.setBackground(null);
            }
            if(itemList.get(position).length()> 4){
                holder.mSize.setTextSize(12);
            }
            holder.mSize.setText(itemList.get(position));
            holder.mSize.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onFieldChanged();
                    //Log.i("ColorSizeAdapter",""+itemList.get(position));
                    if(objects.get(position).isSelected()) {
                        //Log.i("ColorSizeAdapter", "remove the border");
                        objects.get(position).setSelected(false);
                        itemSelectedPosition = -1;
                        resource = "";
                    }
                    else{
                        Log.i("ColorSizeAdapter", "Add the border. Value = "+objects.get(position).getName());
                        objects.get(position).setSelected(true);
                        if(itemSelectedPosition != -1)
                            objects.get(itemSelectedPosition).setSelected(false);
                        itemSelectedPosition = position;
                        resource = objects.get(position).getName();
                    }
                    notifyDataSetChanged();
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView mColor;
        private TextView mSize;

        /**
         * Instantiates a new card view holder.
         *
         * @param v
         *            the v
         */
        private CardViewHolder(View v/*,UzaCardAdapter adapter*/)
        {
            super(v);
            if(item == COLOR)
                mColor = (ImageView) v.findViewById(R.id.color_item);
            if(item == SIZE)
                mSize = (TextView) v.findViewById(R.id.size_item);
        }
    }

    public String getResource(){
        return this.resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }
    /* This interface allow  a fargment to be informed about any changes within a field*/

    public interface OnFieldChangedListener {
        void onFieldChangedListener();
    }


    public void onFieldChanged() {
        if (mListener != null) {
            mListener.onFieldChangedListener();
        }
    }
}
