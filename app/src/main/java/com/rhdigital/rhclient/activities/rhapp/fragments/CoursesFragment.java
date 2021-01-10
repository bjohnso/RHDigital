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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.rhapp.adapters.CoursesRecyclerViewAdapter;
import com.rhdigital.rhclient.activities.rhapp.adapters.ProgramsRecyclerViewAdapter;
import com.rhdigital.rhclient.activities.rhapp.viewmodel.CoursesViewModel;
import com.rhdigital.rhclient.common.dto.VideoControlActionDto;
import com.rhdigital.rhclient.common.interfaces.OnClickCallback;
import com.rhdigital.rhclient.common.services.NavigationService;
import com.rhdigital.rhclient.common.services.VideoPlayerService;
import com.rhdigital.rhclient.database.model.Course;
import com.rhdigital.rhclient.database.model.Program;
import com.rhdigital.rhclient.databinding.FragmentCoursesBinding;

import java.util.HashMap;
import java.util.List;

public class CoursesFragment extends Fragment {

    private final String TAG = "COURSES_FRAGMENT";

    // VIEW
    private FragmentCoursesBinding binding;

    // VIEW MODEL
    private CoursesViewModel coursesViewModel;

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

        coursesViewModel = new CoursesViewModel(getActivity().getApplication());
        coursesViewModel.init(getArguments().getString("programId"))
                .observe(getViewLifecycleOwner(), complete -> {
                    if (complete) {
                        initialiseUI();
                        initialiseLiveData();
                    }
                });
        binding.setViewModel(coursesViewModel);
        return binding.getRoot();
    }

    private void initialiseUI() {
        calculateImageDimensions();
        initialiseRecyclerView();
    }

    private void initialiseLiveData() {
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
                    onUpdateCourses(courses);
                } else {
                    Toast.makeText(getContext(), R.string.server_error_courses, Toast.LENGTH_LONG).show();
                }
            }
        };

        coursesObservable = coursesViewModel.getCourses();
        coursesObservable.observe(getViewLifecycleOwner(), coursesObserver);
    }

    private void initialiseRecyclerView() {
        OnClickCallback callback = (action) -> {
            VideoControlActionDto videoControlAction = (VideoControlActionDto)action;
            switch (videoControlAction.getActionType()) {
                case VideoControlActionDto.MAXIMISE:
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("videoData", videoControlAction.getVideo());
                        NavigationService.getINSTANCE()
                                .navigate(
                                        getActivity().getLocalClassName(),
                                        R.id.videosFragment, bundle, null
                                );
            }
        };
        coursesRecyclerViewAdapter = new CoursesRecyclerViewAdapter(coursesViewModel.program.getValue(), callback);
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

    private void onUpdateCourses(List<Course> courses) {
        coursesRecyclerViewAdapter.setCourses(courses);
        if (coursesPosterObservable != null) {
            coursesPosterObservable.removeObservers(this);
        }
        coursesPosterObservable = coursesViewModel.getAllCoursePosters(getContext(), courses, width, height);
        coursesPosterObservable.observe(getActivity(), coursesPosterObserver);
    }

    private void onUpdateProgramPosters(HashMap<String, Bitmap> posterMap) {
        coursesRecyclerViewAdapter.setPosterUriMap(posterMap);
    }

    @Override
    public void onDestroyView() {
        this.coursesRecyclerViewAdapter = null;
        VideoPlayerService.getInstance().destroyAllVideoStreams(false);
        super.onDestroyView();
    }
}
