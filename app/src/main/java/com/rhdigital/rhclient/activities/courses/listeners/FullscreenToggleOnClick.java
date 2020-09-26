//package com.rhdigital.rhclient.activities.courses.listeners;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.view.View;
//
//import androidx.navigation.NavController;
//import androidx.navigation.NavOptions;
//
//import com.rhdigital.rhclient.R;
//import com.rhdigital.rhclient.activities.courses.CoursesActivity;
//import com.rhdigital.rhclient.activities.courses.services.VideoPlayerService;
//import com.rhdigital.rhclient.common.services.NavigationService;
//
//public class FullscreenToggleOnClick implements View.OnClickListener {
//
//  private Context context;
//  private String className;
//  private Bundle postNavigationRestoreData;
//
//  public FullscreenToggleOnClick(Context context, String className, Bundle postNavigationRestoreData) {
//    this.context = context;
//    this.className = className;
//    this.postNavigationRestoreData = postNavigationRestoreData;
//  }
//
//  @Override
//  public void onClick(View view) {
//    if (!VideoPlayerService.getInstance().isFullScreen()) {
//      ((CoursesActivity) context).revealVideoPlayerFullscreen(true);
//      ((CoursesActivity) context).configureScreenOrientation(true);
//      NavigationService.getINSTANCE().navigate(className, R.id.coursesVideoPlayerFullscreenFragment, null, postNavigationRestoreData);
//    }
//    else {
//      ((CoursesActivity) context).configureScreenOrientation(false);
//      ((CoursesActivity) context).revealVideoPlayerFullscreen(false);
//      NavigationService.getINSTANCE().navigate(className, R.id.coursesTabFragment, null, postNavigationRestoreData);
//    }
//  }
//}
