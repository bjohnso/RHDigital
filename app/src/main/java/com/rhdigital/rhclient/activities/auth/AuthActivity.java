package com.rhdigital.rhclient.activities.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.auth.services.Authenticator;
import com.rhdigital.rhclient.activities.courses.CoursesActivity;
import com.rhdigital.rhclient.common.services.NavigationService;
import com.rhdigital.rhclient.common.services.Toaster;
import com.rhdigital.rhclient.database.RHDatabase;
import com.rhdigital.rhclient.database.model.User;
import com.rhdigital.rhclient.database.viewmodel.UserViewModel;

import java.util.ArrayList;


public class AuthActivity extends AppCompatActivity {

  //Firebase
  private FirebaseFirestore remoteDB;

  //ViewModel
  private UserViewModel userViewModel;

  private LiveData<User> usertask;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Toaster.getINSTANCE().setContext(this);
    Authenticator.getInstance().init(this);
    userViewModel = new UserViewModel(getApplication());

    setContentView(R.layout.activity_auth);

    //Initialise Navigator
    NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
      .findFragmentById(R.id.nav_host_auth);
    NavController navController = navHostFragment.getNavController();

    // Call Navigation Service
    NavigationService.getINSTANCE().initNav(
      getLocalClassName(),
      navController,
      R.navigation.auth_nav_graph,
      R.id.signInFragment);
  }

  public void updateUserEmail(String uuid, String newEmail) {
    usertask = userViewModel.getAuthenticatedUser(uuid);
      usertask.observe(this, user -> {
        if (user != null) {
          Log.d("CHANGEEMAIL", "Fetched Local User");
          user.setEmail(newEmail);
          FirebaseFirestore.getInstance()
            .collection("users")
            .document(uuid)
            .set(user)
            .addOnCompleteListener(this, task -> {
              Log.d("CHANGEEMAIL", "Update Local User Attempted");
              if (task.isSuccessful()) {
                Log.d("CHANGEEMAIL", "Update Local User Success");
                userViewModel.updateUser(user);
                Toaster.getINSTANCE()
                  .ToastMessage(getResources().getString(R.string.server_success_email_update), true);
                NavigationService.getINSTANCE().navigate(getLocalClassName(),
                  R.id.signInFragment, null, null);
              } else {
                Log.d("CHANGEEMAIL", "Update Local User Failed!");
                Toaster.getINSTANCE()
                  .ToastMessage(getResources().getString(R.string.device_connection_error), true);
              }
              usertask.removeObservers(this);
            });
        }
      });
  }

  public void launchCoursesActivity() {
    Intent intent = new Intent(this, CoursesActivity.class);
    startActivity(intent);
  }

  //TODO: IMPLEMENT PHONE AUTHENTICATION

  public UserViewModel getUserViewModel() {
    return userViewModel;
  }

}
