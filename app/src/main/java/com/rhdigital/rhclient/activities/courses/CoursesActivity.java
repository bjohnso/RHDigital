package com.rhdigital.rhclient.activities.courses;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.courses.listeners.BottomNavOnClick;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import static com.rhdigital.rhclient.R.menu.courses_menu_top;

public class CoursesActivity extends AppCompatActivity {

    //Components
    Toolbar mToolbar;
    BottomNavigationView mBottomNavigationView;

    //Static Components
    CoordinatorLayout appBarContainer;
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        mToolbar = findViewById(R.id.topNavigationView);
        mBottomNavigationView = findViewById(R.id.bottomNavigationView);
        mToolbar.setTitle("Courses");
        appBarContainer = findViewById(R.id.app_bar);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        //Setup Toolbar
        setSupportActionBar(mToolbar);

        //Set Listeners
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavOnClick(this));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(courses_menu_top, menu);
        return true;
    }


    @SuppressLint("SourceLockedOrientationActivity")
    public void configureScreenOrientation(boolean isLandscape) {
      // Forced Orientation Landscape
      if (isLandscape)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
      else
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void revealVideoPlayerFullscreen(boolean isPlayer) {
      if (isPlayer) {
        appBarContainer.setVisibility(View.GONE);
        bottomNavigationView.setVisibility(View.GONE);
      } else {
        appBarContainer.setVisibility(View.VISIBLE);
        bottomNavigationView.setVisibility(View.VISIBLE);
        invalidateOptionsMenu();
      }
    }
}
