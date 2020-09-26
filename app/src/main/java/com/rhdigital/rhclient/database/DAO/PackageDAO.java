package com.rhdigital.rhclient.database.DAO;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import com.rhdigital.rhclient.database.model.Package;

import java.util.List;

@Dao
public abstract class PackageDAO extends BaseDAO<Package> {

  // DELETE
  @Query("DELETE FROM packages")
  abstract public void deleteAll();

  @Query("DELETE FROM packages WHERE id = :id")
  abstract public int deleteById(int id);

  // GET
  @Query("SELECT * FROM packages WHERE id = :id")
  abstract public LiveData<Package> findById(@NonNull String id);

  @Query("SELECT * FROM packages")
  abstract public LiveData<List<Package>> getAll();

  @Query("SELECT * FROM packages WHERE is_authorised = 1;")
  abstract public LiveData<List<Package>> getAllAuthorised();

  @Query("SELECT * FROM packages WHERE is_authorised = 0;")
  abstract public LiveData<List<Package>> getAllUnauthorised();

  // AUTH
  @Query("UPDATE packages SET is_authorised = 1 WHERE id = :id")
  abstract public int authorise(String id);

  @Query("UPDATE packages SET is_authorised = 0")
  abstract public void deauthorise();

}
