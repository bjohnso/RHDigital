package com.rhdigital.rhclient.database.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "programs")
public class Program extends Model {

  @ColumnInfo(name = "id")
  @NonNull
  @PrimaryKey
  private String id;

  @NonNull
  @ColumnInfo(name = "title")
  private String title;

  @NonNull
  @ColumnInfo(name = "type")
  private String type;

  @NonNull
  @ColumnInfo(name = "price")
  private Double price;

  @NonNull
  @ColumnInfo(name = "poster_url")
  private String posterUrl;

  @ColumnInfo(name = "is_authorised")
  private boolean isAuthorised = false;

  @Ignore
  public Program() {}

  public Program(@NonNull String id, @NonNull String title, @NonNull String type, @NonNull Double price, @NonNull String posterUrl) {
    this.id = id;
    this.title = title;
    this.type = type;
    this.price = price;
    this.posterUrl = posterUrl;
  }

  @NonNull
  public String getId() { return id; }

  @NonNull
  public String getTitle() {
    return title;
  }

  @NonNull
  public Double getPrice() {
    return price;
  }

  @NonNull
  public String getType() {
    return type;
  }

  @NonNull
  public String getPosterUrl() { return posterUrl; }

  public boolean isAuthorised() {
    return isAuthorised;
  }

  public void setId(@NonNull String id) {
    this.id = id;
  }

  public void setTitle(@NonNull String title) {
    this.title = title;
  }

  public void setPrice(@NonNull Double price) {
    this.price = price;
  }

  public void setAuthorised(boolean authorised) {
    isAuthorised = authorised;
  }

  public void setType(@NonNull String type) {
    this.type = type;
  }

  public void setPosterUrl(@NonNull String posterUrl) { this.posterUrl = posterUrl; }
}
