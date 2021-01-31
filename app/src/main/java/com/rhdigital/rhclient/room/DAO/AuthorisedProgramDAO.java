package com.rhdigital.rhclient.room.DAO;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.rhdigital.rhclient.room.model.AuthorisedProgram;

import java.util.List;

@Dao
public abstract class AuthorisedProgramDAO extends BaseDAO<AuthorisedProgram> {

    // DELETE
    @Query("DELETE FROM authorised_programs")
    abstract public void deleteAll();

    // GET
    @Query("SELECT * FROM authorised_programs WHERE id = :id")
    abstract public LiveData<AuthorisedProgram> findById(@NonNull String id);

    @Query("SELECT * FROM authorised_programs")
    abstract public LiveData<List<AuthorisedProgram>> getAll();
}
