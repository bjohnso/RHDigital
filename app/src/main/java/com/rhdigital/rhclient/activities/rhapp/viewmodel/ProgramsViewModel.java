package com.rhdigital.rhclient.activities.rhapp.viewmodel;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.RHApplication;
import com.rhdigital.rhclient.common.dto.RemoteResourceDto;
import com.rhdigital.rhclient.common.services.RemoteResourceService;
import com.rhdigital.rhclient.database.model.Course;
import com.rhdigital.rhclient.database.model.Program;
import com.rhdigital.rhclient.database.repository.RHRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProgramsViewModel extends AndroidViewModel {

  private RHRepository rhRepository;

  public ProgramsViewModel(@NonNull Application application) {
    super(application);
    rhRepository = new RHRepository(application);
  }

  public void init() {
    ViewModelStoreOwner viewModelStoreOwner =
            (ViewModelStoreOwner) ((RHApplication)getApplication()).getCurrentActivity();
    RHAppViewModel rhAppViewModel = new ViewModelProvider(viewModelStoreOwner).get(RHAppViewModel.class);
    rhAppViewModel.isBackButtonActive.setValue(false);
    rhAppViewModel.isTitleCenter.setValue(false);
    rhAppViewModel.title.setValue(getApplication().getString(R.string.title_programs));
  }

  public LiveData<List<Program>> getAllPrograms() { return rhRepository.getAllPrograms(); }

  public LiveData<List<Program>> getAllAuthorisedPrograms() {return rhRepository.getAllAuthorisedPrograms(); }

  public LiveData<List<Program>> getAllUndiscoveredPrograms() {return rhRepository.getAllUndiscoveredPrograms(); }

  public LiveData<HashMap<String, Bitmap>> getAllProgramPosters(Context context, List<Program> programs, int width, int height) {
    List<RemoteResourceDto> data = new ArrayList<>();
    for (Program program: programs) {
      // TODO: USE VIDEO THUMBNAILS
      data.add(
              new RemoteResourceDto(program.getId(), program.getPosterUrl())
      );
    }
    return new RemoteResourceService().getAllBitmap(context, data, width, height);
  }

  public void insert(Program program) { rhRepository.insert(program); }
}
