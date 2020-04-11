package com.example.rhdigital.database.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.rhdigital.database.model.CourseWithWorkbooks;

import java.util.List;

@Dao
public abstract class CourseWithWorkbooksDAO {

  @Transaction
  @Query("SELECT * FROM courses")
  abstract public LiveData<List<CourseWithWorkbooks>> getAllCoursesWithWorkbooks();

}
