package com.rhdigital.rhclient.activities.rhapp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.RHApplication;
import com.rhdigital.rhclient.activities.rhapp.viewmodel.RHAppViewModel;
import com.rhdigital.rhclient.common.services.NavigationService;
import com.rhdigital.rhclient.databinding.ActivityRhappBinding;

public class RHAppActivity extends AppCompatActivity {

  private Toolbar mToolbar;
  private BottomNavigationView mBottomNavigationView;
  private ActivityRhappBinding binding;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // REGISTER ACTIVITY TO APPLICATION
    ((RHApplication)getApplicationContext()).setCurrentActivity(this);

    // VIEW BINDING
    binding = ActivityRhappBinding.inflate(getLayoutInflater());
    binding.setViewModel(
            new ViewModelProvider(this).get(RHAppViewModel.class)
    );
    setContentView(binding.getRoot());

    // INITIALISE VIEW COMPONENTS
    mToolbar = binding.topNavigationView;

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
}
