package com.rhdigital.rhclient.activities.rhauth;


import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.LiveData;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.rhdigital.rhclient.R;

import com.rhdigital.rhclient.common.services.NavigationService;

import com.rhdigital.rhclient.database.model.User;
import com.rhdigital.rhclient.database.viewmodel.UserViewModel;

import java.util.ArrayList;


public class RHAuthActivity extends AppCompatActivity {

  //ViewModel
  private UserViewModel userViewModel;

  private LiveData<User> usertask;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    userViewModel = new UserViewModel(getApplication());

    setContentView(R.layout.activity_auth);

    // INITIALISE NAVIGATION COMPONENT && CALL NAVIGATION SERVICE
    NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
      .findFragmentById(R.id.nav_host_rhauth);
    NavController navController = navHostFragment.getNavController();
    NavigationService.getINSTANCE().initNav(
      getLocalClassName(),
      navController,
      R.navigation.rhauth_nav_graph,
      R.id.signInFragment);
  }

  public void updateUserEmail(String uuid, String newEmail) {
  }

  public void launchCoursesActivity() {
  }

  //TODO: IMPLEMENT PHONE AUTHENTICATION

  public UserViewModel getUserViewModel() {
    return userViewModel;
  }

}
