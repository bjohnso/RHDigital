package com.rhdigital.rhclient.activities.auth.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.tabs.TabLayout;
import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.common.services.NavigationService;

public class SignUpPhoneFragment extends Fragment {

  private SignUpFragment parent;
  private NavController navController;
  private AutoCompleteTextView phoneInput;
  private Button submitButton;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.sign_up_phone_layout, container, false);

    phoneInput = (AutoCompleteTextView) view.findViewById(R.id.sign_up_phone_input);
    submitButton = (Button) view.findViewById(R.id.sign_up_phone_submit_btn);
    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    submitButton.setOnClickListener(new SubmitOnClick(getActivity().getLocalClassName()));
  }

  public static class SubmitOnClick implements View.OnClickListener {

    private String parentClassName;

    public SubmitOnClick(String parentClassName) {
      this.parentClassName = parentClassName;
    }

    @Override
    public void onClick(View view) {
      NavigationService.getINSTANCE().navigate(parentClassName, R.id.signUpDetailsFragment, null, null);
    }
  }
}
