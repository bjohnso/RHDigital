package com.rhdigital.rhclient.activities.courses.listeners;

import androidx.fragment.app.Fragment;

import com.rhdigital.rhclient.activities.courses.fragments.CoursesTabFragment;
import com.google.android.material.tabs.TabLayout;

public class TabLayoutOnClick implements TabLayout.OnTabSelectedListener {

    private Fragment fragment;

    public TabLayoutOnClick(Fragment fragment){
        this.fragment = fragment;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        CoursesTabFragment coursesTabFragment = (CoursesTabFragment) fragment;

        switch (tab.getPosition()){
            case 0:

                break;
            case 1:

                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
