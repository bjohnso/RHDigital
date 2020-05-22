package com.rhdigital.rhclient.activities.courses;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.rhdigital.rhclient.R;
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

      NavController childController = Navigation.findNavController(this, R.id.nav_host_courses);
      NavOptions navOptions = new NavOptions.Builder()
        .setLaunchSingleTop(true)
        .setPopUpTo(childController.getGraph().getStartDestination(), false)
        .build();

        //Set Listeners
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavOnClick(childController, navOptions));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(courses_menu_top, menu);
        return true;
    }

  @Override
  protected void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
    Log.d("NAV", "SAVED STATE ACTIVITY");
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

  public static class BottomNavOnClick implements BottomNavigationView.OnNavigationItemSelectedListener {

    private NavController navController;
    private NavOptions navOptions;

    public BottomNavOnClick(NavController navController, NavOptions navOptions) {
      this.navController =  navController;
      this.navOptions = navOptions;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
      switch (menuItem.getItemId()){
        case R.id.courses_bottom_nav_courses:
          navController.navigate(R.id.coursesTabFragment, null, navOptions);
          return true;
        case R.id.courses_bottom_nav_workbooks:
          navController.navigate(R.id.myWorkbooksFragment, null, navOptions);
          return true;
        case R.id.courses_bottom_nav_research:
          navController.navigate(R.id.myResearchFragment, null, navOptions);
          return true;
      }
      return false;
    }
  }
}
