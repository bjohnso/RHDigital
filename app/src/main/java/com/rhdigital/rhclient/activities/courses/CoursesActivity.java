package com.rhdigital.rhclient.activities.courses;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.courses.fragments.CoursesTabFragment;
import com.rhdigital.rhclient.activities.courses.fragments.CoursesVideoPlayerFullscreenFragment;
import com.rhdigital.rhclient.activities.courses.listeners.BottomNavOnClick;
import com.rhdigital.rhclient.common.adapters.SectionsStatePagerAdapter;
import com.rhdigital.rhclient.activities.courses.fragments.MyResearchFragment;
import com.rhdigital.rhclient.activities.courses.fragments.MyWorkbooksFragment;
import com.rhdigital.rhclient.common.loader.CustomViewPager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

import static com.rhdigital.rhclient.R.menu.courses_menu_top;

public class CoursesActivity extends AppCompatActivity {

    //Components
    SectionsStatePagerAdapter sectionsStatePagerAdapter;
    CustomViewPager mCustomViewPager;
    Toolbar mToolbar;
    BottomNavigationView mBottomNavigationView;

    //Static Components
    CoordinatorLayout appBarContainer;
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        mCustomViewPager = findViewById(R.id.container_courses);
        mToolbar = findViewById(R.id.topNavigationView);
        mBottomNavigationView = findViewById(R.id.bottomNavigationView);
        mToolbar.setTitle("Courses");
        appBarContainer = findViewById(R.id.app_bar);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        //Setup Toolbar
        setSupportActionBar(mToolbar);

        setUpViewPager(mCustomViewPager);

        //Set Listeners
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavOnClick(this));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(courses_menu_top, menu);
        return true;
    }

    private void setUpViewPager(CustomViewPager customViewPager){
        sectionsStatePagerAdapter = new SectionsStatePagerAdapter(getSupportFragmentManager());
        // Fragments
      sectionsStatePagerAdapter.setFragmentPagingMap(new HashMap<Integer, String>(){
        {put(0, "CoursesTabFragment");
          put(1, "MyWorkbooksFragment");
          put(2, "MyResearchFragment");
          put(3, "CoursesVideoPlayerFullscreenFragment");}});

        customViewPager.setAdapter(sectionsStatePagerAdapter);
        customViewPager.setSwipeable(false);
    }

    public int getViewPager(){
        return mCustomViewPager.getCurrentItem();
    }

    public void setViewPager(int position){
//        int preserve = -1;
//        CoursesTabFragment frag = null;
//        if (position == 0) {
//            frag = (CoursesTabFragment) sectionsStatePagerAdapter.getItem(position);
//            preserve = frag.getViewPager();
//        }
        mCustomViewPager.setCurrentItem(position);

//        if (preserve != -1) {
//            frag.getTabLayout().getTabAt(preserve).select();
//        }
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
