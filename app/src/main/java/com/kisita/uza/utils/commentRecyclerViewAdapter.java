package com.kisita.uza.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.kisita.uza.R;
import com.kisita.uza.ui.commentFragment;
import com.kisita.uza.ui.commentFragment.OnListFragmentInteractionListener;

import java.util.ArrayList;
import java.util.List;

public class commentRecyclerViewAdapter extends RecyclerView.Adapter<commentRecyclerViewAdapter.ViewHolder> {

    private final List<commentFragment.ArticleComment> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final Context mContext;
    private final StaggeredGridLayoutManager mGridManager;

    public commentRecyclerViewAdapter(ArrayList<commentFragment.ArticleComment> items, OnListFragmentInteractionListener listener, Context context, StaggeredGridLayoutManager llm) {
        mValues = items;
        mListener = listener;
        mContext = context;
        mGridManager  = llm;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).name);
        holder.mContentView.setText(mValues.get(position).text);
        if(FirebaseAuth.getInstance().getCurrentUser().getEmail().equalsIgnoreCase(mValues.get(position).name)){
            holder.mIdLayout.setBackgroundColor(mContext.getResources().getColor(R.color.comment_dk));
        }
        //Log.i("GRID",">Grid set at offset position : "+getItemCount());
        mGridManager.scrollToPosition(getItemCount()-1);

        /*holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final LinearLayout mIdLayout;
        public commentFragment.ArticleComment mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            mIdLayout = (LinearLayout) view.findViewById(R.id.llayout);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
