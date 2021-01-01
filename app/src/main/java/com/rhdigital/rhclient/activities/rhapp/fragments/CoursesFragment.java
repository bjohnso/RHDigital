package com.rhdigital.rhclient.activities.rhapp.fragments;

import android.content.res.Resources;
import android.graphics.Bitmap;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.rhapp.adapters.CoursesRecyclerViewAdapter;
import com.rhdigital.rhclient.activities.rhapp.viewmodel.CoursesViewModel;
import com.rhdigital.rhclient.activities.rhapp.viewmodel.RHAppViewModel;
import com.rhdigital.rhclient.database.model.Course;
import com.rhdigital.rhclient.databinding.FragmentCoursesBinding;

import java.util.HashMap;
import java.util.List;

public class CoursesFragment extends Fragment {

    private final String TAG = "COURSES_FRAGMENT";

    // VIEW
    private FragmentCoursesBinding binding;

    // VIEW MODEL
    private CoursesViewModel coursesViewModel;
    private RHAppViewModel rhAppViewModel;

    // ADAPTERS
    private CoursesRecyclerViewAdapter coursesRecyclerViewAdapter;

    // OBSERVABLES
    private LiveData<HashMap<String, Bitmap>> coursesPosterObservable;
    private LiveData<List<Course>> coursesObservable;

    // OBSERVERS
    private Observer<HashMap<String, Bitmap>> coursesPosterObserver;
    private Observer<List<Course>> coursesObserver;

    private int width = 0;
    private int height = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCoursesBinding.inflate(getLayoutInflater());

        rhAppViewModel = new ViewModelProvider(getActivity()).get(RHAppViewModel.class);
        coursesViewModel = new CoursesViewModel(getActivity().getApplication(), "");
        coursesViewModel.configureRHAppViewModel();
        binding.setViewModel(coursesViewModel);

        initialiseLiveData();
        initialiseUI();

        return binding.getRoot();
    }

    private void initialiseUI() {
        calculateImageDimensions();
        initialiseRecyclerView();
    }

    private void initialiseLiveData() {
//        coursesRecyclerViewAdapter = new CoursesRecyclerViewAdapter(getContext());
        coursesPosterObserver = new Observer<HashMap<String, Bitmap>>() {
            @Override
            public void onChanged(HashMap<String, Bitmap> posterMap) {
                if (posterMap != null) {
                    if (coursesObservable.getValue().size() == posterMap.size()) {
                        coursesPosterObservable.removeObserver(this);
                        onUpdateProgramPosters(posterMap);
                    }
                } else {
                    Toast.makeText(getContext(), R.string.server_error_courses, Toast.LENGTH_LONG).show();
                }
            }
        };

        coursesObserver = new Observer<List<Course>>() {
            @Override
            public void onChanged(List<Course> courses) {
                if (courses != null) {
                    coursesObservable.removeObserver(this);
                    onUpdatePrograms(courses);
                } else {
                    Toast.makeText(getContext(), R.string.server_error_courses, Toast.LENGTH_LONG).show();
                }
            }
        };

//        coursesObservable = coursesViewModel.getAllUndiscoveredPrograms();
        coursesObservable.observe(getViewLifecycleOwner(), coursesObserver);
    }

    private void initialiseRecyclerView() {
        binding.coursesRecycler.setHasFixedSize(true);
        binding.coursesRecycler.setItemViewCacheSize(10);
        binding.coursesRecycler.setDrawingCacheEnabled(true);
        binding.coursesRecycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
        binding.coursesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.coursesRecycler.setAdapter(coursesRecyclerViewAdapter);
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

    private void onUpdatePrograms(List<Course> courses) {
//        coursesRecyclerViewAdapter.setPrograms(programs);
        if (coursesPosterObservable != null) {
            coursesPosterObservable.removeObservers(this);
        }
//        coursesPosterObservable = coursesViewModel.getAllProgramPosters(getContext(), courses, width, height);
        coursesPosterObservable.observe(getActivity(), coursesPosterObserver);
    }

    private void onUpdateProgramPosters(HashMap<String, Bitmap> posterMap) {
//        coursesRecyclerViewAdapter.setPosterUriMap(posterMap);
    }
}
