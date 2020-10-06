package com.rhdigital.rhclient.activities.rhauth;


import android.os.Bundle;
import android.provider.ContactsContract;
import android.telecom.Call;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.LiveData;

import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.rhdigital.rhclient.R;

import com.rhdigital.rhclient.activities.rhauth.services.AuthAPIService;
import com.rhdigital.rhclient.activities.rhauth.services.AuthFieldValidationService;
import com.rhdigital.rhclient.common.services.NavigationService;

import com.rhdigital.rhclient.database.model.User;
import com.rhdigital.rhclient.database.services.CallableFunction;
import com.rhdigital.rhclient.database.viewmodel.UserViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RHAuthActivity extends AppCompatActivity {

  private View loader;

  private ExecutorService executorService = Executors.newSingleThreadExecutor();
  private AuthFieldValidationService authFieldValidationService = new AuthFieldValidationService();
  private AuthAPIService authAPIService = new AuthAPIService();

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

  private void startLoader() {
    ViewGroup rootView = (ViewGroup) getWindow().getDecorView().getRootView();
    loader = getLayoutInflater().inflate(R.layout.rh_loader, rootView, false);
    rootView.addView(loader);
  }

  private void stopLoader() {
    ViewGroup rootView = (ViewGroup) getWindow().getDecorView().getRootView();
    rootView.removeView(loader);
    Log.d("AUTH", "STOP LOADER");
  }

  public void updateAuthField(String key, String value) { this.authFieldsMap.put(key, value); }

  public HashMap<String, String> getAuthFieldsMap() { return this.authFieldsMap; }

  // TODO : VALIDATE EMAIL STRATEGY BEFORE CALLING THIS!!!
  public LiveData<Object> signUp() {
    startLoader();
    MutableLiveData<Object> signUpResultData = new MutableLiveData<>();
    Tasks.call(executorService, CallableFunction.callable(this.authAPIService, new Pair<>("signUpNewUser", authFieldsMap)))
      .addOnSuccessListener(data -> {
        stopLoader();
        signUpResultData.postValue(data);
      })
      .addOnFailureListener(error -> {
        stopLoader();
        signUpResultData.postValue(error);
      });
    return signUpResultData;
  }

  //TODO: HANDLE MULTIPLE STRATEGIES
  public LiveData<List<String>> validateAuthFields(String STRATEGY) {
    startLoader();
    MutableLiveData<List<String>> validationResultData = new MutableLiveData<>();
    Tasks.call(executorService, CallableFunction.callable(this.authFieldValidationService, this.authFieldsMap))
      .addOnSuccessListener(validationErrors -> {
          stopLoader();
          validationResultData.postValue(validationErrors);
      })
      .addOnFailureListener(error -> {
          stopLoader();
          validationResultData.postValue(null);
      });
    return validationResultData;
  }

  //TODO: IMPLEMENT PHONE AUTHENTICATION

  public UserViewModel getUserViewModel() {
    return userViewModel;
  }

}
