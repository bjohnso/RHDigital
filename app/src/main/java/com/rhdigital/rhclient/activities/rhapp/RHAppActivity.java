package com.rhdigital.rhclient.activities.rhapp;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.common.services.NavigationService;

public class RHAppActivity extends AppCompatActivity {

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_rhapp);
    Log.d("PROGRAMSTABFRAG", "CREATED");

    //Initialise Navigator
    NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
      .findFragmentById(R.id.nav_host_rhapp);
    NavController navController = navHostFragment.getNavController();
    // Call Navigation Service
    NavigationService.getINSTANCE().initNav(
      getLocalClassName(),
      navController,
      R.navigation.rhapp_nav_graph,
      R.id.programsTabFragment);
  }
}
