package com.rhdigital.rhclient.database.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "authorised_programs")
public class AuthorisedProgram {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "programId")
    String programId;

    @Ignore
    public AuthorisedProgram() { }

    public AuthorisedProgram(@NonNull String programId) {
        this.programId = programId;
    }

    @NonNull
    public String getProgramId() {
        return programId;
    }

    public void setProgramId(@NonNull String programId) {
        this.programId = programId;
    }
}
