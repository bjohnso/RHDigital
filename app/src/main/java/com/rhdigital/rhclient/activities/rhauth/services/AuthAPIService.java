package com.rhdigital.rhclient.activities.rhauth.services;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.functions.FirebaseFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class AuthAPIService {

  MutableLiveData validationResult = new MutableLiveData();

  public MutableLiveData<String> validateEmail(String email) {
    JSONObject data = new JSONObject();
    try {
      data.put("email", email);
      FirebaseFunctions.getInstance()
      .getHttpsCallable("fetchUserByEmail")
      .call(data)
      .addOnSuccessListener(result -> {
        Log.d("AUTHAPISERVICE", result.getData().toString());
        validationResult.postValue("This email address already belongs to an account");
      })
      .addOnFailureListener(error -> {
        validationResult.postValue("VALID");
      });
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return validationResult;
  }

}
