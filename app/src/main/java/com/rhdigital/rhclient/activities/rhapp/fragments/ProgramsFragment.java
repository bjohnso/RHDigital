package com.rhdigital.rhclient.activities.rhapp.fragments;

import android.content.res.Resources;
import android.graphics.Bitmap;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.tabs.TabLayout;
import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.rhapp.RHAppActivity;
import com.rhdigital.rhclient.activities.rhapp.adapters.ProgramsRecyclerViewAdapter;
import com.rhdigital.rhclient.activities.rhapp.viewmodel.RHAppViewModel;
import com.rhdigital.rhclient.common.interfaces.OnClickCallback;
import com.rhdigital.rhclient.common.services.NavigationService;
import com.rhdigital.rhclient.database.model.Program;
import com.rhdigital.rhclient.databinding.FragmentProgramsBinding;
import com.rhdigital.rhclient.activities.rhapp.viewmodel.ProgramsViewModel;

import java.util.HashMap;
import java.util.List;

public class ProgramsFragment extends Fragment {

    private final String TAG = "PROGRAMS_FRAGMENT";

    // VIEW
    private FragmentProgramsBinding binding;

    // VIEW MODEL
    private ProgramsViewModel programsViewModel;

    // ADAPTERS
    private ProgramsRecyclerViewAdapter programsRecyclerViewAdapter;

    private List<Program> programs;

    // OBSERVABLES
    private LiveData<HashMap<String, Bitmap>> programsPosterObservable;
    private LiveData<List<Program>> programsObservable;

    // OBSERVERS
    private Observer<HashMap<String, Bitmap>> programsPosterObserver;
    private Observer<List<Program>> programsObserver;

    private int width = 0;
    private int height = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      binding = FragmentProgramsBinding.inflate(getLayoutInflater());

      programsViewModel = new ProgramsViewModel(getActivity().getApplication());
      programsViewModel.init();
      binding.setViewModel(programsViewModel);

      initialiseUI();

      return binding.getRoot();
    }

    private void initialiseUI() {
        calculateImageDimensions();
        initialiseTabLayout();
        initialiseRecyclerView();
    }

    private void initialiseLiveData() {
        programsPosterObserver = new Observer<HashMap<String, Bitmap>>() {
            @Override
            public void onChanged(HashMap<String, Bitmap> posterMap) {
              if (posterMap != null) {
                if (programs.size() == posterMap.size()) {
                  programsPosterObservable.removeObserver(this);
                  onUpdateProgramPosters(posterMap);
                }
              } else {
                Toast.makeText(getContext(), R.string.server_error_courses, Toast.LENGTH_LONG).show();
              }
            }
        };

          programsObserver = new Observer<List<Program>>() {
            @Override
            public void onChanged(List<Program> programs) {
              if (programs != null) {
                programsObservable.removeObserver(this);
                onUpdatePrograms(programs);
              } else {
                Toast.makeText(getContext(), R.string.server_error_courses, Toast.LENGTH_LONG).show();
              }
            }
          };

          programsObservable = programsViewModel.getPrograms();
          programsObservable.observe(getViewLifecycleOwner(), programsObserver);
    }

    private void initialiseTabLayout() {
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    programsViewModel.isEnrolledState.setValue(false);
                } else if (tab.getPosition() == 1) {
                    programsViewModel.isEnrolledState.setValue(true);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        programsViewModel.isEnrolledState.observe(getViewLifecycleOwner(), isEnrolledState -> {
            initialiseLiveData();
        });
    }

    private void initialiseRecyclerView() {
        OnClickCallback callback = (program) -> {
            Bundle bundle = new Bundle();
            bundle.putString("programId", ((Program)program).getId());
            NavigationService.getINSTANCE()
                    .navigate(
                            getActivity().getLocalClassName(),
                            R.id.coursesFragment, bundle, null
                    );
        };
        programsRecyclerViewAdapter = new ProgramsRecyclerViewAdapter(callback);
        binding.programsRecycler.setHasFixedSize(true);
        binding.programsRecycler.setItemViewCacheSize(10);
        binding.programsRecycler.setDrawingCacheEnabled(true);
        binding.programsRecycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
        binding.programsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.programsRecycler.setAdapter(programsRecyclerViewAdapter);
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
      if (programsPosterObservable != null) {
        programsPosterObservable.removeObservers(this);
      }
      programsPosterObservable = programsViewModel.getAllProgramPosters(getContext(), programs, width, height);
      programsPosterObservable.observe(getActivity(), programsPosterObserver);
    }

    private void onUpdateProgramPosters(HashMap<String, Bitmap> posterMap) {
      programsRecyclerViewAdapter.setPosterUriMap(posterMap);
    }
}




