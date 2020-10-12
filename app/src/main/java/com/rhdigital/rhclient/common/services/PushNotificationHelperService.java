package com.rhdigital.rhclient.common.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.rhdigital.rhclient.R;

import static com.rhdigital.rhclient.common.constants.BroadcastConstants.EMAIL_VERIFICATION;

public class PushNotificationHelperService {

  private final String TAG = this.getClass().getSimpleName();

  private static PushNotificationHelperService INSTANCE;
  private LocalBroadcastManager localBroadcastManager;
  private FirebaseFirestore remoteDB = FirebaseFirestore.getInstance();
  private static final String NOTIFICATION_CHANNEL_ID = "rh_digital";
  private static final String NOTIFICATION_CHANNEL_NAME = "RHDigital";
  private static final String NOTIFICATION_CHANNEL_DESCRIPTION = "RHDigital Notifications";
  private PendingIntent intent;
  private Context context;

  private PushNotificationHelperService() { }

  public static PushNotificationHelperService getINSTANCE() {
    if (INSTANCE == null) {
      INSTANCE = new PushNotificationHelperService();
    }
    return INSTANCE;
  }

  public void setContext(Context context) {
    this.context = context;
    localBroadcastManager = LocalBroadcastManager.getInstance(context);
  }

  public void initialisePendingIntent(Intent intent) {
    this.intent = PendingIntent.getActivity(
      context,
      100,
      intent,
      PendingIntent.FLAG_CANCEL_CURRENT
    );
  }

  public void generateNotificationChannel() {
    if (context != null) {
      //Instantiate Notification Service
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        NotificationChannel notificationChannel =
          new NotificationChannel(NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT);

        notificationChannel.setDescription(NOTIFICATION_CHANNEL_DESCRIPTION);
        NotificationManager manager = context.getSystemService(NotificationManager.class);
        manager.createNotificationChannel(notificationChannel);
      }
    }
  }

  public void saveTokenRemote() {
    FirebaseInstanceId.getInstance().getInstanceId()
      .addOnCompleteListener(task -> {
        if (task.isSuccessful()) {
          String token = task.getResult().getToken();
          remoteDB.collection("users")
            .document(FirebaseAuth.getInstance().getUid())
            .update("pushNotificationToken", token);
        }
      });
  }

  public void sendNotificationToLifeCycleOwner(String title, String body) {
    if (context != null) {
      Log.d(TAG, "SENDING NOTIFICATION TO CONTEXT : "  + context.getPackageName());
      Intent intent = new Intent(EMAIL_VERIFICATION);
      intent.putExtra(title, body);
      localBroadcastManager.sendBroadcast(intent);
    }
  }

  public void displayNotification(String title, String body) {
    if (context != null) {
      Log.d(TAG, "DISPLAY NOTIFICATION");

      NotificationCompat.Builder builder =
        new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
          .setSmallIcon(R.drawable.ic_rh_logo)
          .setContentTitle(title)
          .setContentText(body)
          .setAutoCancel(true)
          .setPriority(NotificationCompat.PRIORITY_DEFAULT);

      if (this.intent != null) {
        builder.setContentIntent(this.intent);
      }

      NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
      notificationManager.notify(1, builder.build());
    }
  }
}
