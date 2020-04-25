package com.rhdigital.rhclient.database.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "workbooks",
        foreignKeys = @ForeignKey(onDelete = CASCADE,
                entity = Course.class,
                parentColumns = "id",
                childColumns = "course_id"
        ), indices = {@Index("course_id")})
public class Workbook {
    @ColumnInfo(name = "id")
    @NonNull
    @PrimaryKey
    private String id;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @NonNull
    @ColumnInfo(name = "workbookURL")
    private String workbookURL;

    @ColumnInfo(name = "course_id")
    private String courseId;

    @Ignore
    public Workbook() {}

    @Ignore
    public Workbook(@NonNull String id, @NonNull String name, @NonNull String workbookURL) {
      this.id = id;
      this.name = name;
      this.workbookURL = workbookURL;
    }

    public Workbook(@NonNull String id, @NonNull String name, @NonNull String workbookURL, @NonNull String courseId) {
      this.id = id;
      this.name = name;
      this.workbookURL = workbookURL;
      this.courseId = courseId;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getWorkbookURL() {
        return workbookURL;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getId() {
        return id;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setWorkbookURL(@NonNull String workbookURL) {
        this.workbookURL = workbookURL;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

  @NonNull
  @Override
  public String toString() {
    return "Id: "
      + this.id
      + "\nName: "
      + this.name
      + "\nWorkbookURL: "
      + this.workbookURL
      + "\nCourseId: "
      + this.courseId;
  }
}
