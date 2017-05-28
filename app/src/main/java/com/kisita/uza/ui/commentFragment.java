package com.kisita.uza.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.kisita.uza.R;
import com.kisita.uza.custom.CustomFragment;
import com.kisita.uza.model.Data;
import com.kisita.uza.utils.commentRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class commentFragment extends CustomFragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_ITEM_UID = "item-uid";
    private static final String TAG = "## CommentFragment";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private String mItemUid;
    private OnListFragmentInteractionListener mListener;
    private commentRecyclerViewAdapter mAdapter;
    private DatabaseReference mComments;
    private FirebaseStorage mStorage;
    private ArrayList<ArticleComment> mList;





    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public commentFragment() {
    }

    // TODO: Customize parameter initialization
    //@SuppressWarnings("unused")
    public static commentFragment newInstance(int columnCount,String uid) {
        commentFragment fragment = new commentFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putString(ARG_ITEM_UID, uid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            mItemUid = getArguments().getString(ARG_ITEM_UID);
        }
        mList = new ArrayList<>();
        mComments = getDb().child("items").child(mItemUid).child("comments");
        mStorage = FirebaseStorage.getInstance();

        mComments.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG,"Data added");
                if(dataSnapshot.hasChildren()) {
                    mList.clear();
                    for(DataSnapshot d :  dataSnapshot.getChildren()) {
                        if(d.child("text").getValue() != null && d.child("name").getValue() != null  && d.child("date").getValue() != null) {
                            mList.add(new ArticleComment(d.child("text").getValue().toString(),
                                    d.child("name").getValue().toString(),
                                    d.child("date").getValue().toString()));
                        }
                    }
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment_list, container, false);

        RecyclerView recList = (RecyclerView) view.findViewById(R.id.cardList);
        recList.setHasFixedSize(true);

        StaggeredGridLayoutManager llm = new StaggeredGridLayoutManager(1,
                StaggeredGridLayoutManager.HORIZONTAL);

        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        mAdapter = new commentRecyclerViewAdapter(mList,null);
        recList.setAdapter(mAdapter);


        EditText comment = (EditText)view.findViewById(R.id.comment);
        comment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Calendar c = Calendar.getInstance();
                    Map<String, Object> childUpdates = new HashMap<>();
                    // Do whatever you want here
                    String comment = getDb().child("users").push().getKey();
                    Log.i(TAG,"/items/" + mItemUid + "/comment/" + comment + "/text");
                    childUpdates.put("/items/" + mItemUid + "/comments/" + comment+ "/text",v.getText().toString());
                    childUpdates.put("/items/" + mItemUid + "/comments/" + comment + "/name", FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    childUpdates.put("/items/" + mItemUid + "/comments/" + comment + "/date", String.valueOf(c.get(Calendar.DATE))+"/"+String.valueOf(c.get(Calendar.MONTH))+"/"+String.valueOf(c.get(Calendar.YEAR)));
                    getDb().updateChildren(childUpdates);
                    v.setText("");
                }
                return false;
            }
        });

        setHasOptionsMenu(true);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(ArticleComment item);
    }

    /**
     * A article item representing a piece of content.
     */
    public static class ArticleComment {
        public final String text;
        public final String name;
        public final String date;

        public ArticleComment(String text, String name, String date) {
            this.text = text;
            this.name = name;
            this.date = date;
        }
    }
}
