package com.example.rhdigital.database.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public abstract class BaseDAO<T> {
    @Insert
    abstract public long insert(T object);
}
