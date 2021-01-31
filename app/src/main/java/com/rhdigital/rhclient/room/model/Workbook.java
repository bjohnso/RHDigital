package com.rhdigital.rhclient.room.model;

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
public class Workbook extends Model {
    @ColumnInfo(name = "id")
    @NonNull
    @PrimaryKey
    private String id;

    @ColumnInfo(name = "program_id")
    private String programId;

    @ColumnInfo(name = "course_id")
    private String courseId;

    @NonNull
    @ColumnInfo(name = "title")
    private String title;

    @NonNull
    @ColumnInfo(name = "language")
    private String language;

    @NonNull
    @ColumnInfo(name = "url")
    private String url;

    @ColumnInfo(name = "is_authorised")
    private boolean isAuthorised = false;

    @Ignore
    public Workbook() {}

    public Workbook(@NonNull String id, @NonNull String programId,
                    @NonNull String courseId, @NonNull String title,
                    @NonNull String language, @NonNull String url) {
      this.id = id;
      this.programId = programId;
      this.courseId = courseId;
      this.title = title;
      this.language = language;
      this.url = url;
    }

  @NonNull
  public String getTitle() {
    return title;
  }

  @NonNull
  public String getId() {
    return id;
  }

  public String getCourseId() {
    return courseId;
  }

  @NonNull
  public String getLanguage() {
    return language;
  }

  @NonNull
  public String getUrl() {
    return url;
  }

  public String getProgramId() { return programId; }

  public void setProgramId(String programId) { this.programId = programId; }

  public boolean isAuthorised() {
    return isAuthorised;
  }

  public void setTitle(@NonNull String title) {
    this.title = title;
  }

  public void setId(@NonNull String id) {
    this.id = id;
  }

  public void setCourseId(String courseId) {
    this.courseId = courseId;
  }

  public void setLanguage(@NonNull String language) {
    this.language = language;
  }

  public void setUrl(@NonNull String url) {
    this.url = url;
  }

  public void setAuthorised(boolean authorised) {
    isAuthorised = authorised;
  }
}
