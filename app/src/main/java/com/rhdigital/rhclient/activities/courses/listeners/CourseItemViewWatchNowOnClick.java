package com.rhdigital.rhclient.activities.courses.listeners;

import android.content.Context;
import android.view.View;

import com.rhdigital.rhclient.activities.courses.adapters.CoursesRecyclerViewAdapter;

public class CourseItemViewWatchNowOnClick implements View.OnClickListener {

  private Context context;
  private CoursesRecyclerViewAdapter.CoursesViewHolder holder;

  public CourseItemViewWatchNowOnClick(Context context, CoursesRecyclerViewAdapter.CoursesViewHolder holder) {
    this.context = context;
    this.holder = holder;
  }

  @Override
  public void onClick(View view) {
    holder.startVideo();
  }
}
