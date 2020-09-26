package com.rhdigital.rhclient;

import android.app.Application;
import android.content.res.Configuration;

import androidx.annotation.NonNull;

public class RHApplication extends Application {

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

//  public VideoPlayerService getVideoPlayerService() {
//      return VideoPlayerService.getInstance();
//    }
}
