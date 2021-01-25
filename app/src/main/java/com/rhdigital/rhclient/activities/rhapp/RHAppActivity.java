package com.rhdigital.rhclient.activities.rhapp;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.RHApplication;
import com.rhdigital.rhclient.activities.rhapp.viewmodel.RHAppViewModel;
import com.rhdigital.rhclient.activities.rhauth.RHAuthActivity;
import com.rhdigital.rhclient.common.services.NavigationService;
import com.rhdigital.rhclient.common.services.VideoPlayerService;
import com.rhdigital.rhclient.databinding.ActivityRhappBinding;

public class RHAppActivity extends AppCompatActivity {

  private ActivityRhappBinding binding;
  private Intent rhAuthIntent;

  @Override
  protected void onStart() {
    super.onStart();
    rhAuthIntent = new Intent(this, RHAuthActivity.class);
    if (FirebaseAuth.getInstance().getCurrentUser() == null) {
      startAuthActivity();
      return;
    }
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // REGISTER ACTIVITY TO APPLICATION
    ((RHApplication)getApplicationContext()).setCurrentActivity(this);

    // VIEW BINDING
    binding = ActivityRhappBinding.inflate(getLayoutInflater());
    binding.setLifecycleOwner(this);
    binding.setViewModel(
            new ViewModelProvider(this).get(RHAppViewModel.class)
    );
    setContentView(binding.getRoot());

    binding.bottomNavigationView.setOnNavigationItemSelectedListener(item ->  {
      switch (item.getItemId()){
        case R.id.bottom_nav_programs:
          NavigationService.getINSTANCE().navigate(getLocalClassName(), R.id.programsFragment, null, null);
          return true;
        case R.id.bottom_nav_reports:
          NavigationService.getINSTANCE().navigate(getLocalClassName(), R.id.reportsFragment, null, null);
          return true;
        case R.id.bottom_nav_profile:
          NavigationService.getINSTANCE().navigate(getLocalClassName(), R.id.profileFragment, null, null);
          return true;
      }
      return false;
    });

    binding.getViewModel().isFullscreenMode.observe(this, isFullscreenMode -> {
      configureScreenOrientation(isFullscreenMode);
    });

    binding.btnBack.setOnClickListener(view -> {
      VideoPlayerService.getInstance().destroyAllVideoStreams(true);
      NavigationService.getINSTANCE().navigateBack(getLocalClassName());
    });

    binding.getViewModel().authorisePrograms().observe(this, authorisedPrograms -> {
      NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
              .findFragmentById(R.id.nav_host_rhapp);
      NavController navController = navHostFragment.getNavController();
      NavigationService.getINSTANCE().initNav(
              getLocalClassName(),
              navController,
              R.navigation.rhapp_nav_graph,
              R.id.programsFragment);
    });
  }

  public void configureScreenOrientation(boolean isLandscape) {
    // Forced Orientation Landscape
    if (isLandscape) {
      ((RHApplication) getApplication())
              .getCurrentActivity()
              .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
    }
    else {
      ((RHApplication) getApplication())
              .getCurrentActivity()
              .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
  }

  public void launchPaymentActivity() {
    //TODO: LAUNCH PAYMENTS ACTIVITY
  }

  public void logout() {
    FirebaseAuth.getInstance().signOut();
    startAuthActivity();
  }

  private void startAuthActivity() { startActivity(rhAuthIntent); }
}
