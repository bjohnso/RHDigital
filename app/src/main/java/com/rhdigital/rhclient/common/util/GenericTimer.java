//package com.rhdigital.rhclient.common.util;
//
//import android.os.Handler;
//import android.os.Message;
//import androidx.fragment.app.Fragment;
//import com.rhdigital.rhclient.activities.auth.fragments.SignInFragment;
//import java.util.concurrent.Callable;
//
//public class GenericTimer implements Callable {
//
//  public static int UI_UNLOCK = 1;
//  public static int EMAIL_VERIFICATION = 2;
//
//  private Handler handler;
//  private int what;
//
//  public GenericTimer(Handler handler, int what) {
//    this.handler = handler;
//    this.what = what;
//  }
//
//  @Override
//  public Object call() throws Exception {
//    Message message = handler.obtainMessage(what);
//    message.sendToTarget();
//    return null;
//  }
//}
