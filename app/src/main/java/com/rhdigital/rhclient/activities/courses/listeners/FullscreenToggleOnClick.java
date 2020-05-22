package com.rhdigital.rhclient.activities.courses.listeners;

import android.content.Context;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.NavOptions;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.courses.CoursesActivity;
import com.rhdigital.rhclient.activities.courses.services.VideoPlayerService;

public class FullscreenToggleOnClick implements View.OnClickListener {

  private Context context;
  private NavController navController;
  private NavOptions navOptions;

  public FullscreenToggleOnClick(Context context, NavController navController, NavOptions navOptions) {
    this.context = context;
    this.navController = navController;
    this.navOptions = navOptions;
  }

  @Override
  public void onClick(View view) {
    if (!VideoPlayerService.getInstance().isFullScreen()) {
      ((CoursesActivity) context).revealVideoPlayerFullscreen(true);
      ((CoursesActivity) context).configureScreenOrientation(true);
      navController.navigate(R.id.coursesVideoPlayerFullscreenFragment, null, navOptions);
    }
    else {
      ((CoursesActivity) context).configureScreenOrientation(false);
      ((CoursesActivity) context).revealVideoPlayerFullscreen(false);
      if (!navController.popBackStack(R.id.coursesTabFragment, false)) {
        navController.navigate(R.id.coursesTabFragment);
      }
    }
  }
}
