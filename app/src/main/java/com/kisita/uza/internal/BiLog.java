package com.kisita.uza.internal;

import android.util.Log;
import com.kisita.uza.BuildConfig;

/*
 * Created by HuguesKi on 08-05-18.
 */

public class BiLog {
    public static void i(String tag,String s){
        if(BuildConfig.DEBUG)
            Log.i(tag,s);
    }
}
