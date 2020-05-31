package com.rhdigital.rhclient.database.viewmodel;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.rhdigital.rhclient.common.services.RemoteResourceService;
import com.rhdigital.rhclient.database.model.Course;
import com.rhdigital.rhclient.database.model.CourseWithWorkbooks;
import com.rhdigital.rhclient.database.model.Workbook;
import com.rhdigital.rhclient.database.repository.RHRepository;

import java.util.HashMap;
import java.util.List;

public class WorkbookViewModel extends AndroidViewModel {
    private RHRepository rhRepository;

    public WorkbookViewModel(@NonNull Application application) {
        super(application);
        rhRepository = new RHRepository(application);
    }

    public LiveData<List<Workbook>> getAllWorkbooks() {
        return rhRepository.getAllWorkbooks();
    }

    public LiveData<List<CourseWithWorkbooks>> getAllCoursesWithWorkbooks() { return rhRepository.getAllCoursesWithWorkbooks(); }

    public LiveData<List<Workbook>> getWorkbooksById(@NonNull int courseId) {
        return rhRepository.getWorkbooksById(courseId);
    }

    public LiveData<HashMap<String, Bitmap>> getAllWorkbookPosters(Context context, List<CourseWithWorkbooks> workbooks, int width, int height) { return new RemoteResourceService().getAllBitmap(context, workbooks, width, height, false); }
}
