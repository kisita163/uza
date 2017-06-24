package com.kisita.uza.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by HuguesKi on 24/06/2017.
 */

public class UzaSpinnerAdapter extends ArrayAdapter<String> {
    public UzaSpinnerAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View v = super.getView(position, convertView, parent);
        if (position == getCount()) {
            ((TextView)v.findViewById(android.R.id.text1)).setText("");
            ((TextView)v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
        }
        ((TextView)v.findViewById(android.R.id.text1)).setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));

        return v;
    }

    @Override
    public int getCount() {
        return super.getCount(); //-1 you dont display last item. It is used as hint.
    }
}
