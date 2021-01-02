package com.rhdigital.rhclient;

import android.app.Activity;
import android.app.Application;
import android.content.res.Configuration;

import androidx.annotation.NonNull;

public class RHApplication extends Application {

  private Activity currentActivity = null;

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

  public void setCurrentActivity(Activity activity) {
    this.currentActivity = activity;
  }

  public Activity getCurrentActivity() {
    return currentActivity;
  }

  //  public VideoPlayerService getVideoPlayerService() {
//      return VideoPlayerService.getInstance();
//    }
}
