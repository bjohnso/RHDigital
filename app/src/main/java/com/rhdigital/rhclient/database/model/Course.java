package com.rhdigital.rhclient.database.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "courses",
  foreignKeys = @ForeignKey(onDelete = CASCADE,
    entity = Package.class,
    parentColumns = "id",
    childColumns = "package_id"
  ), indices = {@Index("package_id")})
public class Course {
    @ColumnInfo(name = "id")
    @NonNull
    @PrimaryKey
    private String id;

    @NonNull
    @ColumnInfo(name = "package_id")
    private String packageId;

    @NonNull
    @ColumnInfo(name = "title")
    private String title;

    @NonNull
    @ColumnInfo(name = "author")
    private String author;

    @NonNull
    @ColumnInfo(name = "description")
    private String description;

    @NonNull
    @ColumnInfo(name = "poster_url")
    private String posterURL;

    @ColumnInfo(name = "is_authorised")
    private boolean isAuthorised = false;

    @Ignore
    public Course() { }

    public Course(@NonNull String id, @NonNull String packageId,
                  @NonNull String title, @NonNull String author,
                  @NonNull String description, @NonNull String posterURL)
    {
      this.id = id;
      this.packageId = packageId;
      this.title = title;
      this.author = author;
      this.description = description;
      this.posterURL = posterURL;
    }

  @NonNull
  public String getTitle() {
    return title;
  }

  @NonNull
  public String getId() {
    return id;
  }

  @NonNull
  public String getAuthor() {
    return author;
  }

  @NonNull
  public String getDescription() {
    return description;
  }

  @NonNull
  public String getPackageId() {
    return packageId;
  }

  @NonNull
  public String getPosterURL() {
    return posterURL;
  }

  public boolean isAuthorised() {
    return isAuthorised;
  }

  public void setAuthorised(boolean authorised) {
    isAuthorised = authorised;
  }

  public void setTitle(@NonNull String title) {
    this.title = title;
  }

  public void setId(@NonNull String id) {
    this.id = id;
  }

  public void setAuthor(@NonNull String author) {
    this.author = author;
  }

  public void setDescription(@NonNull String description) {
    this.description = description;
  }

  public void setPackageId(@NonNull String packageId) {
    this.packageId = packageId;
  }

  public void setPosterURL(@NonNull String posterURL) {
    this.posterURL = posterURL;
  }
}
