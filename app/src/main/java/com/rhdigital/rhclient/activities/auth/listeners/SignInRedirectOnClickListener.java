package com.rhdigital.rhclient.activities.auth.listeners;

import android.view.View;

import androidx.fragment.app.Fragment;

import com.rhdigital.rhclient.activities.auth.AuthActivity;

public class SignInRedirectOnClickListener implements View.OnClickListener {

  private Fragment fragment;

  public SignInRedirectOnClickListener(Fragment fragment) {
    this.fragment = fragment;
  }

  @Override
  public void onClick(View view) {
    AuthActivity activity = (AuthActivity) fragment.getActivity();
    activity.setViewPager(0);
  }
}
