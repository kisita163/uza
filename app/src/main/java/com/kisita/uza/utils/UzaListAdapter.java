package com.kisita.uza.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kisita.uza.R;
import com.kisita.uza.model.UzaListItem;
import com.kisita.uza.ui.ChoicesActivityFragment;
import com.kisita.uza.ui.PaymentMethodsFragment;
import com.kisita.uza.ui.PaymentMethodsFragment.OnPaymentListFragmentInteractionListener;
import com.kisita.uza.ui.StoresFragment;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link UzaListItem} and makes a call to the
 * specified {@link OnPaymentListFragmentInteractionListener}.
 *
 */
public class UzaListAdapter extends RecyclerView.Adapter<UzaListAdapter.ViewHolder> {

    private final List<UzaListItem> mValues;
    private ChoicesActivityFragment.OnFoodSelectedListener mFoodListener = null;
    private StorageReference mStorageRef = null;

    private PaymentMethodsFragment.OnPaymentListFragmentInteractionListener mPaymentListener = null;
    private StoresFragment.OnFragmentInteractionListener mStoresListener = null;

    private final String TAG = "### UzaListAdapter";
    private Context mContext;
    private String mAmount = "0.0";

    public UzaListAdapter(Context context, List<UzaListItem> items, OnPaymentListFragmentInteractionListener listener, String amount) {
        mValues          = items;
        mPaymentListener = listener;
        mStoresListener  = null;
        mContext         = context;
        mAmount          = amount;
    }

    public UzaListAdapter(Context context,List<UzaListItem> items, StoresFragment.OnFragmentInteractionListener listener) {
        mValues = items;
        mStoresListener = listener;
        mPaymentListener = null;
        mContext = context;
    }

    public UzaListAdapter(Context context, List<UzaListItem> items, ChoicesActivityFragment.OnFoodSelectedListener listener) {
        this.mValues = items;
        this.mContext = context;
        this.mFoodListener = listener;
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
        holder.mPaymentName.setText(name);
        // Check which fragment we are working on
        if(mStoresListener == null) { // Case of payment fragment or food choices fragment
            holder.mPaymentIcon.setImageResource(mValues.get(position).icon);//.
        }else {
            Glide.with(mContext)
                    .using(new FirebaseImageLoader())
                    .load(mStorageRef)
                    .dontTransform()
                    .error(R.drawable.ic_store_black_24dp)
                    .into(holder.mPaymentIcon);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"Item selected");
                if (null != mPaymentListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mPaymentListener.onPayementListFragmentInteraction(holder.mItem,mAmount);
                }

                if (null != mFoodListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mFoodListener.onChoiceMadeListener(name);
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
