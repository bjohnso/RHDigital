package com.rhdigital.rhclient.activities.splash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.rhauth.RHAuthActivity;
import com.rhdigital.rhclient.database.RHDatabase;

import com.rhdigital.rhclient.database.services.PopulateRoomAsync;

import java.util.List;

public class SplashActivity extends AppCompatActivity {

  // Animation
  private AnimatedVectorDrawable anim;
  private ImageView logo;

  @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.actvity_rhsplash);

    FirebaseAuth.getInstance().signOut();

      PopulateRoomAsync populateRoomAsync = new PopulateRoomAsync();
      populateRoomAsync.getInserts().observe(this, (Observer<List<Long>>) inserts -> {
        Intent intent = new Intent(getBaseContext(), RHAuthActivity.class);
        getApplicationContext().startActivity(intent);
      });
      populateRoomAsync.populateFromUpstream(RHDatabase.getDatabase(this));

      // Animation
      anim = (AnimatedVectorDrawable) getApplicationContext().getDrawable(R.drawable.rh_vector_animation);
      logo = findViewById(R.id.splash_logo);
      logo.setImageDrawable(anim);
      anim.start();
    }
}
