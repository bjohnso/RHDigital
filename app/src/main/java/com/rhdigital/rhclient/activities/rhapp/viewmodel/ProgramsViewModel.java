package com.rhdigital.rhclient.activities.rhapp.viewmodel;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.common.services.RemoteResourceService;
import com.rhdigital.rhclient.database.model.Program;
import com.rhdigital.rhclient.database.repository.RHRepository;

import java.util.HashMap;
import java.util.List;

public class ProgramsViewModel extends AndroidViewModel {

  private RHRepository rhRepository;

  public ProgramsViewModel(@NonNull Application application) {
    super(application);
    rhRepository = new RHRepository(application);
  }

  public void configureRHAppViewModel() {
    RHAppViewModel rhAppViewModel =
            new ViewModelProvider(getApplication()).get(RHAppViewModel.class);
    rhAppViewModel.isBackButtonActive.postValue(false);
    rhAppViewModel.isTitleCenter.postValue(false);
    rhAppViewModel.title.setValue(getApplication().getString(R.string.title_programs));
  }

  public LiveData<List<Program>> getAllPrograms() { return rhRepository.getAllPrograms(); }

  public LiveData<List<Program>> getAllAuthorisedPrograms() {return rhRepository.getAllAuthorisedPrograms(); }

  public LiveData<List<Program>> getAllUndiscoveredPrograms() {return rhRepository.getAllUndiscoveredPrograms(); }

  public LiveData<HashMap<String, Bitmap>> getAllProgramPosters(Context context, List<Program> programs, int width, int height) { return new RemoteResourceService().getAllBitmap(context, programs, width, height); }

  public void insert(Program program) { rhRepository.insert(program); }
}
