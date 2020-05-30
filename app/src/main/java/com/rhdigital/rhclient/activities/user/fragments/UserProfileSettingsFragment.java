package com.rhdigital.rhclient.activities.user.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.common.services.NavigationService;

public class UserProfileSettingsFragment extends Fragment {

  private ImageView backButton;

  public UserProfileSettingsFragment() { }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.user_profile_settings_layout, container, false);

    backButton = view.findViewById(R.id.user_profile_settings_back_button);
    backButton.setOnClickListener(new BackButtonOnClick(getActivity().getLocalClassName()));
    return view;
  }

  public static class BackButtonOnClick implements View.OnClickListener {

    private String className;

    public BackButtonOnClick(String className) {
      this.className = className;
    }

    @Override
    public void onClick(View view) {
      NavigationService.getINSTANCE().navigate(className,
        R.id.userProfileFragment,
        null);
    }
  }
}
