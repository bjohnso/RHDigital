package com.rhdigital.rhclient.activities.rhapp.viewmodel;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.RHApplication;
import com.rhdigital.rhclient.common.dto.RemoteResourceDto;
import com.rhdigital.rhclient.common.services.RemoteResourceService;
import com.rhdigital.rhclient.room.model.Report;
import com.rhdigital.rhclient.room.repository.RHRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;

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

    public LiveData<HashMap<String, Uri>> getReportUri(List<Report> reports) {
        List<RemoteResourceDto> data = new ArrayList<>();
        for (Report report: reports) {
            data.add(
                    new RemoteResourceDto(
                            report.getId(),
                            report.getUrl(),
                            RemoteResourceDto.REPORT_URI
                    )
            );
        }
        return new RemoteResourceService().getAllPDFURI(data);
    }

    public LiveData<ResponseBody> downloadFile(String downloadURL) {
        return new RemoteResourceService().downloadFile(downloadURL);
    }
}
