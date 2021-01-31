package com.rhdigital.rhclient.room.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.rhdigital.rhclient.room.model.User;
import com.rhdigital.rhclient.room.repository.RHRepository;

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
