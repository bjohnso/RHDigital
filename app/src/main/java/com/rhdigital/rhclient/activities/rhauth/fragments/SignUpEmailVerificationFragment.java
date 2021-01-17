package com.rhdigital.rhclient.activities.rhauth.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.rhauth.RHAuthActivity;
import com.rhdigital.rhclient.common.services.NavigationService;
import com.rhdigital.rhclient.common.util.RHAPIResult;

public class SignUpEmailVerificationFragment extends Fragment {

  private final String TAG = this.getClass().getSimpleName();

  private LinearLayout verificationResendRedirect;
  private LinearLayout changeEmailRedirect;

  @Override
  public void onStart() {
    super.onStart();
    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
      if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
        ((RHAuthActivity)getActivity()).initRHApp();
      } else {
        ((RHAuthActivity)getActivity()).subscribeToEmailVerification()
          .observe(getViewLifecycleOwner(), result -> {
            ((RHAuthActivity)getActivity()).initRHApp();
          });
      }
    } else {
      NavigationService.getINSTANCE()
        .navigate(getActivity().getLocalClassName(),
          R.id.signInFragment,
          null,
          null);
    }
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_sign_up_verification, container, false);

    verificationResendRedirect = view.findViewById(R.id.verification_resend_redirect_redirect);
    changeEmailRedirect = view.findViewById(R.id.change_email_redirect);

    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    verificationResendRedirect.setOnClickListener(new VerificationResendOnClick(this));
    changeEmailRedirect.setOnClickListener(new ChangeEmailOnClick(this));
  }

  public static class ChangeEmailOnClick implements View.OnClickListener {

    private SignUpEmailVerificationFragment fragment;
    private RHAuthActivity rhAuthActivity;

    public ChangeEmailOnClick(Fragment fragment) {
      this.fragment = (SignUpEmailVerificationFragment) fragment;
      this.rhAuthActivity = (RHAuthActivity) fragment.getActivity();
    }

    @Override
    public void onClick(View view) {

    }
  }

  public static class VerificationResendOnClick implements View.OnClickListener {

    private SignUpEmailVerificationFragment fragment;
    private RHAuthActivity rhAuthActivity;

    public VerificationResendOnClick(Fragment fragment) {
      this.fragment = (SignUpEmailVerificationFragment) fragment;
      this.rhAuthActivity = (RHAuthActivity) fragment.getActivity();
    }

    @Override
    public void onClick(View view) {
      rhAuthActivity.sendEmailVerification().observe(rhAuthActivity, result -> {
        if (result != null) {
          Toast.makeText(rhAuthActivity, result.getMessage(), Toast.LENGTH_LONG).show();
        } else {
          Toast.makeText(rhAuthActivity, RHAPIResult.AUTH_FAILURE_RESEND_EMAIL_VERIFICATION, Toast.LENGTH_LONG).show();
        }
      });
    }
  }
}
