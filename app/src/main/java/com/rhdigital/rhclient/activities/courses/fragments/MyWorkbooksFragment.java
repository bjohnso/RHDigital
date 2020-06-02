package com.rhdigital.rhclient.activities.courses.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.courses.CoursesActivity;
import com.rhdigital.rhclient.activities.user.UserActivity;
import com.rhdigital.rhclient.database.model.Course;
import com.rhdigital.rhclient.database.model.CourseWithWorkbooks;
import com.rhdigital.rhclient.database.viewmodel.WorkbookViewModel;
import com.rhdigital.rhclient.activities.courses.adapters.WorkbooksRecyclerViewAdapter;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Phaser;

import okhttp3.ResponseBody;

public class MyWorkbooksFragment extends Fragment {

    private int REQUEST_CODE = 2;

    //Observables
    private LiveData<HashMap<String, Bitmap>> workbookPostersObservable;
    private LiveData<List<CourseWithWorkbooks>> courseWithWorkbooksObservable;
    private LiveData<HashMap<String, HashMap<String, Uri>>> workbookURIObservable;

    //Observers
    private Observer<HashMap<String, Bitmap>> workbookPostersObserver;
    private Observer<List<CourseWithWorkbooks>> courseWithWorkbooksObserver;
    private Observer<HashMap<String, HashMap<String, Uri>>> workbookURIObserver;

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
        workbooksRecyclerViewAdapter = new WorkbooksRecyclerViewAdapter(this, workbookViewModel);

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

      workbookURIObserver = new Observer<HashMap<String, HashMap<String, Uri>>>() {
        @Override
        public void onChanged(HashMap<String, HashMap<String, Uri>> stringListHashMap) {
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

  @Override
  public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    if (resultCode != Activity.RESULT_OK) {
      return ;
    }

    if (requestCode == REQUEST_CODE) {
      String name = data.getStringExtra("NAME");
      String url = data.getStringExtra("URL");
      String action = data.getStringExtra("ACTION");
      if (action.equalsIgnoreCase("VIEW")) {
        String format = "https://drive.google.com/viewerng/viewer?embedded=true&url=%s";
        String fullPath = String.format(Locale.ENGLISH, format, url);
        Uri uri = Uri.parse(fullPath);
        getContext().startActivity(new Intent(Intent.ACTION_VIEW, uri));
      } else if (action.equalsIgnoreCase("SAVE")) {
        Toast.makeText(getContext(), "Download Started", Toast.LENGTH_SHORT).show();;
        workbookViewModel.downloadWorkbook(url)
          .observe(this, res -> {
            if (res != null) {
              if (writeFileToDisk(name, res)) {
                Toast.makeText(getContext(), "Download Complete", Toast.LENGTH_LONG).show();
                return;
              }
            }
            Toast.makeText(getContext(), "Download Failed", Toast.LENGTH_LONG).show();
          });
      }
    }
  }

  private boolean writeFileToDisk(String fileName, ResponseBody responseBody) {
    File file = new File(
      Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
      fileName);
    try {
      InputStream in = responseBody.byteStream();
      OutputStream out = new FileOutputStream(file);
      IOUtils.copy(in, out);
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
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

  public int getREQUEST_CODE() {
    return REQUEST_CODE;
  }
}
