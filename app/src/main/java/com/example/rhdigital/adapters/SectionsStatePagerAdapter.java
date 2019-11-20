package com.example.rhdigital.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.HashMap;
import java.util.Map;

public class SectionsStatePagerAdapter extends FragmentStatePagerAdapter {

    private final Map<String, Fragment> mFragmentMap = new HashMap<>();

    public SectionsStatePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(String name, Fragment fragment){
        if (mFragmentMap.get(name) == null && mFragmentMap.get(fragment) == null){
            mFragmentMap.put(name, fragment);
        }
    }

    public Fragment getItem(String name){
        return mFragmentMap.get(name);
    }

    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return mFragmentMap.size();
    }
}
