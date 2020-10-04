package com.rhdigital.rhclient.database.DAO;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import com.rhdigital.rhclient.database.model.Program;

import java.util.List;

@Dao
public abstract class ProgramDAO extends BaseDAO<Program> {

  // DELETE
  @Query("DELETE FROM programs")
  abstract public void deleteAll();

  @Query("DELETE FROM programs WHERE id = :id")
  abstract public int deleteById(int id);

  // GET
  @Query("SELECT * FROM programs WHERE id = :id")
  abstract public LiveData<Program> findById(@NonNull String id);

  @Query("SELECT * FROM programs")
  abstract public LiveData<List<Program>> getAll();

  @Query("SELECT * FROM programs WHERE is_authorised = 1;")
  abstract public LiveData<List<Program>> getAllAuthorised();

  @Query("SELECT * FROM programs WHERE is_authorised = 0;")
  abstract public LiveData<List<Program>> getAllUnauthorised();

  // AUTH
  @Query("UPDATE programs SET is_authorised = 1 WHERE id = :id")
  abstract public int authorise(String id);

  @Query("UPDATE programs SET is_authorised = 0")
  abstract public void deauthorise();

}
