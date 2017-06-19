package com.kisita.uza.activities;

import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.kisita.uza.R;
import com.kisita.uza.custom.CustomFragment;

/**
 * A placeholder fragment containing a simple view.
 */
public class ChoicesActivityFragment extends Fragment {

    public ChoicesActivityFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_choices, container, false);

        GridView gridview = (GridView) v.findViewById(R.id.gridview);
        gridview.setAdapter(new ChoicesGridAdapter(getActivity()));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Log.i("GridView result","position = "+position + " - id = "+id);
                getActivity().finish();//finishing activity
            }
        });

        return v;
    }

    private class ChoicesGridAdapter extends BaseAdapter {
            private Context mContext;

            public ChoicesGridAdapter(Context c) {
                mContext = c;
            }

            public int getCount() {
                return mThumbIds.length;
            }

            public Object getItem(int position) {
                return null;
            }

            public long getItemId(int position) {
                return 0;
            }

            // create a new ImageView for each item referenced by the Adapter
            public View getView(int position, View convertView, ViewGroup parent) {
                ImageView imageView;
                if (convertView == null) {
                    // if it's not recycled, initialize some attributes
                    imageView = new ImageView(mContext);
                    imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imageView.setPadding(8, 8, 8, 8);
                } else {
                    imageView = (ImageView) convertView;
                }

                imageView.setImageResource(mThumbIds[position]);
                return imageView;
            }

            // references to our images
            private Integer[] mThumbIds = {
                    R.drawable.clothing, R.drawable.shoes,
                    R.drawable.watch, R.drawable.perfumes,
                    R.drawable.livingroom, R.drawable.bedroom,
                    R.drawable.kitchen, R.drawable.garden
            };
    }
}
