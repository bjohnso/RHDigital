package com.rhdigital.rhclient.activities.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Path;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.auth.AuthActivity;
import com.rhdigital.rhclient.database.RHDatabase;
import com.rhdigital.rhclient.common.loader.CustomLoader;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {

    ArrayList<CustomLoader> viewList = new ArrayList<>();
    ArrayList<Path> animationPathList = new ArrayList<>();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser;
    RHDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      firebaseUser = firebaseAuth.getCurrentUser();
      if (firebaseUser != null) {
        firebaseAuth.signOut();
      }
      database = RHDatabase.getDatabase(this);
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_auth);
      Intent intent = new Intent(this, AuthActivity.class);
      this.startActivity(intent);
    }
}
