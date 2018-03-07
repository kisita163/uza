package com.kisita.uza.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.kisita.uza.R;
import com.kisita.uza.custom.CustomActivity;
import com.kisita.uza.ui.CheckoutFragment;
import com.kisita.uza.ui.FavoritesFragment;
import com.kisita.uza.ui.OnSaleFragment;
import com.kisita.uza.ui.SettingsFragment;

public class MainActivity extends CustomActivity {

    private Fragment fragment = OnSaleFragment.newInstance("Arts");

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = OnSaleFragment.newInstance("Arts");
                    setFragment();
                    return true;
                case R.id.navigation_tune:
                    fragment = new SettingsFragment();
                    setFragment();
                    return true;
                case R.id.navigation_notifications:

                    return true;
                case R.id.navigation_cart:
                    //fragment = new CheckoutFragment();
                    setFragment();
                    return true;
                case R.id.navigation_favourite:
                    fragment = new FavoritesFragment();
                    setFragment();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        setFragment();
    }

    void setFragment(){

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame, fragment)
                .commit();
    }

}
