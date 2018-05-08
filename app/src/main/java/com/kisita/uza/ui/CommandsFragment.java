package com.kisita.uza.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.kisita.uza.R;
import com.kisita.uza.custom.CustomFragment;
import com.kisita.uza.internal.BiLog;
import com.kisita.uza.model.Data;
import com.kisita.uza.utils.UzaCommandAdapter;

import java.util.ArrayList;


/*
 * A placeholder fragment containing a simple view.
 */
public class CommandsFragment extends CustomFragment {
    final static String TAG = "### CommandsFragment";
    /*The card adapter*/
    private UzaCommandAdapter mCardadapter;
    /*The list of items*/
    private ArrayList<Data> itemsList;

    public CommandsFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DetailFragment.
     */
    public static CommandsFragment newInstance(ArrayList<Data> data) {
        CommandsFragment f = new CommandsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ITEMS,data);
        f.setArguments(args);
        return f;
    }
    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            itemsList = (ArrayList<Data>) getArguments().getSerializable(ITEMS);
            BiLog.i(TAG,"*** Command received " + itemsList.size());
        }
        //printKeyHash();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_card, container, false);
        setupView(v);
        return v;
    }

    public void notifyChanges(ArrayList<Data> data) {
        if(mCardadapter != null) {
            mCardadapter.resetItemsList(data);
            mCardadapter.notifyDataSetChanged();
        }
    }

    private void setupView(View v)
    {

        RecyclerView recList = v.findViewById(R.id.cardList);
        recList.setHasFixedSize(true);


        StaggeredGridLayoutManager llm = new StaggeredGridLayoutManager(1,
                StaggeredGridLayoutManager.VERTICAL);
        setHasOptionsMenu(true);
        recList.setLayoutManager(llm);
        mCardadapter = new UzaCommandAdapter(this.getContext(),itemsList);
        recList.setAdapter(mCardadapter);
    }

    @Override
    public void onAttach(Context context) {
        BiLog.i(TAG,"Fragment attached. array size is ");
        super.onAttach(context);
    }
}
