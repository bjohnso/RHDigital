package com.rhdigital.rhclient.activities.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.auth.util.Authenticator;
import com.rhdigital.rhclient.activities.courses.CoursesActivity;
import com.rhdigital.rhclient.ui.adapters.SectionsStatePagerAdapter;
import com.rhdigital.rhclient.activities.auth.fragments.SignInFragment;
import com.rhdigital.rhclient.activities.auth.fragments.SignUpFragment;
import com.rhdigital.rhclient.ui.view.CustomViewPager;


public class AuthActivity extends AppCompatActivity {

  private String TAG = "AuthActivity";

    //Components
  private CustomViewPager mCustomViewPager;

    //Auth
  private FirebaseAuth firebaseAuth;
  private FirebaseUser firebaseUser;
  private Authenticator authenticator;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    firebaseAuth = FirebaseAuth.getInstance();
    authenticator = new Authenticator(this);

    final Observer<FirebaseUser> authObserver = new Observer<FirebaseUser>() {
      @Override
      public void onChanged(FirebaseUser user) {
        firebaseUser = user;
        authenticator.postAuthenticate(user);
      }
    };

    // Check Auth Status
    firebaseUser = firebaseAuth.getCurrentUser();
    if (firebaseUser != null) {
      firebaseAuth.signOut();
    }

    setContentView(R.layout.activity_auth);
    mCustomViewPager = findViewById(R.id.container_auth);
    setUpViewPager(mCustomViewPager);
  }

  private void setUpViewPager(CustomViewPager customViewPager){
    SectionsStatePagerAdapter sectionsStatePagerAdapter = new SectionsStatePagerAdapter(getSupportFragmentManager());
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
