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

import java.util.EmptyStackException;
import java.util.Stack;

public class MainActivity extends CustomActivity {

    private static final String CURRENT_FRAGMENT_ID = "current_fragment_id";
    private Fragment fragment = OnSaleFragment.newInstance();

    private int mCurrentFragmentId = R.id.navigation_home;

    private Stack<Integer> mFragmentsStack = new Stack<>();

    private BottomNavigationView mBottomNavigationView;

    private boolean mEmptyingStack = false;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            setFragment(setCurrentFragment(item.getItemId()));
            Log.i(TAG,"onNavigationItemSelected");
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

        if(!mEmptyingStack) {
            mFragmentsStack.push(fragmentId);
            Log.i(TAG,"Stack : "+ mFragmentsStack);
        }else{
            mEmptyingStack = false;
        }

        return fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomNavigationView = findViewById(R.id.navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

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
                .addToBackStack(null)
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

    @Override
    public void onBackPressed() {
        try {
            mEmptyingStack = true;
            mFragmentsStack.pop();
            mBottomNavigationView.setSelectedItemId((mFragmentsStack.peek()));
            Log.i(TAG,"Stack(after pop) : "+ mFragmentsStack);
        }catch(EmptyStackException e){
            finish();
        }
    }
}
