package com.rhdigital.rhclient.activities.courses.fragments;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.courses.CoursesActivity;
import com.rhdigital.rhclient.database.model.Course;
import com.rhdigital.rhclient.database.viewmodel.CourseViewModel;
import com.rhdigital.rhclient.activities.courses.adapters.CoursesRecyclerViewAdapter;

import java.util.HashMap;
import java.util.List;

public class MyCoursesFragment extends Fragment {

  //View Model
  private CourseViewModel courseViewModel;

  //Adapters
  private CoursesRecyclerViewAdapter coursesRecyclerViewAdapter;

  private List<Course> courses;

  //Observables
  private LiveData<HashMap<String, Bitmap>> videoPosterObservable;
  private LiveData<HashMap<String, Uri>> videoUrlObservable;
  private LiveData<List<Course>> courseObservable;

  //Observers
  private Observer<HashMap<String, Bitmap>> videoPosterObserver;
  private Observer<HashMap<String, Uri>> videoUrlObserver;
  private Observer<List<Course>> courseObserver;

  //Components
  private RecyclerView recyclerView;

  private boolean hasAttachedRecycler = false;

  private int width = 0;
  private int height = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.my_courses_layout, container, false);
      recyclerView = view.findViewById(R.id.my_courses_recycler);

      //Initialise View Model
      courseViewModel = ((CoursesActivity)getActivity()).getCourseViewModel();

      // Initialise Adapter
      coursesRecyclerViewAdapter = new CoursesRecyclerViewAdapter(getContext());

      calculateImageDimensions();

      ((CoursesActivity)getActivity()).setToolbarTitle("Courses");

      // Observers
      videoUrlObserver = new Observer<HashMap<String, Uri>>() {
        @Override
        public void onChanged(HashMap<String, Uri> stringUriHashMap) {
          if (stringUriHashMap != null) {
            if (courses.size() == stringUriHashMap.size()) {
              videoUrlObservable.removeObserver(this);
            }
            coursesRecyclerViewAdapter.setVideoURIMap(stringUriHashMap);
            if (!hasAttachedRecycler) {
              hasAttachedRecycler = true;
              recyclerView.setAdapter(coursesRecyclerViewAdapter);
            }
          } else {
            Toast.makeText(getContext(), R.string.server_error_courses, Toast.LENGTH_LONG);
          }
        }
      };

      videoPosterObserver = new Observer<HashMap<String, Bitmap>>() {
        @Override
        public void onChanged(HashMap<String, Bitmap> stringBitmapHashMap) {
          if (stringBitmapHashMap != null) {
            if (courses.size() == stringBitmapHashMap.size()) {
              videoPosterObservable.removeObserver(this);
              videoUrlObservable = courseViewModel.getAllVideoUri(courses, width, height);
              videoUrlObservable.observe(getActivity(), videoUrlObserver);
            }
            coursesRecyclerViewAdapter.setImageUriMap(stringBitmapHashMap);
          } else {
            Toast.makeText(getContext(), R.string.server_error_courses, Toast.LENGTH_LONG);
          }
        }
      };

      courseObserver = new Observer<List<Course>>() {
        @Override
        public void onChanged(List<Course> c) {
          if (c != null) {
            courseObservable.removeObserver(this);
            receiveCourses(c);
          } else {
            Toast.makeText(getContext(), R.string.server_error_courses, Toast.LENGTH_LONG);
          }
        }
      };

      // Initialise RecyclerView
      recyclerView.setHasFixedSize(true);
      recyclerView.setItemViewCacheSize(10);
      recyclerView.setDrawingCacheEnabled(true);
      recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
      recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

      // Call Courses Observer
      courseObservable = ((CoursesActivity)getActivity()).getAuthorisedCoursesObservable();
      if ((courses = courseObservable.getValue()) != null) {
        receiveCourses(courses);
      } else {
        courseObservable.observe(getActivity(), courseObserver);
      }
      return view;
    }

    private void receiveCourses(List<Course> courses) {
      coursesRecyclerViewAdapter.setCourses(courses);
      this.courses = courses;
      if (videoPosterObservable != null) {
        videoPosterObservable.removeObservers(this);
      }
      videoPosterObservable = courseViewModel.getAllVideoPosters(getContext(), courses, width, height);
      videoPosterObservable.observe(getActivity(), videoPosterObserver);
    }

  private void calculateImageDimensions() {
    float dip = 230f;
    Resources r = getResources();
    float px = TypedValue.applyDimension(
      TypedValue.COMPLEX_UNIT_DIP,
      dip,
      r.getDisplayMetrics()
    );

    DisplayMetrics displayMetrics = new DisplayMetrics();
    getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    width = displayMetrics.widthPixels;
    height = Math.round(px);
  }
}
