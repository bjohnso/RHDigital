package com.example.rhdigital.activities.courses;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

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
    SectionsStatePagerAdapter sectionsStatePagerAdapter;
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
        int preserve = -1;
        CoursesTabFragment frag = null;
        if (position == 0) {
            frag = (CoursesTabFragment) sectionsStatePagerAdapter.getItem(position);
            preserve = frag.getViewPager();
        }
        mCustomViewPager.setCurrentItem(position);

        if (preserve != -1) {
            frag.getTabLayout().getTabAt(preserve).select();
        }
    }

}
