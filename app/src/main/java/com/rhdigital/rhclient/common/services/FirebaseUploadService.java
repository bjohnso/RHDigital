package com.rhdigital.rhclient.common.services;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class FirebaseUploadService {

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
    });
  }
}
