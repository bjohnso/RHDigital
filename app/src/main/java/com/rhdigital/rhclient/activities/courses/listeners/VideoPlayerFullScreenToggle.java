package com.rhdigital.rhclient.activities.courses.listeners;

import android.content.Context;
import android.view.View;

import com.rhdigital.rhclient.activities.courses.CoursesActivity;
import com.rhdigital.rhclient.activities.courses.services.VideoPlayerService;

public class VideoPlayerFullScreenToggle implements View.OnClickListener {

  Context context;

  public VideoPlayerFullScreenToggle(Context context) {
    this.context = context;
  }

  @Override
  public void onClick(View view) {
    if (!VideoPlayerService.getInstance().isFullScreen()) {
      CoursesActivity coursesActivity = (CoursesActivity) context;
      coursesActivity.setViewPager(3);
    } else {
      CoursesActivity coursesActivity = (CoursesActivity) context;
      coursesActivity.setViewPager(0);
    }
  }
}
