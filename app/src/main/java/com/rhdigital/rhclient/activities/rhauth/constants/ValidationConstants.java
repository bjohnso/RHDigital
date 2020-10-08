package com.rhdigital.rhclient.activities.rhauth.constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ValidationConstants {

  public static int VALIDATION_STRATEGY_FULL_EMAIL = 0;
  public static int VALIDATION_STRATEGY_SIGN_UP_EMAIL = 1;
  public static int VALIDATION_STRATEGY_SIGN_UP_DETAILS = 2;

  private static final HashMap<String, List<String>> VALIDATION_STRATEGY_FULL_EMAIL_MAP;
  static {
    VALIDATION_STRATEGY_FULL_EMAIL_MAP = new HashMap<>();
    VALIDATION_STRATEGY_FULL_EMAIL_MAP.put("email", Arrays.asList("textValidation", "emailValidation", "emailVerification"));
    VALIDATION_STRATEGY_FULL_EMAIL_MAP.put("password", Arrays.asList("textValidation", "passwordValidation"));
    VALIDATION_STRATEGY_FULL_EMAIL_MAP.put("firstName", Arrays.asList("textValidation"));
    VALIDATION_STRATEGY_FULL_EMAIL_MAP.put("lastName", Arrays.asList("textValidation"));
  }

  private static final HashMap<String, List<String>> VALIDATION_STRATEGY_SIGN_UP_EMAIL_MAP;
  static {
    VALIDATION_STRATEGY_SIGN_UP_EMAIL_MAP = new HashMap<>();
    VALIDATION_STRATEGY_SIGN_UP_EMAIL_MAP.put("email", Arrays.asList("textValidation", "emailValidation", "emailVerification"));
  }

  private static final HashMap<String, List<String>> VALIDATION_STRATEGY_SIGN_UP_DETAILS_MAP;
  static {
    VALIDATION_STRATEGY_SIGN_UP_DETAILS_MAP = new HashMap<>();
    VALIDATION_STRATEGY_SIGN_UP_DETAILS_MAP.put("password", Arrays.asList("textValidation", "passwordValidation"));
    VALIDATION_STRATEGY_SIGN_UP_DETAILS_MAP.put("firstName", Arrays.asList("textValidation"));
    VALIDATION_STRATEGY_SIGN_UP_DETAILS_MAP.put("lastName", Arrays.asList("textValidation"));
  }

  public static final HashMap<Integer, HashMap<String, List<String>>> VALIDATION_STRATEGY_MAP;
  static {
    VALIDATION_STRATEGY_MAP = new HashMap<>();
    VALIDATION_STRATEGY_MAP.put(VALIDATION_STRATEGY_FULL_EMAIL, VALIDATION_STRATEGY_FULL_EMAIL_MAP);
    VALIDATION_STRATEGY_MAP.put(VALIDATION_STRATEGY_SIGN_UP_EMAIL, VALIDATION_STRATEGY_SIGN_UP_EMAIL_MAP);
    VALIDATION_STRATEGY_MAP.put(VALIDATION_STRATEGY_SIGN_UP_DETAILS, VALIDATION_STRATEGY_SIGN_UP_DETAILS_MAP);
  }
}
