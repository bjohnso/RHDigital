package com.rhdigital.rhclient.activities.rhapp.viewmodel;

import android.app.Application;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.rhdigital.rhclient.RHApplication;
import com.rhdigital.rhclient.common.dto.PopulateRoomDto;
import com.rhdigital.rhclient.room.RHDatabase;
import com.rhdigital.rhclient.room.model.AuthorisedProgram;
import com.rhdigital.rhclient.room.model.AuthorisedReport;
import com.rhdigital.rhclient.room.model.User;
import com.rhdigital.rhclient.room.repository.RHRepository;

import java.util.List;

public class RHAppViewModel extends AndroidViewModel {

    public MutableLiveData<String> title = new MutableLiveData("");
    public MutableLiveData<Boolean> isTitleCenter = new MutableLiveData(false);
    public MutableLiveData<Boolean> isBackButtonActive = new MutableLiveData(false);
    public MutableLiveData<Boolean> isActionButtonActive = new MutableLiveData(false);
    public MutableLiveData<Boolean> isEnrollState = new MutableLiveData(false);
    public MutableLiveData<Boolean> isFullscreenMode = new MutableLiveData(false);

    private RHRepository rhRepository;

    public RHAppViewModel(@NonNull Application application) {
        super(application);
        this.rhRepository = new RHRepository(application);
    }

    public LiveData<List<AuthorisedProgram>> authorisePrograms() {
        MutableLiveData<List<AuthorisedProgram>> authorisedPrograms = new MutableLiveData();
        LifecycleOwner lifecycleOwner = (LifecycleOwner) ((RHApplication)getApplication()).getCurrentActivity();
        rhRepository.getAuthorisedPrograms().observe(
                lifecycleOwner, authorised -> {
                    if (authorised != null) {
                        rhRepository.authorisePrograms(authorised);
                        authorisedPrograms.postValue(authorised);
                    }
                });
        return authorisedPrograms;
    }

    public LiveData<List<AuthorisedReport>> authoriseReports() {
        MutableLiveData<List<AuthorisedReport>> authorisedReports = new MutableLiveData();
        LifecycleOwner lifecycleOwner = (LifecycleOwner) ((RHApplication)getApplication()).getCurrentActivity();
        rhRepository.getAuthorisedReports().observe(
                lifecycleOwner, authorised -> {
                    if (authorised != null) {
                        rhRepository.authoriseReports(authorised);
                        authorisedReports.postValue(authorised);
                    }
                });
        return authorisedReports;
    }

    public LiveData<User> fetchRemoteUser(String userId) {
        MutableLiveData<User> user = new MutableLiveData();
        LifecycleOwner lifecycleOwner = (LifecycleOwner) ((RHApplication)getApplication()).getCurrentActivity();
        rhRepository.syncWithRemote(
                RHDatabase.getDatabase(getApplication()),
                new PopulateRoomDto(PopulateRoomDto.USER)
        ).observe(lifecycleOwner, inserts -> rhRepository.getAuthenticatedUser(
                userId
        ).observe(
                lifecycleOwner, u -> user.postValue(u)
        ));
        return user;
    }

    @BindingAdapter("app:center")
    public static void setCenter(View view, Boolean isCenter) {
        Toolbar.LayoutParams params =
                new Toolbar.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = isCenter ? Gravity.CENTER : Gravity.START;
        view.setLayoutParams(params);
    }
}
