package com.kisita.uza.utils;


import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kisita.uza.R;

import static android.graphics.Typeface.BOLD;

/*
 * Created by HuguesKi on 28-04-18.
 */

public class UzaProgressDialog extends Dialog {
    private TextView logo;

    public UzaProgressDialog(Context context,String message) {
        super(context, R.style.UzaProgressDialog);
        WindowManager.LayoutParams wlmp = getWindow().getAttributes();

        wlmp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        wlmp.gravity = Gravity.CENTER_HORIZONTAL;


        getWindow().setAttributes(wlmp);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);


        setTitle(null);
        setCancelable(false);
        setOnCancelListener(null);
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        // App logo
        logo = new TextView(context);
        logo.setText(context.getString(R.string.app_name));
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "font/atmosphere.ttf");;
        logo.setTypeface(typeface,BOLD);
        logo.setTextSize(40);
        layout.addView(logo, params);

        // Message
        TextView messageView = new TextView(context);
        messageView.setText(message);
        messageView.setTextSize(20);
        layout.addView(messageView,params);
        addContentView(layout, params);
    }
}
