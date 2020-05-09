package com.rhdigital.rhclient.database.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "courses")
public class Course {
    @ColumnInfo(name = "id")
    @NonNull
    @PrimaryKey
    private String id;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @NonNull
    @ColumnInfo(name = "description")
    private String description;

    @NonNull
    @ColumnInfo(name = "thumb")
    private String thumbnailURL;

    @NonNull
    @ColumnInfo(name = "videoURL")
    private String videoURL;

    @ColumnInfo(name = "isAuthorised")
    private boolean isAuthorised = false;

    @Ignore
    public Course() { }

    public Course(@NonNull String id, @NonNull String name, @NonNull String description, @NonNull String thumbnailURL, @NonNull String videoURL) {
      this.id = id;
      this.name = name;
      this.description = description;
      this.thumbnailURL = thumbnailURL;
      this.videoURL = videoURL;
    }

    public String getId() {
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
    public String getThumbnailURL() {
        return thumbnailURL;
    }

    @NonNull
    public String getVideoURL() {
        return videoURL;
    }

  public void setAuthorised(boolean authorised) {
    isAuthorised = authorised;
  }

  public boolean isAuthorised() {
    return isAuthorised;
  }

  public void setDescription(@NonNull String description) {
        this.description = description;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public void setThumbnailURL(@NonNull String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public void setVideoURL(@NonNull String videoURL) {
        this.videoURL = videoURL;
    }

  @NonNull
  @Override
  public String toString() {
    return "Id: "
      + this.id
      + "\nName: "
      + this.name
      + "\nthumbnailURL: "
      + this.thumbnailURL
      + "\nvideoURL: "
      + this.videoURL;
  }
}
