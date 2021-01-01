package com.rhdigital.rhclient.activities.rhapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.database.model.Program;
import com.rhdigital.rhclient.database.repository.RHRepository;

public class CoursesViewModel extends AndroidViewModel {

    public MutableLiveData<Program> program = new MutableLiveData();
    public RHRepository rhRepository;

    public CoursesViewModel(@NonNull Application application, String programId) {
        super(application);
        rhRepository = new RHRepository(application);
//        program.postValue(rhRepository);
    }

    public void configureRHAppViewModel() {
        RHAppViewModel rhAppViewModel =
                new ViewModelProvider(getApplication()).get(RHAppViewModel.class);
        rhAppViewModel.isBackButtonActive.postValue(true);
        rhAppViewModel.isTitleCenter.postValue(true);
        rhAppViewModel.title.setValue(getApplication().getString(R.string.title_programs));
    }
}
