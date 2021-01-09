package com.rhdigital.rhclient.activities.rhapp;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.RHApplication;
import com.rhdigital.rhclient.activities.courses.CoursesActivity;
import com.rhdigital.rhclient.activities.rhapp.viewmodel.RHAppViewModel;
import com.rhdigital.rhclient.common.services.NavigationService;
import com.rhdigital.rhclient.databinding.ActivityRhappBinding;

public class RHAppActivity extends AppCompatActivity {

  private ActivityRhappBinding binding;

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
      }
      return false;
    });

    binding.btnBack.setOnClickListener(view -> {
      NavigationService.getINSTANCE().navigateBack(getLocalClassName());
    });

    // INITIALISE NAVIGATION COMPONENT && CALL NAVIGATION SERVICE
    NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
      .findFragmentById(R.id.nav_host_rhapp);
    NavController navController = navHostFragment.getNavController();
    NavigationService.getINSTANCE().initNav(
      getLocalClassName(),
      navController,
      R.navigation.rhapp_nav_graph,
      R.id.programsFragment);
  }
}
