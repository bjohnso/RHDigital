package com.rhdigital.rhclient.activities.courses;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.navigation.NavController;

import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.rhdigital.rhclient.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rhdigital.rhclient.activities.auth.AuthActivity;
import com.rhdigital.rhclient.activities.user.UserActivity;
import com.rhdigital.rhclient.common.services.NavigationService;

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

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
          startAuthActivity();
        }

        mToolbar = findViewById(R.id.topNavigationView);
        mBottomNavigationView = findViewById(R.id.bottomNavigationView);
        mToolbar.setTitle("Courses");
        appBarContainer = findViewById(R.id.app_bar);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        //Setup Toolbar
        setSupportActionBar(mToolbar);

      //Initialise Navigator
      NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
        .findFragmentById(R.id.nav_host_courses);
      NavController navController = navHostFragment.getNavController();
      // Call Navigation Service
      NavigationService.getINSTANCE().initNav(
        getLocalClassName(),
        navController,
        R.navigation.courses_nav_graph,
        R.id.coursesTabFragment);

        //Set Listeners
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavOnClick(getLocalClassName()));
        mToolbar.setOnMenuItemClickListener(new MenuItemOnClick(this));
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

    private void startAuthActivity() {
      Intent intent = new Intent(this, AuthActivity.class);
      startActivity(intent);
    }

  public static class BottomNavOnClick implements BottomNavigationView.OnNavigationItemSelectedListener {

      private String className;

    public BottomNavOnClick(String className) {
      this.className = className;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
      switch (menuItem.getItemId()){
        case R.id.courses_bottom_nav_courses:
          NavigationService.getINSTANCE().navigate(className, R.id.coursesTabFragment, null);
          return true;
        case R.id.courses_bottom_nav_workbooks:
          NavigationService.getINSTANCE().navigate(className, R.id.myWorkbooksFragment, null);
          return true;
        case R.id.courses_bottom_nav_research:
          NavigationService.getINSTANCE().navigate(className, R.id.myResearchFragment, null);
          return true;
      }
      return false;
    }
  }

  public static class MenuItemOnClick implements Toolbar.OnMenuItemClickListener {

      Context context;

      public MenuItemOnClick(Context context) {
        this.context = context;
      }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
      if (menuItem.getItemId() == R.id.courses_top_nav_profile) {
        Intent intent = new Intent(context, UserActivity.class);
        context.startActivity(intent);
      }
      return false;
    }
  }
}
