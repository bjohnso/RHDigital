package com.example.rhdigital.database.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.RoomDatabase;

import com.example.rhdigital.database.DAO.CourseDAO;
import com.example.rhdigital.database.RHDatabase;
import com.example.rhdigital.database.model.Course;

import java.util.ArrayList;
import java.util.List;

public class RHRepository {
    private CourseDAO courseDAO;

    public RHRepository(Application application) {
        RHDatabase db = RHDatabase.getDatabase(application);
        courseDAO = db.courseDAO();
    }

    public LiveData<List<Course>> getAllCourses() {
        return courseDAO.getAllCourses();
    }

    public void insert(Course course) {
        new insertAsyncTask(courseDAO).execute(course);
    }

    private class insertAsyncTask extends AsyncTask<Course, Void, Void> {
        private CourseDAO asyncTaskDAO;
        public insertAsyncTask(CourseDAO dao) {
            asyncTaskDAO = dao;
        }

        @Override
        protected Void doInBackground(Course... courses) {
            asyncTaskDAO.insert(courses[0]);
            return null;
        }
    }
}
