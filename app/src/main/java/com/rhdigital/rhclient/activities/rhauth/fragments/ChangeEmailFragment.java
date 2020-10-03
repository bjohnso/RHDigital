//package com.rhdigital.rhclient.activities.auth.fragments;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//import android.os.Message;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AutoCompleteTextView;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.DialogFragment;
//import androidx.fragment.app.Fragment;
//import androidx.lifecycle.LifecycleOwner;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.rhdigital.rhclient.R;
//import com.rhdigital.rhclient.activities.auth.AuthActivity;
//import com.rhdigital.rhclient.activities.auth.services.Authenticator;
//import com.rhdigital.rhclient.common.services.NavigationService;
//import com.rhdigital.rhclient.common.services.Toaster;
//import com.rhdigital.rhclient.common.util.GenericTimer;
//
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
//import static com.rhdigital.rhclient.common.util.ValidationHelper.isValidEmailAddress;
//
//public class ChangeEmailFragment extends Fragment {
//
//  private int REQUEST_CODE = 1;
//
//  //Threading
//  private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
//  private Handler handler = new Handler(Looper.getMainLooper()) {
//    @Override
//    public void handleMessage(@NonNull Message msg) {
//      if (msg.what == GenericTimer.UI_UNLOCK) {
//        setUIEnabled();
//      }
//    }
//  };
//
//  private AutoCompleteTextView emailInput;
//  private Button submitButton;
//  private String uuid;
//
//  @Override
//  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//    super.onActivityCreated(savedInstanceState);
//    if (getArguments() != null) {
//      uuid = getArguments().getString("UUID");
//    }
//
//    if (uuid == null) {
//      NavigationService.getINSTANCE()
//        .navigate(getActivity().getLocalClassName(), R.id.signInFragment, null, null);
//    }
//  }
//
//  @Nullable
//  @Override
//  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//    View view = inflater.inflate(R.layout.user_change_email_layout, container, false);
//
//    emailInput = view.findViewById(R.id.user_change_email_input);
//    submitButton = view.findViewById(R.id.user_change_email_submit);
//
//    //Set Listeners
//    submitButton.setOnClickListener(new SubmitOnClick(this));
//
//    return view;
//  }
//
//  @Override
//  public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//    if (resultCode != Activity.RESULT_OK) {
//      return ;
//    }
//
//    if (requestCode == REQUEST_CODE) {
//      assert data != null;
//      if (data.getBooleanExtra("AUTH_SUCCESS", false)) {
//        Log.d("CHANGEEMAIL", "AUTH SUCCESS!");
//        changeEmail();
//      }
//    }
//  }
//
//  public void changeEmail() {
//    String email = getEmailInput().getText().toString();
//
//    if (FirebaseAuth.getInstance().getCurrentUser() == null) {
//      NavigationService.getINSTANCE()
//        .navigate(getActivity().getLocalClassName(),
//          R.id.signInFragment, null, null);
//      return;
//    }
//
//    if (email != null && !email.isEmpty() && isValidEmailAddress(email)) {
//      Authenticator.getInstance()
//        .isExistingUserEmail(email)
//        .observe((LifecycleOwner) getContext(), isExisting -> {
//          setUIDisableTimeout();
//          if (isExisting) {
//            Toaster.getINSTANCE()
//              .ToastMessage(getContext().getResources().getString(R.string.user_error_email_duplicate) ,true);
//          } else {
//            Log.d("CHANGEEMAIL", "Attempting Email Update");
//            String uuid = getUUID();
//            if (FirebaseAuth.getInstance().getCurrentUser().getUid().equalsIgnoreCase(uuid)) {
//              FirebaseAuth.getInstance().getCurrentUser().updateEmail(email)
//                .addOnCompleteListener(getActivity(), task -> {
//                  if (task.isSuccessful()) {
//                    FirebaseAuth.getInstance().getCurrentUser()
//                      .reload()
//                      .addOnCompleteListener(reloadTask -> {
//                        Log.d("CHANGEEMAIL", "email updated on firebase");
//                        ((AuthActivity) getActivity()).updateUserEmail(uuid, email);
//                      });
//                  } else {
//                    DialogFragment dialogFragment;
//                    Bundle args = new Bundle();
//                    args.putString("EMAIL", FirebaseAuth.getInstance().getCurrentUser().getEmail());
//                    dialogFragment = new ReauthenticateModalFragment();
//                    dialogFragment.setArguments(args);
//                    dialogFragment.setTargetFragment(this, getREQUEST_CODE());
//                    dialogFragment.show(getParentFragmentManager(), "REAUTH");
//                  }
//                });
//            }
//          }
//        });
//    } else {
//      setUIDisableTimeout();
//      Toast.makeText(getContext(), "Please Enter a valid email address", Toast.LENGTH_SHORT).show();
//    }
//  }
//
//  public int getREQUEST_CODE() {
//    return REQUEST_CODE;
//  }
//
//  public String getUUID() {
//    return uuid;
//  }
//
//  public AutoCompleteTextView getEmailInput() {
//    return emailInput;
//  }
//
//  public void setUIDisableTimeout() {
//    this.scheduledExecutorService.schedule(new GenericTimer(handler, GenericTimer.UI_UNLOCK), 3, TimeUnit.SECONDS);
//  }
//
//  public void setUIDisable() {
//    this.submitButton.setEnabled(false);
//    this.submitButton.setBackgroundResource(R.drawable.submit_inactive);
//  }
//
//  public void setUIEnabled() {
//    this.submitButton.setEnabled(true);
//    this.submitButton.setBackgroundResource(R.drawable.submit_active);
//  }
//
//  // Listeners
//  public static class SubmitOnClick implements View.OnClickListener {
//
//    private ChangeEmailFragment fragment;
//
//    public SubmitOnClick(Fragment fragment) {
//      this.fragment = (ChangeEmailFragment) fragment;
//    }
//
//    @Override
//    public void onClick(View view) {
//      fragment.setUIDisable();
//      fragment.changeEmail();
//    }
//  }
//}
