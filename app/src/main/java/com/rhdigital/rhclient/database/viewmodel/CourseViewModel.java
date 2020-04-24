package com.rhdigital.rhclient.database.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.rhdigital.rhclient.database.model.Course;
import com.rhdigital.rhclient.database.repository.RHRepository;

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

    public long insert(Course course) {
        return rhRepository.insert(course);
    }
}
