package com.kisita.uza.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.kisita.uza.R;
import com.kisita.uza.custom.CustomActivity;
import com.kisita.uza.ui.CheckoutFragment;
import com.kisita.uza.ui.ChoicesFragment;
import com.kisita.uza.ui.FavoritesFragment;
import com.kisita.uza.ui.OnSaleFragment;
import com.kisita.uza.ui.SettingsFragment;

public class MainActivity extends CustomActivity {

    private static final String CURRENT_FRAGMENT_ID = "current_fragment_id";
    private Fragment fragment = OnSaleFragment.newInstance("Arts");

    private int mCurrentFragmentId = 0;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            setCurrentFragment(item.getItemId());
            setFragment();
            return true;
        }
    };

    void setCurrentFragment(int fragmentId){
        switch (fragmentId) {
            case R.id.navigation_home:
                fragment = OnSaleFragment.newInstance("Arts");
                mCurrentFragmentId = fragmentId;
                break;
            case R.id.navigation_tune:
                fragment = new SettingsFragment();
                mCurrentFragmentId = fragmentId;
                break;
            case R.id.navigation_user:
                fragment = ChoicesFragment.newInstance(getString(R.string.settings));
                mCurrentFragmentId = fragmentId;
                break;
            case R.id.navigation_cart:
                fragment = new CheckoutFragment();
                mCurrentFragmentId = fragmentId;
                break;
            case R.id.navigation_favourite:
                fragment = new FavoritesFragment();
                mCurrentFragmentId = fragmentId;
                break;
            default:
                fragment = OnSaleFragment.newInstance("Arts");
                mCurrentFragmentId = R.id.navigation_home;
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if(savedInstanceState != null){
            mCurrentFragmentId = savedInstanceState.getInt(CURRENT_FRAGMENT_ID);
        }

        setCurrentFragment(mCurrentFragmentId);
        setFragment();
    }

    void setFragment(){
        /* Note : You should not add transactions to the back stack when the transaction is for horizontal navigation
        (such as when switching tabs) or when modifying the content appearance (such as when adjusting filters).
         */
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right,R.anim.slide_out_left,R.anim.slide_in_left,R.anim.slide_out_right)
                .replace(R.id.frame,fragment)
                .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        if(mCurrentFragmentId != 0){
            outState.putInt(CURRENT_FRAGMENT_ID,mCurrentFragmentId);
        }
        super.onSaveInstanceState(outState);
    }
}
