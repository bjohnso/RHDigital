package com.rhdigital.rhclient.common.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rhdigital.rhclient.common.dto.RemoteResourceDto;
import com.rhdigital.rhclient.room.http.ApiClient;
import com.rhdigital.rhclient.room.http.ApiInterface;
import com.rhdigital.rhclient.room.model.embedded.CourseWithWorkbooks;
import com.rhdigital.rhclient.room.model.Workbook;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemoteResourceService {

  private HashMap<String, StorageReference> urlMap = new HashMap<>();
  private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
  private MutableLiveData<HashMap<String, Bitmap>> liveImageMap;
  private MutableLiveData<HashMap<String, Uri>> liveVideoUriMap;
  private MutableLiveData<HashMap<String, Uri>> liveDocumentMap;
  private MutableLiveData<HashMap<String, HashMap<String, Uri>>> liveWorkbookMap;
  private MutableLiveData<Bitmap> liveProfilePhotoUri;
  private HashMap<String, Bitmap> liveImageMapSurrogate = new HashMap<>();
  private HashMap<String, Uri> liveVideoUriMapSurrogate = new HashMap<>();
  private HashMap<String, Uri> liveDocumentMapSurrogate = new HashMap<>();
  private HashMap<String, HashMap<String, Uri>> liveWorkbookMapSurrogate = new HashMap<>();

  private MutableLiveData<ResponseBody> liveBinaryDownload;

  private ApiInterface apiInterface;

  public RemoteResourceService() {
    StorageReference root = firebaseStorage.getReference();
    // TODO: STORE IN SHARED PREFS
    urlMap.put("l", root.child("programs/posters/drawable-ldpi"));
    urlMap.put("m", root.child("programs/posters/drawable-mdpi"));
    urlMap.put("h", root.child("programs/posters/drawable-hdpi"));
    urlMap.put("x", root.child("programs/posters/drawable-xhdpi"));
    urlMap.put("xx", root.child("programs/posters/drawable-xxhdpi"));
    urlMap.put("xxx", root.child("programs/posters/drawable-xxxhdpi"));
    urlMap.put("video", root.child("programs/videos"));
    urlMap.put("workbook", root.child("programs/workbooks"));
    urlMap.put("doc", root.child("documents"));
    urlMap.put("profile_photo", root.child("profile_photos"));
  }

  public LiveData<HashMap<String, Bitmap>> getAllBitmap(Context context, List<RemoteResourceDto> data, int width, int height) {
    if (liveImageMap == null) {
      liveImageMap = new MutableLiveData<>();
    }
    for (RemoteResourceDto item : data) {
      getImageStorageReference(
              context,
              item.getResourceUrl())
              .getDownloadUrl()
              .addOnFailureListener(error -> {
                liveImageMapSurrogate = null;
                liveImageMap.setValue(null);
              })
              .addOnSuccessListener(uri -> {
                //Preload Images into Disk Cache
                Glide.with(context)
                        .asBitmap()
                        .load(uri)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .skipMemoryCache(false)
                        .into(new CustomTarget<Bitmap>(width, height) {
                          @Override
                          public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            //Populate UriMapLiveData
                            liveImageMapSurrogate.put(item.getResourceId(), resource);
                            liveImageMap.setValue(liveImageMapSurrogate);
                          }

                          @Override
                          public void onLoadCleared(@Nullable Drawable placeholder) {

                          }
                        });
              });
    }
    return liveImageMap;
  }

  public LiveData<HashMap<String, Uri>> getAllVideoURI(List<RemoteResourceDto> data, int width, int height) {
    if (liveVideoUriMap == null) {
      liveVideoUriMap = new MutableLiveData<>();
    }
    for (RemoteResourceDto item: data) {
      getVideoStorageReference(item.getResourceUrl())
              .getDownloadUrl()
              .addOnFailureListener(error -> {
                liveVideoUriMapSurrogate = null;
                liveVideoUriMap.setValue(null);
              })
              .addOnSuccessListener(uri -> {
                liveVideoUriMapSurrogate.put(item.getResourceId(), uri);
                liveVideoUriMap.setValue(liveVideoUriMapSurrogate);
                Log.d("REMOTE", "URI : " + uri);
              });
    }
    return liveVideoUriMap;
  }

  public LiveData<HashMap<String, HashMap<String, Uri>>> getAllWorkbookURI(List<CourseWithWorkbooks> workbooks) {
    if (liveWorkbookMap == null) {
      liveWorkbookMap = new MutableLiveData<>();
    }
    loadWorkbookUri(workbooks);
    return liveWorkbookMap;
  }

  public LiveData<ResponseBody> downloadWorkbook(String downloadURL) {
    if (apiInterface == null) {
      apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    }
    if (liveBinaryDownload == null) {
      liveBinaryDownload = new MutableLiveData<>();
    }
    apiInterface.getBinaryData(downloadURL).enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        liveBinaryDownload.setValue(response.body());
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
        liveBinaryDownload.setValue(null);
      }
    });
    return liveBinaryDownload;
  }

  public LiveData<HashMap<String, Uri>> getAllDocumentURI(String... docIds) {
    if (liveDocumentMap == null) {
      liveDocumentMap = new MutableLiveData<>();
    }
    loadDocumentUri(docIds);
    return liveDocumentMap;
  }

  public LiveData<Bitmap> getProfilePhoto(Context context, String id, int width, int height) {
    if (liveProfilePhotoUri == null) {
      liveProfilePhotoUri = new MutableLiveData<>();
    }
    loadProfileImageUri(context, id, width, height);
    return liveProfilePhotoUri;
  }


  private void loadWorkbookUri(List<CourseWithWorkbooks> workbooks) {
    for (CourseWithWorkbooks courseWithWorkbooks: workbooks) {
      List<Workbook> bookList = courseWithWorkbooks.getWorkbooks();
      for (Workbook workbook: bookList) {
        getWorkbookStorageReference(workbook.getUrl())
        .getDownloadUrl()
        .addOnFailureListener(error -> {
          liveWorkbookMapSurrogate = null;
          liveWorkbookMap.setValue(null);
        })
        .addOnSuccessListener(uri -> {
          if (liveWorkbookMapSurrogate.get(courseWithWorkbooks.getCourse().getId()) == null) {
            liveWorkbookMapSurrogate.put(courseWithWorkbooks.getCourse().getId(), new HashMap<>());
          }
          liveWorkbookMapSurrogate.get(courseWithWorkbooks.getCourse().getId()).put(workbook.getId(), uri);
          liveWorkbookMap.setValue(liveWorkbookMapSurrogate);
        });
      }
    }
  }

  private void loadProfileImageUri(Context context, String id, int width, int height) {
    getProfileImageStorageReference(id)
      .getDownloadUrl()
      .addOnFailureListener(error -> {
        liveProfilePhotoUri.setValue(null);
      })
      .addOnSuccessListener(uri -> {
        //Preload Image into Disk Cache
        Glide.with(context)
                .asBitmap()
                .load(uri)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(false)
                .into(new CustomTarget<Bitmap>(width, height) {
                  @Override
                  public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    //Populate LiveData
                    liveProfilePhotoUri.setValue(resource);
                  }

                  @Override
                  public void onLoadCleared(@Nullable Drawable placeholder) {

                  }
      });
    });
  }

  private void loadDocumentUri(String... documentIds) {
    for (String id: documentIds) {
      getDocumentStorageReference(id)
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

//  private void loadProgramPosterUri(Context context, List<Program> programs, int width, int height) {
//    for (Program program : programs) {
//      getImageResourceURL(
//              context,
//              program.getPosterUrl())
//              .getDownloadUrl()
//              .addOnFailureListener(error -> {
//                liveProgramPosterMapSurrogate = null;
//                liveProgramPosterMap.setValue(null);
//              })
//              .addOnSuccessListener(uri -> {
//                //Preload Images into Disk Cache
//                Glide.with(context)
//                        .load(uri)
//                        .asBitmap()
//                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .skipMemoryCache(false)
//                        .into(new SimpleTarget<Bitmap>(width, height) {
//                          @Override
//                          public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                            //Populate UriMapLiveData
//                            liveProgramPosterMapSurrogate.put(program.getId(), resource);
//                            liveProgramPosterMap.setValue(liveProgramPosterMapSurrogate);
//                          }
//                        });
//              });
//    }
//  }

//  private void loadWorkbookImageUri(Context context, List<CourseWithWorkbooks> workbooks, int width, int height) {
//    for (CourseWithWorkbooks w : workbooks) {
//      getImageResourceURL(
//        context,
//        w.getCourse().getWorkbookPosterURL())
//        .getDownloadUrl()
//        .addOnFailureListener(error -> {
//          liveImageMapSurrogate = null;
//          liveImageMap.setValue(null);
//        })
//        .addOnSuccessListener(uri -> {
//          //Preload Images into Disk Cache
//          Glide.with(context)
//            .load(uri)
//            .asBitmap()
//            .diskCacheStrategy(DiskCacheStrategy.ALL)
//            .skipMemoryCache(false)
//            .into(new SimpleTarget<Bitmap>(width, height) {
//              @Override
//              public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                //Populate UriMapLiveData
//                liveImageMapSurrogate.put(w.getCourse().getId(), resource);
//                liveImageMap.setValue(liveImageMapSurrogate);
//              }
//            });
//        });
//    }
//  }

  private StorageReference getImageStorageReference(Context context, String endpoint) {
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
    return ref.child(endpoint.substring(1));
  }

  private StorageReference getProfileImageStorageReference(String id) {
    return urlMap.get("profile_photo").child(id);
  }

  private StorageReference getVideoStorageReference(String endpoint) {
    //TODO add screen density to check to determine best resolution to download
    return urlMap.get("video").child(endpoint);
  }

  private StorageReference getDocumentStorageReference(String endpoint) {
    return urlMap.get("doc").child(endpoint);
  }

  private StorageReference getWorkbookStorageReference(String endpoint) {
    return urlMap.get("workbook").child(endpoint);
  }
}
