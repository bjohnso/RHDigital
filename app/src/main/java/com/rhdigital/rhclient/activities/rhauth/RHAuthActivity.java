package com.rhdigital.rhclient.activities.rhauth;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.LiveData;

import androidx.lifecycle.MutableLiveData;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.rhdigital.rhclient.R;

import com.rhdigital.rhclient.activities.rhauth.services.AuthAPIService;
import com.rhdigital.rhclient.activities.rhauth.services.AuthFieldValidationService;
import com.rhdigital.rhclient.activities.rhroom.RHRoomActivity;
import com.rhdigital.rhclient.common.services.NavigationService;

import com.rhdigital.rhclient.common.services.PushNotificationHelperService;
import com.rhdigital.rhclient.common.util.RHAPIResult;
import com.rhdigital.rhclient.room.model.User;
import com.rhdigital.rhclient.common.interfaces.CallableFunction;
import com.rhdigital.rhclient.room.viewmodel.UserViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.rhdigital.rhclient.common.constants.BroadcastConstants.EMAIL_VERIFICATION;

public class RHAuthActivity extends AppCompatActivity {

  private String TAG = this.getClass().getSimpleName();

  private LocalBroadcastManager localBroadcastManager;

  private View loader;

  private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      if (FirebaseAuth.getInstance().getCurrentUser() != null) {
        FirebaseAuth.getInstance()
                .getCurrentUser()
                .reload()
                .addOnCompleteListener(task -> {
                  if (task.isSuccessful()) {
                    String title = "Email Verification";
                    PushNotificationHelperService.getINSTANCE().displayNotification(title, intent.getStringExtra(title));
                    initRHRoom();
                  }
                });
      }
    }
  };

  private void registerBroadcastReceiver() {
    localBroadcastManager = LocalBroadcastManager.getInstance(this);
    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction(EMAIL_VERIFICATION);
    localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);
  }

  private void unregisterBroadcastReceiver() {
    localBroadcastManager.unregisterReceiver(broadcastReceiver);
  }

  private ExecutorService executorService = Executors.newSingleThreadExecutor();
  private AuthFieldValidationService authFieldValidationService = new AuthFieldValidationService();
  private AuthAPIService authAPIService = new AuthAPIService();

  private MutableLiveData<String> emailVerificationResult = new MutableLiveData<>();

  //ViewModel
  private UserViewModel userViewModel;

  private LiveData<User> usertask;

  private HashMap<String, String> authFieldsMap = new HashMap<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    userViewModel = new UserViewModel(getApplication());

    setContentView(R.layout.activity_auth);

    // INITIALISE NAVIGATION COMPONENT && CALL NAVIGATION SERVICE
    NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
      .findFragmentById(R.id.nav_host_rhauth);
    NavController navController = navHostFragment.getNavController();
    NavigationService.getINSTANCE().initNav(
      getLocalClassName(),
      navController,
      R.navigation.rhauth_nav_graph,
      R.id.signInFragment);
  }

  @Override
  protected void onStart() {
    super.onStart();
//    FirebaseAuth.getInstance().signOut();
    initRHRoom();
    registerBroadcastReceiver();
    PushNotificationHelperService.getINSTANCE().setContext(this);
    PushNotificationHelperService.getINSTANCE().generateNotificationChannel();
    FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
      if (task.isSuccessful()) {
        authFieldsMap.put("fcmToken", task.getResult());
      }
    });
  }

  @Override
  protected void onStop() {
    super.onStop();
    unregisterBroadcastReceiver();
  }

  private void startLoader() {
    ViewGroup rootView = (ViewGroup) getWindow().getDecorView().getRootView();
    loader = getLayoutInflater().inflate(R.layout.rh_loader, rootView, false);
    rootView.addView(loader);
  }

  private void stopLoader() {
    ViewGroup rootView = (ViewGroup) getWindow().getDecorView().getRootView();
    rootView.removeView(loader);
  }

  public void updateAuthField(String key, String value) { this.authFieldsMap.put(key, value); }

  public HashMap<String, String> getAuthFieldsMap() { return this.authFieldsMap; }

  public LiveData<RHAPIResult> signUp() {
    startLoader();
    MutableLiveData<RHAPIResult> signUpResultData = new MutableLiveData<>();
    Tasks.call(executorService, CallableFunction.callable(this.authAPIService, new Pair<>("signUpNewUser", authFieldsMap)))
      .addOnSuccessListener(data -> {
        AuthCredential credential = (AuthCredential) data.getPayload();
        stopLoader();
        signIn(credential);
        signUpResultData.postValue(data);
      })
      .addOnFailureListener(error -> {
        signUpResultData.postValue(null);
        stopLoader();
      });
    return signUpResultData;
  }

  public LiveData<RHAPIResult> signIn(AuthCredential authCredential) {
    startLoader();
    MutableLiveData<RHAPIResult> signInResultData = new MutableLiveData<>();
    FirebaseAuth.getInstance().signInWithCredential(authCredential)
      .addOnSuccessListener(authResult -> {
        signInResultData.postValue(new RHAPIResult("Sign In Successful", authResult, true));
        stopLoader();
      })
      .addOnFailureListener(error -> {
        stopLoader();
        signInResultData.postValue(new RHAPIResult(error.getMessage(), error, false));
      });
    return signInResultData;
  }

  public LiveData<RHAPIResult> sendEmailVerification() {
    startLoader();
    HashMap<String, String> dataMap = new HashMap();
    dataMap.put("fcmToken", authFieldsMap.get("fcmToken"));
    dataMap.put("uid", FirebaseAuth.getInstance().getUid());
    MutableLiveData<RHAPIResult> resultData = new MutableLiveData<>();
    Tasks.call(executorService, CallableFunction.callable(this.authAPIService, new Pair<>("sendEmailVerification", dataMap)))
            .addOnSuccessListener(data -> {
              stopLoader();
              resultData.postValue(data);
            })
            .addOnFailureListener(error -> {
              resultData.postValue(null);
              stopLoader();
            });
    return resultData;
  }

  public LiveData<String> subscribeToEmailVerification() {
    return this.emailVerificationResult;
  }

  //TODO: HANDLE MULTIPLE STRATEGIES
  public LiveData<List<String>> validateAuthFields(int STRATEGY) {
    startLoader();
    MutableLiveData<List<String>> validationResultData = new MutableLiveData<>();
    Tasks.call(executorService, CallableFunction.callable(this.authFieldValidationService, this.authFieldsMap, STRATEGY))
      .addOnSuccessListener(validationErrors -> {
          validationResultData.postValue(validationErrors);
          stopLoader();
      })
      .addOnFailureListener(error -> {
          validationResultData.postValue(null);
          stopLoader();
      });
    return validationResultData;
  }

  //TODO: IMPLEMENT PHONE AUTHENTICATION

  public UserViewModel getUserViewModel() {
    return userViewModel;
  }

  public void initRHRoom() {
    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
      if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
        Intent intent = new Intent(this, RHRoomActivity.class);
        startActivity(intent);
      } else {
        NavigationService.getINSTANCE().navigate(getLocalClassName(), R.id.signUpEmailVerificationFragment, null, null);
      }
    }
  }

}
