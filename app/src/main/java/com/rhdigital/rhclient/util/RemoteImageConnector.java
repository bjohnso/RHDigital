package com.rhdigital.rhclient.util;

import android.content.Context;
import android.content.MutableContextWrapper;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rhdigital.rhclient.database.model.Course;

import java.io.File;
import java.net.URI;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class RemoteImageConnector {

  private HashMap<String, StorageReference> urlMap = new HashMap<>();
  private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
  private MutableLiveData<HashMap<String, Bitmap>> liveUriMap;
  HashMap<String, Bitmap> map = new HashMap<>();

  public RemoteImageConnector() {
    StorageReference root = firebaseStorage.getReference();
    urlMap.put("l", root.child("courses/posters/drawable-ldpi"));
    urlMap.put("m", root.child("courses/posters/drawable-mdpi"));
    urlMap.put("h", root.child("courses/posters/drawable-hdpi"));
    urlMap.put("x", root.child("courses/posters/drawable-xhdpi"));
    urlMap.put("xx", root.child("courses/posters/drawable-xxhdpi"));
    urlMap.put("xxx", root.child("courses/posters/drawable-xxxhdpi"));
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

  public LiveData<HashMap<String, Bitmap>> getAllURI(Context context, List<Course> courses, int width, int height) {
    if (liveUriMap == null) {
      liveUriMap = new MutableLiveData<>();
      loadUri(context, courses, width, height);
    }
    return liveUriMap;
  }

  private void loadUri(Context context, List<Course> courses, int width, int height) {

    for (Course c : courses) {
      getResourceURL(
          context,
          c.getThumbnailURL())
        .getDownloadUrl()
        .addOnSuccessListener(uri -> {
          //Preload Images into Disk Cache
          Glide.with(context)
            .load(uri)
            .asBitmap()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .skipMemoryCache(false)
            .into(new SimpleTarget<Bitmap>(width, height) {
              @Override
              public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                //Populate UriMapLiveData
                map.put(c.getId(), resource);
                liveUriMap.setValue(map);
              }
            });
        });
    }
  }
}
