package com.rhdigital.rhclient.activities.rhroom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.rhapp.RHAppActivity;
import com.rhdigital.rhclient.common.dto.PopulateRoomDto;
import com.rhdigital.rhclient.database.RHDatabase;
import com.rhdigital.rhclient.database.repository.RHRepository;
import com.rhdigital.rhclient.database.services.FirebaseRoomService;

import java.util.List;

public class RHRoomActivity extends AppCompatActivity {

  // Animation
  private AnimatedVectorDrawable anim;
  private ImageView logo;

  private RHRepository rhRepository;

  // Auth
  private Intent rhAppIntent;

  @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.actvity_rhsplash);
      startSplashAnimation();
    }

  @Override
  protected void onStart() {
    super.onStart();
    rhRepository = new RHRepository(getApplication());
    rhAppIntent = new Intent(this, RHAppActivity.class);
    populateRoom(new PopulateRoomDto(PopulateRoomDto.APP_START));
  }

  public void populateRoom(PopulateRoomDto populateRoom) {
    rhRepository.syncWithRemote(
            RHDatabase.getDatabase(this),
            populateRoom
    ).observe(this, (Observer<List<Long>>) inserts -> {
      if (inserts == null) {
        Log.e("SplashActivity", "Population Failed");
      }
      startRHAppActivity();
    });
  }

  private void startSplashAnimation() {
    anim = (AnimatedVectorDrawable) getApplicationContext().getDrawable(R.drawable.rh_vector_animation);
    logo = findViewById(R.id.splash_logo);
    logo.setImageDrawable(anim);
    anim.start();
  }

  private void startRHAppActivity() {
    startActivity(rhAppIntent);
  }
}
