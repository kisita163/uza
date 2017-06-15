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

    private FirebaseStorage mStorage;
    private StorageReference mStorageRef;

    private final OnListFragmentInteractionListener mPaymentListener;
    private final StoresFragment.OnFragmentInteractionListener mStoresListener;

    private final String TAG = "### UzaListAdapter";

    private Context mContext;

    public UzaListAdapter(Context context, List<UzaListItem> items, OnListFragmentInteractionListener listener) {
        mStorage = FirebaseStorage.getInstance();
        mValues = items;
        mPaymentListener = listener;
        mStoresListener = null;
        mContext = context;
    }

    public UzaListAdapter(Context context,List<UzaListItem> items, StoresFragment.OnFragmentInteractionListener listener) {
        mStorage = FirebaseStorage.getInstance();
        mValues = items;
        mStoresListener = listener;
        mPaymentListener = null;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String path = "gs://glam-afc14.appspot.com/merchants_icons/" + mValues.get(position).name + "/drawable-xxhdpi/"+mValues.get(position).name.toLowerCase()+".png";
        Log.i(TAG,path);
        mStorageRef = mStorage.getReferenceFromUrl("gs://glam-afc14.appspot.com/merchants_icons/" + mValues.get(position).name + "/drawable-xxhdpi/"+mValues.get(position).name.toLowerCase()+".png");
        holder.mItem = mValues.get(position);
        holder.mPaymentName.setText(mValues.get(position).name);
        if(mStoresListener == null) {
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
