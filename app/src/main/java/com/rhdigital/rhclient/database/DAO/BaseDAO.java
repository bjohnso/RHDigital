package com.rhdigital.rhclient.database.DAO;

import androidx.room.Dao;
import androidx.room.Insert;

@Dao
public abstract class BaseDAO<T> {
    @Insert
    abstract public long insert(T object);
}
