package com.rhdigital.rhclient.activities.rhapp;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.common.services.NavigationService;

public class RHAppActivity extends AppCompatActivity {

  private Toolbar mToolbar;
  private BottomNavigationView mBottomNavigationView;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_rhapp);

    // INITIALISE VIEW COMPONENTS
    mToolbar = findViewById(R.id.topNavigationView);

    // INITIALISE NAVIGATION COMPONENT && CALL NAVIGATION SERVICE
    NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
      .findFragmentById(R.id.nav_host_rhapp);
    NavController navController = navHostFragment.getNavController();
    NavigationService.getINSTANCE().initNav(
      getLocalClassName(),
      navController,
      R.navigation.rhapp_nav_graph,
      R.id.programsTabFragment);


  }

  public void setToolbarTitle(String title) {
    mToolbar.setTitle(title);
  }
}
