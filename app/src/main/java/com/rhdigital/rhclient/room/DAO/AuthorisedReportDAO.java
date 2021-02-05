package com.rhdigital.rhclient.room.DAO;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.rhdigital.rhclient.room.model.AuthorisedReport;

import java.util.List;

@Dao
public abstract class AuthorisedReportDAO extends BaseDAO<AuthorisedReport> {

    // DELETE
    @Query("DELETE FROM authorised_reports")
    abstract public void deleteAll();

    // GET
    @Query("SELECT * FROM authorised_reports WHERE id = :id")
    abstract public LiveData<AuthorisedReport> findById(@NonNull String id);

    @Query("SELECT * FROM authorised_reports")
    abstract public LiveData<List<AuthorisedReport>> getAll();
}