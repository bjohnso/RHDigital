package com.rhdigital.rhclient.activities.rhauth.services;

import android.util.Log;
import android.util.Pair;
import android.util.Patterns;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.android.gms.tasks.Task;
import com.rhdigital.rhclient.database.services.CallableFunction;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import static com.rhdigital.rhclient.activities.rhauth.constants.ValidationConstants.EMAIL_STRATEGY_MAP;
import static com.rhdigital.rhclient.activities.rhauth.constants.ValidationConstants.GLOBAL_FIELD_MAP;

public class AuthFieldValidationService implements CallableFunction<HashMap<String, String>, List<String>> {

  private AuthAPIService authAPIService = new AuthAPIService();

  public AuthFieldValidationService() { }

  public void validateEmailStrategyData(HashMap<String, String> fields) {
    Iterator<Map.Entry<String, List<String>>> it = EMAIL_STRATEGY_MAP.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry<String, List<String>> pair = it.next();
      for (String member: pair.getValue()) {
        taskExecutor(member, pair);
      }
    }
  }

  public List<String> validatePartialStrategyData(HashMap<String, String> fields) {
    HashMap<String, String> usedKeyMap = new HashMap<>();
    ArrayList<String> validationErrors = new ArrayList<>();
    Iterator<Map.Entry<String,String>> it = fields.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry<String, String> pair = it.next();
      for (String member: GLOBAL_FIELD_MAP.get(pair.getKey())) {
        Pair<String, String> validationResult = (Pair<String, String>)taskExecutor(member, new Pair<>(pair.getKey(), pair.getValue()));
        if (validationResult.second != "VALID") {
          // CHECK FOR DUPLICATES
          if (usedKeyMap.get(validationResult.first) == null) {
            usedKeyMap.put(validationResult.first, validationResult.second);
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
      return new Pair<>(pair.first, (String) this.authAPIService.call(new Pair<>("verifyEmail", pair.second)));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public List<String> call(HashMap<String, String> arg) throws Exception {
    return this.validatePartialStrategyData(arg);
  }

  @Override
  public List<String> then(HashMap<String, String>... args) throws Exception {
    return null;
  }
}
