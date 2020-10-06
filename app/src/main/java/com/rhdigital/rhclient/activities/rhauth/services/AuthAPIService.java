package com.rhdigital.rhclient.activities.rhauth.services;

import android.util.Log;
import android.util.Pair;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.functions.FirebaseFunctions;
import com.google.gson.Gson;
import com.rhdigital.rhclient.database.services.CallableFunction;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;

public class AuthAPIService implements CallableFunction<Pair<String, Object>, Object> {

  public String verifyEmail(Object arg) {
    String email = (String) arg;
    Semaphore semaphore = new Semaphore(0);
    AtomicReference<String> result = new AtomicReference<>("");
    JSONObject data = new JSONObject();
    try {
      data.put("email", email);
      FirebaseFunctions.getInstance()
      .getHttpsCallable("fetchUserByEmail")
      .call(data)
      .addOnSuccessListener(response -> {
        Log.d("AUTHAPI", response.getData().toString());
        result.set("This email address already belongs to an account");
        semaphore.release();
      })
      .addOnFailureListener(error -> {
        result.set("VALID");
        semaphore.release();
      });
      semaphore.acquire();
    } catch (InterruptedException | JSONException e) {
      e.printStackTrace();
    }
    return result.get();
  }

  public Object signUpNewUser(Object arg) {
    HashMap<String, String> authFieldsMap = (HashMap<String, String>) arg;
    Gson gson = new Gson();
    String data = gson.toJson(authFieldsMap);
    Semaphore semaphore = new Semaphore(0);
    AtomicReference<Object> result = new AtomicReference<>(null);
    try {
      JSONObject signUpData = new JSONObject(data);
      FirebaseFunctions.getInstance()
        .getHttpsCallable("signUpNewUser")
        .call(signUpData)
        .addOnSuccessListener(response -> {
          Log.d("AUTHAPI", response.getData().toString());
          result.set(response.getData());
          semaphore.release();
        })
        .addOnFailureListener(error -> {
          Log.d("AUTHAPI", error.getMessage());
          result.set(error.getMessage());
          semaphore.release();
        });
      semaphore.acquire();
    } catch (InterruptedException | JSONException e) {
      e.printStackTrace();
    }
    return result.get();
  }

  @Override
  public Object call(Pair<String, Object> pair) throws Exception {
    try {
      // USE REFLECTION TO INFER POINTER TO CLASS MEMBER
      Method method = this.getClass().getDeclaredMethod(pair.first, Object.class);
      method.setAccessible(true);
      return method.invoke(this, pair.second);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    };
    return null;
  }

  @Override
  public String then(Pair<String, Object>... args) throws Exception {
    return null;
  }
}
