package com.example.rhdigital.activities.courses.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rhdigital.R;
import com.example.rhdigital.database.model.Course;
import com.example.rhdigital.database.viewmodel.CourseViewModel;
import com.example.rhdigital.ui.adapters.CoursesRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class DiscoverCoursesFragment extends Fragment {

    //View Model
    private CourseViewModel courseViewModel;

    //Adapters
    private CoursesRecyclerViewAdapter coursesRecyclerViewAdapter;

    //Components
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.discover_courses_layout, container, false);
        recyclerView = view.findViewById(R.id.discover_courses_recycler);

        //Initialise View Model
        courseViewModel = new CourseViewModel(getActivity().getApplication());

        // Initialise Adapter
        coursesRecyclerViewAdapter = new CoursesRecyclerViewAdapter();

        // Create an Observer
        final Observer<List<Course>> courseObserver = courses -> coursesRecyclerViewAdapter.setCourses(courses);

        // Initialise RecyclerView
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(coursesRecyclerViewAdapter);

        // Call Observer
        courseViewModel.getAllCourses().observe(getActivity(), courseObserver);
        return view;
    }

}

