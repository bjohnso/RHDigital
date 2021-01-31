package com.rhdigital.rhclient.activities.rhapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.RHApplication;
import com.rhdigital.rhclient.room.model.Report;
import com.rhdigital.rhclient.room.repository.RHRepository;

import java.util.List;

public class ReportsViewModel extends AndroidViewModel {

    private RHRepository rhRepository;

    public ReportsViewModel(@NonNull Application application) {
        super(application);
        rhRepository = new RHRepository(application);
    }

    public void init() {
        ViewModelStoreOwner viewModelStoreOwner =
                (ViewModelStoreOwner) ((RHApplication)getApplication()).getCurrentActivity();
        RHAppViewModel rhAppViewModel = new ViewModelProvider(viewModelStoreOwner).get(RHAppViewModel.class);
        rhAppViewModel.isEnrollState.setValue(false);
        rhAppViewModel.isActionButtonActive.setValue(false);
        rhAppViewModel.isBackButtonActive.setValue(false);
        rhAppViewModel.isTitleCenter.setValue(false);
        rhAppViewModel.title.setValue(getApplication().getString(R.string.title_reports));
    }

    public LiveData<List<Report>> getReports() {
        return rhRepository.getAllAuthorisedReports();
    }
}
