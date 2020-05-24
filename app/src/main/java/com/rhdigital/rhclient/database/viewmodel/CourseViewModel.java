package com.rhdigital.rhclient.database.viewmodel;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.rhdigital.rhclient.database.model.Course;
import com.rhdigital.rhclient.database.repository.RHRepository;
import com.rhdigital.rhclient.common.services.RemoteResourceService;

import java.util.HashMap;
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

    public LiveData<List<Course>> getAllAuthorisedCourses() {return rhRepository.getAllAuthorisedCourses(); }

    public LiveData<List<Course>> getAllUndiscoveredCourses() {return rhRepository.getAllUndiscoveredCourses(); }

    public LiveData<HashMap<String, Uri>> getAllVideoUri(List<Course> courses, int width, int height) { return new RemoteResourceService().getAllVideoURI(courses, width, height); }

    public LiveData<HashMap<String, Bitmap>> getAllBitmap(Context context, List<Course> courses, int width, int height) { return new RemoteResourceService().getAllBitmap(context, courses, width, height); }

    public void insert(Course course) {
        rhRepository.insert(course);
    }
}
