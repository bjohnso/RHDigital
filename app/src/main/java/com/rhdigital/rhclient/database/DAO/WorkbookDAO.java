package com.rhdigital.rhclient.database.DAO;

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

    @Query("SELECT * FROM workbooks")
    abstract public LiveData<List<Workbook>> getAllWorkbooks();

    @Query("SELECT * FROM workbooks WHERE course_id = :courseId")
    abstract public LiveData<List<Workbook>> getWorkbooksByCourseId(int courseId);

}
