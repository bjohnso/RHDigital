package com.rhdigital.rhclient.activities.rhapp.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.rhdigital.rhclient.RHApplication;
import com.rhdigital.rhclient.database.model.Course;
import com.rhdigital.rhclient.database.model.CourseDescription;
import com.rhdigital.rhclient.database.model.Workbook;
import com.rhdigital.rhclient.database.repository.RHRepository;
import java.util.List;

public class CourseItemViewModel extends AndroidViewModel {

    public LiveData<Course> course;
    public LiveData<List<Workbook>> workbooks;
    public RHRepository rhRepository;

    public CourseItemViewModel(@NonNull Application application) {
        super(application);
        rhRepository = new RHRepository(application);
    }

    public LiveData<Boolean> init(String courseId) {
        MutableLiveData complete = new MutableLiveData(false);
        LifecycleOwner lifecycleOwner = (LifecycleOwner) ((RHApplication)getApplication()).getCurrentActivity();
        course = rhRepository.getCourse(courseId);
        course.observe(lifecycleOwner, course -> {
            complete.setValue(true);
        });
        return complete;
    }

    public LiveData<List<Workbook>> getWorkbooks() {
        if (course != null) {
            String courseId = course.getValue().getId();
            return rhRepository.getAllWorkbooksByCourseId(courseId);
        }
        return null;
    }

    public LiveData<List<CourseDescription>> getCourseDescriptions() {
        if (course != null) {
            String courseId = course.getValue().getId();
            return rhRepository.getAllCourseDescriptionsByCourseId(courseId);
        }
        return null;
    }
}
