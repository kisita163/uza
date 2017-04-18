package com.uza.ui;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
 * The Class MainFragment is the base fragment that shows the list of various
 * products. You can add your code to do whatever you want related to products
 * for your app.
 */
public class MainFragment extends CustomFragment
{

	/** The product list. */
	private ArrayList<Data> iList;

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.main_container, null);
		((MainActivity) getActivity()).toolbar.findViewById(
				R.id.spinner_toolbar).setVisibility(View.VISIBLE);

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

		initPager(v);
	}

	/**
	 * Inits the pager view.
	 * 
	 * @param v
	 *            the root view
	 */
	private void initPager(View v)
	{
		ViewPager pager = (ViewPager) v.findViewById(R.id.pager);
		pager.setPageMargin(10);

		pager.setAdapter(new PageAdapter());
	}

	/**
	 * The Class PageAdapter is adapter class for ViewPager and it simply holds
	 * a RecyclerView with dummy images. You need to write logic for loading
	 * actual images.
	 */
	private class PageAdapter extends PagerAdapter
	{

		/* (non-Javadoc)
		 * @see android.support.v4.view.PagerAdapter#getCount()
		 */
		@Override
		public int getCount()
		{
			return 5;
		}

		/* (non-Javadoc)
		 * @see android.support.v4.view.PagerAdapter#instantiateItem(android.view.ViewGroup, int)
		 */
		@Override
		public Object instantiateItem(ViewGroup container, int pos)
		{
			final View v = getLayoutInflater(null).inflate(
					R.layout.pager_card_view, null);

			RecyclerView recList = (RecyclerView) v.findViewById(R.id.cardList);
			recList.setHasFixedSize(true);
			LinearLayoutManager llm = new LinearLayoutManager(getActivity());
			llm.setOrientation(LinearLayoutManager.VERTICAL);
			recList.setLayoutManager(llm);

			CardAdapter ca = new CardAdapter();
			recList.setAdapter(ca);

			((MainActivity) getActivity()).enableActionBarAutoHide(recList);
			container.addView(v,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT);
			return v;
		}

		/* (non-Javadoc)
		 * @see android.support.v4.view.PagerAdapter#destroyItem(android.view.ViewGroup, int, java.lang.Object)
		 */
		@Override
		public void destroyItem(ViewGroup container, int position, Object object)
		{
			try
			{
				// super.destroyItem(container, position, object);
				// if(container.getChildAt(position)!=null)
				// container.removeViewAt(position);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		/* (non-Javadoc)
		 * @see android.support.v4.view.PagerAdapter#isViewFromObject(android.view.View, java.lang.Object)
		 */
		@Override
		public boolean isViewFromObject(View arg0, Object arg1)
		{
			return arg0 == arg1;
		}

		/* (non-Javadoc)
		 * @see android.support.v4.view.PagerAdapter#getPageTitle(int)
		 */
		@Override
		public CharSequence getPageTitle(int position)
		{
			if (position == 0)
				return "MEN";
			if (position == 1)
				return "WOMEN";
			if (position == 2)
				return "KIDS";
			if (position == 3)
				return "TRADITIONAL";
			if (position == 4)
				return "SPECIAL";
			return "Page-" + position;
		}

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
			vh.lbl1.setVisibility(d.getTexts()[0] == null ? View.GONE
					: View.VISIBLE);
			vh.lbl2.setText(d.getTexts()[1]);
			vh.lbl3.setText(d.getTexts()[2]);
			vh.lbl4.setText(d.getTexts()[3]);
			vh.img.setImageResource(d.getResources()[0]);
		}

		/* (non-Javadoc)
		 * @see android.support.v7.widget.RecyclerView.Adapter#onCreateViewHolder(android.view.ViewGroup, int)
		 */
		@Override
		public CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
		{
			View itemView = LayoutInflater.from(viewGroup.getContext())
					.inflate(R.layout.card_item, viewGroup, false);
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

			/** The textviews. */
			protected TextView lbl1, lbl2, lbl3, lbl4;

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
				lbl4 = (TextView) v.findViewById(R.id.lbl4);
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
		al.add(new Data(new String[] { null,
				"Ally Capellino Frank Brown - Fott Shop 2014", "$200-$400",
				"Shop.fott.com" }, new int[] { R.drawable.popularity_img1 }));
		al.add(new Data(new String[] { "50%\nOFF", "Tap & DYE Legacy", "$67",
				"Tapanddye" }, new int[] { R.drawable.popularity_img2 }));
		al.add(new Data(new String[] { null, "Piper Felt Hat by Brixton",
				"$94", "Tapanddye" }, new int[] { R.drawable.popularity_img3 }));
		al.add(new Data(new String[] { null, "HIKE Abysss Stone", "$42",
				"Handwers" }, new int[] { R.drawable.popularity_img4 }));
		al.add(new Data(new String[] { "20%\nOFF", "Lenovo Leather Belt",
				"$12", "Lenovo" }, new int[] { R.drawable.popularity_img5 }));

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
		super.onCreateOptionsMenu(menu, inflater);

	}
}
