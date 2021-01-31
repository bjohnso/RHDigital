package com.rhdigital.rhclient.room.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "authorised_programs")
public class AuthorisedProgram {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    String id;

    @Ignore
    public AuthorisedProgram() { }

    public AuthorisedProgram(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }
}
