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
    entity = Program.class,
    parentColumns = "id",
    childColumns = "program_id"
  ), indices = {@Index("program_id")})
public class Course extends Model {
    @ColumnInfo(name = "id")
    @NonNull
    @PrimaryKey
    private String id;

    @NonNull
    @ColumnInfo(name = "program_id")
    private String programId;

    @NonNull
    @ColumnInfo(name = "title")
    private String title;

    @NonNull
    @ColumnInfo(name = "author")
    private String author;

    @NonNull
    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "is_authorised")
    private boolean isAuthorised = false;

    @Ignore
    public Course() { }

    public Course(@NonNull String id, @NonNull String programId,
                  @NonNull String title, @NonNull String author,
                  @NonNull String description)
    {
      this.id = id;
      this.programId = programId;
      this.title = title;
      this.author = author;
      this.description = description;
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
  public String getProgramId() { return programId; }

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

  public void setProgramId(@NonNull String programId) { this.programId = programId; }
}
