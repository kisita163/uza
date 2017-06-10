package com.kisita.uza.utils;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.kisita.uza.R;

import java.util.ArrayList;

/**
 * Created by Hugues on 04/06/2017.
 */
public class SpinnerColorAdapter extends BaseAdapter {

    private ArrayList<String> mList;
    private Context mContext;
    private LayoutInflater inflter;

    public SpinnerColorAdapter(Context context,ArrayList<String> list) {
        mList = list;
        mContext = context;
        inflter = (LayoutInflater.from(context));
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if(row == null)
        {
            row = inflter.inflate(R.layout.color_layout, null);
        }

        ImageView  img = (ImageView)row.findViewById(R.id.color_item);
        int color;
        //Log.i("Color adapter","result :"+(int)Long.parseLong( str.trim(), 16)+"- value is : "+str.trim());

        try {
            color = Color.parseColor(mList.get(position).trim());
            //color = Integer.valueOf(str);
            //color = (int)Long.parseLong( str.trim(), 16);
        }catch (NumberFormatException e){
            color = Color.BLACK;
            Log.e("Format issue in color",e.getMessage());
        }
        img.setBackgroundColor(color);


        return row;

    }
}
