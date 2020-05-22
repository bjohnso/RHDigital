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

                return true;
            case R.id.courses_bottom_nav_workbooks:

                return true;
            case R.id.courses_bottom_nav_research:

                return true;
        }
        return false;
    }
}
