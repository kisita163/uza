package com.kisita.uza.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.kisita.uza.R;
import com.kisita.uza.ui.CommentFragment;
import com.kisita.uza.ui.CommentFragment.OnListFragmentInteractionListener;

import java.util.ArrayList;
import java.util.List;

public class UzaCommentAdapter extends RecyclerView.Adapter<UzaCommentAdapter.ViewHolder> {

    private final List<CommentFragment.ArticleComment> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final Context mContext;
    private final StaggeredGridLayoutManager mGridManager;

    public UzaCommentAdapter(ArrayList<CommentFragment.ArticleComment> items, OnListFragmentInteractionListener listener, Context context, StaggeredGridLayoutManager llm) {
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
        mGridManager.scrollToPosition(getItemCount()-1);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mIdView;
        final TextView mContentView;
        final LinearLayout mIdLayout;
        CommentFragment.ArticleComment mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = view.findViewById(R.id.id);
            mContentView =  view.findViewById(R.id.content);
            mIdLayout = view.findViewById(R.id.llayout);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
