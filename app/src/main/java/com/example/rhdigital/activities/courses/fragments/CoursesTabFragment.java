package com.example.rhdigital.activities.courses.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.rhdigital.R;
import com.example.rhdigital.activities.courses.listeners.TabLayoutOnClick;
import com.example.rhdigital.ui.adapters.SectionsStatePagerAdapter;
import com.example.rhdigital.ui.view.CustomViewPager;
import com.google.android.material.tabs.TabLayout;

public class CoursesTabFragment extends Fragment implements RHFragment {

    //Components
    private boolean isParent = true;
    CustomViewPager mCustomViewPager;
    TabLayout mTabLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.courses_layout, container, false);

        mCustomViewPager = (CustomViewPager) view.findViewById(R.id.courses_tab_layout_container);
        mTabLayout = (TabLayout) view.findViewById(R.id.tab_layout);

        setUpViewPager(mCustomViewPager);

        //Listeners

        mTabLayout.addOnTabSelectedListener(new TabLayoutOnClick(this));

        return view;
    }

    public TabLayout getTabLayout() {
        return mTabLayout;
    }

    private void setUpViewPager(CustomViewPager customViewPager){
        SectionsStatePagerAdapter sectionsStatePagerAdapter = new SectionsStatePagerAdapter(getChildFragmentManager());
        // Fragments
        sectionsStatePagerAdapter.addFragment(new MyCoursesFragment());
        sectionsStatePagerAdapter.addFragment(new DiscoverCoursesFragment());

        customViewPager.setAdapter(sectionsStatePagerAdapter);
        customViewPager.setSwipeable(false);
    }

    public int getViewPager(){
        return mCustomViewPager.getCurrentItem();
    }

    public void setViewPager(int position){
        mCustomViewPager.setCurrentItem(position);
    }

    @Override
    public boolean isParent() {
        return this.isParent;
    }

    @Override
    public void setIsParent(boolean parent) {

    }
}
