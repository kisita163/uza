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
import com.kisita.uza.ui.DetailFragment;

import java.util.ArrayList;

/**
 * Created by Hugues on 27/04/2017.
 */
public class PageAdapter extends PagerAdapter {

    private Context mContext;
    private FirebaseStorage mReference;
    private String key;
    private int len;
    private ArrayList<String> urls;



    public PageAdapter(Context context, FirebaseStorage reference,String key,int len) {
        this.mContext = context;
        this.mReference = reference;
        this.key = key;
        this.len = len;
    }

    public PageAdapter(Context context, FirebaseStorage reference,String key,int len,ArrayList<String> urls) {
        this.mContext = context;
        this.mReference = reference;
        this.key = key;
        this.len = len;
        this.urls = urls;
    }

    /* (non-Javadoc)
         * @see android.support.v4.view.PagerAdapter#getCount()
         */
    @Override
    public int getCount()
    {
        if(urls.size() > 0){
            return urls.size();
        }else{
            return len;
        }
    }

    /* (non-Javadoc)
     * @see android.support.v4.view.PagerAdapter#instantiateItem(android.view.ViewGroup, int)
     */
    @Override
    public Object instantiateItem(ViewGroup container, int arg0)
    {
        final ImageView img = (ImageView)LayoutInflater.from(container.getContext())
                .inflate(R.layout.img, container, false);
        final int pos = arg0;
        final String ref;

        if(pos == 0)
            ref = "gs://glam-afc14.appspot.com/" + key + "/android.png";
        else
            ref = "gs://glam-afc14.appspot.com/" + key + "/android"+pos+".png";


        if(urls.size() > 0){
            Glide.with(mContext)
                    .load(urls.get(arg0))
                    .fitCenter()
                    .error(R.drawable.on_sale_item6)
                    .into(img);
        }else {
            Glide.with(mContext)
                    .using(new FirebaseImageLoader())
                    .load(mReference.getReferenceFromUrl(ref))
                    .fitCenter()
                    .error(R.drawable.on_sale_item6)
                    .into(img);
        }


        //img.setImageResource(R.drawable.product_detail_bottom_banner);
        /*img.setOnClickListener(new View.OnClickListener() {
            @Override
        public void onClick(View v) {
                Glide.with(mContext)
                        .using(new FirebaseImageLoader())
                        .load(mReference.getReferenceFromUrl(ref))
                        .fitCenter()
                        .error(R.drawable.on_sale_item6)
                        .into((ImageView) ((UzaActivity)mContext).findViewById(R.id.imageView1));
            }});*/

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
