package com.rhdigital.rhclient.common.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rhdigital.rhclient.database.http.ApiClient;
import com.rhdigital.rhclient.database.http.ApiInterface;
import com.rhdigital.rhclient.database.model.Program;
import com.rhdigital.rhclient.database.model.embedded.CourseWithWorkbooks;
import com.rhdigital.rhclient.database.model.Workbook;
;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemoteResourceService {

  private HashMap<String, StorageReference> urlMap = new HashMap<>();
  private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
  private MutableLiveData<HashMap<String, Bitmap>> liveProgramPosterMap;
  private MutableLiveData<HashMap<String, Uri>> liveVideoUriMap;
  private MutableLiveData<HashMap<String, Uri>> liveDocumentMap;
  private MutableLiveData<HashMap<String, HashMap<String, Uri>>> liveWorkbookMap;
  private MutableLiveData<Bitmap> liveProfilePhotoUri;
  private HashMap<String, Bitmap> liveProgramPosterMapSurrogate = new HashMap<>();
  private HashMap<String, Uri> liveVideoUriMapSurrogate = new HashMap<>();
  private HashMap<String, Uri> liveDocumentMapSurrogate = new HashMap<>();
  private HashMap<String, HashMap<String, Uri>> liveWorkbookMapSurrogate = new HashMap<>();

  private MutableLiveData<ResponseBody> liveBinaryDownload;

  private ApiInterface apiInterface;

  public RemoteResourceService() {
    StorageReference root = firebaseStorage.getReference();
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
      return ref.child(endpoint.substring(1));
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

  private StorageReference getWorkbookResourceURL(String endpoint) {
    return urlMap.get("workbook").child(endpoint);
  }

  public LiveData<HashMap<String, HashMap<String, Uri>>> getAllWorkbookURI(List<CourseWithWorkbooks> workbooks) {
    if (liveWorkbookMap == null) {
      liveWorkbookMap = new MutableLiveData<>();
    }
    loadWorkbookUri(workbooks);
    return liveWorkbookMap;
  }

  public LiveData<HashMap<String, Bitmap>> getAllBitmap(Context context, Object list, int width, int height) {
    if (liveProgramPosterMap == null) {
      liveProgramPosterMap = new MutableLiveData<>();
    }
    loadProgramPosterUri(context, (List<Program>) list, width, height);
    return liveProgramPosterMap;
  }

//  public LiveData<Bitmap> getProfilePhoto(Context context, String id, int width, int height) {
//    if (liveProfilePhotoUri == null) {
//      liveProfilePhotoUri = new MutableLiveData<>();
//    }
//    loadProfileImageUri(context, id, width, height);
//    return liveProfilePhotoUri;
//  }
//
//  public LiveData<HashMap<String, Uri>> getAllVideoURI(List<Course> courses, int width, int height) {
//    if (liveVideoUriMap == null) {
//      liveVideoUriMap = new MutableLiveData<>();
//    }
//    loadVideoUri(courses, width, height);
//    return liveVideoUriMap;
//  }
//
//  public LiveData<HashMap<String, Uri>> getAllDocumentURI(String... docIds) {
//   if (liveDocumentMap == null) {
//     liveDocumentMap = new MutableLiveData<>();
//   }
//    loadDocumentUri(docIds);
//   return liveDocumentMap;
//  }

  private void loadWorkbookUri(List<CourseWithWorkbooks> workbooks) {
    for (CourseWithWorkbooks courseWithWorkbooks: workbooks) {
      List<Workbook> bookList = courseWithWorkbooks.getWorkbooks();
      for (Workbook workbook: bookList) {
        getWorkbookResourceURL(workbook.getUrl())
        .getDownloadUrl()
        .addOnFailureListener(error -> {
          Log.d("REMOTE", "URI FETCH FAILED");
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

  private void loadProfilePosterUri(Context context, String id, int width, int height) {
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

//  private void loadVideoUri(List<Video> videos, int width, int height) {
//    for (Video video: videos) {
//      getVideoResourceURL(video.getUrl())
//        .getDownloadUrl()
//        .addOnFailureListener(error -> {
//          liveVideoUriMapSurrogate = null;
//          liveVideoUriMap.setValue(null);
//        })
//        .addOnSuccessListener(uri -> {
//        liveVideoUriMapSurrogate.put(video.getId(), uri);
//        liveVideoUriMap.setValue(liveVideoUriMapSurrogate);
//        Log.d("REMOTE", "URI : " + uri);
//      });
//    }
//  }

  private void loadProgramPosterUri(Context context, List<Program> programs, int width, int height) {
    for (Program program : programs) {
      getImageResourceURL(
          context,
          program.getPosterUrl())
        .getDownloadUrl()
        .addOnFailureListener(error -> {
          liveProgramPosterMapSurrogate = null;
          liveProgramPosterMap.setValue(null);
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
                liveProgramPosterMapSurrogate.put(program.getId(), resource);
                liveProgramPosterMap.setValue(liveProgramPosterMapSurrogate);
              }
            });
        });
    }
  }

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
}
