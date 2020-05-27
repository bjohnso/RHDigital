package com.rhdigital.rhclient.database.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Update;

import com.rhdigital.rhclient.database.model.User;

@Dao
public abstract class BaseDAO<T> {
  @Insert
  abstract public long insert(T object);

  @Update
  abstract public void update(T object);
}
