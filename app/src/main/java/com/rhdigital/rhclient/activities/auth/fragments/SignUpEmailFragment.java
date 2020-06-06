package com.rhdigital.rhclient.activities.auth.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.activities.auth.AuthActivity;
import com.rhdigital.rhclient.common.services.NavigationService;

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

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    submitButton.setOnClickListener(new SubmitOnClick(this));
  }

  public AutoCompleteTextView getEmailInput() {
    return emailInput;
  }

  public Button getSubmitButton() {
    return submitButton;
  }

  public static class SubmitOnClick implements View.OnClickListener {

    private SignUpEmailFragment fragment;

    public SubmitOnClick(Fragment fragment) {
      this.fragment = (SignUpEmailFragment) fragment;
    }

    @Override
    public void onClick(View view) {
      AuthActivity authActivity;
      String email = fragment.getEmailInput().getText().toString();
      if (email != null && !email.isEmpty()) {
        authActivity = (AuthActivity) fragment.getActivity();
        authActivity.getAuthenticator()
          .isExistingUser(email)
          .observe((LifecycleOwner) fragment.getContext(), isExisting -> {
            if (isExisting) {
              Toast.makeText(fragment.getContext(), "This email address already belongs to an account", Toast.LENGTH_SHORT).show();
            } else {
              Bundle bundle = new Bundle();
              bundle.putString("EMAIL", email);
              NavigationService.getINSTANCE()
                .navigate(fragment.getActivity().getLocalClassName(),
                  R.id.signUpDetailsFragment,
                bundle, null);
            }
          });
      } else {
        Toast.makeText(fragment.getContext(), "Email address is empty. Please Enter a valid email address", Toast.LENGTH_SHORT).show();
      }
    }
  }
}
