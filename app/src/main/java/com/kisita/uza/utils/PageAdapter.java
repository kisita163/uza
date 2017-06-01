package com.kisita.uza.utils;

import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kisita.uza.R;

/**
 * Created by Hugues on 27/04/2017.
 */
public class PageAdapter extends PagerAdapter {

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
        //Log.i("PagerAdapter", "int value is : " + arg0);
        ImageView img = (ImageView)LayoutInflater.from(container.getContext())
                .inflate(R.layout.img, container, false);

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
