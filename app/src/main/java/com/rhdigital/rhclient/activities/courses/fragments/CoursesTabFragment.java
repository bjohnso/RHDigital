//package com.rhdigital.rhclient.activities.courses.fragments;
//
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.navigation.NavController;
//import androidx.navigation.NavDestination;
//import androidx.navigation.fragment.NavHostFragment;
//
//import com.rhdigital.rhclient.R;
//import com.google.android.material.tabs.TabLayout;
//import com.rhdigital.rhclient.common.services.NavigationService;
//
//public class CoursesTabFragment extends Fragment {
//    //Components
//    private boolean isParent = true;
//    TabLayout tabLayout;
//
//  @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//      View view = inflater.inflate(R.layout.courses_layout, container, false);
//      tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
//      return view;
//    }
//
//  @Override
//  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//    super.onViewCreated(view, savedInstanceState);
//    // Navigation
//    NavHostFragment navHostFragment = (NavHostFragment) getChildFragmentManager()
//      .findFragmentById(R.id.nav_host_courses_tab);
//    NavController navController = navHostFragment.getNavController();
//
//    navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
//      if (destination.getId() == R.id.discoverCoursesFragment && tabLayout.getSelectedTabPosition() == 1) {
//        tabLayout.selectTab(tabLayout.getTabAt(0));
//      } else if (destination.getId() == R.id.myCoursesFragment && tabLayout.getSelectedTabPosition() == 0) {
//        tabLayout.selectTab(tabLayout.getTabAt(1));
//      }
//    });
//
//    NavigationService.getINSTANCE().initNav(
//      getClass().getName(),
//      navController,
//      R.navigation.programs_tab_nav_graph,
//      R.id.discoverCoursesFragment);
//    tabLayout.addOnTabSelectedListener(new TabOnClick(navController, getClass().getName()));
//  }
//
//  private static class TabOnClick implements TabLayout.OnTabSelectedListener {
//    private NavController navController;
//    private String className;
//
//    public TabOnClick(NavController navController, String className){
//      this.navController = navController;
//      this.className = className;
//    }
//
//    @Override
//    public void onTabSelected(TabLayout.Tab tab) {
//      switch (tab.getPosition()){
//        case 0:
//          if (navController.getCurrentDestination().getId() != R.id.discoverCoursesFragment)
//            NavigationService.getINSTANCE().navigate(className, R.id.discoverCoursesFragment, null, null);
//          break;
//        case 1:
//          if (navController.getCurrentDestination().getId() != R.id.myCoursesFragment)
//            NavigationService.getINSTANCE().navigate(className, R.id.myCoursesFragment, null, null);
//          break;
//      }
//    }
//
//    @Override
//    public void onTabUnselected(TabLayout.Tab tab) {
//
//    }
//
//    @Override
//    public void onTabReselected(TabLayout.Tab tab) {
//
//    }
//  }
//}
