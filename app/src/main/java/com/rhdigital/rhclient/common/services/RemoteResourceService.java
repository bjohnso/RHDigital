package com.rhdigital.rhclient.common.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;

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
  private MutableLiveData<HashMap<String, Uri>> livePDFMap;
  private MutableLiveData<Bitmap> liveProfilePhotoUri;
  private HashMap<String, Bitmap> liveImageMapSurrogate = new HashMap<>();
  private HashMap<String, Uri> liveVideoUriMapSurrogate = new HashMap<>();
  private HashMap<String, Uri> liveDocumentMapSurrogate = new HashMap<>();
  private HashMap<String, Uri> livePDFMapSurrogate = new HashMap<>();

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
    urlMap.put("share_research_reports", root.child("share_research_reports"));
    urlMap.put("doc", root.child("documents"));
    urlMap.put("profile_photo", root.child("profile_photos"));
  }

  // IMAGES

  public LiveData<HashMap<String, Bitmap>> getAllBitmap(Context context, List<RemoteResourceDto> data, int width, int height) {
    if (liveImageMap == null) {
      liveImageMap = new MutableLiveData<>();
    }

    for (RemoteResourceDto item : data) {
      StorageReference reference;
      if (item.getType() == RemoteResourceDto.PROGRAM_POSTER) {
        reference =
                getImageStorageReference(
                        context,
                        item.getResourceUrl()
                );
      } else if (item.getType() == RemoteResourceDto.PROFILE_PHOTO) {
        reference =
                getProfileImageStorageReference(
                        item.getResourceUrl()
                );
      } else {
        break;
      }

      reference
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

  // VIDEO

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
              });
    }
    return liveVideoUriMap;
  }

  // WORKBOOKS

  public LiveData<HashMap<String, Uri>> getAllPDFURI(List<RemoteResourceDto> data) {
    if (livePDFMap == null) {
      livePDFMap = new MutableLiveData<>();
    }
    for (RemoteResourceDto item: data) {
      StorageReference reference;
      if (item.getType() == RemoteResourceDto.WORKBOOK_URI) {
        reference = getWorkbookStorageReference(item.getResourceUrl());
      } else if (item.getType() == RemoteResourceDto.REPORT_URI) {
        reference = getReportStorageReference(item.getResourceUrl());
      } else {
        break;
      }

      reference
              .getDownloadUrl()
              .addOnFailureListener(error -> {
                livePDFMapSurrogate = null;
                livePDFMap.setValue(null);
              })
              .addOnSuccessListener(uri -> {
                livePDFMapSurrogate.put(item.getResourceId(), uri);
                livePDFMap.setValue(livePDFMapSurrogate);
              });
    }
    return livePDFMap;
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

  // DOCUMENTS

  public LiveData<HashMap<String, Uri>> getAllDocumentURI(String... docIds) {
    if (liveDocumentMap == null) {
      liveDocumentMap = new MutableLiveData<>();
    }
    for (String id: docIds) {
      getDocumentStorageReference(id)
              .getDownloadUrl()
              .addOnFailureListener(error -> {
                liveDocumentMapSurrogate = null;
                liveDocumentMap.setValue(null);
              })
              .addOnSuccessListener(uri -> {
                //TODO: IMPLEMENT A CLEANER TRUNCATING STRATEGY
                String identifier = id.split("\\.")[0];
                liveDocumentMapSurrogate.put(identifier, uri);
                liveDocumentMap.setValue(liveDocumentMapSurrogate);
              });
    }
    return liveDocumentMap;
  }

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

  private StorageReference getProfileImageStorageReference(String endpoint) {
    return urlMap.get("profile_photo").child(endpoint);
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

  private StorageReference getReportStorageReference(String endpoint) {
    return urlMap.get("share_research_reports").child(endpoint);
  }
}
