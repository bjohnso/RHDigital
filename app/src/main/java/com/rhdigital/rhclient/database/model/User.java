package com.rhdigital.rhclient.database.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {

  @ColumnInfo(name = "id")
  @NonNull
  @PrimaryKey
  private String id;

  @ColumnInfo(name = "username")
  private String username;

  @ColumnInfo(name = "email")
  private String email;

  @ColumnInfo(name = "cell")
  private String cell;

  @ColumnInfo(name = "name")
  private String name;

  @ColumnInfo(name = "surname")
  private String surname;

  @ColumnInfo(name = "title")
  private String title;

  @ColumnInfo(name = "city")
  private String city;

  @ColumnInfo(name = "country")
  private String country;

  @ColumnInfo(name = "about")
  private String about;

  @Ignore
  public User() { }

  public User(@NonNull String id,
              String username,
              String email,
              String cell,
              String name,
              String surname,
              String title,
              String city,
              String country,
              String about) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.cell = cell;
    this.name = name;
    this.surname = surname;
    this.title = title;
    this.city = city;
    this.country = country;
    this.about = about;
  }

  public void setId(@NonNull String id) {
    this.id = id;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setCell(String cell) {
    this.cell = cell;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public void setAbout(String about) {
    this.about = about;
  }

  @NonNull
  public String getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public String getEmail() {
    return email;
  }

  public String getCell() {
    return cell;
  }

  public String getName() {
    return name;
  }

  public String getSurname() {
    return surname;
  }

  public String getTitle() {
    return title;
  }

  public String getCity() {
    return city;
  }

  public String getCountry() {
    return country;
  }

  public String getAbout() {
    return about;
  }
}
