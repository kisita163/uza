package com.kisita.uza.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.kisita.uza.R;
import com.kisita.uza.custom.CustomFragment;
import com.kisita.uza.model.UzaListItem;
import com.kisita.uza.ui.FixedContents.FoodContent;
import com.kisita.uza.ui.FixedContents.PaymentContent;
import com.kisita.uza.utils.UzaListAdapter;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ChoicesActivityFragment extends Fragment {

    private OnFoodSelectedListener mListener;

    public ChoicesActivityFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_choices, container, false);

        setHasOptionsMenu(true);
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new GridLayoutManager(context,1));

            DividerItemDecoration dividerVertical = new DividerItemDecoration(recyclerView.getContext(),
                    DividerItemDecoration.VERTICAL);

            recyclerView.addItemDecoration(dividerVertical);
            Log.i("ChoiceActiviyFragment","listener passed to adapter");
            recyclerView.setAdapter(new UzaListAdapter(context,FoodContent.ITEMS,mListener));
        }
        return view;
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFoodSelectedListener {
        // TODO: Update argument type and name
        void onFoodSelectedListener(String name);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void foodSelected(String name) {
        if (mListener != null) {
            mListener.onFoodSelectedListener(name);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnFoodSelectedListener) {
            mListener = (OnFoodSelectedListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnFoodSelectedListener");
        }
    }
}
