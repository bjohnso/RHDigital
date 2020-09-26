package com.rhdigital.rhclient.activities.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.rhdigital.rhclient.database.RHDatabase;
import com.rhdigital.rhclient.database.repository.RHRepository;
import com.rhdigital.rhclient.database.services.PopulateRoomAsync;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      RHRepository rhRepository = new RHRepository((this).getApplication());
      PopulateRoomAsync populateRoomAsync = new PopulateRoomAsync();
      populateRoomAsync.populateFromUpstream(RHDatabase.getDatabase(this));
      // Intent intent = new Intent(this, AuthActivity.class);
      //Intent intent = new Intent(this, CoursesActivity.class);
      //this.startActivity(intent);
    }
}
