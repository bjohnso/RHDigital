package com.rhdigital.rhclient.activities.rhauth.services;

import android.util.Log;
import android.util.Pair;
import android.util.Patterns;

import com.rhdigital.rhclient.common.util.RHAPIResult;
import com.rhdigital.rhclient.common.interfaces.CallableFunction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.rhdigital.rhclient.activities.rhauth.constants.ValidationConstants.VALIDATION_STRATEGY_MAP;

public class AuthFieldValidationService implements CallableFunction<Object, List<String>> {

  private final String TAG = this.getClass().getSimpleName().toUpperCase();

  private AuthAPIService authAPIService = new AuthAPIService();

  public AuthFieldValidationService() { }

  public List<String> validateAuthStrategyData(HashMap<String, String> fields, int STRATEGY) {
    Log.d(TAG, "RUNNING AUTH VALIDATION STRATEGY FOR KEY : " + STRATEGY);
    HashMap<String, List<String>> STRATEGY_MAP = VALIDATION_STRATEGY_MAP.get(STRATEGY);
    HashMap<String, String> usedKeyMap = new HashMap<>();
    ArrayList<String> validationErrors = new ArrayList<>();
    Iterator<Map.Entry<String, List<String>>> it = STRATEGY_MAP.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry<String, List<String>> pair = it.next();
      for (String member: pair.getValue()) {
        Pair<String, String> validationResult = (Pair<String, String>)taskExecutor(member, new Pair<>(pair.getKey(), fields.get(pair.getKey())));
        if (validationResult.second != "VALID") {
          // CHECK FOR DUPLICATE KEYS
          if (usedKeyMap.get(validationResult.first) == null) {
            usedKeyMap.put(validationResult.first, validationResult.second);
            // REMOVE DUPLICATE VALUES
            for (int i = 0; i < validationErrors.size(); i++) {
              if (validationErrors.get(i).equals(validationResult.second)) {
                validationErrors.remove(i);
              }
            }
            // ADD VALIDATION ERROR
            validationErrors.add(validationResult.second);
            break ;
          }
        }
      }
    }
    return validationErrors;
  }

  public Object taskExecutor(String task, Object arg) {
    try {
      // USE REFLECTION TO INFER POINTER TO CLASS MEMBER
      Method method = this.getClass().getDeclaredMethod(task, Pair.class);
      method.setAccessible(true);
      return method.invoke(this, arg);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
    return null;
  }

  // MEMBER FUNCTIONS
  public Pair<String, String> textValidation(Pair<String, String> pair) {
    if (pair.second != null && pair.second.isEmpty() == false) {
      return new Pair<>(pair.first, "VALID");
    } else {
      return new Pair<>(pair.first, "Please complete empty fields");
    }
  }

  public Pair<String, String> passwordValidation(Pair<String, String> pair) {
    if (pair.second != null && pair.second.length() > 5) {
      return new Pair<>(pair.first, "VALID");
    } else {
      return new Pair<>(pair.first, "Password must contain at least 6 characters");
    }
  }

  public Pair<String, String> emailValidation(Pair<String, String> pair) {
    if (Patterns.EMAIL_ADDRESS.matcher(pair.second).matches()) {
      return new Pair<>(pair.first, "VALID");
    } else {
      return new Pair<>(pair.first, "Please enter a valid email address");
    }
  }

  public Pair<String, String> emailVerification(Pair<String, String> pair) {
    try {
      RHAPIResult result = this.authAPIService.call(new Pair<>("verifyUniqueEmail", pair.second));
      return new Pair<>(pair.first, result.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public List<String> call(Object... args) throws Exception {
    HashMap<String, String> fields = (HashMap<String, String>) args[0];
    int strategyKey = (int) args[1];
    return this.validateAuthStrategyData(fields, strategyKey);
  }

  @Override
  public List<String> then(Object... args) throws Exception {
    return null;
  }
}
