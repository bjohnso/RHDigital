package com.rhdigital.rhclient.activities.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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

import com.google.firebase.firestore.FirebaseFirestore;
import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.auth.services.Authenticator;
import com.rhdigital.rhclient.activities.courses.CoursesActivity;
import com.rhdigital.rhclient.common.services.NavigationService;
import com.rhdigital.rhclient.database.RHDatabase;
import com.rhdigital.rhclient.database.viewmodel.UserViewModel;

import java.util.ArrayList;


public class AuthActivity extends AppCompatActivity {

  private String TAG = "AuthActivity";
  private Context context;

  // Database
  RHDatabase database;

  //Auth
  private Authenticator authenticator;

  //ViewModel
  private UserViewModel userViewModel;

  //Observables
  private MutableLiveData<FirebaseUser> userObservable;
  private MutableLiveData<ArrayList<Long>> populateRoomObservable;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    context = this;
    authenticator = new Authenticator(this);
    database = RHDatabase.getDatabase(this);
    userViewModel = new UserViewModel(getApplication());

    setContentView(R.layout.activity_auth);

    //Initialise Navigator
    NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_auth);
    NavController navController = navHostFragment.getNavController();
    // Call Navigation Service
    NavigationService.getINSTANCE().initNav(
      getLocalClassName(),
      navController,
      R.navigation.auth_nav_graph,
      R.id.signInFragment);
  }

  public LiveData<Boolean> signIn(String email, String password) {
    LiveData<Boolean> authObserver = authenticator.authenticateEmail(email, password);
    authObserver.observe(this, isSuccessful -> {
        if (isSuccessful) {
          launchCoursesActivity();
        }
      });
    return authObserver;
  }

  public LiveData<Boolean> register(String email, String password, String firstName, String lastName) {
    LiveData<Boolean> authObserver = authenticator.registerEmail(email, password, firstName, lastName);
    authObserver.observe(this, isSuccessful -> {
      if (isSuccessful) {
        registrationLaunchCoursesActivity(firstName, lastName);
      }
    });
    return authObserver;
  }

  public void registrationLaunchCoursesActivity(String firstName, String lastName) {
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    authenticator.postRegistration(firebaseUser, firstName, lastName)
    .observe(this, isSuccessful -> {
      if (isSuccessful) {
        NavigationService.getINSTANCE()
          .navigate(getLocalClassName(),
            R.id.signInFragment,
            null,
            null);
      }
    });
  }

  public void launchCoursesActivity() {
    FirebaseUser firebaseUser;
    if ((firebaseUser = FirebaseAuth.getInstance().getCurrentUser()) != null) {
      authenticator
        .populateRoomFromUpstream()
        .observe(this, ids -> {
          if (ids.size() > 0) {
            Intent intent = new Intent(context, CoursesActivity.class);
            authenticator.postAuthenticate(firebaseUser, intent);
          }
        });
    }
  }

  public UserViewModel getUserViewModel() {
    return userViewModel;
  }

  public Authenticator getAuthenticator() {
    return authenticator;
  }
}
