package com.rhdigital.rhclient.activities.courses.listeners;

import android.app.Activity;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.courses.CoursesActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavOnClick implements BottomNavigationView.OnNavigationItemSelectedListener {

    private Activity activity;

    public BottomNavOnClick(Activity activity){
        this.activity = activity;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        CoursesActivity coursesActivity = (CoursesActivity) this.activity;

        switch (menuItem.getItemId()){
            case R.id.courses_bottom_nav_courses:
                coursesActivity.setViewPager(0);
                return true;
            case R.id.courses_bottom_nav_workbooks:
                coursesActivity.setViewPager(1);
                return true;
            case R.id.courses_bottom_nav_research:
                coursesActivity.setViewPager(2);
                return true;
        }
        return false;
    }
}
