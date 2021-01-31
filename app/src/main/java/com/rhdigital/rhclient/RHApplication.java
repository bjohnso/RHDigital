package com.rhdigital.rhclient;

import android.app.Activity;
import android.app.Application;
import android.content.res.Configuration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.rhdigital.rhclient.activities.rhapp.fragments.RHAppFragment;

public class RHApplication extends Application {

  private AppCompatActivity currentActivity = null;
  private RHAppFragment currentFragment = null;

  @Override
  public void onCreate() {
    super.onCreate();
  }

  @Override
  public void onConfigurationChanged(@NonNull Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
  }

  public void setCurrentActivity(AppCompatActivity activity) {
    this.currentActivity = activity;
  }

  public Activity getCurrentActivity() {
    return currentActivity;
  }

  public void setCurrentFragment(RHAppFragment currentFragment) {
    this.currentFragment = currentFragment;
  }

  public RHAppFragment getCurrentFragment() {
    return currentFragment;
  }
}
