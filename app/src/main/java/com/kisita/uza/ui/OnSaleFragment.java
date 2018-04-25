package com.kisita.uza.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.kisita.uza.R;
import com.kisita.uza.custom.CustomFragment;
import com.kisita.uza.model.Data;
import com.kisita.uza.utils.UzaCardAdapter;
import java.util.ArrayList;

/*
 * The Class OnSaleFragment is the fragment that shows the products in GridView.
 */
public class OnSaleFragment extends CustomFragment
{
	//final static String TAG = "### OnSaleFragment";

	private UzaCardAdapter mCardAdapter;

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)cd
	 */

	public OnSaleFragment() {
		// Required empty public constructor
	}

	public static OnSaleFragment newInstance(ArrayList<Data> itemsList) {
		OnSaleFragment fragment = new OnSaleFragment();
		Bundle args = new Bundle();
		args.putSerializable(ITEMS,itemsList);
		fragment.setArguments(args);
		return fragment;
	}

	@SuppressLint({ "InflateParams", "InlinedApi" })
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.fragment_onsale, null);
		setHasOptionsMenu(true);
		setupView(v);
		return v;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			itemsList = (ArrayList<Data>) getArguments().getSerializable(ITEMS);
		}
	}

	/**
	 * Setup the view components for this fragment. You write your code for
	 * initializing the views, setting the adapters, touch and click listeners
	 * etc.
	 * 
	 * @param v
	 *            the base view of fragment
	 */

	private void setupView(View v)
	{

        RecyclerView mRecList = v.findViewById(R.id.cardList);

		mRecList.setHasFixedSize(true);

		StaggeredGridLayoutManager llm = new StaggeredGridLayoutManager(1,
				StaggeredGridLayoutManager.VERTICAL);

		mRecList.setLayoutManager(llm);

		mCardAdapter = new UzaCardAdapter(this.getContext(),itemsList);
		mRecList.setAdapter(mCardAdapter);
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void notifyChanges(ArrayList<Data> data){
		if(mCardAdapter != null) {
			mCardAdapter.resetItemsList(data);
			mCardAdapter.notifyDataSetChanged();
		}
	}
}
