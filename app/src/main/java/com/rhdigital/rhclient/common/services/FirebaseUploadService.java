package com.rhdigital.rhclient.common.services;

import android.content.Context;
import android.net.Uri;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class FirebaseUploadService {

  private FirebaseStorage firebaseStorage;
  private FirebaseUploadService INSTANCE;

  private FirebaseUploadService() { }

  public FirebaseUploadService getInstance() {
    if (INSTANCE == null) {
      this.INSTANCE = new FirebaseUploadService();
    }
    return INSTANCE;
  }

  public void uploadProfileImage(File file, String id) {
    Uri uri = Uri.fromFile(file);
    firebaseStorage.getReference("profile_photos")
      .child(id);
    UploadTask uploadTask =
  }
}
