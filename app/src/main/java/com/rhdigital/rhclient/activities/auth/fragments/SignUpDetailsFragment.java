//package com.rhdigital.rhclient.activities.auth.fragments;
//
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//import android.os.Message;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AutoCompleteTextView;
//import android.widget.Button;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//import com.rhdigital.rhclient.R;
//import com.rhdigital.rhclient.activities.auth.services.Authenticator;
//import com.rhdigital.rhclient.common.services.NavigationService;
//import com.rhdigital.rhclient.common.util.GenericTimer;
//
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
//public class SignUpDetailsFragment extends Fragment {
//
//  //Threading
//  private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
//  private Handler handler = new Handler(Looper.getMainLooper()) {
//    @Override
//    public void handleMessage(@NonNull Message msg) {
//      if (msg.what == 1) {
//        submitButton.setEnabled(true);
//        submitButton.setBackgroundResource(R.drawable.submit_active);
//      }
//    }
//  };
//
//  String email;
//  AutoCompleteTextView firstNameInput;
//  AutoCompleteTextView lastNameInput;
//  AutoCompleteTextView passwordInput;
//  Button submitButton;
//
//  @Override
//  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//    super.onActivityCreated(savedInstanceState);
//    if (getArguments() != null) {
//      if (getArguments().get("EMAIL") != null)
//        email = getArguments().get("EMAIL").toString();
//    }
//  }
//
//  @Nullable
//  @Override
//  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//    View view = inflater.inflate(R.layout.sign_up_details_layout, container, false);
//    firstNameInput = view.findViewById(R.id.sign_up_details_first_name_input);
//    lastNameInput = view.findViewById(R.id.sign_up_details_last_name_input);
//    passwordInput = view.findViewById(R.id.sign_up_details_password_input);
//    submitButton = view.findViewById(R.id.sign_up_details_submit_btn);
//    return view;
//  }
//
//  @Override
//  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//    super.onViewCreated(view, savedInstanceState);
//    submitButton.setOnClickListener(new SubmitOnClick(this));
//  }
//
//  public void setSubmitDisableTimeout() {
//    this.scheduledExecutorService.schedule(new GenericTimer(handler, GenericTimer.UI_UNLOCK), 2, TimeUnit.SECONDS);
//  }
//
//  public void setSubmitDisable() {
//    this.submitButton.setEnabled(false);
//    this.submitButton.setBackgroundResource(R.drawable.submit_inactive);
//  }
//
//  public AutoCompleteTextView getFirstNameInput() {
//    return firstNameInput;
//  }
//
//  public AutoCompleteTextView getLastNameInput() {
//    return lastNameInput;
//  }
//
//  public AutoCompleteTextView getPasswordInput() {
//    return passwordInput;
//  }
//
//  public String getEmail() {
//    return email;
//  }
//
//  public static class SubmitOnClick implements View.OnClickListener {
//
//    private SignUpDetailsFragment fragment;
//
//    public SubmitOnClick(Fragment fragment) {
//      this.fragment = (SignUpDetailsFragment) fragment;
//    }
//
//    @Override
//    public void onClick(View view) {
//      fragment.setSubmitDisable();
//     if (fragment.getEmail() != null) {
//       String firstName = fragment.getFirstNameInput().getText().toString();
//       String lastName = fragment.getLastNameInput().getText().toString();
//       String password = fragment.getPasswordInput().getText().toString();
//       if (isInputValid(firstName, lastName, password)) {
//         Authenticator.getInstance().register(fragment.getEmail(),
//           fragment.getPasswordInput().getText().toString(),
//           fragment.getFirstNameInput().getText().toString(),
//           fragment.getLastNameInput().getText().toString())
//           .observe(fragment.getViewLifecycleOwner(), success -> {
//             if (!success) {
//               fragment.setSubmitDisableTimeout();
//             } else {
//               NavigationService.getINSTANCE().navigate(fragment.getActivity().getLocalClassName(),
//                 R.id.signInWelcomeFragment,
//                 null,
//                 null);
//             }
//           });
//       } else {
//         fragment.setSubmitDisableTimeout();
//       }
//      }
//    }
//
//    private boolean isInputValid(String firstName, String lastName, String password) {
//      String error = null;
//      if (firstName.isEmpty() || lastName.isEmpty()) {
//        error = "Please provide your first name and last name";
//      } else if (password.length() < 6) {
//        error = "Password must contain 6 or more characters";
//      }
//      if (error != null) {
//        Toast.makeText(fragment.getContext(), error, Toast.LENGTH_LONG).show();
//        return false;
//      }
//      return true;
//    }
//  }
//}
