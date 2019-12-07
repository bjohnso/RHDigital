package com.example.rhdigital.activities.courses.listeners;

import android.app.Activity;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.rhdigital.R;
import com.example.rhdigital.activities.courses.CoursesActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CoursesOnClick implements BottomNavigationView.OnNavigationItemSelectedListener {

    private Activity activity;

    public CoursesOnClick(Activity activity){
        this.activity = activity;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        CoursesActivity coursesActivity = (CoursesActivity) this.activity;

        switch (menuItem.getItemId()){
            case R.id.nav_courses:
                coursesActivity.setViewPager(1);
                return true;
            case R.id.nav_workbooks:
                coursesActivity.setViewPager(2);
                return true;
            case R.id.nav_research:
                coursesActivity.setViewPager(3);
                return true;

                default:
                    return false;
        }
    }
}
