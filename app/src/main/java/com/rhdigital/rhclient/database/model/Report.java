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
    entity = Package.class,
    parentColumns = "id",
    childColumns = "package_id"
  ), indices = {@Index("package_id")})
public class Report {
  @ColumnInfo(name = "id")
  @NonNull
  @PrimaryKey
  private String id;

  @ColumnInfo(name = "package_id")
  @NonNull
  private String packageId;

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

  public Report(@NonNull String id, @NonNull String packageId,
                @NonNull String month, @NonNull String url) {
    this.id = id;
    this.packageId = packageId;
    this.month = month;
    this.url = url;
  }

  @NonNull
  public String getPackageId() {
    return packageId;
  }

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

  public boolean isAuthorised() {
    return isAuthorised;
  }

  public void setPackageId(@NonNull String packageId) {
    this.packageId = packageId;
  }

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
