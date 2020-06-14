package com.rhdigital.rhclient.common.services;

import android.content.Context;
import android.widget.Toast;

public class Toaster {

  private static Toaster INSTANCE;
  private Context context;

  private Toaster() {

  }

  public static Toaster getINSTANCE() {
    if (INSTANCE == null) {
      INSTANCE = new Toaster();
    }
    return INSTANCE;
  }

  public void setContext(Context context) {
    this.context = context;
  }

  public void ToastMessage(String message, boolean isLong) {
    if (isLong)
      Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    else
      Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
  }
}
