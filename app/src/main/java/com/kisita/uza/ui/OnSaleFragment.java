package com.kisita.uza.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.ForwardingListener;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kisita.uza.R;
import com.kisita.uza.activities.ChoicesActivity;
import com.kisita.uza.custom.CustomFragment;
import com.kisita.uza.listerners.ItemChildEventListener;
import com.kisita.uza.model.Data;
import com.kisita.uza.utils.UzaCardAdapter;

import java.util.ArrayList;

import static com.kisita.uza.model.Data.UZA.TYPE;


/**
 * The Class OnSaleFragment is the fragment that shows the products in GridView.
 */
public class OnSaleFragment extends CustomFragment
{
	final static String TAG = "### OnSaleFragment";
	final static String QUERY = "QUERY";
	private static final int RESULT_CODE = 1;
	private UzaCardAdapter mCardadapter;
	private ArrayList<Data> itemsList;

	private DatabaseReference mDatabase;
	private ItemChildEventListener mChildEventListener;
	private String mQuery;

	private FloatingActionButton mMenu0;
	private FloatingActionButton mMenu1;
	private FloatingActionButton mMenu2;
	private FloatingActionButton mMenu3;
	private FloatingActionButton mMenu5;
    private FloatingActionMenu  fabMenu;
    private android.support.design.widget.FloatingActionButton foodButton;

	private OnFragmentInteractionListener mListener;

	private String[]  mTypes;

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */

	public OnSaleFragment() {
		// Required empty public constructor
	}

	public static OnSaleFragment newInstance(String query) {
		OnSaleFragment fragment = new OnSaleFragment();
		Bundle args = new Bundle();
		args.putString(QUERY, query);
		fragment.setArguments(args);
		return fragment;
	}

	@SuppressLint({ "InflateParams", "InlinedApi" })
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.fragment_onsale, null);
		setHasOptionsMenu(true);
		setupView(v);
		return v;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mQuery = getArguments().getString(QUERY);
		}
	}

	public void setupFab(){
		switch (mQuery) {
			case "Men":
			case "Women":
				mMenu0.setLabelText("All");
				mMenu1.setLabelText("Clothing");
				mMenu2.setLabelText("Shoes & Bags");
				mMenu3.setLabelText("Watches & Accessories");
				mMenu5.setLabelText("Perfumes & Beauty");
				mTypes = new String []{"Clothing","Shoes & Bags","Watches & Accessories","Perfumes & Beauty"};
				break;
			case "Kids":
				mMenu0.setLabelText("All");
				mMenu1.setLabelText("Clothing");
				mMenu1.setImageResource(R.drawable.toys_baby);

				mMenu2.setLabelText("Shoes & Bags");
				mMenu2.setImageResource(R.drawable.baby_shoes);

				mMenu3.setLabelText("Toys & Accessories");
				mMenu3.setImageResource(R.drawable.clothing_baby);

				mMenu5.setLabelText("Bathing  & Skin care");
				mMenu5.setImageResource(R.drawable.bath_baby);
				mTypes = new String []{"Clothing","Shoes & Bags","Toys & Accessories","Bathing  & Skin care"};
				break;
			case "Electronic":
				mMenu0.setLabelText("All");

				mMenu1.setLabelText("Home");
				mMenu1.setImageResource(R.drawable.light);

				mMenu2.setLabelText("Video games");
				mMenu2.setImageResource(R.drawable.game);

				mMenu3.setLabelText("Phones & Accessories");
				mMenu3.setImageResource(R.drawable.phone);

				mMenu5.setLabelText("Computers & Tablets");
				mMenu5.setImageResource(R.drawable.laptop);
				mTypes = new String []{"Home","Video games","Phones & Accessories","Computers & Tablets"};
				break;
			case "Home":
				mMenu0.setLabelText("All");

				mMenu1.setLabelText("Living & Dining room");
				mMenu1.setImageResource(R.drawable.livingroom);

				mMenu2.setLabelText("Bedroom");
				mMenu2.setImageResource(R.drawable.bedroom);

				mMenu3.setLabelText("Kitchen & Bath room");
				mMenu3.setImageResource(R.drawable.kitchen);

				mMenu5.setLabelText("Garden");
				mMenu5.setImageResource(R.drawable.garden);

				mTypes = new String []{"Living & Dining room","Bedroom","Kitchen & Bath room","Garden"};
				break;
            case "Food":
                fabMenu.setVisibility(View.GONE);
                break;
			case "Commands":
				fabMenu.setVisibility(View.GONE);
				foodButton.setVisibility(View.GONE);
				break;
		}
	}

	/* (non-Javadoc)
	 * @see com.whatshere.custom.CustomFragment#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v)
	{
		ArrayList<Data> tmpList = new ArrayList<>();
		String s = "Others";
		switch (v.getId()){
			case (R.id.menu_0):
				mCardadapter.setItemsList(itemsList);
				mCardadapter.notifyDataSetChanged();
				return;
			case(R.id.menu_1): // get clothes
				s = mTypes[0];
				break;
			case(R.id.menu_2):
				s = mTypes[1];
				break;
			case(R.id.menu_3):
				s = mTypes[2];
				break;
			case(R.id.menu_5):
				s = mTypes[3];
				break;
		}
		for (Data d : itemsList) {
			Log.i(TAG, "Item clicked. type = " + d.getTexts()[TYPE] + " selected is  : "+s);
			if (d.getTexts()[TYPE].equalsIgnoreCase(s)) {
				tmpList.add(d);
			}
		}
		mCardadapter.setItemsList(tmpList);
		mCardadapter.notifyDataSetChanged();
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

		RecyclerView recList = (RecyclerView) v.findViewById(R.id.cardList);

		mMenu0 = (FloatingActionButton) v.findViewById(R.id.menu_0);
		mMenu1 = (FloatingActionButton)v.findViewById(R.id.menu_1);
		mMenu2 = (FloatingActionButton)v.findViewById(R.id.menu_2);
		mMenu3 = (FloatingActionButton)v.findViewById(R.id.menu_3);
		mMenu5 = (FloatingActionButton)v.findViewById(R.id.menu_5);

        fabMenu= (FloatingActionMenu)v.findViewById(R.id.menu_labels_right) ;

		foodButton = (android.support.design.widget.FloatingActionButton)v.findViewById(R.id.fabCart);

		if(mQuery.equalsIgnoreCase("food")){
			foodButton.setVisibility(View.VISIBLE);
			foodButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					startActivityForResult(new Intent(getActivity(), ChoicesActivity.class),1);
				}
			});
		}else{
			foodButton.setVisibility(View.GONE);
		}

		mMenu0.setOnClickListener(this);
		mMenu1.setOnClickListener(this);
		mMenu2.setOnClickListener(this);
		mMenu3.setOnClickListener(this);
		mMenu5.setOnClickListener(this);

		setupFab();
		itemsList = new ArrayList<>();
		recList.setHasFixedSize(true);


		StaggeredGridLayoutManager llm = new StaggeredGridLayoutManager(2,
				StaggeredGridLayoutManager.VERTICAL);

		recList.setLayoutManager(llm);
		mCardadapter = new UzaCardAdapter(this.getContext(),itemsList);
		recList.setAdapter(mCardadapter);
		if(mQuery.equalsIgnoreCase("Commands")){
			showProgressDialog();
			loadCommandData();
		}else{
			loadData();
		}
	}

	/*load command data */
	private void  loadCommandData()
	{
		// get the seller id
		mDatabase = FirebaseDatabase.getInstance().getReference();
		mDatabase.keepSynced(true);

		final Query itemsQuery = getCommandsQuery(mDatabase);
		itemsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				itemsList.clear();
				for(DataSnapshot d : dataSnapshot.getChildren()){
					//Log.i(TAG,d.child("key").getValue().toString());
					getDb().child("items").child(d.child("key").getValue().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
						@Override
						public void onDataChange(DataSnapshot dataSnapshot) {
							//Log.i(TAG,dataSnapshot.toString());
							ItemChildEventListener.initCommandlist(dataSnapshot,itemsList,"All");
							mCardadapter.notifyDataSetChanged();
							new Thread(new Runnable() {
								@Override
								public void run()
								{
									try
									{
										Thread.sleep(2000);

									} catch (Exception e)
									{
										e.printStackTrace();
									} finally
									{
										getActivity().runOnUiThread(new Runnable() {
											@Override
											public void run()
											{
												hideProgressDialog();
											}
										});
									}
								}
							}).start();
						}

						@Override
						public void onCancelled(DatabaseError databaseError) {

						}
					});
				}
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {

			}
		});
	}

	/**
	 * Load  product data for displaying on the RecyclerView.
	 */
	private void  loadData()
	{
		SharedPreferences sharedPref = getContext().getSharedPreferences(getContext().getResources().getString(R.string.uza_keys),
				Context.MODE_PRIVATE);
		final String store = sharedPref.getString(getContext().getString(R.string.uza_store),"All");
		mDatabase = FirebaseDatabase.getInstance().getReference();
		mDatabase.keepSynced(true);

		final Query itemsQuery = getQuery(mDatabase);
        itemsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemsList.clear();
				//Log.i(TAG,dataSnapshot.toString());
                ItemChildEventListener.initItemlist(dataSnapshot,itemsList,store);
                mCardadapter.notifyDataSetChanged();
                mChildEventListener = new ItemChildEventListener(itemsList, mCardadapter,store);
                itemsQuery.addChildEventListener(mChildEventListener);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
	}

	public Query getQuery(DatabaseReference databaseReference) {
			return databaseReference.child("items")
					.orderByChild("category")
					.startAt(mQuery)
					.endAt(mQuery);
	}

	public Query getCommandsQuery(DatabaseReference databaseReference) {
		return databaseReference.child("commands")
				.orderByChild("seller")
				.startAt("Kitea")
				.endAt("Kitea");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mChildEventListener = null;
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
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		void onFragmentInteraction();
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onStoresPressed() {
		if (mListener != null) {
			mListener.onFragmentInteraction();
		}
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof OnFragmentInteractionListener) {
			mListener = (OnFragmentInteractionListener) context;
		} else {
			throw new RuntimeException(context.toString()
					+ " must implement OnNewArticleInteractionListener");
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==RESULT_CODE)
		{
			String food_type=data.getStringExtra(getString(R.string.food_type));
			Log.i(TAG,"received food type is  : "+food_type);
		}
	}
}
