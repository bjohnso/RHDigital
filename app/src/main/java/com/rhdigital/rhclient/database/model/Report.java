package com.rhdigital.rhclient.database.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "reports",
  foreignKeys = @ForeignKey(onDelete = CASCADE,
    entity = Program.class,
    parentColumns = "id",
    childColumns = "program_id"
  ), indices = {@Index("program_id")})
public class Report extends Model {
  @ColumnInfo(name = "id")
  @NonNull
  @PrimaryKey
  private String id;

  @ColumnInfo(name = "title")
  @NonNull
  private String title;

  @ColumnInfo(name = "program_id")
  @NonNull
  private String programId;

  @NonNull
  @ColumnInfo(name = "month")
  private String month;

  @NonNull
  @ColumnInfo(name = "url")
  private String url;

  @ColumnInfo(name = "is_authorised")
  private boolean isAuthorised = false;

  @Ignore
  public Report() {}

  public Report(@NonNull String id, @NonNull String programId,
                @NonNull String title, @NonNull String month,
                @NonNull String url) {
    this.id = id;
    this.programId = programId;
    this.title = title;
    this.month = month;
    this.url = url;
  }

  @NonNull
  public String getProgramId() { return programId; }

  @NonNull
  public String getId() {
    return id;
  }

  @NonNull
  public String getUrl() {
    return url;
  }

  @NonNull
  public String getMonth() {
    return month;
  }

  @NonNull
  public String getTitle() { return title; }

  public boolean isAuthorised() {
    return isAuthorised;
  }

  public void setProgramId(@NonNull String programId) { this.programId = programId; }

  public void setTitle(@NonNull String title) { this.title = title; }

  public void setId(@NonNull String id) {
    this.id = id;
  }

  public void setAuthorised(boolean authorised) {
    isAuthorised = authorised;
  }

  public void setUrl(@NonNull String url) {
    this.url = url;
  }

  public void setMonth(@NonNull String month) {
    this.month = month;
  }
}
