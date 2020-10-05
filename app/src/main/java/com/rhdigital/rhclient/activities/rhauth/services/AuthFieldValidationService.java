package com.rhdigital.rhclient.activities.rhauth.services;

import android.util.Log;
import android.util.Patterns;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.rhdigital.rhclient.activities.rhauth.constants.ValidationConstants.EMAIL_STRATEGY_MAP;
import static com.rhdigital.rhclient.activities.rhauth.constants.ValidationConstants.GLOBAL_FIELD_MAP;

public class AuthFieldValidationService {

  public AuthFieldValidationService() { }

  public List<String> validateEmailStrategyData(HashMap<String, String> fields) {
    ArrayList<String> validationErrors = new ArrayList<>();

    Iterator<Map.Entry<String, List<String>>> it = EMAIL_STRATEGY_MAP.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry<String, List<String>> pair = it.next();
      for (String member: pair.getValue()) {
        try {
          // USE REFLECTION TO INFER POINTER TO CLASS MEMBER
          Method method = this.getClass().getDeclaredMethod(member, String.class);
          method.setAccessible(true);
          String validation;
          if ((validation = (String) method.invoke(this, fields.get(pair.getKey()))) != "VALID") {
            // REMOVE DUPLICATE ERRORS
            for (int i = 0; i < validationErrors.size(); i++) {
              if (validationErrors.get(i) == validation) {
                validationErrors.remove(i);
              }
            }
            // ADD VALIDATION ERROR
            validationErrors.add(validation);
            break ;
          };
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
          e.printStackTrace();
        }
      }
    }
    return validationErrors;
  }

  public List<String> validatePartialStrategyData(HashMap<String, String> fields) {
    ArrayList<String> validationErrors = new ArrayList<>();

    Iterator<Map.Entry<String,String>> it = fields.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry<String, String> pair = it.next();
      for (String member: GLOBAL_FIELD_MAP.get(pair.getKey())) {
        try {
          // USE REFLECTION TO INFER POINTER TO CLASS MEMBER
          Method method = this.getClass().getDeclaredMethod(member, String.class);
          method.setAccessible(true);
          String validation;
          if ((validation = (String) method.invoke(this, fields.get(pair.getKey()))) != "VALID") {
            // REMOVE DUPLICATE ERRORS
            for (int i = 0; i < validationErrors.size(); i++) {
              if (validationErrors.get(i) == validation) {
                validationErrors.remove(i);
              }
            }
            // ADD VALIDATION ERROR
            validationErrors.add(validation);
            break ;
          };
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
          e.printStackTrace();
        }
      }
    }
    return validationErrors;
  }

  public String textValidation(String field) {
    if (field != null && !field.isEmpty()) {
      return "VALID";
    }
    return "Please complete empty fields";
  }

  public String passwordValidation(String field) {
    if (field != null && field.length() > 5) {
      return "VALID";
    }
    return "Password must be at least 6 characters long";
  }

  public String emailValidation(String field) {
    if (Patterns.EMAIL_ADDRESS.matcher(field).matches()) {
      return "VALID";
    }
    return "Please enter a valid email address";
  }
}
