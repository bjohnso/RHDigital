package com.example.rhdigital.database.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "courses")
public class Course {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @NonNull
    @ColumnInfo(name = "description")
    private String description;

    @NonNull
    @ColumnInfo(name = "videoURL")
    private String videoURL;

    public Course(@NonNull String name, @NonNull String description, @NonNull String videoURL) {
        this.name = name;
        this.description = description;
        this.videoURL = videoURL;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getVideoURL() {
        return videoURL;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public void setVideoURL(@NonNull String videoURL) {
        this.videoURL = videoURL;
    }
}
