package com.rhdigital.rhclient.database.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.rhdigital.rhclient.database.DAO.AuthorisedProgramDAO;
import com.rhdigital.rhclient.database.DAO.BaseDAO;
import com.rhdigital.rhclient.database.DAO.CourseDAO;
import com.rhdigital.rhclient.database.DAO.CourseDescriptionDAO;
import com.rhdigital.rhclient.database.DAO.ProgramDAO;
import com.rhdigital.rhclient.database.DAO.ReportDAO;
import com.rhdigital.rhclient.database.DAO.VideoDAO;
import com.rhdigital.rhclient.database.DAO.embedded.CourseWithWorkbooksDAO;
import com.rhdigital.rhclient.database.DAO.UserDAO;
import com.rhdigital.rhclient.database.DAO.WorkbookDAO;
import com.rhdigital.rhclient.database.RHDatabase;
import com.rhdigital.rhclient.database.model.AuthorisedProgram;
import com.rhdigital.rhclient.database.model.Course;
import com.rhdigital.rhclient.database.model.CourseDescription;
import com.rhdigital.rhclient.database.model.Report;
import com.rhdigital.rhclient.database.model.Video;
import com.rhdigital.rhclient.database.model.Program;
import com.rhdigital.rhclient.database.model.User;
import com.rhdigital.rhclient.database.model.Workbook;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RHRepository {
    protected CourseDAO courseDAO;
    protected CourseDescriptionDAO courseDescriptionDAO;
    protected AuthorisedProgramDAO authorisedProgramDAO;
    protected ProgramDAO programDAO;
    protected ReportDAO reportDAO;
    protected UserDAO userDAO;
    protected VideoDAO videoDAO;
    protected WorkbookDAO workbookDAO;
    protected CourseWithWorkbooksDAO courseWithWorkbooksDAO;
    protected ExecutorService executorService;

    public RHRepository(Application application) {
        RHDatabase db = RHDatabase.getDatabase(application);
        courseDAO = db.courseDAO();
        courseDescriptionDAO = db.courseDescriptionDAO();
        programDAO = db.programDAO();
        reportDAO = db.reportDAO();
        userDAO = db.userDAO();
        videoDAO = db.videoDAO();
        workbookDAO = db.workbookDAO();
        courseWithWorkbooksDAO = db.courseWithWorkbooksDAO();
        executorService = Executors.newCachedThreadPool();
    }

    // COURSES
    public LiveData<List<Course>> getAllCourses() { return courseDAO.getAll(); }

    public LiveData<List<Course>> getAllAuthorisedCourses() {return courseDAO.getAllAuthorised(); }

    public LiveData<List<Course>> getAllUndiscoveredCourses() { return courseDAO.getAllUnauthorised(); }

    public LiveData<Course> getCourse(@NonNull String courseId) { return courseDAO.findById(courseId); }

    public LiveData<List<Course>> getAllCoursesByProgramId(@NonNull String programId) { return courseDAO.findByProgramId(programId); }

    // COURSE DESCRIPTIONS
    public LiveData<List<CourseDescription>> getAllCourseDescriptionsByCourseId(@NonNull String courseId) { return courseDescriptionDAO.findByCourseId(courseId); }

    // PROGRAMS
    public LiveData<List<Program>> getAllPrograms() { return programDAO.getAll(); }

    public LiveData<List<Program>> getAllAuthorisedPrograms() { return programDAO.getAllAuthorised(); }

    public LiveData<List<Program>> getAllUndiscoveredPrograms() { return programDAO.getAllUnauthorised(); }

    public LiveData<Program> getProgram(@NonNull String programId) { return programDAO.findById(programId); }

    // REPORTS
    public LiveData<List<Report>> getAllReports() { return reportDAO.getAll(); }

    public LiveData<List<Report>> getAllAuthorisedReports() { return reportDAO.getAllAuthorised(); }

    // USER
    public LiveData<List<User>> getAllUsers() { return userDAO.getAll(); }

    public LiveData<User> getAuthenticatedUser(String id) { return userDAO.getAuthenticatedUser(id); }

    public void deleteUser(User user) {
      try { executorService.submit(new DeleteService(userDAO, user)); }
      catch (Exception e) { e.printStackTrace(); }
    }

    // VIDEO
    public LiveData<List<Video>> getAllVideos() { return videoDAO.getAll(); }

    public LiveData<Video> getVideoByCourseId(String courseId) { return videoDAO.findByCourseId(courseId); }

    public LiveData<List<Video>> getAllAuthorisedVideos() { return videoDAO.getAllAuthorised(); }

    public LiveData<List<Video>> getAllUndiscoveredVideos() { return videoDAO.getAllUnauthorised(); }

    // WORKBOOKS
    public LiveData<List<Workbook>> getAllWorkbooks() { return workbookDAO.getAll(); }

    public LiveData<List<Workbook>> getAllAuthorisedWorkbooks() { return workbookDAO.getAllAuthorised(); }

    public LiveData<List<Workbook>> getAllUndiscoveredWorkbooks() { return workbookDAO.getAllUnauthorised(); }

    public LiveData<List<Workbook>> getAllWorkbooksByCourseId(@NonNull String courseId) { return workbookDAO.findByCourseId(courseId); }

    // PREMIUM
    public LiveData<List<AuthorisedProgram>> getAuthorisedPrograms() {
        return authorisedProgramDAO.getAll();
    }

    public void authorisePrograms(@NonNull List<AuthorisedProgram> authorisedPrograms) {
        try {
            executorService.submit(
                    new AuthProgramService(authorisedPrograms)
            ).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(@NonNull Object o) {
      try {
        if (o.getClass().getSimpleName().equalsIgnoreCase(User.class.getSimpleName())) {
          executorService.submit(new UpdateService(userDAO, o));
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    public Long insert(@NonNull Object o) {
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

  private class AuthProgramService implements Callable<Void> {
    private List<AuthorisedProgram> authorisedPrograms;
    public AuthProgramService(List<AuthorisedProgram> authorisedPrograms) {
      this.authorisedPrograms = authorisedPrograms;
    }

    @Override
    public Void call() throws Exception {
        programDAO.deauthoriseAll();
        courseDAO.deauthoriseAll();
        videoDAO.deauthoriseAll();
        workbookDAO.deauthoriseAll();
        for (AuthorisedProgram authorisedProgram: authorisedPrograms) {
            programDAO.authorise(authorisedProgram.getProgramId());
            courseDAO.authorise(authorisedProgram.getProgramId());
            videoDAO.authorise(authorisedProgram.getProgramId());
            workbookDAO.authorise(authorisedProgram.getProgramId());
        }
      return null;
    }
  }
}
