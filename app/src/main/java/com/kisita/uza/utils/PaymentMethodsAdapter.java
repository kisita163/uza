package com.kisita.uza.utils;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kisita.uza.R;
import com.kisita.uza.ui.PaymentMethodsFragment.OnListFragmentInteractionListener;
import com.kisita.uza.ui.dummy.PaymentContent.PaymentItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PaymentItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class PaymentMethodsAdapter extends RecyclerView.Adapter<PaymentMethodsAdapter.ViewHolder> {

    private final List<PaymentItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public PaymentMethodsAdapter(List<PaymentItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
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
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
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
        public PaymentItem mItem;

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
