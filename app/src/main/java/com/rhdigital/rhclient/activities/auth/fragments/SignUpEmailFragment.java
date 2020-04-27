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

import com.rhdigital.rhclient.R;

public class SignUpEmailFragment extends Fragment {

  private AutoCompleteTextView emailInput;
  private Button submitButton;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.sign_up_email_layout, container, false);

    emailInput = (AutoCompleteTextView) view.findViewById(R.id.sign_up_email_input);
    submitButton = (Button) view.findViewById(R.id.sign_up_email_submit_btn);
    return view;
  }

}
