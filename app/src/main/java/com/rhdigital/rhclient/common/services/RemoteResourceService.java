package com.rhdigital.rhclient.common.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rhdigital.rhclient.database.model.Course;
import com.rhdigital.rhclient.database.model.CourseWithWorkbooks;

import java.util.HashMap;
import java.util.List;

public class RemoteResourceService {

  private HashMap<String, StorageReference> urlMap = new HashMap<>();
  private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
  private MutableLiveData<HashMap<String, Bitmap>> liveImageMap;
  private MutableLiveData<HashMap<String, Uri>> liveVideoUriMap;
  private MutableLiveData<HashMap<String, Uri>> liveDocumentMap;
  private MutableLiveData<Bitmap> liveProfilePhotoUri;
  private HashMap<String, Bitmap> liveImageMapSurrogate = new HashMap<>();
  private HashMap<String, Uri> liveVideoUriMapSurrogate = new HashMap<>();
  private HashMap<String, Uri> liveDocumentMapSurrogate = new HashMap<>();

  public RemoteResourceService() {
    StorageReference root = firebaseStorage.getReference();
    urlMap.put("l", root.child("courses/posters/drawable-ldpi"));
    urlMap.put("m", root.child("courses/posters/drawable-mdpi"));
    urlMap.put("h", root.child("courses/posters/drawable-hdpi"));
    urlMap.put("x", root.child("courses/posters/drawable-xhdpi"));
    urlMap.put("xx", root.child("courses/posters/drawable-xxhdpi"));
    urlMap.put("xxx", root.child("courses/posters/drawable-xxxhdpi"));
    urlMap.put("video", root.child("courses/videos"));
    urlMap.put("doc", root.child("documents"));
    urlMap.put("profile_photo", root.child("profile_photos"));
  }

  private StorageReference getImageResourceURL(Context context, String endpoint) {
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
      return ref.child(endpoint);
  }

  private StorageReference getProfileImageResourceURL(String id) {
    return urlMap.get("profile_photo").child(id);
  }

  private StorageReference getVideoResourceURL(String endpoint) {
    //TODO add screen density to check to determine best resolution to download
    return urlMap.get("video").child(endpoint);
  }

  private StorageReference getDocumentResourceURL(String endpoint) {
    return urlMap.get("doc").child(endpoint);
  }

  public LiveData<HashMap<String, Bitmap>> getAllBitmap(Context context, Object list, int width, int height, boolean isVideo) {
    if (liveImageMap == null) {
      liveImageMap = new MutableLiveData<>();
    }
    if (isVideo)
      loadVideoImageUri(context, (List<Course>) list, width, height);
    else
      loadWorkbookImageUri(context, (List<CourseWithWorkbooks>) list, width, height);
    return liveImageMap;
  }

  public LiveData<Bitmap> getProfilePhoto(Context context, String id, int width, int height) {
    if (liveProfilePhotoUri == null) {
      liveProfilePhotoUri = new MutableLiveData<>();
    }
    loadProfileImageUri(context, id, width, height);
    return liveProfilePhotoUri;
  }

  public LiveData<HashMap<String, Uri>> getAllVideoURI(List<Course> courses, int width, int height) {
    if (liveVideoUriMap == null) {
      liveVideoUriMap = new MutableLiveData<>();
    }
    loadVideoUri(courses, width, height);
    return liveVideoUriMap;
  }

  public LiveData<HashMap<String, Uri>> getAllDocumentURI(String... docIds) {
   if (liveDocumentMap == null) {
     liveDocumentMap = new MutableLiveData<>();
   }
    loadDocumentUri(docIds);
   return liveDocumentMap;
  }

  private void loadProfileImageUri(Context context, String id, int width, int height) {
    getProfileImageResourceURL(id)
      .getDownloadUrl()
      .addOnFailureListener(error -> {
        liveProfilePhotoUri.setValue(null);
      })
      .addOnSuccessListener(uri -> {
        //Preload Image into Disk Cache
        Glide.with(context)
          .load(uri)
          .asBitmap()
          .diskCacheStrategy(DiskCacheStrategy.ALL)
          .skipMemoryCache(false)
          .into(new SimpleTarget<Bitmap>(width, height) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
              //Populate LiveData
              liveProfilePhotoUri.setValue(resource);
            }
      });
    });
  }

  private void loadDocumentUri(String... documentIds) {
    for (String id: documentIds) {
      getDocumentResourceURL(id)
        .getDownloadUrl()
        .addOnFailureListener(error -> {
          liveDocumentMapSurrogate = null;
          liveDocumentMap.setValue(null);
        })
        .addOnSuccessListener(uri -> {
          //TODO: IMPLEMENT A CLEANER TRUNCATING STRATEGY
          Log.d("REMOTE", "new doc Id : " + id);
          String identifier = id.split("\\.")[0];
          liveDocumentMapSurrogate.put(identifier, uri);
          liveDocumentMap.setValue(liveDocumentMapSurrogate);
        });
    }
  }

  private void loadVideoUri(List<Course> courses, int width, int height) {
    for (Course c : courses) {
      getVideoResourceURL(c.getVideoURL())
        .getDownloadUrl()
        .addOnFailureListener(error -> {
          liveVideoUriMapSurrogate = null;
          liveVideoUriMap.setValue(null);
        })
        .addOnSuccessListener(uri -> {
        liveVideoUriMapSurrogate.put(c.getId(), uri);
        liveVideoUriMap.setValue(liveVideoUriMapSurrogate);
        Log.d("REMOTE", "URI : " + uri);
      });
    }
  }

  private void loadVideoImageUri(Context context, List<Course> courses, int width, int height) {
    for (Course c : courses) {
      getImageResourceURL(
          context,
          c.getVideoPosterURL())
        .getDownloadUrl()
        .addOnFailureListener(error -> {
          liveImageMapSurrogate = null;
          liveImageMap.setValue(null);
        })
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
                liveImageMapSurrogate.put(c.getId(), resource);
                liveImageMap.setValue(liveImageMapSurrogate);
              }
            });
        });
    }
  }

  private void loadWorkbookImageUri(Context context, List<CourseWithWorkbooks> workbooks, int width, int height) {
    for (CourseWithWorkbooks w : workbooks) {
      getImageResourceURL(
        context,
        w.getCourse().getWorkbookPosterURL())
        .getDownloadUrl()
        .addOnFailureListener(error -> {
          liveImageMapSurrogate = null;
          liveImageMap.setValue(null);
        })
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
                liveImageMapSurrogate.put(w.getCourse().getId(), resource);
                liveImageMap.setValue(liveImageMapSurrogate);
              }
            });
        });
    }
  }
}
