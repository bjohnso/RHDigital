package com.rhdigital.rhclient.util;

import android.content.Context;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.net.URI;
import java.util.HashMap;

public class RemoteImageConnector {

  private static RemoteImageConnector instance = null;
  private HashMap<String, StorageReference> urlMap = new HashMap<>();
  private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

  private RemoteImageConnector() {
    StorageReference root = firebaseStorage.getReference();
    urlMap.put("l", root.child("courses/posters/drawable-ldpi"));
    urlMap.put("m", root.child("courses/posters/drawable-mdpi"));
    urlMap.put("h", root.child("courses/posters/drawable-hdpi"));
    urlMap.put("x", root.child("courses/posters/drawable-xhdpi"));
    urlMap.put("xx", root.child("courses/posters/drawable-xxhdpi"));
    urlMap.put("xxx", root.child("courses/posters/drawable-xxxhdpi"));
  }

  public static RemoteImageConnector getInstance() {
    if (instance == null) {
      instance = new RemoteImageConnector();
    }
    return instance;
  }

  public StorageReference getResourceURL(Context context, String endpoint) {
    float density = context.getResources().getDisplayMetrics().density;
    StorageReference ref = null;

    if (density <= 0.75) {
      ref = urlMap.get("l");
    } else if (density <= 1.0) {
      ref = urlMap.get("m");
    } else if (density <= 1.5) {
      ref = urlMap.get("h");
    } else if (density <= 2.0) {
      ref = urlMap.get("x");
    } else if (density <= 3.0) {
      ref = urlMap.get("xx");
    } else if (density <= 4.0) {
      ref = urlMap.get("xxx");
    } else {
      ref = urlMap.get("h");
    }
    return ref = ref.child(endpoint + ".png");
  }
}
