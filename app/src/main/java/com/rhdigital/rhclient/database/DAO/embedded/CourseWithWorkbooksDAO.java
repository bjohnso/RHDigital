package com.rhdigital.rhclient.database.DAO.embedded;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.rhdigital.rhclient.database.model.embedded.CourseWithWorkbooks;

import java.util.List;

@Dao
public abstract class CourseWithWorkbooksDAO {

  @Transaction
  @Query("SELECT * FROM courses")
  abstract public LiveData<List<CourseWithWorkbooks>> getAllCoursesWithWorkbooks();

  @Transaction
  @Query("SELECT * FROM courses WHERE isAuthorised = 1;")
  abstract public LiveData<List<CourseWithWorkbooks>> getAllAuthorisedCoursesWithWorkbooks();

}
