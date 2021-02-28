package com.rhdigital.rhclient.activities.rhapp.viewmodel;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.RHApplication;
import com.rhdigital.rhclient.common.dto.PopulateRoomDto;
import com.rhdigital.rhclient.common.dto.RemoteResourceDto;
import com.rhdigital.rhclient.common.dto.UserFieldDto;
import com.rhdigital.rhclient.common.services.RemoteResourceService;
import com.rhdigital.rhclient.room.RHDatabase;
import com.rhdigital.rhclient.room.model.User;
import com.rhdigital.rhclient.room.repository.RHRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ProfileViewModel extends AndroidViewModel {

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private RHRepository rhRepository;
    public MutableLiveData<User> user;
    public LinkedHashMap<String, UserFieldDto> userFieldMap = new LinkedHashMap<>();
    public Boolean hasChanges = false;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        rhRepository = new RHRepository(application);
        user = new MutableLiveData<>();
    }

    public void init() {
        ViewModelStoreOwner viewModelStoreOwner =
                (ViewModelStoreOwner) ((RHApplication)getApplication()).getCurrentActivity();
        RHAppViewModel rhAppViewModel = new ViewModelProvider(viewModelStoreOwner).get(RHAppViewModel.class);
        rhAppViewModel.isFullscreenMode.setValue(false);
        rhAppViewModel.isEnrollState.setValue(false);
        rhAppViewModel.isActionButtonActive.setValue(false);
        rhAppViewModel.isBackButtonActive.setValue(false);
        rhAppViewModel.isTitleCenter.setValue(false);
        rhAppViewModel.title.setValue(getApplication().getString(R.string.title_profile));
    }

    public void initEdit() {
        ViewModelStoreOwner viewModelStoreOwner =
                (ViewModelStoreOwner) ((RHApplication)getApplication()).getCurrentActivity();
        RHAppViewModel rhAppViewModel = new ViewModelProvider(viewModelStoreOwner).get(RHAppViewModel.class);
        rhAppViewModel.isFullscreenMode.setValue(false);
        rhAppViewModel.isEnrollState.setValue(false);
        rhAppViewModel.isActionButtonActive.setValue(true);
        rhAppViewModel.isBackButtonActive.setValue(true);
        rhAppViewModel.isTitleCenter.setValue(false);
        rhAppViewModel.title.setValue(getApplication().getString(R.string.title_edit_profile));
    }

    public LiveData<User> getUser(String userId) {
        return rhRepository.getAuthenticatedUser(userId);
    }

    public LiveData<HashMap<String, Bitmap>> getProfilePhoto(Context context, String userId, int width, int height) {
        ArrayList<RemoteResourceDto> list = new ArrayList<>();
        list.add(
                new RemoteResourceDto(userId, userId, RemoteResourceDto.PROFILE_PHOTO)
        );
        return new RemoteResourceService().getAllBitmap(context,
                list,
                width,
                height
        );
    }

    public void initUserFieldMap(User user) {
        userFieldMap.put(
                "First Name", new UserFieldDto("First Name", user.getName(), UserFieldDto.BASIC)
        );
        userFieldMap.put(
                "Last Name", new UserFieldDto("Last Name", user.getSurname(), UserFieldDto.BASIC)
        );
        userFieldMap.put(
                "Title", new UserFieldDto("Title", user.getTitle(), UserFieldDto.BASIC)
        );
        userFieldMap.put(
                "City", new UserFieldDto("City", user.getCity(), UserFieldDto.BASIC)
        );
        userFieldMap.put(
                "Country", new UserFieldDto("Country", user.getCountry(), UserFieldDto.COUNTRY)
        );
        userFieldMap.put(
                "About", new UserFieldDto("About", user.getAbout(), UserFieldDto.BASIC)
        );
        userFieldMap.put(
                "Industry", new UserFieldDto("Industry", user.getIndustry(), UserFieldDto.BASIC)
        );
    }

    public void updateUserFieldMap(UserFieldDto userField) {
        UserFieldDto current = userFieldMap.get(userField.getField());
        if (current == null ||
                current.getValue() == null ||
                !current.getValue().equals(userField.getValue())
        ) {
            this.userFieldMap.put(userField.getField(), userField);
            this.hasChanges = true;
        }
    }

    public void updateUser() {
        User user = this.user.getValue();
        if (user != null) {
            user.setName(userFieldMap.get("First Name").getValue());
            user.setSurname(userFieldMap.get("Last Name").getValue());
            user.setTitle(userFieldMap.get("Title").getValue());
            user.setCity(userFieldMap.get("City").getValue());
            user.setCountry(userFieldMap.get("Country").getValue());
            user.setAbout(userFieldMap.get("About").getValue());
            user.setIndustry(userFieldMap.get("Industry").getValue());
            this.hasChanges = false;
        }
        this.user.setValue(user);
    }

    public Task<Void> updateRemoteUser() {
        updateUser();
        User user = this.user.getValue();
        if (user != null) {
            return firebaseFirestore.collection("users")
                    .document(user.getId())
                    .set(user, SetOptions.merge());
        }
        return null;
    }

    public LiveData<ArrayList<Long>> fetchRemoteUser() {
        User current = this.user.getValue();
        if (current != null) {
            return rhRepository.syncWithRemote(
                    RHDatabase.getDatabase(
                          getApplication().getApplicationContext()
                    ),
                    new PopulateRoomDto(PopulateRoomDto.USER)
            );
        }
        return null;
    }
}
