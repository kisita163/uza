package com.kisita.uza.ui;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kisita.uza.R;
import com.kisita.uza.custom.CustomFragment;
import com.kisita.uza.model.UzaListItem;
import com.kisita.uza.ui.FixedContents.BooksContent;
import com.kisita.uza.ui.FixedContents.ElectronicsContent;
import com.kisita.uza.ui.FixedContents.KidsContent;
import com.kisita.uza.ui.FixedContents.SettingsContent;
import com.kisita.uza.utils.UzaListAdapter;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ChoicesFragment extends CustomFragment {

    private OnItemSelectedListener mListener;

    final static String QUERY = "QUERY";
    private String mQuery;

    public ChoicesFragment() {
    }


    public static ChoicesFragment newInstance(String query) {
        ChoicesFragment fragment = new ChoicesFragment();
        Bundle args = new Bundle();
        args.putString(QUERY, query);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mQuery = getArguments().getString(QUERY);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_choices, container, false);

        setHasOptionsMenu(true);
        // Set the adapter
        Context context = view.getContext();
        List<UzaListItem> list = null;
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.storeList);
        recyclerView.setLayoutManager(new GridLayoutManager(context,1));

        DividerItemDecoration dividerVertical = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);

        recyclerView.addItemDecoration(dividerVertical);
        Log.i("ChoiceActiviyFragment","listener passed to adapter");

        if(mQuery.equalsIgnoreCase(getString(R.string.settings))){
            list = SettingsContent.ITEMS;
        }else if(mQuery.equalsIgnoreCase(getString(R.string.women))){
            list = SettingsContent.ITEMS;
        }else if(mQuery.equalsIgnoreCase(getString(R.string.kids))){
            list = KidsContent.ITEMS;
        }else if(mQuery.equalsIgnoreCase(getString(R.string.electronic))){
            list = ElectronicsContent.ITEMS;
        }else if(mQuery.equalsIgnoreCase(getString(R.string.books))){
            list = BooksContent.ITEMS;
        }
        if(list != null)
            recyclerView.setAdapter(new UzaListAdapter(context, list,mListener));

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnItemSelectedListener {
        void onChoiceMadeListener(String name);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void foodSelected(String name) {
        if (mListener != null) {
            mListener.onChoiceMadeListener(name);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnItemSelectedListener) {
            mListener = (OnItemSelectedListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnItemSelectedListener");
        }
    }
}
