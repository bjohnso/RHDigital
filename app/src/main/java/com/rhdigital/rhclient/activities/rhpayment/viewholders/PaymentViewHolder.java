package com.rhdigital.rhclient.activities.rhpayment.viewholders;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.rhdigital.rhclient.RHApplication;
import com.rhdigital.rhclient.common.dto.PopulateRoomDto;
import com.rhdigital.rhclient.room.RHDatabase;
import com.rhdigital.rhclient.room.model.AuthorisedProgram;
import com.rhdigital.rhclient.room.model.Program;
import com.rhdigital.rhclient.room.model.User;
import com.rhdigital.rhclient.room.repository.RHRepository;

import java.util.List;

public class PaymentViewHolder extends AndroidViewModel {

    private RHRepository rhRepository;

    public PaymentViewHolder(@NonNull Application application) {
        super(application);
        this.rhRepository = new RHRepository(application);
    }

    public LiveData<Program> getProgram(String programId) {
        return rhRepository.getProgram(programId);
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

    public LiveData<User> fetchRemoteUser(String userId) {
        MutableLiveData<User> user = new MutableLiveData();
        LifecycleOwner lifecycleOwner = (LifecycleOwner) ((RHApplication)getApplication()).getCurrentActivity();
        rhRepository.syncWithRemote(
                RHDatabase.getDatabase(getApplication()),
                new PopulateRoomDto(PopulateRoomDto.USER)
        ).observe(lifecycleOwner, inserts -> {
            rhRepository.getAuthenticatedUser(
                    userId
            ).observe(
                    lifecycleOwner, u -> user.postValue(u)
            );
        });
        return user;
    }
}
