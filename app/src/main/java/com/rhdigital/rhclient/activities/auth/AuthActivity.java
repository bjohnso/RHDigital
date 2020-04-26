package com.rhdigital.rhclient.activities.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.courses.CoursesActivity;
import com.rhdigital.rhclient.database.model.Course;
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

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    firebaseAuth = FirebaseAuth.getInstance();

    // Check Auth Status
    firebaseUser = firebaseAuth.getCurrentUser();
    if (firebaseUser != null) {
      firebaseAuth.signOut();
      // User is Authenticated, Start Courses Activity
//      Intent intent = new Intent(this, CoursesActivity.class);
//      this.startActivity(intent);
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
  }

  public int getViewPager(){
        return mCustomViewPager.getCurrentItem();
  }

  public void setViewPager(int position){
        mCustomViewPager.setCurrentItem(position);
  }

  public void Register(String email, String password) {
    if (email != null && password != null && !email.isEmpty() && !password.isEmpty()) {
      firebaseAuth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(this, task -> {
          if (task.isSuccessful()) {
            firebaseUser = firebaseAuth.getCurrentUser();
            startCourseActivity();
          } else {
            if (password.length() < 6) {
              Toast.makeText(this, "Sorry, your password needs to be at least 6 characters. Please try again.", Toast.LENGTH_LONG).show();
            } else {
              Toast.makeText(this, "Oops, An unknown error has occurred. This incident has been reported to the RHDigital Team. Please try again later.", Toast.LENGTH_LONG).show();
            }
          }
        });
    } else {
      Toast.makeText(this, "Please enter a valid email address and password.", Toast.LENGTH_LONG).show();
    }
  }

  public void Login(String email, String password) {
    if (email != null && password != null && !email.isEmpty() && !password.isEmpty()) {
      firebaseAuth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(this, task -> {
          if (task.isSuccessful()) {
            firebaseUser = firebaseAuth.getCurrentUser();
            startCourseActivity();
          } else {
            if (password.length() < 6) {
              Toast.makeText(this, "Sorry, your password needs to be at least 6 characters. Please try again.", Toast.LENGTH_LONG).show();
            } else {
              Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
          }
        });
    } else {
      Toast.makeText(this, "Please enter a valid email address and password.", Toast.LENGTH_LONG).show();
    }
  }

  private void startCourseActivity() {
    Intent intent = new Intent(this, CoursesActivity.class);
    startActivity(intent);
  }
}
