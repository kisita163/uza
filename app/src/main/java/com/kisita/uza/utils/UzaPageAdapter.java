package com.kisita.uza.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.kisita.uza.R;
import com.kisita.uza.internal.BiLog;
import com.kisita.uza.model.Data;
import java.util.ArrayList;

/*
 * Created by Hugues on 27/04/2017.
 */

public class UzaPageAdapter extends PagerAdapter implements BikekoViewPager.OnPageChangeListener {

    private static final String TAG = "### UzaPageAdapter";

    private Context mContext;

    /** Product images. */
    private ArrayList<String> urls;

    /** The view that hold dots. */
    private LinearLayout vDots;

    private View.OnClickListener listener;

    public UzaPageAdapter(Context context, Data data, LinearLayout dotLayout, View.OnClickListener listener) {
        this.mContext = context;
        this.urls     = data.getPictures();
        this.vDots    = dotLayout;
        this.listener = listener;
        setupDotBar();
        //Collections.reverse(this.urls);
    }

    /* (non-Javadoc)
         * @see android.support.v4.view.PagerAdapter#getCount()
         */
    @Override
    public int getCount()
    {
        if(urls != null) {
            return urls.size();
        }else
            return 0;
    }

    /* (non-Javadoc)
     * @see android.support.v4.view.PagerAdapter#instantiateItem(android.view.ViewGroup, int)
     */
    @Override
    public Object instantiateItem(ViewGroup container, int arg0)
    {
        final RelativeLayout view = (RelativeLayout) LayoutInflater.from(container.getContext())
                .inflate(R.layout.img, container, false);
        final ImageView img = view.findViewById(R.id.img);
        final ProgressBar progressBar = view.findViewById(R.id.progress_bar);

        container.addView(view,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        if(urls.size() > 0) {
            Glide.with(mContext)
                    .load(urls.get(arg0))
                    .fitCenter()
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            BiLog.i(TAG,"Failed to load image");
                            img.setImageResource(R.drawable.anonymous_user);
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            BiLog.i(TAG,"Image loaded");
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .error(R.drawable.anonymous_user)
                    .into(img);
        }
        img.setOnClickListener(listener);
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
        for (int i = 0; i < urls.size(); i++)
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
}
