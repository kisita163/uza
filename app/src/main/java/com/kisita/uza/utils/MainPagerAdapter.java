package com.kisita.uza.utils;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by HuguesKi on 20/06/2017.
 */

public class MainPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Fragment> mFragments;

    private ArrayList<String> mFragmentNames;

    public MainPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments, ArrayList<String> fragmentNames) {
        super(fm);
        this.mFragments = fragments;
        this.mFragmentNames = fragmentNames;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {

        return mFragments.get(position);
    }
    @Override
    public int getCount() {
        return mFragments.size();
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentNames.get(position);
    }

    public void remove(Fragment fragment){
        mFragments.remove(fragment);
    }

    public void add(Fragment fragment,String name){
        mFragments.add(fragment);
        mFragmentNames.add(name);
    }

    public void clean() {
        mFragmentNames.clear();
        mFragments.clear();
    }
}
