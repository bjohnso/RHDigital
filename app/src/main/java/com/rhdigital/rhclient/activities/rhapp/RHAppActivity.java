package com.rhdigital.rhclient.activities.rhapp;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.RHApplication;
import com.rhdigital.rhclient.activities.rhapp.fragments.RHAppFragment;
import com.rhdigital.rhclient.activities.rhapp.viewmodel.RHAppViewModel;
import com.rhdigital.rhclient.activities.rhauth.RHAuthActivity;
import com.rhdigital.rhclient.common.providers.CustomFileProvider;
import com.rhdigital.rhclient.common.services.FirebaseUploadService;
import com.rhdigital.rhclient.common.services.NavigationService;
import com.rhdigital.rhclient.common.services.PushNotificationHelperService;
import com.rhdigital.rhclient.common.services.VideoPlayerService;
import com.rhdigital.rhclient.common.util.ImageProcessor;
import com.rhdigital.rhclient.databinding.ActivityRhappBinding;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;

public class RHAppActivity extends AppCompatActivity {

  public static final int IMAGE_PICKER_CODE = 100;
  public static final int DOWNLOAD_PDF = 101;
  private Intent imageUploadData = null;
  private String fileName = null;
  private ResponseBody fileResponseBody = null;
  private ActivityRhappBinding binding;
  private Intent rhAuthIntent;

  @Override
  protected void onStart() {
    super.onStart();

    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    StrictMode.setThreadPolicy(policy);

    rhAuthIntent = new Intent(this, RHAuthActivity.class);
    if (FirebaseAuth.getInstance().getCurrentUser() == null) {
      startAuthActivity();
      return;
    }
    PushNotificationHelperService.getINSTANCE().setContext(this);
    PushNotificationHelperService.getINSTANCE().generateNotificationChannel();
    PushNotificationHelperService.getINSTANCE().saveTokenRemote();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // REGISTER ACTIVITY TO APPLICATION
    ((RHApplication)getApplicationContext()).setCurrentActivity(this);

    // VIEW BINDING
    binding = ActivityRhappBinding.inflate(getLayoutInflater());
    binding.setLifecycleOwner(this);
    binding.setViewModel(
            new ViewModelProvider(this).get(RHAppViewModel.class)
    );
    setContentView(binding.getRoot());

    binding.buttonBack.setOnClickListener(view -> {
      RHAppFragment currentFragment = ((RHApplication)getApplication())
              .getCurrentFragment();
      if (currentFragment != null) {
        currentFragment.onBack();
      } else {
        navigateBack();
      }
    });

    binding.buttonAction.setOnClickListener(view -> {
      RHAppFragment currentFragment = ((RHApplication)getApplication())
              .getCurrentFragment();
      if (currentFragment != null) {
        currentFragment.onAction();
      }
    });

    binding.bottomNavigationView.setOnNavigationItemSelectedListener(item ->  {
      switch (item.getItemId()){
        case R.id.bottom_nav_programs:
          NavigationService.getINSTANCE().navigate(getLocalClassName(), R.id.programsFragment, null, null);
          return true;
        case R.id.bottom_nav_reports:
          NavigationService.getINSTANCE().navigate(getLocalClassName(), R.id.reportsFragment, null, null);
          return true;
        case R.id.bottom_nav_profile:
          NavigationService.getINSTANCE().navigate(getLocalClassName(), R.id.profileFragment, null, null);
          return true;
      }
      return false;
    });

    binding.getViewModel().isFullscreenMode.observe(this, isFullscreenMode -> configureScreenOrientation(isFullscreenMode));

    binding.getViewModel().authorisePrograms().observe(this, authorisedPrograms ->
            binding.getViewModel().authoriseReports().observe(this, authorisedReports ->
                    initNavGraph()));
  }

  private void initNavGraph() {
    NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
            .findFragmentById(R.id.nav_host_rhapp);
    NavController navController = navHostFragment.getNavController();
    NavigationService.getINSTANCE().initNav(
            getLocalClassName(),
            navController,
            R.navigation.rhapp_nav_graph,
            R.id.programsFragment);
  }

  public void navigateBack() {
    VideoPlayerService.getInstance().destroyAllVideoStreams(true);
    NavigationService.getINSTANCE().navigateBack(getLocalClassName());
  }

  public void configureScreenOrientation(boolean isLandscape) {
    // Forced Orientation Landscape
    if (isLandscape) {
      ((RHApplication) getApplication())
              .getCurrentActivity()
              .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
    }
    else {
      ((RHApplication) getApplication())
              .getCurrentActivity()
              .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == IMAGE_PICKER_CODE) {
        //Request Permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
          ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
          imageUploadData = data;
        } else {
          handleImageUpload(data);
        }
      }
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    if (requestCode == 0) {
      if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        if (imageUploadData != null) {
          handleImageUpload(imageUploadData);
          imageUploadData = null;
        } else {
          Toast.makeText(this, getResources().getString(R.string.server_error_image_upload), Toast.LENGTH_LONG).show();
        }
      }
    } else if (requestCode == 1) {
      //Request Permissions
      if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
        writeFileToDisk(fileName, fileResponseBody);
      }
    }
  }

  // FILE DOWNLOAD

  public void writeFileToDisk(String fileName, ResponseBody responseBody) {
    File file = new File(
            getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
            fileName);

    //Request Permissions
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
      this.fileName = fileName;
      this.fileResponseBody = responseBody;
    } else {
      try {
        InputStream in = responseBody.byteStream();
        OutputStream out = new FileOutputStream(file);
        IOUtils.copy(in, out);
      } catch (IOException e) {
        e.printStackTrace();
      }
      Uri uri = CustomFileProvider
              .getUriForFile(this,
                      getApplicationContext().getPackageName() + ".provider",
                      file);
      sendNotification(uri, "New PDF Download", fileName);
      Toast.makeText(this, "Download Complete", Toast.LENGTH_LONG).show();
    }
  }

  public void sendNotification(Uri fileOnDisk, String title, String body) {
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    intent.setDataAndType(fileOnDisk, "application/pdf");
    intent.putExtra("NAME", title);
    intent.putExtra("BODY", body);
    PushNotificationHelperService.getINSTANCE().initialisePendingIntent(intent);
    PushNotificationHelperService.getINSTANCE().displayNotification(
            intent.getStringExtra("NAME"),
            intent.getStringExtra("BODY"));
  }

  // IMAGE UPLOAD

  public void handleImageUpload(Intent data) {
    if (data.getType() != null) {
      try {
        //Get FileDescriptor for File in external storage from URI
        ParcelFileDescriptor fileDescriptor = getContentResolver().openFileDescriptor(data.getData(), "r");

        //Fetch name of file from ContentResolver and create a new File in internal App storage
        File file = new File(getCacheDir(), ImageProcessor.getInstance().getFileName(getContentResolver(), data.getData()));
        FileInputStream in = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileOutputStream out = new FileOutputStream(file);
        IOUtils.copy(in, out);

        Bitmap bitmap = ImageProcessor.getInstance().processImageForUpload(file, data.getType());
        if (bitmap != null) {
          Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(),
            bitmap,
            FirebaseAuth.getInstance().getUid(),
            null));
          FirebaseUploadService.getInstance().uploadProfileImage(uri, FirebaseAuth.getInstance().getUid())
                  .observe(this, uploaded -> {
                    if (uploaded) {
                      RHAppFragment currentFragment = ((RHApplication)getApplication())
                              .getCurrentFragment();
                      if (currentFragment != null) {
                        currentFragment.onImageUpload();
                      }
                    }
                  });
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public void launchPaymentActivity() {
    //TODO: LAUNCH PAYMENTS ACTIVITY
  }

  public void logout() {
    FirebaseAuth.getInstance().signOut();
    startAuthActivity();
  }

  private void startAuthActivity() { startActivity(rhAuthIntent); }
}
