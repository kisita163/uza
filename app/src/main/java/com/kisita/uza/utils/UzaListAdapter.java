package com.kisita.uza.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kisita.uza.R;
import com.kisita.uza.model.UzaListItem;
import com.kisita.uza.ui.ChoicesFragment;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link UzaListItem} and makes a call to the
 * specified listener.
 *
 */
public class UzaListAdapter extends RecyclerView.Adapter<UzaListAdapter.ViewHolder> {

    private final List<UzaListItem> mValues;
    private ChoicesFragment.OnItemSelectedListener mChoiceListener = null;


    private final String TAG = "### UzaListAdapter";
    private Context mContext;

    public UzaListAdapter(Context context, List<UzaListItem> items, ChoicesFragment.OnItemSelectedListener listener) {
        this.mValues = items;
        this.mContext = context;
        this.mChoiceListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final String name = mContext.getString( mValues.get(position).name);

        holder.mItem = mValues.get(position);
        holder.mItemName.setText(name);
        // Check which fragment we are working on
        holder.mItemIcon.setImageResource(mValues.get(position).icon);//.
        if(!name.equalsIgnoreCase(mContext.getString(R.string.currency)))
            holder.mCurrentCurrency.setVisibility(View.INVISIBLE);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"Item selected");


                if (null != mChoiceListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    Log.i(TAG,"");
                    mChoiceListener.onChoiceMadeListener(name);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final ImageView mItemIcon;
        final TextView mItemName;
        final TextView mCurrentCurrency;
        UzaListItem mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mItemName = (TextView) view.findViewById(R.id.payment_name);
            mItemIcon = (ImageView) view.findViewById(R.id.payment_icon);
            mCurrentCurrency = (TextView)view.findViewById(R.id.current_currency);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mItemName.getText() + "'";
        }
    }
}
