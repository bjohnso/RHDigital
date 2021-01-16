package com.rhdigital.rhclient.database.DAO;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.rhdigital.rhclient.database.model.Course;

import java.util.List;

@Dao
public abstract class CourseDAO extends BaseDAO<Course> {

    // DELETE
    @Query("DELETE FROM courses")
    abstract public void deleteAll();

    @Query("DELETE FROM courses WHERE id = :id")
    abstract public int deleteById(int id);

    // GET
    @Query("SELECT * FROM courses WHERE program_id = :id")
    abstract public LiveData<List<Course>> findByProgramId(@NonNull String id);

    @Query("SELECT * FROM courses WHERE id = :id")
    abstract public LiveData<Course> findById(@NonNull String id);

    @Query("SELECT * FROM courses")
    abstract public LiveData<List<Course>> getAll();

    @Query("SELECT * FROM courses WHERE is_authorised = 1;")
    abstract public LiveData<List<Course>> getAllAuthorised();

    @Query("SELECT * FROM courses WHERE is_authorised = 0;")
    abstract public LiveData<List<Course>> getAllUnauthorised();

    // AUTH
    @Query("UPDATE courses SET is_authorised = 1 WHERE program_id = :programId")
    abstract public int authorise(@NonNull String programId);

    @Query("UPDATE courses SET is_authorised = 0 WHERE program_id = :programId")
    abstract public void deauthorise(String programId);
}
