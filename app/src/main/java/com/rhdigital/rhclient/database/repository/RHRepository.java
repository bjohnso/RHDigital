package com.rhdigital.rhclient.database.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.rhdigital.rhclient.database.DAO.BaseDAO;
import com.rhdigital.rhclient.database.DAO.CourseDAO;
import com.rhdigital.rhclient.database.DAO.embedded.CourseWithWorkbooksDAO;
import com.rhdigital.rhclient.database.DAO.PackageDAO;
import com.rhdigital.rhclient.database.DAO.UserDAO;
import com.rhdigital.rhclient.database.DAO.WorkbookDAO;
import com.rhdigital.rhclient.database.RHDatabase;
import com.rhdigital.rhclient.database.model.Course;
import com.rhdigital.rhclient.database.model.embedded.CourseWithWorkbooks;
import com.rhdigital.rhclient.database.model.Package;
import com.rhdigital.rhclient.database.model.User;
import com.rhdigital.rhclient.database.model.Workbook;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RHRepository {
    private PackageDAO packageDAO;
    private CourseDAO courseDAO;
    private WorkbookDAO workbookDAO;
    private UserDAO userDAO;
    private CourseWithWorkbooksDAO courseWithWorkbooksDAO;
    private ExecutorService executorService;

    public RHRepository(Application application) {
        RHDatabase db = RHDatabase.getDatabase(application);
        packageDAO = db.packageDAO();
        courseDAO = db.courseDAO();
        workbookDAO = db.workbookDAO();
        userDAO = db.userDAO();
        courseWithWorkbooksDAO = db.courseWithWorkbooksDAO();
        executorService = Executors.newCachedThreadPool();
    }

    // USER
    public LiveData<List<User>> getAllUsers() { return userDAO.getAllUsers(); };

    public LiveData<User> getAuthenticatedUser(String id) {return userDAO.getAuthenticatedUser(id);}

    public void deleteUser(User user) {
      try {
        executorService.submit(new DeleteService(userDAO, user));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    // PACKAGES
    public LiveData<List<Package>> getAllPackages() { return packageDAO.getAllPackages(); }

    public LiveData<List<Package>> getAllAuthorisedPackages() {return packageDAO.getAllAuthorisedPackages(); }

    public LiveData<List<Package>> getAllUndiscoveredPackages() { return packageDAO.getAllUndiscoveredPackages(); }

    // COURSES
    public LiveData<List<Course>> getAllCourses() {
        return courseDAO.getAllCourses();
    }

    public LiveData<List<Course>> getAllAuthorisedCourses() {return courseDAO.getAllAuthorisedCourses(); }

    public LiveData<List<Course>> getAllUndiscoveredCourses() { return courseDAO.getAllUndiscoveredCourses(); }

    // WORKBOOKS
    public LiveData<List<Workbook>> getAllWorkbooks() { return workbookDAO.getAllWorkbooks(); }

    public LiveData<List<Workbook>> getWorkbooksById(@NonNull int courseId) { return workbookDAO.getWorkbooksByCourseId(courseId); }


    // COURSES WITH WORKBOOKS
    public LiveData<List<CourseWithWorkbooks>> getAllCoursesWithWorkbooks() { return courseWithWorkbooksDAO.getAllCoursesWithWorkbooks(); }

    public LiveData<List<CourseWithWorkbooks>> getAllAuthorisedCoursesWithWorkbooks() { return courseWithWorkbooksDAO.getAllAuthorisedCoursesWithWorkbooks(); }


    public void authoriseCourse(String id) {
      try {
        executorService.submit(new AuthCourseService(courseDAO, id, true)).get();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    public void unauthoriseAllCourses() {
      try {
        executorService.submit(new AuthCourseService(courseDAO, null, false)).get();
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

  private class DeleteService implements Callable<Void> {
    private BaseDAO dao;
    private Object obj;

    public DeleteService(BaseDAO dao, Object obj) {
      this.dao = dao;
      this.obj = obj;
    }

    @Override
    public Void call() throws Exception {
      this.dao.delete(obj);
      return null;
    }
  }

  private class AuthCourseService implements Callable<Integer> {
    private CourseDAO courseDAO;
    private String id;
    private boolean authorise;
    public AuthCourseService(CourseDAO courseDAO, String id, boolean authorise) {
      this.courseDAO = courseDAO;
      this.id = id;
      this.authorise = authorise;
    }

    @Override
    public Integer call() throws Exception {
      if (authorise)
        return this.courseDAO.authorise(id);
      this.courseDAO.deauthorise();
      return -1;
    }
  }
}
