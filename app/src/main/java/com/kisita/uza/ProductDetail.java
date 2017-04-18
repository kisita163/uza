package com.uza;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.uza.custom.CustomActivity;

/**
 * The Activity ProductDetail is launched when user select a product item from
 * product list or grid views in other sections of the app. Currently it shows
 * Dummy details of product with dummy pics. You need to write your own code to
 * load and display actual contents.
 */
public class ProductDetail extends CustomActivity
{

	/** The pager. */
	private ViewPager pager;

	/** The view that hold dots. */
	private LinearLayout vDots;

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pr_detail);

		setupView();
	}

	/**
	 * Setup the click & other events listeners for the view components of this
	 * screen. You can add your logic for Binding the data to TextViews and
	 * other views as per your need.
	 */
	private void setupView()
	{
		setTouchNClick(R.id.fabCart);
		setTouchNClick(R.id.btnLike);
		setTouchNClick(R.id.btnComment);
		setTouchNClick(R.id.btnMore);

		getSupportActionBar().setTitle("Product Detail");

		initPager();
	}

	/**
	 * Inits the pager view.
	 */
	private void initPager()
	{
		pager = (ViewPager) findViewById(R.id.pager);
		pager.setPageMargin(10);

		pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int pos)
			{
				if (vDots == null || vDots.getTag() == null)
					return;
				((ImageView) vDots.getTag())
						.setImageResource(R.drawable.dot_gray);
				((ImageView) vDots.getChildAt(pos))
						.setImageResource(R.drawable.dot_blue);
				vDots.setTag(vDots.getChildAt(pos));
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2)
			{
			}

			@Override
			public void onPageScrollStateChanged(int arg0)
			{
			}
		});
		vDots = (LinearLayout) findViewById(R.id.vDots);

		pager.setAdapter(new PageAdapter());
		setupDotbar();
	}

	/**
	 * Setup the dotbar to show dots for pages of view pager with one dot as
	 * selected to represent current page position.
	 */
	private void setupDotbar()
	{
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		param.setMargins(10, 0, 0, 0);
		vDots.removeAllViews();
		for (int i = 0; i < 5; i++)
		{
			ImageView img = new ImageView(this);
			img.setImageResource(i == 0 ? R.drawable.dot_blue
					: R.drawable.dot_gray);
			vDots.addView(img, param);
			if (i == 0)
			{
				vDots.setTag(img);
			}

		}
	}

	/**
	 * The Class PageAdapter is adapter class for ViewPager and it simply holds
	 * a Single image view with dummy images. You need to write logic for
	 * loading actual images.
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
		public Object instantiateItem(ViewGroup container, int arg0)
		{
			final ImageView img = (ImageView) getLayoutInflater().inflate(
					R.layout.img, null);

			img.setImageResource(R.drawable.product_detail_bottom_banner);

			container.addView(img,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT);
			return img;
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

	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.cart, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/* (non-Javadoc)
	 * @see com.kisita.uza.custom.CustomActivity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.menu_cart)
			startActivity(new Intent(this, CheckoutActivity.class));
		return super.onOptionsItemSelected(item);
	}
}
