package com.rhdigital.rhclient.activities.rhauth.fragments;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.rhauth.RHAuthActivity;
import com.rhdigital.rhclient.common.services.NavigationService;
import com.rhdigital.rhclient.common.util.GenericTimer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.rhdigital.rhclient.activities.rhauth.constants.ValidationConstants.VALIDATION_STRATEGY_SIGN_IN_EMAIL;

public class SignInFragment extends Fragment {

    private List<String> validationErrors;

    //Threading
    private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    private Handler handler = new Handler(Looper.getMainLooper()) {
      @Override
      public void handleMessage(@NonNull Message msg) {
        if (msg.what == 1) {
          submit.setEnabled(true);
          submit.setBackgroundResource(R.drawable.submit_active);
        }
      }
    };

    //Observer
    private LiveData<Boolean> authTask;

    // Components
    private AutoCompleteTextView emailInput;
    private AutoCompleteTextView passwordInput;
    private Button submit;
    private LinearLayout resetPasswordRedirect;
    private LinearLayout signUpRedirect;

    //Observables

    private TextView line;
    private TextView welcome;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        // Initialise Components
        emailInput = (AutoCompleteTextView) view.findViewById(R.id.sign_in_email_input);
        passwordInput = (AutoCompleteTextView) view.findViewById(R.id.sign_in_password_input);
        submit = (Button) view.findViewById(R.id.sign_in_submit_btn);
        resetPasswordRedirect = (LinearLayout) view.findViewById(R.id.sign_in_helper);
        signUpRedirect = (LinearLayout) view.findViewById(R.id.sign_up_redirect);

        line = (TextView) view.findViewById(R.id.line);
        welcome = (TextView) view.findViewById(R.id.welcome);

      return view;
    }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    //Set Listeners
    signUpRedirect.setOnClickListener(new RedirectSignUpOnClick(getActivity().getLocalClassName()));
    submit.setOnClickListener(new SubmitOnClick(this));
  }

    public void setSubmitDisableTimeout() {
      this.scheduledExecutorService.schedule(new GenericTimer(handler, GenericTimer.UI_UNLOCK), 3, TimeUnit.SECONDS);
    }

    public void setSubmitDisable() {
      this.submit.setEnabled(false);
      this.submit.setBackgroundResource(R.drawable.submit_inactive);
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

    public String getEmailText() {
      return this.emailInput.getText().toString();
    }

    public String getPasswordText() {
      return this.passwordInput.getText().toString();
    }

  // Listeners
  public static class SubmitOnClick implements View.OnClickListener {

      private SignInFragment fragment;
      private RHAuthActivity rhAuthActivity;

      public SubmitOnClick(SignInFragment fragment) {
        this.fragment = fragment;
        this.rhAuthActivity = (RHAuthActivity) fragment.getActivity();
      }

    @Override
    public void onClick(View view) {
        fragment.setSubmitDisable();
        rhAuthActivity.updateAuthField("email", fragment.getEmailText());
        rhAuthActivity.updateAuthField("password", fragment.getPasswordText());
        rhAuthActivity.validateAuthFields(VALIDATION_STRATEGY_SIGN_IN_EMAIL).observe(fragment.getViewLifecycleOwner(), validationErrors -> {
          fragment.setSubmitDisableTimeout();
          if (validationErrors != null) {
            fragment.updateValidationErrors(validationErrors);
            if (fragment.getValidationErrors().size() < 1) {
              rhAuthActivity.signIn(EmailAuthProvider.getCredential(fragment.getEmailText(), fragment.getPasswordText()))
                .observe(fragment.getViewLifecycleOwner(), result -> {
                  if (result.getSuccess()) {
                    rhAuthActivity.initRHRoom();
                  } else {
                    Toast.makeText(fragment.getContext(), result.getMessage(), Toast.LENGTH_LONG).show();
                  }
                });
            }
          } else {
            Toast.makeText(fragment.getContext(), "Auth Validation Service Failed", Toast.LENGTH_LONG);
          }
        });
    }
  }

  public static class RedirectSignUpOnClick implements View.OnClickListener {

      private String parentClassName;

      public RedirectSignUpOnClick(String parentClassName) {
        this.parentClassName = parentClassName;
      }

    @Override
    public void onClick(View view) {
        NavigationService.getINSTANCE()
          .navigate(parentClassName,
            R.id.signUpTabFragment,
            null, null);
    }
  }
}
