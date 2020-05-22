package com.rhdigital.rhclient.activities.courses.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.courses.listeners.TabLayoutOnClick;
import com.google.android.material.tabs.TabLayout;
import com.rhdigital.rhclient.common.view.RHFragment;

public class CoursesTabFragment extends Fragment implements RHFragment {

    //Components
    private boolean isParent = true;
    TabLayout mTabLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.courses_layout, container, false);

        mTabLayout = (TabLayout) view.findViewById(R.id.tab_layout);

        //Listeners

        mTabLayout.addOnTabSelectedListener(new TabLayoutOnClick(this));

        return view;
    }

    public TabLayout getTabLayout() {
        return mTabLayout;
    }


    @Override
    public boolean isParent() {
        return this.isParent;
    }

    @Override
    public void setIsParent(boolean parent) {

    }
}
