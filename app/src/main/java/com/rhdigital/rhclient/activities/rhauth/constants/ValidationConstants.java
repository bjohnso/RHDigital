package com.rhdigital.rhclient.activities.rhauth.constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ValidationConstants {

  public static String EMAIL_STRATEGY = "emailStrategy";
  public static String PARTIAL_STRATEGY = "partialStrategy";

  public static final HashMap<String, List<String>> EMAIL_STRATEGY_MAP;
  static {
    EMAIL_STRATEGY_MAP = new HashMap<>();
    EMAIL_STRATEGY_MAP.put("email", Arrays.asList("textValidation", "emailValidation"));
    EMAIL_STRATEGY_MAP.put("password", Arrays.asList("textValidation", "passwordValidation"));
    EMAIL_STRATEGY_MAP.put("firstName", Arrays.asList("textValidation"));
    EMAIL_STRATEGY_MAP.put("lastName", Arrays.asList("textValidation"));
  }

  public static final HashMap<String, List<String>> GLOBAL_FIELD_MAP;
  static {
    GLOBAL_FIELD_MAP = new HashMap<>();
    GLOBAL_FIELD_MAP.put("email", Arrays.asList("textValidation", "emailValidation", "emailVerification"));
    GLOBAL_FIELD_MAP.put("password", Arrays.asList("textValidation", "passwordValidation"));
    GLOBAL_FIELD_MAP.put("firstName", Arrays.asList("textValidation"));
    GLOBAL_FIELD_MAP.put("lastName", Arrays.asList("textValidation"));
  }
}
