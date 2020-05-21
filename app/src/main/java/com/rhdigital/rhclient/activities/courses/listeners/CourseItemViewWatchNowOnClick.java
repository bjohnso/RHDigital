package com.rhdigital.rhclient.activities.courses.listeners;

import android.content.Context;
import android.view.View;

import com.rhdigital.rhclient.activities.courses.adapters.CoursesRecyclerViewAdapter;
import com.rhdigital.rhclient.activities.courses.view.CoursesViewHolder;

public class CourseItemViewWatchNowOnClick implements View.OnClickListener {

  private Context context;
  private CoursesViewHolder holder;

  public CourseItemViewWatchNowOnClick(Context context, CoursesViewHolder holder) {
    this.context = context;
    this.holder = holder;
  }

  @Override
  public void onClick(View view) {
    holder.initVideoPlayer();
  }
}
