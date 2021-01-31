package com.rhdigital.rhclient.room.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

@Dao
public abstract class BaseDAO<T> {
  @Insert
  abstract public long insert(T object);

  @Update
  abstract public void update(T object);

  @Delete
  abstract public void delete(T object);

}
