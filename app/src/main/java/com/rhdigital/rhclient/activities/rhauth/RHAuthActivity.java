package com.rhdigital.rhclient.activities.rhauth;


import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.LiveData;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.rhdigital.rhclient.R;

import com.rhdigital.rhclient.activities.rhauth.services.AuthFieldValidationService;
import com.rhdigital.rhclient.common.services.NavigationService;

import com.rhdigital.rhclient.database.model.User;
import com.rhdigital.rhclient.database.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.rhdigital.rhclient.activities.rhauth.constants.ValidationConstants.EMAIL_STRATEGY;
import static com.rhdigital.rhclient.activities.rhauth.constants.ValidationConstants.PARTIAL_STRATEGY;


public class RHAuthActivity extends AppCompatActivity {

  private AuthFieldValidationService authFieldValidationService = new AuthFieldValidationService();

  //ViewModel
  private UserViewModel userViewModel;

  private LiveData<User> usertask;

  private HashMap<String, String> authFieldsMap = new HashMap<>();

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

  public void updateAuthField(String key, String value) { this.authFieldsMap.put(key, value); }

  public HashMap<String, String> getAuthFieldsMap() { return this.authFieldsMap; }

  //TODO: HANDLE MULTIPLE STRATEGIES
  public List<String> validateAuthFields(String STRATEGY) {
    if (STRATEGY == EMAIL_STRATEGY) {
      return this.authFieldValidationService.validateEmailStrategyData(this.authFieldsMap);
    }

    if (STRATEGY == PARTIAL_STRATEGY) {
      return this.authFieldValidationService.validatePartialStrategyData(this.authFieldsMap);
    }
    return null;
  }

  //TODO: IMPLEMENT PHONE AUTHENTICATION

  public UserViewModel getUserViewModel() {
    return userViewModel;
  }

}
