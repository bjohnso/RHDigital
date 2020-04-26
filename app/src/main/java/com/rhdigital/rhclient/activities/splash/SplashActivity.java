package com.rhdigital.rhclient.activities.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.auth.AuthActivity;
import com.rhdigital.rhclient.activities.courses.CoursesActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        Intent intent = new Intent(this, AuthActivity.class);
        this.startActivity(intent);
    }
}
