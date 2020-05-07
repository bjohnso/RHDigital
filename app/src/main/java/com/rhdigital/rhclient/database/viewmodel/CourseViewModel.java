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
import com.rhdigital.rhclient.util.RemoteImageConnector;

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

    public LiveData<HashMap<String, Bitmap>> getAllUri(Context context, List<Course> courses, int width, int height) { return RemoteImageConnector.getInstance().getAllURI(context, courses, width, height); }

    public long insert(Course course) {
        return rhRepository.insert(course);
    }
}
