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
import com.rhdigital.rhclient.activities.courses.CoursesActivity;
import com.rhdigital.rhclient.database.model.Course;
import com.rhdigital.rhclient.database.model.CourseWithWorkbooks;
import com.rhdigital.rhclient.database.viewmodel.WorkbookViewModel;
import com.rhdigital.rhclient.activities.courses.adapters.WorkbooksRecyclerViewAdapter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Phaser;

public class MyWorkbooksFragment extends Fragment {

    //Observables
    private LiveData<HashMap<String, Bitmap>> workbookPostersObservable;
    private LiveData<List<CourseWithWorkbooks>> courseWithWorkbooksObservable;
    private LiveData<HashMap<String, List<Uri>>> workbookURIObservable;

    //Observers
    private Observer<HashMap<String, Bitmap>> workbookPostersObserver;
    private Observer<List<CourseWithWorkbooks>> courseWithWorkbooksObserver;
    private Observer<HashMap<String, List<Uri>>> workbookURIObserver;

    //View Model
    private WorkbookViewModel workbookViewModel;

    //Adapters
    private WorkbooksRecyclerViewAdapter workbooksRecyclerViewAdapter;

    //Components
    private RecyclerView recyclerView;

    private boolean hasAttachedRecycler = false;

    private int width = 0;
    private int height = 0;

    private List<CourseWithWorkbooks> courseWithWorkbooks;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_workbooks_layout, container, false);
        recyclerView = view.findViewById(R.id.workbooks_recycler_view);

        calculateImageDimensions();

        ((CoursesActivity)getActivity()).setToolbarTitle("Workbooks");

        //Initialise View Model
        workbookViewModel = ((CoursesActivity)getActivity()).getWorkbookViewModel();

        //Initialise Adapter
        workbooksRecyclerViewAdapter = new WorkbooksRecyclerViewAdapter();

        //Create an Observer
        courseWithWorkbooksObserver = new Observer<List<CourseWithWorkbooks>>() {
          @Override
          public void onChanged(List<CourseWithWorkbooks> c) {
            if (c != null) {
              courseWithWorkbooksObservable.removeObserver(this::onChanged);
              receiveWorkbooks(c);
            }
          }
        };

      workbookURIObserver = new Observer<HashMap<String, List<Uri>>>() {
        @Override
        public void onChanged(HashMap<String, List<Uri>> stringListHashMap) {
          if (stringListHashMap != null) {
            boolean complete = true;
            if (stringListHashMap.size() >= courseWithWorkbooks.size()) {
              for(CourseWithWorkbooks c : courseWithWorkbooks) {
                if (stringListHashMap.get(c.getCourse().getId()) == null
                  || stringListHashMap.get(c.getCourse().getId()).size() < c.getWorkbooks().size()) {
                  complete = false;
                  break;
                }
              }
              if (complete) {
                workbookURIObservable.removeObserver(this::onChanged);
                workbooksRecyclerViewAdapter.setWorkbookURI(stringListHashMap);
                if (!hasAttachedRecycler) {
                  hasAttachedRecycler = true;
                  recyclerView.setAdapter(workbooksRecyclerViewAdapter);
                }
              }
            }
          } else {
            Toast.makeText(getContext(), R.string.server_error_courses, Toast.LENGTH_LONG);
          }
        }
      };

        workbookPostersObserver = new Observer<HashMap<String, Bitmap>>() {
          @Override
          public void onChanged(HashMap<String, Bitmap> stringBitmapHashMap) {
            if (stringBitmapHashMap != null) {
              if (stringBitmapHashMap.size() >= courseWithWorkbooks.size()) {
                workbookPostersObservable.removeObserver(this::onChanged);
                workbooksRecyclerViewAdapter.setImageUriMap(stringBitmapHashMap);
                workbookURIObservable = workbookViewModel.getAllWorkbookUri(courseWithWorkbooks);
                workbookURIObservable.observe(getActivity(), workbookURIObserver);
              }
            } else {
              Toast.makeText(getContext(), R.string.server_error_courses, Toast.LENGTH_LONG);
            }
          }
        };

        // Initialise RecyclerView
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        //Call Observer
        courseWithWorkbooksObservable = ((CoursesActivity)getActivity()).getCourseWithWorkbooksObservable();
        if ((courseWithWorkbooks = courseWithWorkbooksObservable.getValue()) != null) {
          receiveWorkbooks(courseWithWorkbooks);
        } else {
          courseWithWorkbooksObservable.observe(getActivity(), courseWithWorkbooksObserver);
        }
        return view;
    }

  private void receiveWorkbooks(List<CourseWithWorkbooks> workbooks) {
      workbooksRecyclerViewAdapter.setWorkbooks(workbooks);
      this.courseWithWorkbooks = workbooks;
      if (workbookPostersObservable != null) {
        workbookPostersObservable.removeObservers(this);
      }
      workbookPostersObservable = workbookViewModel.getAllWorkbookPosters(getContext(), courseWithWorkbooks, width, height);
      workbookPostersObservable.observe(getActivity(), workbookPostersObserver);
    }

  private void calculateImageDimensions() {
    float dip = 120f;
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
