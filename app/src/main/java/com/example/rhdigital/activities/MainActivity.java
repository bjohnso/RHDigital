package com.example.rhdigital.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.rhdigital.R;
import com.example.rhdigital.adapters.SectionsStatePagerAdapter;
import com.example.rhdigital.view.CustomViewPager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, AuthActivity.class);
        this.startActivity(intent);
    }
}
