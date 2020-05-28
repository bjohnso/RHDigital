package com.rhdigital.rhclient.activities.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.auth.AuthActivity;
import com.rhdigital.rhclient.activities.auth.services.Authenticator;
import com.rhdigital.rhclient.common.services.FirebaseUploadService;
import com.rhdigital.rhclient.common.services.NavigationService;
import com.rhdigital.rhclient.database.model.User;
import com.rhdigital.rhclient.database.viewmodel.UserViewModel;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class UserActivity extends AppCompatActivity {

  private static int IMAGE_PICKER_CODE = 100;
  private FirebaseFirestore remoteDB = FirebaseFirestore.getInstance();
  private Authenticator authenticator;
  private UserViewModel userViewModel;
  private LiveData<User> userObservable;
  private LiveData<HashMap<String, Uri>> documentObservable;
  private String documentNames[];
  private User user;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user);

    authenticator = new Authenticator(this);

    //Initialise View model
    userViewModel = new UserViewModel(getApplication());

    //Initialise Observers
    final Observer<User> userObserver = u -> {
      Log.d("USER", "USER NAME : " + u.getName() + "\nUSER SURNAME : " + u.getSurname());
    };

    //Initialise Navigator
    NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_user);
    NavController navController = navHostFragment.getNavController();

    final Observer<HashMap<String, Uri>> documentObserver = new Observer<HashMap<String, Uri>>() {
      @Override
      public void onChanged(HashMap<String, Uri> stringUriHashMap) {
        // Call Navigation Service Once Document Uri's have been fetched
        if (stringUriHashMap.size() >= documentNames.length) {
          documentObservable.removeObserver(this::onChanged);
          NavigationService.getINSTANCE().initNav(
            getLocalClassName(),
            navController,
            R.navigation.user_nav_graph,
            R.id.userProfileFragment);
        }
      }
    };

    // Fetching all documents in Remote, extrapolating the document names from the querydocumentsnapshot, and passing as a String array of arguments to RemoteResourceService
    // We then asign the result of this operation to an observable and call observe to be updated onchange;
    remoteDB.collection("documents")
      .get()
      .addOnSuccessListener(this, snapshot -> {
        if (snapshot != null) {
          List<DocumentSnapshot> docs = snapshot.getDocuments();
          this.documentNames = new String[docs.size()];
          for (int i = 0; i < docs.size(); i++) {
            this.documentNames[i] = docs.get(i).get("name").toString();
          }
          documentObservable = userViewModel.getAllDocumentUri(this.documentNames);
          documentObservable.observe(this, documentObserver);
        }
      });

    //Set Observers
    userObservable = userViewModel.getAuthenticatedUser(FirebaseAuth.getInstance().getUid());
    userObservable.observe(this, userObserver);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == IMAGE_PICKER_CODE) {
        FirebaseUploadService.getInstance().uploadProfileImage(data.getData(), FirebaseAuth.getInstance().getUid());
      }
    }
  }

  public void logout() {
    authenticator.logout();
    Intent intent = new Intent(this, AuthActivity.class);
    startActivity(intent);
  }

  public void openImageChooser() {
    Intent intent = new Intent(Intent.ACTION_PICK);
    intent.setType("image/*");
    String mimeTypes[] = {"image/png", "image/jpeg"};
    intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
    startActivityForResult(intent, IMAGE_PICKER_CODE);
  }

  public Uri getDocumentUri(String type) {
    return documentObservable.getValue().get(type);
  }

  public LiveData<User> getUser() {
    return userObservable;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public void updateUser(User user, Context context) {
    remoteDB.collection("users")
      .document(user.getId())
      .set(user)
      .addOnCompleteListener(task -> {
        if (task.isSuccessful()) {
          userViewModel.updateUser(user);
          Toast.makeText(context,
            "Information Saved",
            Toast.LENGTH_SHORT).show();
        } else {
          Toast.makeText(context,
            "Unable to connect. Please make sure you are connected to the internet and try again",
            Toast.LENGTH_LONG).show();
        }
      });
  }

  public UserViewModel getUserViewModel() { return userViewModel; }
}
