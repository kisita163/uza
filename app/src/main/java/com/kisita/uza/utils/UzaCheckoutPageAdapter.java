package com.kisita.uza.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kisita.uza.R;
import com.kisita.uza.activities.UzaActivity;
import com.kisita.uza.model.Data;
import com.kisita.uza.ui.CheckoutFragment;

import java.util.ArrayList;

import static com.kisita.uza.utils.UzaFunctions.getCurrency;


/*
 * Created by Hugues on 27/04/2017.
 */

public class UzaCheckoutPageAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {

    private static final String TAG = "### UzaCheckoutAdapter";

    private Context mContext;

    /** Product images. */
    private ArrayList<Data> mItemList;

    /** The view that hold dots. */
    private LinearLayout vDots;

    private CheckoutFragment mFragment;

    public UzaCheckoutPageAdapter(Context context, ArrayList<Data> itemList, LinearLayout dotLayout, CheckoutFragment fragment) {
        this.mContext = context;
        this.mItemList= itemList;
        this.vDots    = dotLayout;
        this.mFragment = fragment;
        setupDotBar();
    }

    /* (non-Javadoc)
         * @see android.support.v4.view.PagerAdapter#getCount()
         */
    @Override
    public int getCount()
    {
        if(mItemList != null) {
            return mItemList.size();
        }else
            return 0;
    }

    /* (non-Javadoc)
     * @see android.support.v4.view.PagerAdapter#instantiateItem(android.view.ViewGroup, int)
     */
    @Override
    public Object instantiateItem(ViewGroup container, final int arg0)
    {
        final View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.item_checkout_description, container, false);

        String priceWithCurrency = mItemList.get(arg0).getPrice() + " " + getCurrency(mContext);

        container.addView(view,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT);

        ImageView remove   = view.findViewById(R.id.favourite);
        remove.setImageResource(R.drawable.ic_action_close);

        TextView  author   = view.findViewById(R.id.item_author);
        TextView  name     = view.findViewById(R.id.item_name);
        TextView  size     = view.findViewById(R.id.item_size);
        TextView  type     = view.findViewById(R.id.item_type);
        TextView  price    = view.findViewById(R.id.item_price);

        ImageView picture  = view.findViewById(R.id.ticket_image);


        Glide.with(mContext)
                .load(mItemList.get(arg0).getPictures().get(mItemList.get(arg0).getPictures().size() - 1))
                .fitCenter()
                .error(R.drawable.ic_action_logo)
                .into(picture);

        author.setText(mItemList.get(arg0).getAuthor());
        name.setText(mItemList.get(arg0).getName());
        type.setText(mItemList.get(arg0).getType());
        price.setText(priceWithCurrency);
        size.setText(mItemList.get(arg0).getSize());




        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFragment.onRemovePressedListener(mItemList.get(arg0));
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDetailFragment(arg0);
            }
        });
        return view;
    }

    /* (non-Javadoc)
     * @see android.support.v4.view.PagerAdapter#destroyItem(android.view.ViewGroup, int, java.lang.Object)
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
    }

    /* (non-Javadoc)
     * @see android.support.v4.view.PagerAdapter#isViewFromObject(android.view.View, java.lang.Object)
     */
    @Override
    public boolean isViewFromObject(View arg0, Object arg1)
    {
        return arg0 == arg1;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }



    @Override
    public void onPageSelected(int position) {
        if (vDots == null || vDots.getTag() == null)
            return;
        ((ImageView) vDots.getTag())
                .setImageResource(R.drawable.dot_gray);
        ((ImageView) vDots.getChildAt(position))
                .setImageResource(R.drawable.dot_black);
        vDots.setTag(vDots.getChildAt(position));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * Setup the dotbar to show dots for pages of view pager with one dot as
     * selected to represent current page position.
     */
    private void setupDotBar()
    {
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        param.setMargins(10, 0, 0, 0);
        vDots.removeAllViews();
        Log.i(TAG,"The current list size is  : "+mItemList.size());
        for (int i = 0; i < mItemList.size(); i++)
        {
            ImageView img = new ImageView(mContext);
            img.setImageResource(i == 0 ? R.drawable.dot_black
                    : R.drawable.dot_gray);
            vDots.addView(img, param);
            if (i == 0)
            {
                vDots.setTag(img);
            }
        }
    }

    @Override
    public void notifyDataSetChanged() {
        setupDotBar();
        super.notifyDataSetChanged();
    }

    private void openDetailFragment(int position) {
        Intent intent = new Intent(mContext, UzaActivity.class);
        intent.putExtra("fragment", 3);
        intent.putExtra("details", mItemList.get(position));
        mContext.startActivity(intent);
    }
}
