package com.kisita.uza;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.kisita.uza.custom.CustomActivity;
import com.kisita.uza.model.Data;
import com.kisita.uza.ui.CheckoutFragment;
import com.kisita.uza.ui.DetailFragment;

public class CheckoutActivity extends CustomActivity {
    /** The toolbar. */
    public Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        toolbar = (Toolbar) findViewById(R.id.checkout_toolbar);
        toolbar.setTitle("Checkout");

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        CheckoutFragment f = new CheckoutFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, f)
                .commit();
    }
}
