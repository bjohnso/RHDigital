package com.rhdigital.rhclient.database.repository;

import android.animation.ObjectAnimator;
import android.app.Application;
import android.content.Intent;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

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

import java.lang.reflect.Array;
import java.util.ArrayList;
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

    public LiveData<User> getAuthenticatedUser(String id) {return userDAO.getAuthenticatedUser(id);}

    public LiveData<List<Course>> getAllCourses() {
        return courseDAO.getAllCourses();
    }

    public LiveData<List<Course>> getAllAuthorisedCourses() {return courseDAO.getAllAuthorisedCourses(); }

    public LiveData<List<Course>> getAllUndiscoveredCourses() { return courseDAO.getAllUndiscoveredCourses(); }

    public LiveData<List<Workbook>> getAllWorkbooks() { return workbookDAO.getAllWorkbooks(); }

    public LiveData<List<CourseWithWorkbooks>> getAllCoursesWithWorkbooks() { return courseWithWorkbooksDAO.getAllCoursesWithWorkbooks(); }

    public LiveData<List<CourseWithWorkbooks>> getAllAuthorisedCoursesWithWorkbooks() { return courseWithWorkbooksDAO.getAllAuthorisedCoursesWithWorkbooks(); }

    public LiveData<List<Workbook>> getWorkbooksById(@NonNull int courseId) { return workbookDAO.getWorkbooksByCourseId(courseId); }

    public void authoriseCourse(String id) {
      try {
        executorService.submit(new AuthCourseService(courseDAO, id)).get();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    public void update(Object o) {
      try {
        if (o.getClass().getSimpleName().equalsIgnoreCase(User.class.getSimpleName())) {
          executorService.submit(new UpdateService(userDAO, o));
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    public Long insert(Object o) {
        try {
          if (o.getClass().getSimpleName().equalsIgnoreCase(Course.class.getSimpleName())) {
            return executorService.submit(new InsertService(courseDAO, o)).get();
          } else if (o.getClass().getSimpleName().equalsIgnoreCase(User.class.getSimpleName())) {
            return executorService.submit(new InsertService(userDAO, o)).get();
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
        return null;
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
          return this.dao.insert(obj);
        }
    }

    private class UpdateService implements Callable<Void> {
      private BaseDAO dao;
      private Object obj;

      public UpdateService(BaseDAO dao, Object obj) {
        this.dao = dao;
        this.obj = obj;
      }

      @Override
      public Void call() throws Exception {
        this.dao.update(obj);
        return null;
      }
    }

  private class AuthCourseService implements Callable<Integer> {
    private CourseDAO courseDAO;
    private String id;
    public AuthCourseService(CourseDAO courseDAO, String id) {
      this.courseDAO = courseDAO;
      this.id = id;
    }

    @Override
    public Integer call() throws Exception {
      return this.courseDAO.authorise(id);
    }
  }
}
