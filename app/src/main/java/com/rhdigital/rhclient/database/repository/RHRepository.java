package com.rhdigital.rhclient.database.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.rhdigital.rhclient.database.DAO.BaseDAO;
import com.rhdigital.rhclient.database.DAO.CourseDAO;
import com.rhdigital.rhclient.database.DAO.CourseWithWorkbooksDAO;
import com.rhdigital.rhclient.database.DAO.UserDAO;
import com.rhdigital.rhclient.database.DAO.WorkbookDAO;
import com.rhdigital.rhclient.database.RHDatabase;
import com.rhdigital.rhclient.database.model.Course;
import com.rhdigital.rhclient.database.model.CourseWithWorkbooks;
import com.rhdigital.rhclient.database.model.User;
import com.rhdigital.rhclient.database.model.Workbook;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RHRepository {
    private CourseDAO courseDAO;
    private WorkbookDAO workbookDAO;
    private UserDAO userDAO;
    private CourseWithWorkbooksDAO courseWithWorkbooksDAO;
    private ExecutorService executorService;

    public RHRepository(Application application) {
        RHDatabase db = RHDatabase.getDatabase(application);
        courseDAO = db.courseDAO();
        workbookDAO = db.workbookDAO();
        userDAO = db.userDAO();
        courseWithWorkbooksDAO = db.courseWithWorkbooksDAO();
        executorService = Executors.newCachedThreadPool();
    }

    public LiveData<List<User>> getAllUsers() { return userDAO.getAllUsers(); };

    public LiveData<List<Course>> getAllCourses() {
        return courseDAO.getAllCourses();
    }

    public LiveData<List<Course>> getAllAuthorisedCourses() {return courseDAO.getAllAuthorisedCourses(); };

    public LiveData<List<Workbook>> getAllWorkbooks() { return workbookDAO.getAllWorkbooks(); }

    public LiveData<List<CourseWithWorkbooks>> getAllCoursesWithWorkbooks() { return courseWithWorkbooksDAO.getAllCoursesWithWorkbooks(); }

    public LiveData<List<Workbook>> getWorkbooksById(@NonNull int courseId) { return workbookDAO.getWorkbooksByCourseId(courseId); }

    public void authoriseCourse(String id) {
      courseDAO.authorise(id);
    }

    public long insert(User user) {
      try {
        return executorService.submit(new InsertService(userDAO, user)).get();
      } catch (Exception e) {
        e.printStackTrace();
      }
      return -1;
    }

    public long insert(Course course) {
        try {
            return executorService.submit(new InsertService(courseDAO, course)).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    private class InsertService implements Callable<Long> {
        private BaseDAO dao;
        private Object obj;
        public InsertService(BaseDAO dao, Object obj) {
            this.dao = dao;
            this.obj = obj;
        }

        @Override
        public Long call() throws Exception {
            return this.dao.insert(this.obj);
        }
    }
}
