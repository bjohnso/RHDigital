package com.rhdigital.rhclient.database.viewmodel;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.rhdigital.rhclient.database.model.User;
import com.rhdigital.rhclient.database.repository.RHRepository;

import java.util.BitSet;
import java.util.HashMap;

public class UserViewModel extends AndroidViewModel {
  private RHRepository rhRepository;

  public UserViewModel(@NonNull Application application) {
    super(application);
    rhRepository = new RHRepository(application);
  }

  public LiveData<User> getAuthenticatedUser(String id) {
    return rhRepository.getAuthenticatedUser(id);
  }

  public void updateUser(User user) {
    rhRepository.update(user);
  }

//  public LiveData<HashMap<String, Uri>> getAllDocumentUri(String... docIds) {
//    return new RemoteResourceService().getAllDocumentURI(docIds);
//  }
//
//  public LiveData<Bitmap> getProfilePhoto(Context context, String id, int width, int height) {
//    return new RemoteResourceService().getProfilePhoto(context, id, width, height);
//  }
}
