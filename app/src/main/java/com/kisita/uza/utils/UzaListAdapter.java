package com.kisita.uza.utils;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kisita.uza.R;
import com.kisita.uza.model.UzaListItem;
import com.kisita.uza.ui.PaymentMethodsFragment.OnListFragmentInteractionListener;
import com.kisita.uza.ui.StoresFragment;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link UzaListItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 *
 */
public class UzaListAdapter extends RecyclerView.Adapter<UzaListAdapter.ViewHolder> {

    private final List<UzaListItem> mValues;

    private final OnListFragmentInteractionListener mPaymentListener;
    private final StoresFragment.OnFragmentInteractionListener mStoresListener;

    public UzaListAdapter(List<UzaListItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mPaymentListener = listener;
        mStoresListener = null;
    }

    public UzaListAdapter(List<UzaListItem> items, StoresFragment.OnFragmentInteractionListener listener) {
        mValues = items;
        mStoresListener = listener;
        mPaymentListener = null;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mPaymentName.setText(mValues.get(position).name);
        holder.mPaymentIcon.setImageResource(mValues.get(position).icon);//.                     setText(mValues.get(position).content);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mPaymentListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mPaymentListener.onListFragmentInteraction(holder.mItem);
                }

                if (null != mStoresListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mStoresListener.onFragmentInteraction();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mPaymentIcon;
        public final TextView mPaymentName;
        public UzaListItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mPaymentName = (TextView) view.findViewById(R.id.payment_name);
            mPaymentIcon = (ImageView) view.findViewById(R.id.payment_icon);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mPaymentName.getText() + "'";
        }
    }
}
