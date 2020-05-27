package com.rhdigital.rhclient.activities.user;

import android.content.Context;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.common.services.NavigationService;
import com.rhdigital.rhclient.database.model.User;
import com.rhdigital.rhclient.database.viewmodel.UserViewModel;

public class UserActivity extends AppCompatActivity {

  private FirebaseFirestore remoteDB = FirebaseFirestore.getInstance();
  private UserViewModel userViewModel;
  private LiveData<User> userObservable;
  private User user;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user);

    //Initialise View model
    userViewModel = new UserViewModel(getApplication());

    //Initialise Observers
    final Observer<User> userObserver = u -> {
      Log.d("USER", "USER NAME : " + u.getName() + "\nUSER SURNAME : " + u.getSurname());
    };

    //Set Observers
    userObservable = userViewModel.getAuthenticatedUser(FirebaseAuth.getInstance().getUid());
    userObservable.observe(this, userObserver);

    //Initialise Navigator
    NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_user);
    NavController navController = navHostFragment.getNavController();
    // Call Navigation Service
    NavigationService.getINSTANCE().initNav(
      getLocalClassName(),
      navController,
      R.navigation.user_nav_graph,
      R.id.userProfileFragment);
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
