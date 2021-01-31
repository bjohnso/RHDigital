package com.rhdigital.rhclient.room.DAO;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.rhdigital.rhclient.room.model.CourseDescription;

import java.util.List;

@Dao
public abstract class CourseDescriptionDAO extends BaseDAO<CourseDescription> {

    // DELETE
    @Query("DELETE FROM course_descriptions")
    abstract public void deleteAll();

    // GET
    @Query("SELECT * FROM course_descriptions WHERE course_id = :id")
    abstract public LiveData<List<CourseDescription>> findById(@NonNull long id);

    @Query("SELECT * FROM course_descriptions WHERE course_id = :courseId")
    abstract public LiveData<List<CourseDescription>> findByCourseId(@NonNull String courseId);
}
