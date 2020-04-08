package com.example.rhdigital.database.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.rhdigital.database.model.Course;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface CourseDAO {
    //CRUD OPERATIONS

    @Insert
    void insert(Course course);

    @Query("DELETE FROM courses")
    void deleteAll();

    @Query("DELETE FROM courses WHERE id = :id")
    int deleteById(int id);

    @Query("SELECT * FROM courses")
    LiveData<List<Course>> getAllCourses();
}
