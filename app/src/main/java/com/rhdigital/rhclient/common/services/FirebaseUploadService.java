package com.rhdigital.rhclient.common.services;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class FirebaseUploadService {

  private MutableLiveData<Boolean> liveUploaded;
  private FirebaseStorage firebaseStorage;
  private static FirebaseUploadService INSTANCE;

  private FirebaseUploadService() {
    firebaseStorage = FirebaseStorage.getInstance();
  }

  public static FirebaseUploadService getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new FirebaseUploadService();
    }
    return INSTANCE;
  }

  public void uploadProfileImage(Uri uri, String id) {
    StorageReference ref = firebaseStorage.getReference("profile_photos")
      .child(id);
    UploadTask uploadTask = ref.putFile(uri);

    uploadTask.addOnFailureListener(e -> {
      e.printStackTrace();
    }).addOnSuccessListener(taskSnapshot -> {
      Log.d("UPLOAD", taskSnapshot.getMetadata().getName());
      if (liveUploaded == null) {
        liveUploaded = new MutableLiveData<>();
      }
      liveUploaded.setValue(true);
    });
  }

  public MutableLiveData<Boolean> getLiveUploaded() {
    if (liveUploaded == null) {
      liveUploaded = new MutableLiveData<>();
    }
    return liveUploaded;
  }
}
