package com.rhdigital.rhclient.activities.courses.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.rhdigital.rhclient.R;
import com.google.android.material.tabs.TabLayout;
import com.rhdigital.rhclient.activities.courses.CoursesActivity;
import com.rhdigital.rhclient.activities.courses.services.VideoPlayerService;

public class CoursesTabFragment extends Fragment {

    //Components
    private boolean isParent = true;
    TabLayout tabLayout;

  @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.courses_layout, container, false);
      tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
      return view;
    }

  @Override
  public void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
    Log.d("NAV", "STATE SAVED " + tabLayout.getSelectedTabPosition());
    outState.putInt("TAB_POSITION", tabLayout.getSelectedTabPosition());
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // Navigation
    Log.d("NAV", "TAB FRAG CREATED");
    if (savedInstanceState != null && savedInstanceState.get("TAB_POSITION") != null) {
      Log.d("NAV", "TAB RESTORED " + savedInstanceState.getInt("TAB_POSITION"));
      tabLayout.selectTab(tabLayout.getTabAt(savedInstanceState.getInt("TAB_POSITION")));
    }

    NavController childController = Navigation.findNavController(getActivity(), R.id.nav_host_courses_tab);
    NavOptions navOptions = new NavOptions.Builder()
      .setLaunchSingleTop(true)
      .build();
    tabLayout.addOnTabSelectedListener(new TabOnClick(childController, navOptions));
  }

  private static class TabOnClick implements TabLayout.OnTabSelectedListener {
    private NavController navController;
    private NavOptions navOptions;

    public TabOnClick(NavController navController, NavOptions navOptions){
      this.navController = navController;
      this.navOptions = navOptions;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
      switch (tab.getPosition()){
        case 0:
          navController.navigate(R.id.discoverCoursesFragment, null, navOptions);
          break;
        case 1:
          navController.navigate(R.id.myCoursesFragment, null, navOptions);
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
}
