package com.rhdigital.rhclient.activities.rhapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.RHApplication;
import com.rhdigital.rhclient.database.model.User;
import com.rhdigital.rhclient.database.repository.RHRepository;

public class ProfileViewModel extends AndroidViewModel {

    private RHRepository rhRepository;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        rhRepository = new RHRepository(application);
    }

    public void init() {
        ViewModelStoreOwner viewModelStoreOwner =
                (ViewModelStoreOwner) ((RHApplication)getApplication()).getCurrentActivity();
        RHAppViewModel rhAppViewModel = new ViewModelProvider(viewModelStoreOwner).get(RHAppViewModel.class);
        rhAppViewModel.isBackButtonActive.setValue(false);
        rhAppViewModel.isTitleCenter.setValue(false);
        rhAppViewModel.title.setValue(getApplication().getString(R.string.title_profile));
    }

    public LiveData<User> getUser(String userId) {
        return rhRepository.getAuthenticatedUser(userId);
    }
}
