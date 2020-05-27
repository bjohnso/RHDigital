package com.rhdigital.rhclient.database.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.rhdigital.rhclient.database.model.User;
import com.rhdigital.rhclient.database.repository.RHRepository;

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
}
