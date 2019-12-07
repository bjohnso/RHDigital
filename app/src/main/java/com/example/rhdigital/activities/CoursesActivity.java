package com.example.rhdigital.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rhdigital.R;
import com.example.rhdigital.adapters.SectionsStatePagerAdapter;
import com.example.rhdigital.fragments.DiscoverCoursesFragment;
import com.example.rhdigital.fragments.MyCoursesFragment;
import com.example.rhdigital.fragments.MyResearchFragment;
import com.example.rhdigital.fragments.MyWorkbooksFragment;
import com.example.rhdigital.view.CustomViewPager;

public class CoursesActivity extends AppCompatActivity {

    //Components
    CustomViewPager mCustomViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        mCustomViewPager = findViewById(R.id.container_courses);
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
