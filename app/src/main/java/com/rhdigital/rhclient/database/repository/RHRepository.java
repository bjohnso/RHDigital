package com.rhdigital.rhclient.database.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.rhdigital.rhclient.database.DAO.BaseDAO;
import com.rhdigital.rhclient.database.DAO.CourseDAO;
import com.rhdigital.rhclient.database.DAO.CourseWithWorkbooksDAO;
import com.rhdigital.rhclient.database.DAO.WorkbookDAO;
import com.rhdigital.rhclient.database.RHDatabase;
import com.rhdigital.rhclient.database.model.Course;
import com.rhdigital.rhclient.database.model.CourseWithWorkbooks;
import com.rhdigital.rhclient.database.model.Workbook;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RHRepository {
    private CourseDAO courseDAO;
    private WorkbookDAO workbookDAO;
    private CourseWithWorkbooksDAO courseWithWorkbooksDAO;
    private ExecutorService executorService;

    public RHRepository(Application application) {
        RHDatabase db = RHDatabase.getDatabase(application);
        courseDAO = db.courseDAO();
        workbookDAO = db.workbookDAO();
        courseWithWorkbooksDAO = db.courseWithWorkbooksDAO();
        executorService = Executors.newCachedThreadPool();
    }

    public LiveData<List<Course>> getAllCourses() {
        return courseDAO.getAllCourses();
    }

    public LiveData<List<Workbook>> getAllWorkbooks() { return workbookDAO.getAllWorkbooks(); }

    public LiveData<List<CourseWithWorkbooks>> getAllCoursesWithWorkbooks() { return courseWithWorkbooksDAO.getAllCoursesWithWorkbooks(); }

    public LiveData<List<Workbook>> getWorkbooksById(@NonNull int courseId) { return workbookDAO.getWorkbooksByCourseId(courseId); }

    public long insert(Course course) {
        try {
            return executorService.submit(new insertService(courseDAO, course)).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    private class insertService implements Callable<Long> {
        private BaseDAO dao;
        private Object obj;
        public insertService(CourseDAO dao, Object obj) {
            this.dao = dao;
            this.obj = obj;
        }

        @Override
        public Long call() throws Exception {
            return this.dao.insert(this.obj);
        }
    }
}
