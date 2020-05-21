package com.rhdigital.rhclient.activities.courses.listeners;

import android.content.Context;
import android.view.View;

import com.rhdigital.rhclient.activities.courses.adapters.CoursesRecyclerViewAdapter;

public class CourseItemViewBackOnClick implements View.OnClickListener {

  Context context;
  CoursesRecyclerViewAdapter.CoursesViewHolder holder;

  public CourseItemViewBackOnClick(Context context, CoursesRecyclerViewAdapter.CoursesViewHolder holder) {
    this. context = context;
    this.holder = holder;
  }

  @Override
  public void onClick(View view) {
    holder.disableVideo();
    holder.destroyVideo();
  }
}
