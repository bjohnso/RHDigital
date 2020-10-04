package com.rhdigital.rhclient.activities.splash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.rhdigital.rhclient.activities.rhapp.RHAppActivity;
import com.rhdigital.rhclient.activities.rhauth.RHAuthActivity;
import com.rhdigital.rhclient.database.RHDatabase;

import com.rhdigital.rhclient.database.services.PopulateRoomAsync;

import java.util.List;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      PopulateRoomAsync populateRoomAsync = new PopulateRoomAsync();
      populateRoomAsync.getInserts().observe(this, (Observer<List<Long>>) inserts -> {
        Intent intent = new Intent(getBaseContext(), RHAuthActivity.class);
        getApplicationContext().startActivity(intent);
      });
      populateRoomAsync.populateFromUpstream(RHDatabase.getDatabase(this));
    }
}
