//package com.rhdigital.rhclient.activities.auth.fragments;
//
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//import android.os.Message;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.rhdigital.rhclient.R;
//import com.rhdigital.rhclient.common.services.NavigationService;
//import com.rhdigital.rhclient.common.services.Toaster;
//import com.rhdigital.rhclient.common.util.GenericTimer;
//
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
//public class EmailVerificationFragment extends Fragment {
//
//  //Threading
//  private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
//  private Handler handler = new Handler(Looper.getMainLooper()) {
//    @Override
//    public void handleMessage(@NonNull Message msg) {
//      if (msg.what == GenericTimer.EMAIL_VERIFICATION) {
//        firebaseUser.reload()
//        .addOnCompleteListener(task -> {
//          if (task.isSuccessful()) {
//            if (firebaseUser.isEmailVerified()) {
//              NavigationService.getINSTANCE()
//                .navigate(getActivity().getLocalClassName(),
//                  R.id.signInFragment,
//                  null,
//                  null);
//            } else {
//              scheduledExecutorService.schedule(new GenericTimer(handler, GenericTimer.EMAIL_VERIFICATION), 5000, TimeUnit.MILLISECONDS);
//            }
//          } else {
//            scheduledExecutorService.schedule(new GenericTimer(handler, GenericTimer.EMAIL_VERIFICATION), 5000, TimeUnit.MILLISECONDS);
//          }
//        });
//      } else if (msg.what == GenericTimer.UI_UNLOCK) {
//        setSubmitEnable();
//      }
//    }
//  };
//
//  private FirebaseUser firebaseUser;
//
//  //Components
//  private Button resendVerification;
//  private Button changeEmail;
//  private TextView emailText;
//
//  @Override
//  public void onStart() {
//    super.onStart();
//    if (FirebaseAuth.getInstance().getCurrentUser() == null ||
//      (FirebaseAuth.getInstance().getCurrentUser() != null &&
//        FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()))
//      NavigationService.getINSTANCE()
//        .navigate(getActivity().getLocalClassName(),
//          R.id.signInFragment,
//          null,
//          null);
//  }
//
//  @Nullable
//  @Override
//  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//    View view = inflater.inflate(R.layout.sign_up_email_verification_layout, container, false);
//
//    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//
//    if (firebaseUser.isEmailVerified()) {
//      Toast.makeText(getContext(), "Email Verified!", Toast.LENGTH_LONG).show();
//    } else {
//      scheduledExecutorService.schedule(new GenericTimer(handler, GenericTimer.EMAIL_VERIFICATION), 5000, TimeUnit.MILLISECONDS);
//    }
//
//    resendVerification = view.findViewById(R.id.sign_up_email_verification_resend);
//    changeEmail = view.findViewById(R.id.sign_up_email_change_email);
//    emailText = view.findViewById(R.id.textView);
//
//    emailText.append(FirebaseAuth.getInstance().getCurrentUser().getEmail());
//
//    //Set Listeners
//    resendVerification.setOnClickListener(new ResendOnClick());
//    changeEmail.setOnClickListener(new ChangeOnClick());
//
//    return view;
//  }
//
//  public void setSubmitDisableTimeout() {
//    this.scheduledExecutorService.schedule(new GenericTimer(handler, GenericTimer.UI_UNLOCK), 3, TimeUnit.SECONDS);
//  }
//
//  public void setSubmitDisable() {
//    this.resendVerification.setEnabled(false);
//    this.resendVerification.setBackgroundResource(R.drawable.submit_inactive);
//
//    this.changeEmail.setEnabled(false);
//    this.changeEmail.setBackgroundResource(R.drawable.submit_inactive);
//  }
//
//  public void setSubmitEnable() {
//    this.resendVerification.setEnabled(true);
//    this.resendVerification.setBackgroundResource(R.drawable.submit_active);
//
//    this.changeEmail.setEnabled(true);
//    this.changeEmail.setBackgroundResource(R.drawable.submit_active);
//  }
//
//  private class ResendOnClick implements View.OnClickListener {
//
//    @Override
//    public void onClick(View view) {
//      setSubmitDisable();
//      firebaseUser.sendEmailVerification()
//      .addOnCompleteListener(task -> {
//        setSubmitDisableTimeout();
//        if (task.isSuccessful()) {
//          Toaster.getINSTANCE()
//            .ToastMessage(getActivity().getResources()
//              .getString(R.string.server_success_email_verification), true);
//        } else {
//          Toaster.getINSTANCE()
//            .ToastMessage(getActivity().getResources()
//              .getString(R.string.server_error_email_verification), true);
//        }
//      });
//    }
//  }
//
//  private class ChangeOnClick implements View.OnClickListener {
//    @Override
//    public void onClick(View view) {
//      Bundle bundle = new Bundle();
//      bundle.putString("UUID", FirebaseAuth.getInstance().getCurrentUser().getUid());
//      NavigationService.getINSTANCE()
//        .navigate(getActivity().getLocalClassName(),
//          R.id.changeEmailFragment,
//          bundle,
//          null);
//    }
//  }
//}
