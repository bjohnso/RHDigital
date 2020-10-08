package com.rhdigital.rhclient.activities.rhapp.fragments;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.media.tv.TvContract;
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
import com.rhdigital.rhclient.activities.rhapp.RHAppActivity;
import com.rhdigital.rhclient.activities.rhapp.adapters.ProgramsRecyclerViewAdapter;
import com.rhdigital.rhclient.database.model.Program;
import com.rhdigital.rhclient.viewholder.ProgramViewModel;

import java.util.HashMap;
import java.util.List;

public class ProgramsFragment extends Fragment {

    private final String TAG = "PROGRAMS_FRAGMENT";

    // VIEW MODEL
    private ProgramViewModel programsViewModel;

    // ADAPTERS
    private ProgramsRecyclerViewAdapter programsRecyclerViewAdapter;

    private List<Program> programs;

    // OBSERVABLES
    private LiveData<HashMap<String, Bitmap>> programPosterObservable;
    private LiveData<List<Program>> programObservable;

    // OBSERVERS
    private Observer<HashMap<String, Bitmap>> programPosterObserver;
    private Observer<List<Program>> programObserver;

    // COMPONENTS
    private RecyclerView recyclerView;

    private int width = 0;
    private int height = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.fragment_programs, container, false);

      Log.d(TAG, "INIT");

      // CALCULATE VIEW DIMENSIONS
      calculateImageDimensions();

      // TODO: IDENTIFY SELF IN THE FRAGMENT MANAGER

      // IDENTIFY SELF IN THE PARENT ACTIVITY
      ((RHAppActivity)this.getActivity()).setToolbarTitle("Programs");

      // TODO: INITIALISE COMPONENTS

      // INITIALISE LIVE DATA
      initialiseLiveData();

      // INITIALISE RECYCLERVIEW
      initialiseRecyclerView(view);

      return view;
    }

    private void initialiseLiveData() {
      programsViewModel = new ProgramViewModel(getActivity().getApplication());
      programsRecyclerViewAdapter = new ProgramsRecyclerViewAdapter(getContext());
      programPosterObserver = new Observer<HashMap<String, Bitmap>>() {
        @Override
        public void onChanged(HashMap<String, Bitmap> posterMap) {
          if (posterMap != null) {
            if (programs.size() == posterMap.size()) {
              programPosterObservable.removeObserver(this);
              onUpdateProgramPosters(posterMap);
            }
          } else {
            Toast.makeText(getContext(), R.string.server_error_courses, Toast.LENGTH_LONG);
          }
        }
      };

      programObserver = new Observer<List<Program>>() {
        @Override
        public void onChanged(List<Program> programs) {
          if (programs != null) {
            programObservable.removeObserver(this);
            onUpdatePrograms(programs);
          } else {
            Toast.makeText(getContext(), R.string.server_error_courses, Toast.LENGTH_LONG);
          }
        }
      };

      programObservable = programsViewModel.getAllUndiscoveredPrograms();
      programObservable.observe(getViewLifecycleOwner(), programObserver);
    }

    private void initialiseRecyclerView(View view) {
      recyclerView = view.findViewById(R.id.programs_recycler);
      recyclerView.setHasFixedSize(true);
      recyclerView.setItemViewCacheSize(10);
      recyclerView.setDrawingCacheEnabled(true);
      recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
      recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
      recyclerView.setAdapter(programsRecyclerViewAdapter);
    }

    private void calculateImageDimensions() {
      float dip = 220f;
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

    private void onUpdatePrograms(List<Program> programs) {
      programsRecyclerViewAdapter.setPrograms(programs);
      this.programs = programs;
      if (programPosterObservable != null) {
        programPosterObservable.removeObservers(this);
      }
      programPosterObservable = programsViewModel.getAllProgramPosters(getContext(), programs, width, height);
      programPosterObservable.observe(getActivity(), programPosterObserver);
    }

    private void onUpdateProgramPosters(HashMap<String, Bitmap> posterMap) {
      programsRecyclerViewAdapter.setPosterUriMap(posterMap);
    }
}




