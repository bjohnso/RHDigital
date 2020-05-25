package com.rhdigital.rhclient.activities.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.auth.services.Authenticator;
import com.rhdigital.rhclient.activities.courses.CoursesActivity;
import com.rhdigital.rhclient.common.services.NavigationService;
import com.rhdigital.rhclient.database.RHDatabase;

import java.util.ArrayList;


public class AuthActivity extends AppCompatActivity {

  private String TAG = "AuthActivity";
  private Context context;

    //Components
  private View navControllerView;

  // Database
  RHDatabase database;

    //Auth
  private FirebaseAuth firebaseAuth;
  private FirebaseUser firebaseUser;
  private Authenticator authenticator;

    //Observables
  private MutableLiveData<FirebaseUser> userObservable;
  private MutableLiveData<ArrayList<Long>> populateRoomObservable;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    context = this;
    firebaseAuth = FirebaseAuth.getInstance();
    authenticator = new Authenticator(this);

    navControllerView = findViewById(R.id.nav_host_auth);

    database = RHDatabase.getDatabase(this);

    //Observers
    //LISTENS FOR ROOM POPULATION COMPLETION
    final Observer<ArrayList<Long>> populateRoomObserver = new Observer<ArrayList<Long>>() {
      @Override
      public void onChanged(ArrayList<Long> ids) {
        if (ids.size() > 0) {
          populateRoomObservable.removeObserver(this);
          Intent intent = new Intent(context, CoursesActivity.class);
          authenticator.postAuthenticate(firebaseUser, intent);
        }
      }
    };

    // LISTENS FOR AUTHENTICATION EVENT AND TRIGGERS DATA PULL FROM UPSTREAM
    final Observer<FirebaseUser> authObserver = new Observer<FirebaseUser>() {
      @Override
      public void onChanged(FirebaseUser user) {
        if (user != null) {
          userObservable.removeObserver(this);
          firebaseUser = user;
          populateRoomObservable = authenticator.populateRoomFromUpstream();
          populateRoomObservable.observe((LifecycleOwner) context, populateRoomObserver);
        }
      }
    };

    // Check Auth Status

    firebaseUser = firebaseAuth.getCurrentUser();

    if (firebaseUser != null) {
      populateRoomObservable = authenticator.populateRoomFromUpstream();
      populateRoomObservable.observe((LifecycleOwner) context, populateRoomObserver);
    }

    userObservable = authenticator.getFirebaseUser();
    userObservable.observe(this, authObserver);

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

  private void startCourseActivity() {
    Intent intent = new Intent(this, CoursesActivity.class);
    authenticator.postAuthenticate(firebaseUser, intent);
  }

  public Authenticator getAuthenticator() {
    return authenticator;
  }
}
