package com.kisita.uza.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
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
    private Fragment fragment = OnSaleFragment.newInstance();

    private int mCurrentFragmentId = 0;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            setFragment(setCurrentFragment(item.getItemId()));
            return true;
        }
    };

    private Fragment setCurrentFragment(int fragmentId){
        Fragment fragment;
        switch (fragmentId) {
            case R.id.navigation_home:
                fragment = OnSaleFragment.newInstance();
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
                fragment = OnSaleFragment.newInstance();
                mCurrentFragmentId = R.id.navigation_home;
                break;
        }

        return fragment;
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

        setFragment(setCurrentFragment(mCurrentFragmentId));
    }

    public void setFragment(Fragment f){
        /* Note : You should not add transactions to the back stack when the transaction is for horizontal navigation
        (such as when switching tabs) or when modifying the content appearance (such as when adjusting filters).
         */
        Log.i(TAG,"setFragment()  "+fragment.toString());
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right,R.anim.slide_out_left,R.anim.slide_in_left,R.anim.slide_out_right)
                .replace(R.id.frame,f)
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
