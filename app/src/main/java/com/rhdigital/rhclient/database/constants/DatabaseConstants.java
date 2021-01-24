package com.rhdigital.rhclient.database.constants;

public class DatabaseConstants {

  public static final String collectionsAll[] = {
    "programs",
    "courses",
    "reports",
    "videos",
    "workbooks",
    "users",
  };

  public static final String collectionsUser[] = {
          "users"
  };

  public static final String collectionsReports[] = {
          "reports"
  };

  public static final String collectionsProgramContent[] = {
          "programs",
          "courses",
          "videos",
          "workbooks",
  };

  public static final String DAOsALL[] = {
    "programDAO",
    "courseDAO",
          "courseDescriptionDAO",
    "reportDAO",
    "videoDAO",
    "workbookDAO",
    "userDAO",
          "authorisedProgramDAO",
  };

  public static final String DAOsUser[] = {
          "userDAO",
          "authorisedProgramDAO",
  };

  public static final String DAOsReports[] = {
          "reportDAO"
  };

  public static final String DAOsProgramContent[] = {
          "programDAO",
          "courseDAO",
          "courseDescriptionDAO",
          "videoDAO",
          "workbookDAO",
  };

  public static final String DAOPath = "com.rhdigital.rhclient.database.DAO.";
}
