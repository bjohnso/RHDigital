package com.rhdigital.rhclient.activities.rhauth.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.common.util.GenericTimer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SignUpPhoneFragment extends Fragment {

  //Threading
  private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
  private Handler handler = new Handler(Looper.getMainLooper()) {
    @Override
    public void handleMessage(@NonNull Message msg) {
      if (msg.what == GenericTimer.UI_UNLOCK) {
        submitButton.setEnabled(true);
        submitButton.setBackgroundResource(R.drawable.submit_active);
      }
    }
  };

  private AutoCompleteTextView phoneInput;
  private Button submitButton;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_sign_up_phone, container, false);

    phoneInput = (AutoCompleteTextView) view.findViewById(R.id.sign_up_phone_input);
    submitButton = (Button) view.findViewById(R.id.sign_up_phone_submit_btn);

    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    submitButton.setOnClickListener(new SubmitOnClick(this));
  }

  public void setSubmitDisableTimeout() {
    this.scheduledExecutorService.schedule(new GenericTimer(handler, GenericTimer.UI_UNLOCK), 2, TimeUnit.SECONDS);
  }

  public void setSubmitDisable() {
    this.submitButton.setEnabled(false);
    this.submitButton.setBackgroundResource(R.drawable.submit_inactive);
  }

  public AutoCompleteTextView getPhoneInput() {
    return phoneInput;
  }

  public static class SubmitOnClick implements View.OnClickListener {

    private SignUpPhoneFragment fragment;

    public SubmitOnClick(Fragment fragment) {
      this.fragment = (SignUpPhoneFragment) fragment;
    }

    @Override
    public void onClick(View view) {
    }
  }
}
