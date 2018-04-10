package com.kisita.uza.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.kisita.uza.R;

/*
 * Created by HuguesKi on 09-04-18.
 */

public class Settings {

    private static SharedPreferences getSharedPreferences(Context context){
        return context.getSharedPreferences(context.getResources().getString(R.string.uza_keys),
                Context.MODE_PRIVATE);
    }
}
