package com.rhdigital.rhclient.activities.auth.listeners;

import android.view.View;

import androidx.fragment.app.Fragment;

import com.rhdigital.rhclient.activities.auth.AuthActivity;
import com.rhdigital.rhclient.activities.auth.fragments.SignInFragment;

public class SignInOnClickListener implements View.OnClickListener {

  private Fragment fragment;

  public SignInOnClickListener(Fragment fragment) {
    this.fragment = fragment;
  }

  @Override
  public void onClick(View view) {
    AuthActivity authActivity = (AuthActivity) fragment.getActivity();
    SignInFragment frag = (SignInFragment) fragment;
    frag.setSubmitDisableTimeout();

    authActivity
      .getAuthenticator()
      .authenticateEmail(frag.getEmailText(), frag.getPasswordText());
  }
}
