package com.rhdigital.rhclient.activities.auth.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.auth.AuthActivity;
import com.rhdigital.rhclient.common.services.NavigationService;

public class SignUpDetailsFragment extends Fragment {

  String email = "";
  AutoCompleteTextView firstNameInput;
  AutoCompleteTextView lastNameInput;
  AutoCompleteTextView passwordInput;
  Button submitButton;

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    if (getArguments() != null) {
      email = getArguments().get("EMAIL").toString();
    }
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.sign_up_details_layout, container, false);
    firstNameInput = view.findViewById(R.id.sign_up_details_first_name_input);
    lastNameInput = view.findViewById(R.id.sign_up_details_last_name_input);
    passwordInput = view.findViewById(R.id.sign_up_details_password_input);
    submitButton = view.findViewById(R.id.sign_up_details_submit_btn);
    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    submitButton.setOnClickListener(new SubmitOnClick(this));
  }

  public AutoCompleteTextView getFirstNameInput() {
    return firstNameInput;
  }

  public AutoCompleteTextView getLastNameInput() {
    return lastNameInput;
  }

  public AutoCompleteTextView getPasswordInput() {
    return passwordInput;
  }

  public String getEmail() {
    return email;
  }

  public static class SubmitOnClick implements View.OnClickListener {

    private SignUpDetailsFragment fragment;

    public SubmitOnClick(Fragment fragment) {
      this.fragment = (SignUpDetailsFragment) fragment;
    }

    @Override
    public void onClick(View view) {
      AuthActivity authActivity = (AuthActivity) fragment.getActivity();
      authActivity.register(fragment.getEmail(),
        fragment.getPasswordInput().getText().toString(),
        fragment.getFirstNameInput().getText().toString(),
        fragment.getLastNameInput().getText().toString());
    }
  }

//  if (password.length() < 6) {
//    Toast.makeText(
//      this.context,
//      "Sorry, your password needs to be at least 6 characters. Please try again.",
//      Toast.LENGTH_LONG).show();
//  }

//  else {
//    Toast.makeText(this.context,
//      "Please enter a valid email address and password.",
//      Toast.LENGTH_LONG).show();
//  }

}
