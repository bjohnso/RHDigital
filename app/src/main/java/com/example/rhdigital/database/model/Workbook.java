package com.example.rhdigital.database.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "workbooks",
        foreignKeys = @ForeignKey(
                entity = Course.class,
                parentColumns = "id",
                childColumns = "course_id",
                onDelete = ForeignKey.NO_ACTION
        ), indices = {@Index("course_id")})
public class Workbook {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @NonNull
    @ColumnInfo(name = "description")
    private String description;

    @NonNull
    @ColumnInfo(name = "workbookURL")
    private String workbookURL;

    @ColumnInfo(name = "course_id")
    private long courseId;

    @Ignore
    public Workbook(@NonNull String name, @NonNull String description, @NonNull String workbookURL) {
        this.name = name;
        this.description = description;
        this.workbookURL = workbookURL;
    }

    public Workbook(@NonNull String name, @NonNull String description, @NonNull String workbookURL, @NonNull long courseId) {
        this.name = name;
        this.description = description;
        this.workbookURL = workbookURL;
        this.courseId = courseId;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    @NonNull
    public String getWorkbookURL() {
        return workbookURL;
    }

    public long getCourseId() {
        return courseId;
    }

    public long getId() {
        return id;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    public void setWorkbookURL(@NonNull String workbookURL) {
        this.workbookURL = workbookURL;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }
}
