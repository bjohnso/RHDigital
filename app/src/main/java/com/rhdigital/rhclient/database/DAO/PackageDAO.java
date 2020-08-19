package com.rhdigital.rhclient.database.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Query;

import com.rhdigital.rhclient.database.model.Course;
import com.rhdigital.rhclient.database.model.Package;

import java.util.List;

public abstract class PackageDAO extends BaseDAO<Package> {

  @Query("DELETE FROM packages")
  abstract public void deleteAll();

  @Query("DELETE FROM packages WHERE id = :id")
  abstract public int deleteById(int id);

  @Query("SELECT * FROM packages")
  abstract public LiveData<List<Package>> getAllPackages();

  @Query("SELECT * FROM packages WHERE isAuthorised = 1;")
  abstract public LiveData<List<Package>> getAllAuthorisedPackages();

  @Query("SELECT * FROM packages WHERE isAuthorised = 0;")
  abstract public LiveData<List<Package>> getAllUndiscoveredPackages();

  @Query("UPDATE packages SET isAuthorised = 1 WHERE id = :id")
  abstract public int authorise(String id);

  @Query("UPDATE packages SET isAuthorised = 0")
  abstract public void deauthorise();

}
