package com.rhdigital.rhclient.activities.rhapp.viewmodel;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import com.rhdigital.rhclient.RHApplication;
import com.rhdigital.rhclient.common.dto.RemoteResourceDto;
import com.rhdigital.rhclient.common.services.RemoteResourceService;
import com.rhdigital.rhclient.room.model.Course;
import com.rhdigital.rhclient.room.model.Program;
import com.rhdigital.rhclient.room.repository.RHRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CoursesViewModel extends AndroidViewModel {

    public LiveData<Program> program;
    public LiveData<List<Course>> courses;
    public LiveData<HashMap<String, Bitmap>> coursePosters;
    public RHRepository rhRepository;

    public CoursesViewModel(@NonNull Application application) {
        super(application);
        rhRepository = new RHRepository(application);
    }

    public LiveData<Boolean> init(String programId) {
        MutableLiveData complete = new MutableLiveData(false);
        LifecycleOwner lifecycleOwner = (LifecycleOwner) ((RHApplication)getApplication()).getCurrentActivity();
        program = rhRepository.getProgram(programId);
        program.observe(lifecycleOwner, program -> {
            ViewModelStoreOwner viewModelStoreOwner =
                    (ViewModelStoreOwner) ((RHApplication)getApplication()).getCurrentActivity();
            RHAppViewModel rhAppViewModel = new ViewModelProvider(viewModelStoreOwner).get(RHAppViewModel.class);
            rhAppViewModel.isEnrollState.setValue(!program.isAuthorised());
            rhAppViewModel.isActionButtonActive.setValue(false);
            rhAppViewModel.isBackButtonActive.setValue(true);
            rhAppViewModel.isTitleCenter.setValue(true);
            rhAppViewModel.title.setValue(program.getTitle());
            complete.setValue(true);
        });
        return complete;
    }

    public LiveData<List<Course>> getCourses() {
        if (program != null) {
            String programId = program.getValue().getId();
            return rhRepository.getAllCoursesByProgramId(programId);
        }
        return null;
    }

    public LiveData<HashMap<String, Bitmap>> getAllCoursePosters(Context context, List<Course> courses, int width, int height) {
        List<RemoteResourceDto> data = new ArrayList<>();
        for (Course course: courses) {
            // TODO: USE VIDEO THUMBNAILS
            data.add(
                    new RemoteResourceDto(course.getId(), program.getValue().getPosterUrl())
            );
        }
        return new RemoteResourceService().getAllBitmap(context, data, width, height);
    }
}
