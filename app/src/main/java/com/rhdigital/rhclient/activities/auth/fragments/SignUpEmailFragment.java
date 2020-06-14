package com.rhdigital.rhclient.activities.auth.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

import com.google.firebase.auth.FirebaseAuth;
import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.auth.AuthActivity;
import com.rhdigital.rhclient.activities.auth.services.Authenticator;
import com.rhdigital.rhclient.common.services.NavigationService;
import com.rhdigital.rhclient.common.services.Toaster;
import com.rhdigital.rhclient.common.util.GenericTimer;
import com.rhdigital.rhclient.database.model.User;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.rhdigital.rhclient.common.util.ValidationHelper.isValidEmailAddress;

public class SignUpEmailFragment extends Fragment {

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
  private LinearLayout signInRedirect;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.sign_up_email_layout, container, false);

    emailInput = (AutoCompleteTextView) view.findViewById(R.id.sign_up_email_input);
    submitButton = (Button) view.findViewById(R.id.sign_up_email_submit_btn);
    signInRedirect = (LinearLayout) view.findViewById(R.id.sign_in_redirect);

    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    signInRedirect.setOnClickListener(new RedirectSignInOnClick(getActivity().getLocalClassName()));
    submitButton.setOnClickListener(new SubmitOnClick(this));
  }

  public void setSubmitDisableTimeout() {
    this.scheduledExecutorService.schedule(new GenericTimer(handler, GenericTimer.UI_UNLOCK), 2, TimeUnit.SECONDS);
  }

  public void setSubmitDisable() {
    this.submitButton.setEnabled(false);
    this.submitButton.setBackgroundResource(R.drawable.submit_inactive);
  }

  public AutoCompleteTextView getEmailInput() {
    return emailInput;
  }

  public static class SubmitOnClick implements View.OnClickListener {

    private SignUpEmailFragment fragment;

    public SubmitOnClick(Fragment fragment) {
      this.fragment = (SignUpEmailFragment) fragment;
    }

    @Override
    public void onClick(View view) {
      fragment.setSubmitDisable();
      String email = fragment.getEmailInput().getText().toString();
      if (email != null && !email.isEmpty() && isValidEmailAddress(email)) {
        Authenticator.getInstance()
          .isExistingUserEmail(email)
          .observe((LifecycleOwner) fragment.getContext(), isExisting -> {
            fragment.setSubmitDisableTimeout();
            if (isExisting) {
              Toaster.getINSTANCE()
                .ToastMessage(fragment.getContext().getResources().getString(R.string.user_error_email_duplicate) ,true);
            } else {
                Bundle bundle = new Bundle();
                bundle.putString("EMAIL", email);
                NavigationService
                  .getINSTANCE()
                  .navigate(fragment.getActivity().getLocalClassName(),
                    R.id.signUpDetailsFragment,
                    bundle, null);
            }
          });
      } else {
        fragment.setSubmitDisableTimeout();
        Toast.makeText(fragment.getContext(), "Please Enter a valid email address", Toast.LENGTH_SHORT).show();
      }
    }
  }

  public static class RedirectSignInOnClick implements View.OnClickListener {
    private String parentClassName;

    public RedirectSignInOnClick(String parentClassName) {
      this.parentClassName = parentClassName;
    }

    @Override
    public void onClick(View view) {
      NavigationService.getINSTANCE()
        .navigate(parentClassName,
          R.id.signInFragment,
          null, null);
    }
  }
}
