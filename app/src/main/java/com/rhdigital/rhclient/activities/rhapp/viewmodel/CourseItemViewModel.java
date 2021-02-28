package com.rhdigital.rhclient.activities.rhapp.viewmodel;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.rhdigital.rhclient.RHApplication;
import com.rhdigital.rhclient.common.dto.RemoteResourceDto;
import com.rhdigital.rhclient.common.services.RemoteResourceService;
import com.rhdigital.rhclient.room.model.Course;
import com.rhdigital.rhclient.room.model.CourseDescription;
import com.rhdigital.rhclient.room.model.Video;
import com.rhdigital.rhclient.room.model.Workbook;
import com.rhdigital.rhclient.room.repository.RHRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CourseItemViewModel extends AndroidViewModel {

    public LiveData<Course> course;
    public List<Workbook> workbooks;
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

    public LiveData<HashMap<String, Uri>> getWorkBookUriMap() {
        ArrayList<RemoteResourceDto> data = new ArrayList();
        if (workbooks != null) {
            for (Workbook workbook: workbooks) {
                data.add(
                        new RemoteResourceDto(
                                workbook.getId(),
                                workbook.getUrl(),
                                RemoteResourceDto.WORKBOOK_URI
                        )
                );
            }
        }
        return new RemoteResourceService().getAllPDFURI(data);
    }

    public LiveData<List<CourseDescription>> getCourseDescriptions() {
        if (course != null) {
            String courseId = course.getValue().getId();
            return rhRepository.getAllCourseDescriptionsByCourseId(courseId);
        }
        return null;
    }

    public LiveData<Video> getVideo(String courseId) {
        return rhRepository.getVideoByCourseId(courseId);
    }
}
