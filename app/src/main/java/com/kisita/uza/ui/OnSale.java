package com.uza.ui;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.uza.MainActivity;
import com.uza.ProductDetail;
import com.uza.R;
import com.uza.custom.CustomFragment;
import com.uza.model.Data;

/**
 * The Class OnSale is the fragment that shows the products in GridView.
 */
public class OnSale extends CustomFragment
{

	/** The product list. */
	private ArrayList<Data> iList;

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@SuppressLint({ "InflateParams", "InlinedApi" })
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.on_sale, null);

		((MainActivity) getActivity()).toolbar.setTitle("On Sale");
		((MainActivity) getActivity()).toolbar.findViewById(
				R.id.spinner_toolbar).setVisibility(View.GONE);

		/*if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
		{
			getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}*/

		setHasOptionsMenu(true);
		setupView(v);
		return v;
	}

	/* (non-Javadoc)
	 * @see com.whatshere.custom.CustomFragment#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v)
	{
		super.onClick(v);
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
		loadDummyData();

		RecyclerView recList = (RecyclerView) v.findViewById(R.id.cardList);
		recList.setHasFixedSize(true);
		StaggeredGridLayoutManager llm = new StaggeredGridLayoutManager(2,
				StaggeredGridLayoutManager.VERTICAL);
		recList.setLayoutManager(llm);
		CardAdapter ca = new CardAdapter();
		recList.setAdapter(ca);
	}

	/**
	 * The Class CardAdapter is the adapter for showing products in Card format
	 * inside the RecyclerView. It shows dummy product image and dummy contents,
	 * so you need to display actual contents as per your need.
	 */
	private class CardAdapter extends
			RecyclerView.Adapter<CardAdapter.CardViewHolder>
	{

		/* (non-Javadoc)
		 * @see android.support.v7.widget.RecyclerView.Adapter#getItemCount()
		 */
		@Override
		public int getItemCount()
		{
			return iList.size();
		}

		/* (non-Javadoc)
		 * @see android.support.v7.widget.RecyclerView.Adapter#onBindViewHolder(android.support.v7.widget.RecyclerView.ViewHolder, int)
		 */
		@Override
		public void onBindViewHolder(CardViewHolder vh, int i)
		{

			Data d = iList.get(i);
			vh.lbl1.setText(d.getTexts()[0]);
			vh.lbl2.setText(d.getTexts()[1]);
			vh.lbl3.setText(d.getTexts()[2]);
			vh.img.setImageResource(d.getResources()[0]);
		}

		/* (non-Javadoc)
		 * @see android.support.v7.widget.RecyclerView.Adapter#onCreateViewHolder(android.view.ViewGroup, int)
		 */
		@Override
		public CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
		{
			View itemView = LayoutInflater.from(viewGroup.getContext())
					.inflate(R.layout.grid_item, viewGroup, false);
			itemView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v)
				{
					startActivity(new Intent(getActivity(), ProductDetail.class));
				}
			});
			return new CardViewHolder(itemView);
		}

		/**
		 * The Class CardViewHolder is the View Holder class for Adapter views.
		 */
		public class CardViewHolder extends RecyclerView.ViewHolder
		{

			/** The lbl3. */
			protected TextView lbl1, lbl2, lbl3;

			/** The img. */
			protected ImageView img;

			/**
			 * Instantiates a new card view holder.
			 * 
			 * @param v
			 *            the v
			 */
			public CardViewHolder(View v)
			{
				super(v);
				lbl1 = (TextView) v.findViewById(R.id.lbl1);
				lbl2 = (TextView) v.findViewById(R.id.lbl2);
				lbl3 = (TextView) v.findViewById(R.id.lbl3);
				img = (ImageView) v.findViewById(R.id.img);
			}
		}
	}

	/**
	 * Load dummy product data for displaying on the RecyclerView. You need to
	 * write your own code for loading real products from Web-service or API and
	 * displaying them on RecyclerView.
	 */
	private void loadDummyData()
	{
		ArrayList<Data> al = new ArrayList<Data>();
		al.add(new Data(new String[] { "Raven's Wing Field Note", "$200-$400",
				"Gear Patrol" }, new int[] { R.drawable.on_sale_item1 }));
		al.add(new Data(new String[] { "Y.M.C. Spray Tee (Grey)", "$67",
				"Oi Polloi" }, new int[] { R.drawable.on_sale_item2 }));
		al.add(new Data(new String[] { "Ally Capellino", "$94",
				"Ally Capellino" }, new int[] { R.drawable.on_sale_item3 }));
		al.add(new Data(new String[] { "Mid-Length Wool Cashmere Trench Coat",
				"$42", "Burberry US" }, new int[] { R.drawable.on_sale_item4 }));

		iList = new ArrayList<Data>(al);
		iList.addAll(al);
		iList.addAll(al);
		iList.addAll(al);
		iList.addAll(al);
		iList.addAll(al);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateOptionsMenu(android.view.Menu, android.view.MenuInflater)
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.search_exp, menu);
		menu.findItem(R.id.menu_grid).setVisible(false);
		super.onCreateOptionsMenu(menu, inflater);

	}
}
