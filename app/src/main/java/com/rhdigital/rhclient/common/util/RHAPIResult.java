 package com.rhdigital.rhclient.common.util;

public class RHAPIResult {

  // ERROR
  public static final String AUTH_ERROR_EXISTING_EMAIL = "This email address already belongs to an account.";
  public static final String AUTH_ERROR_INVALID_SIGN_UP = "Your sign up information is incomplete or invalid.";
  public static final String AUTH_ERROR_INVALID_SIGN_IN = "Your sign in credentials are incomplete or invalid.";
  public static final String INTERNAL_ERROR_SERVER_ERROR = "An unknown server error has occurred. Please try again later.";

  // SUCCESS
  public static final String VALIDATION_SUCCESS = "VALID";
  public static final String AUTH_SUCCESS_SIGN_UP = "Your account has successfully been created.";

  boolean success;
  String message;
  Object payload;

  public RHAPIResult(String message, Object payload, boolean success) {
    this.message = message;
    this.payload = payload;
    this.success = success;
  }

  public RHAPIResult() {}

  public String getMessage() { return message; }

  public Object getPayload() { return payload; }

  public void setMessage(String message) { this.message = message; }

  public void setPayload(Object payload) { this.payload = payload; }
}
