package com.rhdigital.rhclient.activities.splash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.rhapp.RHAppActivity;
import com.rhdigital.rhclient.activities.rhauth.RHAuthActivity;
import com.rhdigital.rhclient.database.RHDatabase;

import com.rhdigital.rhclient.database.repository.RHRepository;
import com.rhdigital.rhclient.database.services.PopulateRoomAsync;

import java.util.List;

public class SplashActivity extends AppCompatActivity {

  // Animation
  private AnimatedVectorDrawable anim;
  private ImageView logo;

  // Auth
  private Intent rhAuthIntent;

  @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.actvity_rhsplash);

      FirebaseAuth.getInstance().signOut();

      rhAuthIntent = new Intent(this, RHAuthActivity.class);

      PopulateRoomAsync populateRoomAsync = new PopulateRoomAsync();
      populateRoomAsync.getInserts().observe(this, (Observer<List<Long>>) inserts -> {
//        RHRepository rhRepository = new RHRepository(getApplication());
//        rhRepository.authoriseProgram("37Bd1Esq57d7iT48URfE");
//        rhRepository.authoriseProgram("IoRq8vxEEtCxsNg9BdPP");
//        rhRepository.authoriseProgram("eMIDmA1Dr3F0EaWJvsVC");
        startAuthActivity();
      });
      populateRoomAsync.populateFromUpstream(RHDatabase.getDatabase(this));
      startSplashAnimation();
    }

    private void startSplashAnimation() {
      anim = (AnimatedVectorDrawable) getApplicationContext().getDrawable(R.drawable.rh_vector_animation);
      logo = findViewById(R.id.splash_logo);
      logo.setImageDrawable(anim);
      anim.start();
    }

    private void startAuthActivity() {
      startActivity(rhAuthIntent);
    }
}
