package com.rhdigital.rhclient.util;

import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.rhdigital.rhclient.activities.auth.fragments.SignInFragment;

import java.util.concurrent.Callable;
import java.util.logging.Handler;


public class GenericTimer implements Callable {

  private Fragment fragment;

  public GenericTimer(Fragment fragment) {
    this.fragment = fragment;
  }

  @Override
  public Object call() throws Exception {
    SignInFragment signInFragment = (SignInFragment) fragment;
    Message message = signInFragment.getHandler().obtainMessage(1);
    message.sendToTarget();
    return null;
  }
}
