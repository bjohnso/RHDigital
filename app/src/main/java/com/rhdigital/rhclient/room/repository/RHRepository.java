package com.rhdigital.rhclient.room.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.rhdigital.rhclient.common.dto.PopulateRoomDto;
import com.rhdigital.rhclient.room.DAO.AuthorisedProgramDAO;
import com.rhdigital.rhclient.room.DAO.AuthorisedReportDAO;
import com.rhdigital.rhclient.room.DAO.BaseDAO;
import com.rhdigital.rhclient.room.DAO.CourseDAO;
import com.rhdigital.rhclient.room.DAO.CourseDescriptionDAO;
import com.rhdigital.rhclient.room.DAO.ProgramDAO;
import com.rhdigital.rhclient.room.DAO.ReportDAO;
import com.rhdigital.rhclient.room.DAO.VideoDAO;
import com.rhdigital.rhclient.room.DAO.embedded.CourseWithWorkbooksDAO;
import com.rhdigital.rhclient.room.DAO.UserDAO;
import com.rhdigital.rhclient.room.DAO.WorkbookDAO;
import com.rhdigital.rhclient.room.RHDatabase;
import com.rhdigital.rhclient.room.model.AuthorisedProgram;
import com.rhdigital.rhclient.room.model.AuthorisedReport;
import com.rhdigital.rhclient.room.model.Course;
import com.rhdigital.rhclient.room.model.CourseDescription;
import com.rhdigital.rhclient.room.model.Report;
import com.rhdigital.rhclient.room.model.Video;
import com.rhdigital.rhclient.room.model.Program;
import com.rhdigital.rhclient.room.model.User;
import com.rhdigital.rhclient.room.model.Workbook;
import com.rhdigital.rhclient.room.services.FirebaseRoomService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RHRepository {
    protected CourseDAO courseDAO;
    protected CourseDescriptionDAO courseDescriptionDAO;
    protected AuthorisedProgramDAO authorisedProgramDAO;
    protected AuthorisedReportDAO authorisedReportDAO;
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
        authorisedProgramDAO = db.authorisedProgramDAO();
        authorisedReportDAO = db.authorisedReportDAO();
        executorService = Executors.newCachedThreadPool();
    }

    // REMOTE ROOM SERVICE
    public LiveData<ArrayList<Long>> syncWithRemote(RHDatabase instance, PopulateRoomDto populateRoom) {
        FirebaseRoomService firebaseRoomService = new FirebaseRoomService();
        return firebaseRoomService.populateFromUpstream(
                instance, populateRoom
        );
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

    public LiveData<List<AuthorisedReport>> getAuthorisedReports() {
        return authorisedReportDAO.getAll();
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

    public void authoriseReports(@NonNull List<AuthorisedReport> authorisedReports) {
        try {
            executorService.submit(
                    new AuthReportService(authorisedReports)
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
            programDAO.authorise(authorisedProgram.getId());
            courseDAO.authorise(authorisedProgram.getId());
            videoDAO.authorise(authorisedProgram.getId());
            workbookDAO.authorise(authorisedProgram.getId());
        }
      return null;
    }
  }

    private class AuthReportService implements Callable<Void> {
        private List<AuthorisedReport> authorisedReports;
        public AuthReportService(List<AuthorisedReport> authorisedReports) {
            this.authorisedReports = authorisedReports;
        }

        @Override
        public Void call() throws Exception {
            reportDAO.deauthoriseAll();
            for (AuthorisedReport authorisedReport: authorisedReports) {
                reportDAO.authorise(authorisedReport.getId());
            }
            return null;
        }
    }
}
