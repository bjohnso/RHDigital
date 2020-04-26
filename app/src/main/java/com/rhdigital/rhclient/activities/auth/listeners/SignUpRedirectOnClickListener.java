package com.rhdigital.rhclient.activities.auth.listeners;

import android.app.Activity;
import android.view.ActionMode;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.rhdigital.rhclient.activities.auth.AuthActivity;

public class SignUpRedirectOnClickListener implements View.OnClickListener {

  private Fragment fragment;

  public SignUpRedirectOnClickListener(Fragment fragment) {
    this.fragment = fragment;
  }

  @Override
  public void onClick(View view) {
    AuthActivity activity = (AuthActivity) fragment.getActivity();
    activity.setViewPager(1);
  }
}
