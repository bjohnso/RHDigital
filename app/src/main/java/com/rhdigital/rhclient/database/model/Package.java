package com.rhdigital.rhclient.database.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "packages")
public class Package {

  @ColumnInfo(name = "id")
  @NonNull
  @PrimaryKey
  private String id;

  @NonNull
  @ColumnInfo(name = "title")
  private String title;

  @NonNull
  @ColumnInfo(name = "packageClass")
  private String packageClass;

  @NonNull
  @ColumnInfo(name = "packageClass")
  private Double price;

  @ColumnInfo(name = "isAuthorised")
  private boolean isAuthorised = false;

  public Package() {}

  public Package(@NonNull String id, @NonNull String title, @NonNull String packageClass, @NonNull Double price) {
    this.id = id;
    this.title = title;
    this.packageClass = packageClass;
    this.price = price;
  }

  public void setId(@NonNull String id) {
    this.id = id;
  }

  public void setPackageClass(@NonNull String packageClass) {
    this.packageClass = packageClass;
  }

  public void setPrice(@NonNull Double price) {
    this.price = price;
  }

  public void setTitle(@NonNull String title) {
    this.title = title;
  }

  public void setAuthorised(boolean authorised) {
    isAuthorised = authorised;
  }

  public boolean isAuthorised() {
    return isAuthorised;
  }

  @NonNull
  public Double getPrice() {
    return price;
  }

  @NonNull
  public String getId() {
    return id;
  }

  @NonNull
  public String getPackageClass() {
    return packageClass;
  }

  @NonNull
  public String getTitle() {
    return title;
  }

  @NonNull
  @Override
  public String toString() {
    return "Id: "
      + this.id
      + "\nTitle: "
      + this.title
      + "\npackageClass: "
      + this.packageClass
      + "\nPrice: "
      + this.price;
  }

}
