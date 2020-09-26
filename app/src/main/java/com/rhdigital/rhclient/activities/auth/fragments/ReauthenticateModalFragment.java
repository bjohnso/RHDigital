//package com.rhdigital.rhclient.activities.auth.fragments;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//import android.os.Message;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AutoCompleteTextView;
//import android.widget.Button;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.DialogFragment;
//import androidx.lifecycle.LiveData;
//
//import com.rhdigital.rhclient.R;
//import com.rhdigital.rhclient.activities.auth.services.Authenticator;
//import com.rhdigital.rhclient.common.util.GenericTimer;
//
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
//public class ReauthenticateModalFragment extends DialogFragment {
//
//  //Threading
//  private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
//  private Handler handler = new Handler(Looper.getMainLooper()) {
//    @Override
//    public void handleMessage(@NonNull Message msg) {
//      if (msg.what == GenericTimer.UI_UNLOCK) {
//        setUIEnabled();
//      }
//    }
//  };
//
//  private AutoCompleteTextView passwordInput;
//  private Button submitButton;
//  private String email;
//
//  //Observer
//  private LiveData<Boolean> authTask;
//
//  @Override
//  public void onStart() {
//    super.onStart();
//    if (getArguments() != null) {
//      email = getArguments().getString("EMAIL");
//    }
//
//    if (email == null) {
//      this.dismiss();
//    }
//  }
//
//  @Nullable
//  @Override
//  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//    View view = inflater.inflate(R.layout.reauthenticate_password_confirm_dialog, container, false);
//
//    passwordInput = view.findViewById(R.id.reauthenticate_modal_password_input);
//    submitButton = view.findViewById(R.id.reauthenticate_modal_submit_btn);
//
//    submitButton.setOnClickListener(new ButtonOnClick(this));
//
//    return view;
//  }
//
//  public void reauthenticate() {
//    authTask = Authenticator.getInstance()
//      .authenticate(email, passwordInput.getText().toString(),true);
//      authTask
//      .observe(getViewLifecycleOwner(), isSuccessful -> {
//        if (isSuccessful) {
//          sendDataToParent(isSuccessful);
//        } else {
//          setUIDisableTimeout();
//        }
//      });
//  }
//
//  public void sendDataToParent(Boolean value) {
//    if (getTargetFragment() == null) {
//      this.dismiss();
//      return;
//    }
//    Intent intent = new Intent();
//    intent.putExtra("AUTH_SUCCESS", value);
//    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
//    this.dismiss();
//  }
//
//  public void setUIDisableTimeout() {
//    this.scheduledExecutorService.schedule(new GenericTimer(handler, GenericTimer.UI_UNLOCK), 3, TimeUnit.SECONDS);
//  }
//
//  public void setUIDisabled() {
//    this.submitButton.setEnabled(false);
//    this.submitButton.setBackgroundResource(R.drawable.submit_inactive);
//  }
//
//  public void setUIEnabled() {
//    this.submitButton.setEnabled(true);
//    this.submitButton.setBackgroundResource(R.drawable.submit_active);
//  }
//
//  public static class ButtonOnClick implements View.OnClickListener {
//    private ReauthenticateModalFragment dialogFragment;
//
//    public ButtonOnClick(DialogFragment dialogFragment) {
//      this.dialogFragment = (ReauthenticateModalFragment) dialogFragment;
//    }
//
//    @Override
//    public void onClick(View view) {
//      this.dialogFragment.setUIDisabled();
//      this.dialogFragment.reauthenticate();
//    }
//  }
//}
