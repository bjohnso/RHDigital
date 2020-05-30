package com.rhdigital.rhclient.activities.courses.fragments;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
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
import com.rhdigital.rhclient.database.model.Course;
import com.rhdigital.rhclient.database.viewmodel.CourseViewModel;
import com.rhdigital.rhclient.activities.courses.adapters.CoursesRecyclerViewAdapter;

import java.util.HashMap;
import java.util.List;

public class DiscoverCoursesFragment extends Fragment {

    //View Model
    private CourseViewModel courseViewModel;

    //Adapters
    private CoursesRecyclerViewAdapter coursesRecyclerViewAdapter;

    private List<Course> courses;

  //Observables
  private LiveData<HashMap<String, Bitmap>> bitMapObservable;
  private LiveData<HashMap<String, Uri>> videoUrlObservable;
  private LiveData<List<Course>> courseObservable;

    //Components
    private RecyclerView recyclerView;

    private boolean hasAttachedRecycler = false;

    private int width = 0;
    private int height = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.discover_courses_layout, container, false);
        recyclerView = view.findViewById(R.id.discover_courses_recycler);

        //Initialise View Model
        courseViewModel = new CourseViewModel(getActivity().getApplication());

        // Initialise Adapter
        coursesRecyclerViewAdapter = new CoursesRecyclerViewAdapter(getContext());

        calculateImageDimensions();

        // Create Observers

      final Observer<HashMap<String, Uri>> videoUriObserver = new Observer<HashMap<String, Uri>>() {
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

      final Observer<HashMap<String, Bitmap>> bitMapObserver = new Observer<HashMap<String, Bitmap>>() {
        @Override
        public void onChanged(HashMap<String, Bitmap> stringBitmapHashMap) {
          if (stringBitmapHashMap != null) {
            if (courses.size() == stringBitmapHashMap.size()) {
              bitMapObservable.removeObserver(this);
              videoUrlObservable = courseViewModel.getAllVideoUri(courses, width, height);
              videoUrlObservable.observe(getActivity(), videoUriObserver);
            }
            coursesRecyclerViewAdapter.setImageUriMap(stringBitmapHashMap);
          } else {
            Toast.makeText(getContext(), R.string.server_error_courses, Toast.LENGTH_LONG);
          }
        }
      };

      final Observer<List<Course>> courseObserver = c -> {
        if (c != null) {
          coursesRecyclerViewAdapter.setCourses(c);
          courses = c;
          bitMapObservable = courseViewModel.getAllBitmap(getContext(), c, width, height);
          bitMapObservable.observe(getActivity(), bitMapObserver);
        } else {
          Toast.makeText(getContext(), R.string.server_error_courses, Toast.LENGTH_LONG);
        }
      };

        // Initialise RecyclerView
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(10);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        // Call Observer
        courseObservable = courseViewModel.getAllUndiscoveredCourses();
        courseObservable.observe(getActivity(), courseObserver);
        return view;
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



