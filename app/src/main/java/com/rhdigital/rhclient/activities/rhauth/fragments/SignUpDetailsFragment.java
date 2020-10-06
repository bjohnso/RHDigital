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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.rhauth.RHAuthActivity;
import com.rhdigital.rhclient.common.util.GenericTimer;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.rhdigital.rhclient.activities.rhauth.constants.ValidationConstants.PARTIAL_STRATEGY;

public class SignUpDetailsFragment extends Fragment {

  private List<String> validationErrors;

  //Threading
  private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
  private Handler handler = new Handler(Looper.getMainLooper()) {
    @Override
    public void handleMessage(@NonNull Message msg) {
      if (msg.what == 1) {
        submitButton.setEnabled(true);
        submitButton.setBackgroundResource(R.drawable.submit_active);
      }
    }
  };

  AutoCompleteTextView firstNameInput;
  AutoCompleteTextView lastNameInput;
  AutoCompleteTextView passwordInput;
  Button submitButton;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_sign_up_details, container, false);
    firstNameInput = view.findViewById(R.id.sign_up_details_first_name_input);
    lastNameInput = view.findViewById(R.id.sign_up_details_last_name_input);
    passwordInput = view.findViewById(R.id.sign_up_details_password_input);
    submitButton = view.findViewById(R.id.sign_up_details_submit_btn);
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

  public void updateValidationErrors(List<String> validationErrors) {
    this.validationErrors = validationErrors;
    displayValidationErrors();
  }

  public List<String> getValidationErrors() { return validationErrors; }

  private void displayValidationErrors() {
    String errorMessage = "";
    if (validationErrors != null) {
      for (String error: validationErrors) {
        errorMessage += error + "\n";
      }
    }
    if (!errorMessage.isEmpty()) {
      Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
    }
  }

  public AutoCompleteTextView getFirstNameInput() {
    return firstNameInput;
  }

  public AutoCompleteTextView getLastNameInput() {
    return lastNameInput;
  }

  public AutoCompleteTextView getPasswordInput() {
    return passwordInput;
  }

  public static class SubmitOnClick implements View.OnClickListener {

    private SignUpDetailsFragment fragment;

    private RHAuthActivity rhAuthActivity;

    public SubmitOnClick(Fragment fragment) {
      this.fragment = (SignUpDetailsFragment) fragment;
      this.rhAuthActivity = (RHAuthActivity) fragment.getActivity();
    }

    @Override
    public void onClick(View view) {
      fragment.setSubmitDisable();
      fragment.setSubmitDisableTimeout();
      rhAuthActivity.updateAuthField("firstName", fragment.getFirstNameInput().getText().toString());
      rhAuthActivity.updateAuthField("lastName", fragment.getLastNameInput().getText().toString());
      rhAuthActivity.updateAuthField("password", fragment.getPasswordInput().getText().toString());
      rhAuthActivity.validateAuthFields(PARTIAL_STRATEGY)
        .observe(fragment.getViewLifecycleOwner(), validationErrors -> {
          fragment.setSubmitDisableTimeout();
          if (validationErrors != null) {
            fragment.updateValidationErrors(validationErrors);
            if (fragment.getValidationErrors().size() < 1) {
              rhAuthActivity.signUp().observe(fragment.getViewLifecycleOwner(), result -> {
                Toast.makeText(fragment.getContext(), "Sign Up Was Attempted", Toast.LENGTH_LONG);
              });
            }
          } else {
            Toast.makeText(fragment.getContext(), "Auth Validation Service Failed", Toast.LENGTH_LONG);
          }
        });
    }
  }
}
