package com.rhdigital.rhclient.database.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "videos",
  foreignKeys = @ForeignKey(onDelete = CASCADE,
    entity = Course.class,
    parentColumns = "id",
    childColumns = "course_id"
  ), indices = {@Index("course_id")})
public class Video {
  @ColumnInfo(name = "id")
  @NonNull
  @PrimaryKey
  private String id;

  @ColumnInfo(name = "course_id")
  private String courseId;

  @NonNull
  @ColumnInfo(name = "title")
  private String title;

  @NonNull
  @ColumnInfo(name = "language")
  private String language;

  @NonNull
  @ColumnInfo(name = "subtitle")
  private String subtitle;

  @NonNull
  @ColumnInfo(name = "url")
  private String url;

  @ColumnInfo(name = "is_authorised")
  private boolean isAuthorised = false;

  @Ignore
  public Video() {}

  public Video(@NonNull String id, @NonNull String courseId,
               @NonNull String title, @NonNull String language,
               @NonNull String subtitle, @NonNull String url) {
    this.id = id;
    this.courseId = courseId;
    this.title = title;
    this.language = language;
    this.subtitle = subtitle;
    this.url = url;
  }

  public void setAuthorised(boolean authorised) {
    isAuthorised = authorised;
  }

  public void setUrl(@NonNull String url) {
    this.url = url;
  }

  public void setId(@NonNull String id) {
    this.id = id;
  }

  public void setTitle(@NonNull String title) {
    this.title = title;
  }

  public void setLanguage(@NonNull String language) {
    this.language = language;
  }

  public void setCourseId(String courseId) {
    this.courseId = courseId;
  }

  public void setSubtitle(@NonNull String subtitle) {
    this.subtitle = subtitle;
  }

  @NonNull
  public String getUrl() {
    return url;
  }

  @NonNull
  public String getId() {
    return id;
  }

  @NonNull
  public String getTitle() {
    return title;
  }

  @NonNull
  public String getLanguage() {
    return language;
  }

  public String getCourseId() {
    return courseId;
  }

  @NonNull
  public String getSubtitle() {
    return subtitle;
  }

  public boolean isAuthorised() {
    return isAuthorised;
  }
}
