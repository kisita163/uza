package com.kisita.uza.activities;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.kisita.uza.R;
import com.kisita.uza.custom.CustomActivity;
import com.kisita.uza.ui.CheckoutFragment;
import com.kisita.uza.ui.DetailFragment;
import com.kisita.uza.ui.FavoritesFragment;
import com.kisita.uza.ui.SellingFragment;
import com.kisita.uza.ui.SettingsFragment;

import static com.kisita.uza.model.Data.UZA.NAME;

public class UzaActivity extends CustomActivity {
    /** The toolbar. */
    public Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        toolbar = (Toolbar) findViewById(R.id.checkout_toolbar);

        setFragment(getIntent().getIntExtra("fragment",-1));

        setSupportActionBar(toolbar);

        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }


    void setFragment(int fid){
        String title = "";
        Fragment f = null;
        switch (fid){
            case(0):
                title = "Favorites";
                f = new FavoritesFragment();
                break;
            case(1):
                title = "Checkout";
                f = new CheckoutFragment();
                break;
            case(2):
                title = "Settings";
                f = new SettingsFragment();
                break;
            case(3):
                title = getIntent().getStringArrayExtra("details")[NAME];
                f = DetailFragment.newInstance(getIntent().getStringArrayExtra("details"), getIntent().getByteArrayExtra("picture"));
                break;
            case(4):
                title = "My store";
                f = new SellingFragment();
                break;
            default:
                break;
        }
        toolbar.setTitle(title);
        if(f != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, f)
                    .commit();
            commandsCount();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Log.i(TAG,"***** back pressed");
        getFragmentManager().popBackStackImmediate();
    }
}
