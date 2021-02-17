package com.rhdigital.rhclient.activities.rhauth.services;

import android.util.Log;
import android.util.Pair;

import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.gson.Gson;
import com.rhdigital.rhclient.common.util.RHAPIResult;
import com.rhdigital.rhclient.common.interfaces.CallableFunction;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;

public class AuthAPIService implements CallableFunction<Object, RHAPIResult> {

  private final String TAG = this.getClass().getSimpleName();

  public RHAPIResult verifyUniqueEmail(Object arg) {
    String email = (String) arg;
    Semaphore semaphore = new Semaphore(0);
    AtomicReference<RHAPIResult> taskResult = new AtomicReference<>(null);
    JSONObject data = new JSONObject();
    try {
      data.put("email", email);
      FirebaseFunctions.getInstance()
      .getHttpsCallable("auth-fetchUserByEmail")
      .call(data)
      .addOnSuccessListener(apiResult -> {
        taskResult.set(new RHAPIResult(RHAPIResult.AUTH_ERROR_EXISTING_EMAIL, apiResult.getData(), true));
        semaphore.release();
      })
      .addOnFailureListener(error -> {
        FirebaseFunctionsException exception = (FirebaseFunctionsException) error;
        if (exception.getCode() == FirebaseFunctionsException.Code.NOT_FOUND) {
          taskResult.set(new RHAPIResult(RHAPIResult.VALIDATION_SUCCESS, exception.getDetails(), false));
        } else {
          taskResult.set(new RHAPIResult(RHAPIResult.INTERNAL_ERROR_SERVER_ERROR, exception.getDetails(), false));
        }
        semaphore.release();
      });
      semaphore.acquire();
    } catch (InterruptedException | JSONException e) {
      e.printStackTrace();
    }
    return taskResult.get();
  }

  public RHAPIResult signUpNewUser(Object arg) {
    HashMap<String, String> authFieldsMap = (HashMap<String, String>) arg;
    Gson gson = new Gson();
    String data = gson.toJson(authFieldsMap);
    Semaphore semaphore = new Semaphore(0);
    AtomicReference<RHAPIResult> taskResult = new AtomicReference<>(null);
    try {
      JSONObject signUpData = new JSONObject(data);
      FirebaseFunctions.getInstance()
        .getHttpsCallable("auth-signUpNewUser")
        .call(signUpData)
        .addOnSuccessListener(apiResult -> {
          taskResult.set(new RHAPIResult(
            RHAPIResult.AUTH_SUCCESS_SIGN_UP,
            EmailAuthProvider.getCredential(authFieldsMap.get("email"),
              authFieldsMap.get("password")), true)
          );
          semaphore.release();
        })
        .addOnFailureListener(error -> {
          FirebaseFunctionsException exception = (FirebaseFunctionsException) error;
          if (exception.getCode() == FirebaseFunctionsException.Code.INVALID_ARGUMENT) {
            taskResult.set(new RHAPIResult(RHAPIResult.AUTH_ERROR_INVALID_SIGN_UP, exception.getDetails(), false));
          } else {
            taskResult.set(new RHAPIResult(RHAPIResult.INTERNAL_ERROR_SERVER_ERROR, exception.getDetails(), false));
          }
          semaphore.release();
        });
      semaphore.acquire();
    } catch (InterruptedException | JSONException e) {
      e.printStackTrace();
    }
    return taskResult.get();
  }

  public RHAPIResult sendEmailVerification(Object arg) {
    HashMap<String, String> authFieldsMap = (HashMap<String, String>) arg;
    Gson gson = new Gson();
    String data = gson.toJson(authFieldsMap);
    Semaphore semaphore = new Semaphore(0);
    AtomicReference<RHAPIResult> taskResult = new AtomicReference<>(null);
    try {
      JSONObject verificationData = new JSONObject(data);
      FirebaseFunctions.getInstance()
              .getHttpsCallable("auth-sendEmailVerification")
              .call(verificationData)
              .addOnSuccessListener(apiResult -> {
                taskResult.set(new RHAPIResult(
                        RHAPIResult.AUTH_SUCCESS_RESEND_EMAIL_VERIFICATION,
                        null, true)
                );
                semaphore.release();
              })
              .addOnFailureListener(error -> {
                FirebaseFunctionsException exception = (FirebaseFunctionsException) error;
                  taskResult.set(new RHAPIResult(RHAPIResult.INTERNAL_ERROR_SERVER_ERROR, exception.getDetails(), false));
                semaphore.release();
              });
      semaphore.acquire();
    } catch (InterruptedException | JSONException e) {
      e.printStackTrace();
    }
    return taskResult.get();
  }

  @Override
  public RHAPIResult call(Object... args) throws Exception {
    Pair<String, Object> pair = (Pair<String, Object>) args[0];
    try {
      // USE REFLECTION TO INFER POINTER TO CLASS MEMBER
      Method method = this.getClass().getDeclaredMethod(pair.first, Object.class);
      method.setAccessible(true);
      return (RHAPIResult) method.invoke(this, pair.second);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public RHAPIResult then(Object... args) throws Exception {
    return null;
  }
}
