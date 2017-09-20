package com.kisita.uza.utils;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kisita.uza.R;
import com.kisita.uza.activities.UzaActivity;
import com.kisita.uza.model.Data;
import com.kisita.uza.ui.DetailFragment;

import java.util.ArrayList;

import static com.kisita.uza.model.Data.UZA.UID;

/**
 * Created by Hugues on 27/04/2017.
 */
public class PageAdapter extends PagerAdapter {

    private Context mContext;
    private FirebaseStorage mReference;
    private String key;
    private ArrayList<String> urls;


    public PageAdapter(Context context, FirebaseStorage reference,Data data) {
        this.mContext = context;
        this.mReference = reference;
        this.key  = data.getData()[UID];
        this.urls = data.getPictures();
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
        final ImageView img = (ImageView)LayoutInflater.from(container.getContext())
                .inflate(R.layout.img, container, false);

        if(urls.size() > 0) {
            Glide.with(mContext)
                    .load(urls.get(arg0))
                    .fitCenter()
                    .error(R.drawable.on_sale_item6)
                    .into(img);
        }

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
