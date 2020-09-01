package com.rhdigital.rhclient.database.DAO;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Query;
import com.rhdigital.rhclient.database.model.Report;

import java.util.List;

public abstract class ReportDAO extends BaseDAO<Report> {

  // DELETE
  @Query("DELETE FROM reports")
  abstract public void deleteAll();

  @Query("DELETE FROM reports WHERE id = :id")
  abstract public int deleteById(int id);

  // GET
  @Query("SELECT * FROM reports WHERE package_id = :id")
  abstract public LiveData<List<Report>> findByPackageId(@NonNull String id);

  @Query("SELECT * FROM reports WHERE id = :id")
  abstract public LiveData<Report> findById(@NonNull String id);

  @Query("SELECT * FROM reports")
  abstract public LiveData<List<Report>> getAll();

  @Query("SELECT * FROM reports WHERE is_authorised = 1;")
  abstract public LiveData<List<Report>> getAllAuthorised();

  @Query("SELECT * FROM reports WHERE is_authorised = 0;")
  abstract public LiveData<List<Report>> getAllUnauthorised();

  // AUTH
  @Query("UPDATE reports SET is_authorised = 1 WHERE id = :id")
  abstract public int authorise(@NonNull String id);

  @Query("UPDATE reports SET is_authorised = 0")
  abstract public void deauthorise();
}