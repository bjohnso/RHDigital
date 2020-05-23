package com.rhdigital.rhclient.common.services;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavGraph;
import androidx.navigation.NavOptions;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.courses.services.VideoPlayerService;

import java.util.HashMap;

public class NavigationService {

  private static NavigationService INSTANCE;
  private HashMap<String, NavController> controllerMap = new HashMap<>();
  private Bundle postNavigationRestorationData;

  private NavigationService() { }

  //TODO : CREATE UTILITY TO DETERMINE IF A PARTICULAR NAVCONTROLLER ALREADY EXISTS

  public static NavigationService getINSTANCE() {
    if (INSTANCE == null) {
      INSTANCE = new NavigationService();
    }
    return INSTANCE;
  }

  public void initNav(String className, NavController navController, int graphResourceId, int startDestination) {
      NavGraph navGraph = navController.getNavInflater().inflate(graphResourceId);
      NavController prev;
      if ((prev = controllerMap.get(className)) != null) {
        navGraph.setStartDestination(prev.getGraph().getStartDestination());
        navController.setGraph(navGraph);
      } else {
        navGraph.setStartDestination(startDestination);
        navController.setGraph(navGraph);
      }
      controllerMap.put(className, navController);
  }

  public void addNav(String className, NavController navController) {
    controllerMap.put(className, navController);
  }

  public void navigate(String className, int destinationId, Bundle postNavigationRestorationData) {
    if (postNavigationRestorationData != null) {
      this.postNavigationRestorationData = postNavigationRestorationData;
    }
    NavController navController = controllerMap.get(className);
    NavOptions navOptions = new NavOptions.Builder()
      .setLaunchSingleTop(true)
      .build();
    navController.getGraph().setStartDestination(destinationId);
    navController.navigate(destinationId, null, navOptions);
    controllerMap.put(className, navController);
    if (!VideoPlayerService.getInstance().isFullScreen()
      && VideoPlayerService.getInstance().isVideoEnabled()
      && destinationId != R.id.coursesVideoPlayerFullscreenFragment) {
      VideoPlayerService.getInstance().destroyVideo();
    }
  }

  public Bundle getPostNavigationRestorationData() {
    return postNavigationRestorationData;
  }
}
