package com.rhdigital.rhclient.activities.auth;

import android.content.Context;
import android.content.Intent;
import android.database.Observable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.auth.util.Authenticator;
import com.rhdigital.rhclient.activities.courses.CoursesActivity;
import com.rhdigital.rhclient.database.model.Course;
import com.rhdigital.rhclient.ui.adapters.SectionsStatePagerAdapter;
import com.rhdigital.rhclient.activities.auth.fragments.SignInFragment;
import com.rhdigital.rhclient.activities.auth.fragments.SignUpFragment;
import com.rhdigital.rhclient.ui.view.CustomViewPager;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class AuthActivity extends AppCompatActivity {

  private String TAG = "AuthActivity";
  private Context context;
  private SectionsStatePagerAdapter sectionsStatePagerAdapter;

    //Components
  private CustomViewPager mCustomViewPager;

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

    // LISTENS FOR AUTHENTICATION EVENT
    final Observer<FirebaseUser> authObserver = new Observer<FirebaseUser>() {
      @Override
      public void onChanged(FirebaseUser user) {
        SignInFragment signInFragment = (SignInFragment) sectionsStatePagerAdapter.getItem(0);
        if (user != null) {
          signInFragment.setFieldsValidated();
          signInFragment.addLoader();
          userObservable.removeObserver(this);
          firebaseUser = user;
          populateRoomObservable = authenticator.populateRoomFromUpstream();
          populateRoomObservable.observe((LifecycleOwner) context, populateRoomObserver);
        } else {
          signInFragment.setSubmitDisableTimeout();
        }
      }
    };

    // Check Auth Status
    firebaseUser = firebaseAuth.getCurrentUser();
    if (firebaseUser != null) {
      firebaseAuth.signOut();
    }

    userObservable = authenticator.getFirebaseUser();
    userObservable.observe(this, authObserver);

    setContentView(R.layout.activity_auth);
    mCustomViewPager = findViewById(R.id.container_auth);
    setUpViewPager(mCustomViewPager);
  }

  private void setUpViewPager(CustomViewPager customViewPager){
    sectionsStatePagerAdapter = new SectionsStatePagerAdapter(getSupportFragmentManager());
    sectionsStatePagerAdapter.addFragment(new SignInFragment());
    sectionsStatePagerAdapter.addFragment(new SignUpFragment());
    customViewPager.setAdapter(sectionsStatePagerAdapter);
    customViewPager.setSwipeable(false);
  }

  public int getViewPager(){
        return mCustomViewPager.getCurrentItem();
  }

  public void setViewPager(int position){
        mCustomViewPager.setCurrentItem(position);
  }

  private void startCourseActivity() {
    Intent intent = new Intent(this, CoursesActivity.class);
    startActivity(intent);
  }

  public Authenticator getAuthenticator() {
    return authenticator;
  }
}
