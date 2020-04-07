package com.example.rhdigital.database.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.rhdigital.database.model.Course;
import com.example.rhdigital.database.repository.RHRepository;

import java.util.ArrayList;
import java.util.List;

public class CourseViewModel extends AndroidViewModel {
    private RHRepository rhRepository;

    public CourseViewModel(@NonNull Application application) {
        super(application);
        rhRepository = new RHRepository(application);
    }

    public LiveData<List<Course>> getAllCourses() {
        return rhRepository.getAllCourses();
    }

    public void insert(Course course) {
        rhRepository.insert(course);
    }
}
