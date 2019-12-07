package com.example.rhdigital.activities.courses.listeners;

import android.app.Activity;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.example.rhdigital.R;
import com.example.rhdigital.activities.courses.CoursesActivity;
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

                default:
                    return false;
        }
    }
}
