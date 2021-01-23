package com.rhdigital.rhclient.activities.rhpayment.viewholders;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.rhdigital.rhclient.RHApplication;
import com.rhdigital.rhclient.database.model.AuthorisedProgram;
import com.rhdigital.rhclient.database.model.Program;
import com.rhdigital.rhclient.database.repository.RHRepository;

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
}
