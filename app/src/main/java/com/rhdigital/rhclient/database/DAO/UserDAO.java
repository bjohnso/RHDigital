package com.rhdigital.rhclient.database.DAO;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Update;

import com.rhdigital.rhclient.database.model.Course;
import com.rhdigital.rhclient.database.model.User;

import java.util.List;

@Dao
public abstract class UserDAO extends BaseDAO<User> {

  @Query("DELETE FROM users")
  abstract public void deleteAll();

  @Query("DELETE FROM users WHERE id = :id")
  abstract public void deleteById(String id);

  @Query("SELECT * FROM users WHERE id = :id")
  abstract public LiveData<User> findById(@NonNull String id);

  @Query("SELECT * FROM users WHERE id = :id")
  abstract public LiveData<User> getAuthenticatedUser(String id);

  @Query("SELECT * FROM users")
  abstract public LiveData<List<User>> getAll();

}
