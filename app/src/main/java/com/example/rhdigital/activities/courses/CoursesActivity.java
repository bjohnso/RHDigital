package com.example.rhdigital.activities.courses;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rhdigital.R;
import com.example.rhdigital.activities.courses.listeners.CoursesOnClick;
import com.example.rhdigital.adapters.SectionsStatePagerAdapter;
import com.example.rhdigital.activities.courses.fragments.DiscoverCoursesFragment;
import com.example.rhdigital.activities.courses.fragments.MyCoursesFragment;
import com.example.rhdigital.activities.courses.fragments.MyResearchFragment;
import com.example.rhdigital.activities.courses.fragments.MyWorkbooksFragment;
import com.example.rhdigital.view.CustomViewPager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CoursesActivity extends AppCompatActivity {

    //Components
    CustomViewPager mCustomViewPager;
    BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        mCustomViewPager = findViewById(R.id.container_courses);
        mBottomNavigationView = findViewById(R.id.bottomNavigationView);

        //Set Listeners
        mBottomNavigationView.setOnNavigationItemSelectedListener(new CoursesOnClick(this));

        setUpViewPager(mCustomViewPager);
    }

    private void setUpViewPager(CustomViewPager customViewPager){
        SectionsStatePagerAdapter sectionsStatePagerAdapter = new SectionsStatePagerAdapter(getSupportFragmentManager());
        sectionsStatePagerAdapter.addFragment(new MyCoursesFragment());
        sectionsStatePagerAdapter.addFragment(new DiscoverCoursesFragment());
        sectionsStatePagerAdapter.addFragment(new MyWorkbooksFragment());
        sectionsStatePagerAdapter.addFragment(new MyResearchFragment());
        customViewPager.setAdapter(sectionsStatePagerAdapter);
    }

    public int getViewPager(){
        return mCustomViewPager.getCurrentItem();
    }

    public void setViewPager(int position){
        mCustomViewPager.setCurrentItem(position);
    }

}
