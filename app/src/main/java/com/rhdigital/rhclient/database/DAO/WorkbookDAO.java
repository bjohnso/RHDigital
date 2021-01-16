package com.rhdigital.rhclient.database.DAO;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.rhdigital.rhclient.database.model.Workbook;

import java.util.List;

@Dao
public abstract class WorkbookDAO extends BaseDAO<Workbook> {

    @Query("DELETE FROM workbooks")
    abstract public void deleteAll();

    @Query("DELETE FROM workbooks WHERE id  = :id")
    abstract public int deleteById(int id);

    @Query("SELECT * FROM workbooks WHERE course_id = :id ORDER BY title")
    abstract public LiveData<List<Workbook>> findByCourseId(@NonNull String id);

    @Query("SELECT * FROM workbooks WHERE id = :id")
    abstract public LiveData<Workbook> findById(@NonNull String id);

    @Query("SELECT * FROM workbooks")
    abstract public LiveData<List<Workbook>> getAll();

    @Query("SELECT * FROM workbooks WHERE is_authorised = 1;")
    abstract public LiveData<List<Workbook>> getAllAuthorised();

    @Query("SELECT * FROM workbooks WHERE is_authorised = 0;")
    abstract public LiveData<List<Workbook>> getAllUnauthorised();

    @Query("UPDATE workbooks SET is_authorised = 1 WHERE program_id = :programId")
    abstract public int authorise(String programId);

    @Query("UPDATE workbooks SET is_authorised = 0 WHERE program_id = :programId")
    abstract public void deauthorise(String programId);
}
