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
import com.rhdigital.rhclient.activities.rhauth.services.AuthAPIService;
import com.rhdigital.rhclient.common.services.NavigationService;
import com.rhdigital.rhclient.common.util.GenericTimer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.rhdigital.rhclient.activities.rhauth.constants.ValidationConstants.PARTIAL_STRATEGY;

public class SignUpEmailFragment extends Fragment {

  private List<String> validationErrors;

  private AuthAPIService authAPIService = new AuthAPIService();

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

  private AutoCompleteTextView emailInput;
  private Button submitButton;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_sign_up_email, container, false);

    emailInput = (AutoCompleteTextView) view.findViewById(R.id.sign_up_email_input);
    submitButton = (Button) view.findViewById(R.id.sign_up_email_submit_btn);

    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    submitButton.setOnClickListener(new SubmitOnClick(this));
  }

  public void setSubmitDisableTimeout() {
    this.scheduledExecutorService.schedule(new GenericTimer(handler, GenericTimer.UI_UNLOCK), 1, TimeUnit.SECONDS);
  }

  public void setSubmitDisable() {
    this.submitButton.setEnabled(false);
    this.submitButton.setBackgroundResource(R.drawable.submit_inactive);
  }

  public void updateValidationErrors(List<String> validationErrors) {
    this.validationErrors = validationErrors;
    displayValidationErrors();
  }

  public void updateValidationErrors(String validationError) {
    if (this.validationErrors == null) {
      this.validationErrors = new ArrayList<>();
    }
    this.validationErrors.add(validationError);
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

  public AutoCompleteTextView getEmailInput() { return emailInput; }

  public static class SubmitOnClick implements View.OnClickListener {

    private SignUpEmailFragment fragment;
    private RHAuthActivity rhAuthActivity;

    public SubmitOnClick(Fragment fragment) {
      this.fragment = (SignUpEmailFragment) fragment;
      this.rhAuthActivity = (RHAuthActivity) fragment.getActivity();
    }

    @Override
    public void onClick(View view) {
      fragment.setSubmitDisable();
      String email = fragment.getEmailInput().getText().toString();
      rhAuthActivity.updateAuthField("email", email);
      rhAuthActivity.validateAuthFields(PARTIAL_STRATEGY)
        .observe(fragment.getViewLifecycleOwner(), validationErrors -> {
          fragment.setSubmitDisableTimeout();
          if (validationErrors != null) {
            fragment.updateValidationErrors(validationErrors);
            if (fragment.getValidationErrors().size() < 1) {
              NavigationService.getINSTANCE()
                .navigate(rhAuthActivity.getLocalClassName(),
                  R.id.signUpDetailsFragment,
                  null,
                  null);
            }
          } else {
            Toast.makeText(fragment.getContext(), "Auth Validation Service Failed", Toast.LENGTH_LONG);
          }
        });
    }
  }
}
