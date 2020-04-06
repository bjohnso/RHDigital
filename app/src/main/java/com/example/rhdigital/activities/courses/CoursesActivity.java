package com.example.rhdigital.activities.courses;

import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.rhdigital.R;
import com.example.rhdigital.activities.courses.fragments.CoursesTabFragment;
import com.example.rhdigital.activities.courses.listeners.BottomNavOnClick;
import com.example.rhdigital.ui.adapters.SectionsStatePagerAdapter;
import com.example.rhdigital.activities.courses.fragments.MyResearchFragment;
import com.example.rhdigital.activities.courses.fragments.MyWorkbooksFragment;
import com.example.rhdigital.ui.view.CustomViewPager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import static com.example.rhdigital.R.menu.courses_menu_top;

public class CoursesActivity extends AppCompatActivity {

    //Components
    CustomViewPager mCustomViewPager;
    Toolbar mToolbar;
    BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        mCustomViewPager = findViewById(R.id.container_courses);
        mToolbar = findViewById(R.id.topNavigationView);
        mBottomNavigationView = findViewById(R.id.bottomNavigationView);

        //Setup Toolbar
        setSupportActionBar(mToolbar);

        //Set Listeners
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavOnClick(this));

        setUpViewPager(mCustomViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(courses_menu_top, menu);
        return true;
    }

    private void setUpViewPager(CustomViewPager customViewPager){
        SectionsStatePagerAdapter sectionsStatePagerAdapter = new SectionsStatePagerAdapter(getSupportFragmentManager());
        sectionsStatePagerAdapter.addFragment(new CoursesTabFragment());
        sectionsStatePagerAdapter.addFragment(new MyWorkbooksFragment());
        sectionsStatePagerAdapter.addFragment(new MyResearchFragment());
        customViewPager.setAdapter(sectionsStatePagerAdapter);
        customViewPager.setSwipeable(false);
    }

    public int getViewPager(){
        return mCustomViewPager.getCurrentItem();
    }

    public void setViewPager(int position){
        mCustomViewPager.setCurrentItem(position);
    }

}
